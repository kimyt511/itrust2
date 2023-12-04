package edu.ncsu.csc.itrust2.controllers.api;


import java.util.List;
import java.util.stream.Collectors;
import edu.ncsu.csc.itrust2.forms.ReviewForm;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.Review;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.HospitalService;
import edu.ncsu.csc.itrust2.services.ReviewService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;
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
public class APIReviewController extends APIController {

    private final ReviewService service;
    private final UserService userService;
    private final HospitalService hospitalService;
    private final LoggerUtil loggerUtil;
    private final OfficeVisitService officeVisitService;

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
            if (form.getHcp() != null) {
                final User hcp = userService.findByName(form.getHcp());
                if (!hcp.getRoles().contains(Role.ROLE_HCP)) throw new Exception("there's no such HCP");
                final Review review = service.build(form);
                service.save(review);
                loggerUtil.log(TransactionType.PATIENT_RATE_HCP,
                        LoggerUtil.currentUser(),
                        "Review has created");
                return new ResponseEntity(review, HttpStatus.OK);
            }else{
                loggerUtil.log(TransactionType.PATIENT_RATE_HCP,
                        LoggerUtil.currentUser(),
                        "Failed to create review");
                return new ResponseEntity(errorResponse("Could not add review: hcp should be included "), HttpStatus.NOT_FOUND);
            }

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
            if (form.getHospital() != null) {
                final Hospital hospital = hospitalService.findByName(form.getHospital());
                if (hospital == null) throw new Exception("there's no such hospital");
                final Review review = service.build(form);
                service.save(review);
                loggerUtil.log(TransactionType.PATIENT_RATE_HOSPITAL,
                        LoggerUtil.currentUser(),
                        "Review has created");
                return new ResponseEntity(review, HttpStatus.OK);
            }else{
                loggerUtil.log(TransactionType.PATIENT_RATE_HOSPITAL,
                        LoggerUtil.currentUser(),
                        "Failed to create review");
                return new ResponseEntity(errorResponse("Could not add review: hospital should be included"), HttpStatus.BAD_REQUEST);
            }
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
    @PutMapping("/reviews/hcp")
    public ResponseEntity editHcpReview(@RequestBody final ReviewForm form){
        try {
            // Check for existing review in database
            final Review savedReview = (Review) service.findById(form.getId());
            if (savedReview == null) throw new Exception("No review found");

            final Review review = service.build(form);

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
            if (savedReview == null) throw new Exception("No review found");

            final Review review = service.build(form);

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
    public ResponseEntity deleteHcpReviews(@PathVariable final Long id) {
        try {
            final Review review = (Review) service.findById(id);
            if (review == null) throw new Exception("No review found with id" + id);
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
    public ResponseEntity deleteHospitalReviews(@PathVariable final Long id) {
        try {
            final Review review = (Review) service.findById(id);
            if (review == null) throw new Exception("No review found with id" + id);
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
            if (patient == null) throw new Exception("No patient found with id " + id);
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
            if (hcp == null) throw new Exception("No hcp found with id " + id);
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
            if (hospital == null) throw new Exception("No hospital found with id " + id);
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

    /**
     * Get average rate of review associated by hcp with the id matching the given id.
     *
     * @param id the id of the hcp
     * @return Average rate of reviews associated with hcp
     */
    @GetMapping("/reviews/average/hcp/{id}")
    public ResponseEntity getAverageHcp(@PathVariable final String id){
        try{
            final User hcp = userService.findByName(id);
            if (hcp == null) throw new Exception("No hcp found with id "+ id);
            loggerUtil.log(
                    TransactionType.AVERAGE_HCP_RATE,
                    LoggerUtil.currentUser(),
                    "Get average rate of review associated by hcp with id " + id);
            return new ResponseEntity(service.averageHcp(hcp), HttpStatus.OK);
        }catch (final Exception e){
            loggerUtil.log(
                    TransactionType.AVERAGE_HCP_RATE, LoggerUtil.currentUser(), "Failed to get average rate of reviews");
            return new ResponseEntity(
                    errorResponse("Could not get average rate of reviews: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get average rate of review associated by hospital with the id matching the given id.
     *
     * @param id the id of the hospital
     * @return Average rate of reviews associated with hospital
     */
    @GetMapping("/reviews/average/hospital/{id}")
    public ResponseEntity getAverageHospital(@PathVariable final String id){
        try{
            final Hospital hospital = hospitalService.findByName(id);
            if (hospital == null) throw new Exception("No hospital found with id "+ id);
            loggerUtil.log(
                    TransactionType.AVERAGE_HOSPITAL_RATE,
                    LoggerUtil.currentUser(),
                    "Get average rate of review associated by hospital with id " + id);
            return new ResponseEntity(service.averageHospital(hospital), HttpStatus.OK);
        }catch (final Exception e){
            loggerUtil.log(
                    TransactionType.AVERAGE_HOSPITAL_RATE, LoggerUtil.currentUser(), "Failed to get average rate of reviews");
            return new ResponseEntity(
                    errorResponse("Could not get average rate of reviews: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get a list of HCPs visited by a specific patient.
     */
    @GetMapping("/reviews/hcps/{id}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<List<User>> getVisitedHcps(@PathVariable final String id) {
        try {
            final User patient = userService.findByName(id);
            if (patient == null) throw new Exception("No patient found with id " + id);
            List<User> hcps = officeVisitService
                    .findByPatient(patient)
                    .stream()
                    .map(OfficeVisit::getHcp)
                    .distinct()
                    .collect(Collectors.toList());

            return new ResponseEntity(hcps, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
            errorResponse("Could not retrieve visited HCPs: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get a list of hospitals visited by a specific patient.
     */
    @GetMapping("/reviews/hospitals/{id}")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public ResponseEntity<List<Hospital>> getVisitedHospitals(@PathVariable final String id) {
        try {
            final User patient = userService.findByName(id);
            if (patient == null) throw new Exception("No patient found with id " + id);
            List<Hospital> hospitals = officeVisitService
                    .findByPatient(patient)
                    .stream()
                    .map(OfficeVisit::getHospital)
                    .distinct()
                    .collect(Collectors.toList());

            return new ResponseEntity(hospitals, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse("Could not retrieve visited Hospitals: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
