package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.PersonalRepresentativeForm;
import edu.ncsu.csc.itrust2.models.PersonalRepresentative;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.PersonalRepresentativeService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class APIPersonalRepresentativeController extends APIController {

    private final PersonalRepresentativeService personalRepresentativeService;

    private final UserService userService;

    private final LoggerUtil loggerUtil;

    /**
     * Retrieves a list of representative for a patient in the database
     *
     */
    @GetMapping("/pr/{username}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public ResponseEntity getPersonalRepresentatives(@PathVariable final String username) {
        final User patient = userService.findByName(username);
        return new ResponseEntity(
                personalRepresentativeService.findByPatient(patient), HttpStatus.OK);
    }

    /**
     * Retrieves a list of patients representative for.
     * 
     */
    @GetMapping("/pr/declared/{username}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public List<PersonalRepresentative> getDeclared(@PathVariable final String username) {
        final User patient = userService.findByName(username);
        return personalRepresentativeService.findByRepresentative(patient);
    }

    /**
     * Retrieves all representatives for the current patient
     *
     * @TODO: Replace to getPersonalRepresentatives()
     */
    @GetMapping("/pr/myrepresentatives")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<PersonalRepresentative> getMyRepresentatives() {
        final User self = userService.findByName(LoggerUtil.currentUser());
        return personalRepresentativeService.findByPatient(self);
    }

    /**
     * Retrieves all the patients the current user is representative to.
     *
     * @TODO: Replace to getDeclared()
     */
    @GetMapping("/pr/mypatients")
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    public List<PersonalRepresentative> getMyPatients() {
        final User self = userService.findByName(LoggerUtil.currentUser());
        return personalRepresentativeService.findByRepresentative(self);
    }

    /**
     * From patient, create and save a new representative relationship from the RequestBody
     * provided.
     *
     * @param prForm The relationship to be saved
     * @return response
     */
    @PostMapping("/pr/declare")
    public ResponseEntity createRepresentative(@RequestBody final PersonalRepresentativeForm prForm) {
        try {
            final PersonalRepresentative pr = personalRepresentativeService.build(prForm);

            // If duplicate representative
            if (personalRepresentativeService.existsByPatientAndRepresentative(pr.getPatient(), pr.getRepresentative())) {
                loggerUtil.log(
                        TransactionType.DECLARE_PR,
                        LoggerUtil.currentUser(),
                        "Conflict: Representative " + pr.getRepresentative() + " already exists");
                return new ResponseEntity(
                        errorResponse("Representative already declared"),
                        HttpStatus.CONFLICT);
            }

            personalRepresentativeService.save(pr);
            loggerUtil.log(
                    TransactionType.DECLARE_PR, 
                    pr.getPatient(),
                    pr.getRepresentative());

            return new ResponseEntity(pr, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.DECLARE_PR, 
                    LoggerUtil.currentUser(), 
                    "Failed to declare representative");
            return new ResponseEntity(
                    errorResponse("Could not declare representative: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Undeclare representative from ID provided
     * 
     * @param id The ID of relationship
     * @return response
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @DeleteMapping("/pr/undeclare/{id}")
    public ResponseEntity deleteRepresentative(@PathVariable final String id) {
        try {
            final PersonalRepresentative pr = (PersonalRepresentative) personalRepresentativeService.findById(Long.parseLong(id));
            // Wrong ID value
            if (pr == null) {
                loggerUtil.log(
                    TransactionType.REMOVE_PR,
                    LoggerUtil.currentUser(),
                    "Could not find ID with " + id);
                return new ResponseEntity(
                    errorResponse("No ID with " + id), HttpStatus.NOT_FOUND);
            }

            // Undeclare PR
            TransactionType transactionType;
            User primaryUser;
            User secondaryUser;

            // Undeclare a PR
            if (pr.getPatient() == userService.findByName(LoggerUtil.currentUser())) {
                transactionType = TransactionType.REMOVE_PR;
                primaryUser = pr.getPatient();
                secondaryUser = pr.getRepresentative();
            } else { // Undeclare self as PR
                transactionType = TransactionType.REMOVE_SELF_AS_PR;
                primaryUser = pr.getRepresentative();
                secondaryUser = pr.getPatient();
            }

            personalRepresentativeService.delete(pr);
            loggerUtil.log(
                transactionType,
                primaryUser,
                secondaryUser);
            return new ResponseEntity(id, HttpStatus.OK);
        } catch (Exception e) {
            loggerUtil.log(
                TransactionType.REMOVE_PR, LoggerUtil.currentUser(), "Failed to delete representative");
            return new ResponseEntity(
                    errorResponse("Could not delete representative: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }
}
