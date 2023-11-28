package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.ReviewForm;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.Review;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.HospitalService;
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
    private final UserService userService;
    private final HospitalService hospitalService;
    private final LoggerUtil loggerUtil;

    /**
     * Adds a new review of hcp to the system. Requires patient permissions. Returns an error message if
     * something goes wrong.
     *
     * @param form the review form
     * @return the created review
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/reviews/hcp")
    public ResponseEntity addHcpReview(@RequestBody final ReviewForm form){
        try{
            final Review review = new Review(form);

            if (review.getHcp() != null) {
                service.save(review);
                loggerUtil.log(TransactionType.PATIENT_RATE_HCP,
                        LoggerUtil.currentUser(),
                        "Review has created");
            }

            return new ResponseEntity(review, HttpStatus.OK);

        }catch (final Exception e){
            loggerUtil.log(
                    TransactionType.RATE_CREATE_FAILURE,
                    LoggerUtil.currentUser(),
                    "Failed to create review"
            );
            return new ResponseEntity(errorResponse("Could not add review: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Adds a new review of hospital to the system. Requires patient permissions. Returns an error message if
     * something goes wrong.
     *
     * @param form the review form
     * @return the created review
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PostMapping("/reviews/hospital")
    public ResponseEntity addHospitalReview(@RequestBody final ReviewForm form){
        try{
            final Review review = new Review(form);

            if (review.getHospital() != null) {
                service.save(review);
                loggerUtil.log(TransactionType.PATIENT_RATE_HOSPITAL,
                        LoggerUtil.currentUser(),
                        "Review has created");
            }

            return new ResponseEntity(review, HttpStatus.OK);

        }catch (final Exception e){
            loggerUtil.log(
                    TransactionType.RATE_CREATE_FAILURE,
                    LoggerUtil.currentUser(),
                    "Failed to create review"
            );
            return new ResponseEntity(errorResponse("Could not add review: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Edits a hcp review in the system. The id stored in the form must match an existing review.
     * Requires patient permissions.
     *
     * @param form the edited review form
     * @return the edited review or an error message
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PutMapping("/reivews/hcp")
    public ResponseEntity editHcpReview(@RequestBody final ReviewForm form){
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
                    TransactionType.RATE_EDIT,
                    LoggerUtil.currentUser(),
                    "Review with id " + review.getId() + " edited");
            return new ResponseEntity(review, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.RATE_EDIT, LoggerUtil.currentUser(), "Failed to edit review");
            return new ResponseEntity(
                    errorResponse("Could not update review: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Edits a hospital review in the system. The id stored in the form must match an existing review.
     * Requires patient permissions.
     *
     * @param form the edited review form
     * @return the edited review or an error message
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @PutMapping("/reviews/hospital")
    public ResponseEntity editHospitalReview(@RequestBody final ReviewForm form){
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
                    TransactionType.RATE_EDIT,
                    LoggerUtil.currentUser(),
                    "Review with id " + review.getId() + " edited");
            return new ResponseEntity(review, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.RATE_EDIT, LoggerUtil.currentUser(), "Failed to edit review");
            return new ResponseEntity(
                    errorResponse("Could not update review: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }



    /**
     * Deletes the hcp review with the id matching the given id. Requires patient permissions.
     *
     * @param id the id of the review to delete
     * @return the id of the deleted review
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @DeleteMapping("/reviews/hcp/{id}")
    public ResponseEntity deleteHcpReviews(@PathVariable final String id) {
        try {
            final Review review = (Review) service.findById(Long.parseLong(id));
            if (review == null) {
                loggerUtil.log(
                        TransactionType.RATE_DELETE,
                        LoggerUtil.currentUser(),
                        "Could not find review with id " + id);
                return new ResponseEntity(
                        errorResponse("No review found with id " + id), HttpStatus.NOT_FOUND);
            }
            service.delete(review);
            loggerUtil.log(
                    TransactionType.RATE_DELETE,
                    LoggerUtil.currentUser(),
                    "Deleted review with id " + review.getId());
            return new ResponseEntity(id, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.RATE_DELETE, LoggerUtil.currentUser(), "Failed to delete review");
            return new ResponseEntity(
                    errorResponse("Could not delete review: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes the hospital review with the id matching the given id. Requires patient permissions.
     *
     * @param id the id of the review to delete
     * @return the id of the deleted review
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @DeleteMapping("/reviews/hospital/{id}")
    public ResponseEntity deleteHospitalReviews(@PathVariable final String id) {
        try {
            final Review review = (Review) service.findById(Long.parseLong(id));
            if (review == null) {
                loggerUtil.log(
                        TransactionType.RATE_DELETE,
                        LoggerUtil.currentUser(),
                        "Could not find review with id " + id);
                return new ResponseEntity(
                        errorResponse("No review found with id " + id), HttpStatus.NOT_FOUND);
            }
            service.delete(review);
            loggerUtil.log(
                    TransactionType.RATE_DELETE,
                    LoggerUtil.currentUser(),
                    "Deleted review with id " + review.getId());
            return new ResponseEntity(id, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.RATE_DELETE, LoggerUtil.currentUser(), "Failed to delete review");
            return new ResponseEntity(
                    errorResponse("Could not delete review: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get review associated by paitent with the id matching the given id.
     *
     * @param id the id of the paitent
     * @return List of reviews associated with patient
     */
    @GetMapping("/reviews/patient/{id}")
    public ResponseEntity getPatientReviews(@PathVariable final String id){
        try{
            final User patient = userService.findByName(id);
            if (patient == null) {
                loggerUtil.log(
                        TransactionType.PATIENT_VIEW_RATE,
                        LoggerUtil.currentUser(),
                        "Could not find patient with id " + id);
                return new ResponseEntity(
                        errorResponse("No patient found with id " + id), HttpStatus.NOT_FOUND);
            }
            loggerUtil.log(
                    TransactionType.PATIENT_VIEW_RATE,
                    LoggerUtil.currentUser(),
                    "Get review associated by patient with id " + id);
            return new ResponseEntity(service.findByPatient(patient), HttpStatus.OK);
        }catch (final Exception e){
            loggerUtil.log(
                    TransactionType.PATIENT_VIEW_RATE, LoggerUtil.currentUser(), "Failed to get reviews");
            return new ResponseEntity(
                    errorResponse("Could not get reviews: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get review associated by hcp with the id matching the given id.
     *
     * @param id the id of the hcp
     * @return List of reviews associated with hcp
     */
    @GetMapping("/reviews/hcp/{id}")
    public ResponseEntity getHcpReviews(@PathVariable final String id){
        try{
            final User hcp = userService.findByName(id);
            if (hcp == null) {
                loggerUtil.log(
                        TransactionType.HCP_VIEW_RATE,
                        LoggerUtil.currentUser(),
                        "Could not find hcp with id " + id);
                return new ResponseEntity(
                        errorResponse("No hcp found with id " + id), HttpStatus.NOT_FOUND);
            }
            loggerUtil.log(
                    TransactionType.HCP_VIEW_RATE,
                    LoggerUtil.currentUser(),
                    "Get review associated by hcp with id " + id);
            return new ResponseEntity(service.findByHcp(hcp), HttpStatus.OK);
        }catch (final Exception e){
            loggerUtil.log(
                    TransactionType.HCP_VIEW_RATE, LoggerUtil.currentUser(), "Failed to get reviews");
            return new ResponseEntity(
                    errorResponse("Could not get reviews: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get review associated by hospital with the id matching the given id.
     *
     * @param id the id of the hospital
     * @return List of reviews associated with hospital
     */
    @GetMapping("/reviews/hospital/{id}")
    public ResponseEntity getHospitalReviews(@PathVariable final String id){
        try{
            final Hospital hospital = hospitalService.findByName(id);
            if (hospital == null) {
                loggerUtil.log(
                        TransactionType.HOSPITAL_VIEW_RATE,
                        LoggerUtil.currentUser(),
                        "Could not find hospital with id " + id);
                return new ResponseEntity(
                        errorResponse("No hospital found with id " + id), HttpStatus.NOT_FOUND);
            }
            loggerUtil.log(
                    TransactionType.HOSPITAL_VIEW_RATE,
                    LoggerUtil.currentUser(),
                    "Get review associated by hospital with id " + id);
            return new ResponseEntity(service.findByHospital(hospital), HttpStatus.OK);
        }catch (final Exception e){
            loggerUtil.log(
                    TransactionType.HOSPITAL_VIEW_RATE, LoggerUtil.currentUser(), "Failed to get reviews");
            return new ResponseEntity(
                    errorResponse("Could not get reviews: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }







}
