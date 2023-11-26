package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.VaccinationForm;
import edu.ncsu.csc.itrust2.models.Vaccination;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.services.VaccinationService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(APIController.BASE_PATH)
public class APIVaccinationController extends APIController {

    @Autowired
    private VaccinationService vaccinationService;

    @Autowired
    private UserService userService;

    @Autowired
    private LoggerUtil loggerUtil;

    @PostMapping("/vaccinations")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public ResponseEntity<?> createVaccination(@RequestBody final VaccinationForm vaccinationForm) {
        try {
            Vaccination vaccination = vaccinationService.build(vaccinationForm);
            vaccinationService.saveVaccination(vaccination);
            loggerUtil.log(TransactionType.HCP_ADD_VACCINATION_TO_PATIENT, LoggerUtil.currentUser(), "Vaccination created with id " + vaccination.getId());
            return ResponseEntity.ok(vaccination);
        } catch (final Exception e) {
            loggerUtil.log(TransactionType.HCP_ADD_VACCINATION_TO_PATIENT, LoggerUtil.currentUser());
            return ResponseEntity.badRequest().body(errorResponse("Failed to create vaccination: " + e.getMessage()));
        }
    }

    @PreAuthorize(
            "hasAnyRole('ROLE_PATIENT')")
    @GetMapping("/vaccinations")
    public ResponseEntity<?> getVaccinations() {
        try {
            final User self = userService.findByName(LoggerUtil.currentUser());
            if (self == null) {
                return ResponseEntity.notFound().build();
            }
            List<Vaccination> vaccinations = vaccinationService.getVaccinationsByPatient(self);
            loggerUtil.log(TransactionType.PATIENT_VIEW_VACCINATIONS, LoggerUtil.currentUser(), "Viewing vaccinations for patient: " + self.getUsername());
            return ResponseEntity.ok(vaccinations);
        } catch (final Exception e) {
            loggerUtil.log(TransactionType.PATIENT_VIEW_VACCINATIONS, LoggerUtil.currentUser());
            return ResponseEntity.badRequest().body(errorResponse("Failed to retrieve vaccinations: " + e.getMessage()));
        }
    }
    
    @GetMapping("/vaccinations/patient/{username}")
    @PreAuthorize("hasAnyRole('ROLE_HCP')")
    public ResponseEntity<?> getVaccinationsForPatient(@PathVariable final String username) {
        try {
            User patient = userService.findByName(username);
            if (patient == null) {
                return ResponseEntity.notFound().build();
            }
            List<Vaccination> vaccinations = vaccinationService.getVaccinationsByPatient(patient);
            loggerUtil.log(TransactionType.PATIENT_VIEW_VACCINATIONS, LoggerUtil.currentUser(), "Viewing vaccinations for patient: " + username);
            return ResponseEntity.ok(vaccinations);
        } catch (final Exception e) {
            loggerUtil.log(TransactionType.PATIENT_VIEW_VACCINATIONS, LoggerUtil.currentUser());
            return ResponseEntity.badRequest().body(errorResponse("Failed to retrieve vaccinations: " + e.getMessage()));
        }
    }

    @PutMapping("/vaccinations/{id}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public ResponseEntity<?> updateVaccination(@PathVariable final Long id, @RequestBody final VaccinationForm vaccinationForm) {
        try {
            Vaccination existingVaccination = vaccinationService.findById(id);
            if (existingVaccination == null) {
                return ResponseEntity.notFound().build();
            }
            Vaccination updatedVaccination = vaccinationService.build(vaccinationForm);
            updatedVaccination.setId(id);
            vaccinationService.saveVaccination(updatedVaccination);
            loggerUtil.log(TransactionType.ADMIN_EDIT_VACCINE, LoggerUtil.currentUser(), "Vaccination updated with ID: " + id);
            return ResponseEntity.ok(updatedVaccination);
        } catch (final Exception e) {
            loggerUtil.log(TransactionType.ADMIN_EDIT_VACCINE, LoggerUtil.currentUser());
            return ResponseEntity.badRequest().body(errorResponse("Failed to update vaccination: " + e.getMessage()));
        }
    }

    @DeleteMapping("/vaccinations/{id}")
    @PreAuthorize("hasRole('ROLE_HCP')")
    public ResponseEntity<?> deleteVaccination(@PathVariable final Long id) {
        try {
            Vaccination vaccination = vaccinationService.findById(id);
            if (vaccination == null) {
                return ResponseEntity.notFound().build();
            }
            vaccinationService.deleteVaccination(vaccination);
            loggerUtil.log(TransactionType.ADMIN_DELETE_VACCINE, LoggerUtil.currentUser(), "Vaccination deleted with ID: " + id);
            return ResponseEntity.ok().build();
        } catch (final Exception e) {
            loggerUtil.log(TransactionType.ADMIN_DELETE_VACCINE, LoggerUtil.currentUser());
            return ResponseEntity.badRequest().body(errorResponse("Failed to delete vaccination: " + e.getMessage()));
        }
    }

    // @GetMapping("/vaccinations/officevisit/{visitId}")
    // @PreAuthorize("hasAnyRole('ROLE_HCP', 'ROLE_PATIENT')")
    // public ResponseEntity<?> getVaccinationsForOfficeVisit(@PathVariable final Long visitId) {
    //     final User user = userService.findByName(LoggerUtil.currentUser());
    //     final Collection<Role> roles = user.getRoles();

    //     try {
    //         OfficeVisit officeVisit = officeVisitService.findById(visitId);
    //         if (officeVisit == null) {
    //             logViewVaccinationFailure(roles, LoggerUtil.currentUser(), visitId, "Office visit not found");
    //             return ResponseEntity.notFound().build();
    //         }
    //         List<Vaccination> vaccinations = vaccinationService.getVaccinationsByOfficeVisit(officeVisit);

    //         logViewVaccinationSuccess(roles, LoggerUtil.currentUser(), visitId);

    //         return ResponseEntity.ok(vaccinations);
    //     } catch (final Exception e) {
    //         logViewVaccinationFailure(roles, LoggerUtil.currentUser(), visitId, e.getMessage());
    //         return ResponseEntity.badRequest().body(errorResponse("Failed to retrieve vaccinations for office visit: " + e.getMessage()));
    //     }
    // }

    // private void logViewVaccinationSuccess(Collection<Role> roles, String currentUser, Long visitId) {
    //     if (roles.contains(Role.ROLE_HCP)) {
    //         loggerUtil.log(TransactionType.HCP_VIEW_PATIENT_VACCINATIONS, currentUser, "HCP viewing vaccinations for office visit ID: " + visitId);
    //     } else if (roles.contains(Role.ROLE_PATIENT)) {
    //         loggerUtil.log(TransactionType.PATIENT_VIEW_VACCINATIONS, currentUser, "Patient viewing vaccinations for office visit ID: " + visitId);
    //     }
    // }

    // private void logViewVaccinationFailure(Collection<Role> roles, String currentUser, Long visitId, String errorMessage) {
    //     if (roles.contains(Role.ROLE_HCP)) {
    //         loggerUtil.log(TransactionType.HCP_VIEW_PATIENT_VACCINATIONS, currentUser, "Failed to retrieve vaccinations for office visit ID: " + visitId + "; Error: " + errorMessage);
    //     } else if (roles.contains(Role.ROLE_PATIENT)) {
    //         loggerUtil.log(TransactionType.PATIENT_VIEW_VACCINATIONS, currentUser, "Failed to retrieve vaccinations for office visit ID: " + visitId + "; Error: " + errorMessage);
    //     }
    // }
}
