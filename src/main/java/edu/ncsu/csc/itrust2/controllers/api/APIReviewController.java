package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.ReviewForm;
import edu.ncsu.csc.itrust2.models.Review;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.ReviewService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static edu.ncsu.csc.itrust2.controllers.api.APIController.errorResponse;
@SuppressWarnings({"unchecked", "rawtypes"})
@RestController
@RequiredArgsConstructor
public class APIReviewController {

    private final ReviewService service;
    private final LoggerUtil loggerUtil;

    /**
     * Adds a new review to the system. Requires patient permissions. Returns an error message if
     * something goes wrong.
     *
     * @param form the review form
     * @return the created review
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/reviews")
    public ResponseEntity addReview(@RequestBody final ReviewForm form){
        try{
            final Review review = new Review(form);

            //Make sure data does not conflict with existing reviews
            if (service.existsByHcpAndPatient(review.getHcp(), review.getPatient())){
                loggerUtil.log(
                        TransactionType.ADD_BLOOD_SUGAR_LIMITS,
                        LoggerUtil.currentUser(),
                        "Conflict: review with " + review.getPatient() + "'s review to " + review.getHcp() + " already exists");
                return new ResponseEntity(
                                errorResponse("Review with " + review.getPatient() + " to " + review.getHcp() + " already exists"),
                                HttpStatus.CONFLICT);

            }

            service.save(review);
            loggerUtil.log(
                    TransactionType.ADD_BLOOD_SUGAR_LIMITS,
                    LoggerUtil.currentUser(),
                    "Review has created");
            return new ResponseEntity(review, HttpStatus.OK);

        }catch (final Exception e){
            loggerUtil.log(
                    TransactionType.ADD_BLOOD_SUGAR_LIMITS,
                    LoggerUtil.currentUser(),
                    "Failed to create review"
            );
            return new ResponseEntity(errorResponse("Could not add review: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * Edits a review in the system. The id stored in the form must match an existing review.
     * Requires patient permissions.
     *
     * @param form the edited review form
     * @return the edited review or an error message
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PutMapping("/reivews")
    public ResponseEntity editReview(@RequestBody final ReviewForm form){
        try {
            // Check for existing review in database
            final Review savedReview = (Review) service.findById(form.getId());
            if (savedReview == null) {
                return new ResponseEntity(
                        errorResponse("No review found"),
                        HttpStatus.NOT_FOUND);
            }

            final Review review = new Review(form);

            service.save(review); /* Overwrite existing review */

            loggerUtil.log(
                    TransactionType.DRUG_EDIT,
                    LoggerUtil.currentUser(),
                    "Review with id " + review.getId() + " edited");
            return new ResponseEntity(review, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.DRUG_EDIT, LoggerUtil.currentUser(), "Failed to edit review");
            return new ResponseEntity(
                    errorResponse("Could not update review: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes the review with the id matching the given id. Requires patient permissions.
     *
     * @param id the id of the review to delete
     * @return the id of the deleted review
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @DeleteMapping("/reviews/{id}")
    public ResponseEntity deleteReviews(@PathVariable final String id) {
        try {
            final Review review = (Review) service.findById(Long.parseLong(id));
            if (review == null) {
                loggerUtil.log(
                        TransactionType.DRUG_DELETE,
                        LoggerUtil.currentUser(),
                        "Could not find review with id " + id);
                return new ResponseEntity(
                        errorResponse("No review found with id " + id), HttpStatus.NOT_FOUND);
            }
            service.delete(review);
            loggerUtil.log(
                    TransactionType.DRUG_DELETE,
                    LoggerUtil.currentUser(),
                    "Deleted review with id " + review.getId());
            return new ResponseEntity(id, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.DRUG_DELETE, LoggerUtil.currentUser(), "Failed to delete review");
            return new ResponseEntity(
                    errorResponse("Could not delete review: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }





}
