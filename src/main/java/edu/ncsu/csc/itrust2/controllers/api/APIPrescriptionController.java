package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.PrescriptionService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides REST endpoints that deal with prescriptions. Exposes functionality to add, edit, fetch,
 * and delete prescriptions.
 *
 * @author Connor
 * @author Kai Presler-Marshall
 */
@RestController
@RequiredArgsConstructor
@SuppressWarnings({"rawtypes", "unchecked"})
public class APIPrescriptionController extends APIController {

    private final LoggerUtil loggerUtil;

    private final PrescriptionService prescriptionService;

    private final UserService userService;

    /**
     * Adds a new prescription to the system. Requires HCP permissions.
     *
     * @param form details of the new prescription
     * @return the created prescription
     */
    @PreAuthorize("hasAnyRole('ROLE_HCP')")
    @PostMapping("/prescriptions")
    public ResponseEntity addPrescription(@RequestBody final PrescriptionForm form) {
        try {
            final Prescription p = prescriptionService.build(form);
            prescriptionService.save(p);
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_CREATE,
                    LoggerUtil.currentUser(),
                    p.getPatient().getUsername(),
                    "Created prescription with id " + p.getId());
            return new ResponseEntity(p, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_CREATE,
                    LoggerUtil.currentUser(),
                    "Failed to create prescription");
            return new ResponseEntity(
                    errorResponse("Could not save the prescription: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Edits an existing prescription in the system. Matches prescriptions by ids. Requires HCP
     * permissions.
     *
     * @param form the form containing the details of the new prescription
     * @return the edited prescription
     */
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST')")
    @PutMapping("/prescriptions")
    public ResponseEntity editPrescription(@RequestBody final PrescriptionForm form) {
        try {
            final Prescription p = prescriptionService.build(form);
            final Prescription saved = (Prescription) prescriptionService.findById(p.getId());
            if (saved == null) {
                loggerUtil.log(
                        TransactionType.PRESCRIPTION_EDIT,
                        LoggerUtil.currentUser(),
                        "No prescription found with id " + p.getId());
                return new ResponseEntity(
                        errorResponse("No prescription found with id " + p.getId()),
                        HttpStatus.NOT_FOUND);
            }
            prescriptionService.save(p);
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_EDIT,
                    LoggerUtil.currentUser(),
                    p.getPatient().getUsername(),
                    "Edited prescription with id " + p.getId());
            return new ResponseEntity(p, HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_EDIT,
                    LoggerUtil.currentUser(),
                    "Failed to edit prescription");
            return new ResponseEntity(
                    errorResponse("Failed to update prescription: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Deletes the prescription with the given id.
     *
     * @param id the id
     * @return the id of the deleted prescription
     */
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST')")
    @DeleteMapping("/prescriptions/{id}")
    public ResponseEntity deletePrescription(@PathVariable final Long id) {
        final Prescription p = (Prescription) prescriptionService.findById(id);
        if (p == null) {
            return new ResponseEntity(
                    errorResponse("No prescription found with id " + id), HttpStatus.NOT_FOUND);
        }
        try {
            prescriptionService.delete(p);
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_DELETE,
                    LoggerUtil.currentUser(),
                    p.getPatient().getUsername(),
                    "Deleted prescription with id " + p.getId());
            return new ResponseEntity(p.getId(), HttpStatus.OK);
        } catch (final Exception e) {
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_DELETE,
                    LoggerUtil.currentUser(),
                    p.getPatient().getUsername(),
                    "Failed to delete prescription");
            return new ResponseEntity(
                    errorResponse("Failed to delete prescription: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Returns a collection of all the prescriptions in the system.
     *
     * @return all saved prescriptions
     */
    @PreAuthorize(
            "hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST', 'ROLE_PATIENT')")
    @GetMapping("/prescriptions")
    public List<Prescription> getPrescriptions() {
        final User self = userService.findByName(LoggerUtil.currentUser());
        if (self.isDoctor()) {
            // Return all prescriptions in system
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_VIEW,
                    LoggerUtil.currentUser(),
                    "HCP viewed a list of all prescriptions");
            return (List<Prescription>) prescriptionService.findAll();
        } else {
            // Issue #106
            // Return only prescriptions assigned to the patient
            loggerUtil.log(
                    TransactionType.PATIENT_PRESCRIPTION_VIEW,
                    LoggerUtil.currentUser(),
                    "Patient viewed a list of their prescriptions");
            return prescriptionService.findByPatient(self);
        }
    }

    /**
     * Returns a single prescription using the given id.
     *
     * @param id the id of the desired prescription
     * @return the requested prescription
     */
    @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_OD', 'ROLE_OPH', 'ROLE_VIROLOGIST')")
    @GetMapping("/prescriptions/{id}")
    public ResponseEntity getPrescription(@PathVariable final Long id) {
        final Prescription p = (Prescription) prescriptionService.findById(id);
        if (p == null) {
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_VIEW,
                    LoggerUtil.currentUser(),
                    "Failed to find prescription with id " + id);
            return new ResponseEntity(
                    errorResponse("No prescription found for " + id), HttpStatus.NOT_FOUND);
        } else {
            loggerUtil.log(
                    TransactionType.PRESCRIPTION_VIEW,
                    LoggerUtil.currentUser(),
                    "Viewed prescription  " + id);
            return new ResponseEntity(p, HttpStatus.OK);
        }
    }

    @GetMapping ( "/prescriptions/ehr/search/{username}" )
    public List<Prescription> getEhrPrescription ( @PathVariable final String username ) {
        final List<Long> pre_list = prescriptionService.findEhrByUserName(username);
        List<Prescription> list = new ArrayList<Prescription>();
        for(Long id:pre_list) {
            list.add((Prescription) prescriptionService.findById(id));
        }
        return list;

    }

    @GetMapping ( "/prescriptions/search/{username}" )
    public List<Prescription> getPrescription ( @PathVariable final String username ) {
        final List<Long> pre_list = prescriptionService.findByUserName(username);
        List<Prescription> list = new ArrayList<Prescription>();
        for(Long id:pre_list) {
            list.add((Prescription) prescriptionService.findById(id));
        }
        return list;

    }
}
