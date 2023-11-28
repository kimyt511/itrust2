package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.controllers.api.comm.LogEntryRequestBody;
import edu.ncsu.csc.itrust2.controllers.api.comm.LogEntryTableRow;
import edu.ncsu.csc.itrust2.forms.PersonalRepresentativeForm;
import edu.ncsu.csc.itrust2.models.Diagnosis;
import edu.ncsu.csc.itrust2.models.PersonalRepresentative;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.models.security.LogEntry;
import edu.ncsu.csc.itrust2.services.DiagnosisService;
import edu.ncsu.csc.itrust2.services.PersonalRepresentativeService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.services.security.LogEntryService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.apache.catalina.connector.ResponseFacade;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;

@RestController
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class APIPersonalRepresentativeController extends APIController {

    private final PersonalRepresentativeService personalRepresentativeService;

    private final UserService userService;

    private final LoggerUtil loggerUtil;

    private final LogEntryService leservice;

    private final DiagnosisService diagnosisService;

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
        /** Logging */
        User primaryUser = userService.findByName(LoggerUtil.currentUser());
        User secondaryUser;
        TransactionType transactionType = primaryUser.isDoctor() 
                                            ? TransactionType.HCP_DECLARE_PR 
                                            : TransactionType.DECLARE_PR;
        try {
            final PersonalRepresentative pr = personalRepresentativeService.build(prForm);

            /** If duplicate representative */
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
            secondaryUser = pr.getRepresentative();
            loggerUtil.log(
                    transactionType,
                    primaryUser,
                    secondaryUser);
            return new ResponseEntity(pr, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    transactionType,
                    LoggerUtil.currentUser(), 
                    "Failed to declare representative");
            return new ResponseEntity(
                    errorResponse("Could nt declare representative: " + e.getMessage()), HttpStatus.BAD_REQUEST);
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
            /** Wrong ID value */
            if (pr == null) {
                loggerUtil.log(
                    TransactionType.REMOVE_PR,
                    LoggerUtil.currentUser(),
                    "Could not find ID with " + id);
                return new ResponseEntity(
                    errorResponse("No ID with " + id), HttpStatus.NOT_FOUND);
            }

            /** Logging */
            TransactionType transactionType;
            User primaryUser;
            User secondaryUser;

            /** Undeclare PR */
            if (pr.getPatient() == userService.findByName(LoggerUtil.currentUser())) {
                transactionType = TransactionType.REMOVE_PR;
                primaryUser = pr.getPatient();
                secondaryUser = pr.getRepresentative();
            } else { /** Undeclare self as PR */
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

    /**
     * View access logs of PR. Logs are only available to declared PR / patients.
     * 
     * @param body
     * @param username
     * @return
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/pr/logentries/{username}")
    public ResponseEntity getRepresentativeAccessLogs(@PathVariable final String username) {
        // If not declared, throw error
        User self = userService.findByName(LoggerUtil.currentUser());
        User patient = userService.findByName(username);
        if (!personalRepresentativeService
            .existsByPatientAndRepresentative(self, patient)
            && !personalRepresentativeService
            .existsByPatientAndRepresentative(patient, self)) {
            return new ResponseEntity(
                errorResponse("Forbidden: Not a representative of " + username),
                HttpStatus.FORBIDDEN
            );    
        }

        // If the entries array is null give an error response
        List<LogEntry> entries = leservice.findAllForUser(username);
        if (entries == null) {
            return new ResponseEntity(
                    errorResponse("Error retrieving Log Entries"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Use only log entries that are viewable by the user
        List<LogEntry> visible;
        final User user = userService.findByName(username);
        if (user.getRoles().contains(Role.ROLE_PATIENT)) {
            visible = new ArrayList<>();

            for (final LogEntry le : entries) {
                if (le.getLogCode().isPatientViewable()) {
                    visible.add(le);
                }
            }
        } else {
            visible = entries;
        }
        Collections.reverse(visible);
        
        // Turn these log entries into proper table rows for the application to
        // display
        final List<LogEntryTableRow> table = new ArrayList<>();
        for (final LogEntry le : visible) {
            final LogEntryTableRow row = new LogEntryTableRow();

            row.setPrimary(le.getPrimaryUser());
            row.setSecondary(le.getSecondaryUser());

            // Convert to OffsetDateTime String so that
            // text-based timezone is not included
            row.setDateTime(le.getTime().toOffsetDateTime().toString());
            row.setTransactionType(le.getLogCode().getDescription());
            row.setNumPages(1);

            if (user.getRoles().contains(Role.ROLE_PATIENT)) {
                row.setPatient(true);

                if (le.getPrimaryUser().equals(username)) {
                    final User secondary = userService.findByName(le.getSecondaryUser());
                    if (secondary != null) {
                        row.setRole(secondary.getRoles().toString());
                    }
                } else {
                    final User primary = userService.findByName(le.getPrimaryUser());
                    row.setRole(primary.getRoles().toString());
                }
            }

            table.add(row);
        }

        // Create a log entry 
        loggerUtil.log(
            TransactionType.VIEW_USER_LOG,
            LoggerUtil.currentUser(),
            LoggerUtil.currentUser() + " viewed their access logs");

        return new ResponseEntity(table, HttpStatus.OK);
    }

    /**
     * Returns a list of diagnoses of PR / patients
     *
     * @return List of Diagnoses for the patient
     */
    @PreAuthorize("hasRole('ROLE_PATIENT')")
    @GetMapping("/pr/diagnoses/{username}")
    public ResponseEntity getRepresentativeDiagnosis(@PathVariable final String username) {
        // If not declared, throw error
        User patient = userService.findByName(username);
        User self = userService.findByName(LoggerUtil.currentUser());
        if (!personalRepresentativeService
            .existsByPatientAndRepresentative(self, patient)
            && !personalRepresentativeService
            .existsByPatientAndRepresentative(patient, self)) {
            return new ResponseEntity(
                errorResponse("Forbidden: Not a representative of " + username),
                HttpStatus.FORBIDDEN
            );    
        }

        if (patient == null) {
            return null;
        }
        loggerUtil.log(
                TransactionType.DIAGNOSIS_PATIENT_VIEW_ALL,
                LoggerUtil.currentUser(),
                LoggerUtil.currentUser() + " viewed their diagnoses");

        return new ResponseEntity(
            diagnosisService.findByPatient(patient), HttpStatus.OK);
    }
}
