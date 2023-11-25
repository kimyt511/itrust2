package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.VaccineForm;
import edu.ncsu.csc.itrust2.models.Vaccine;
import edu.ncsu.csc.itrust2.services.VaccineService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class APIVaccineController extends APIController {

    private final VaccineService service;

    private final LoggerUtil loggerUtil;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/vaccine")

    public ResponseEntity addVaccine(@RequestBody final VaccineForm form) {
        try {
            final Vaccine v = new Vaccine(form);

            // Make sure code does not conflict with existing drugs
            if (service.existsByName(v.getName())) {
                return new ResponseEntity(
                        errorResponse("Vaccine with name " + v.getName() + " already exists"),
                        HttpStatus.CONFLICT);
            }

            service.save(v);
            return new ResponseEntity(v, HttpStatus.OK);
        } catch (final Exception e) {
            return new ResponseEntity(
                    errorResponse("Could not add vaccine: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/vaccine")
    public ResponseEntity editVaccine(@RequestBody final VaccineForm form) {
        try {
            // Check for existing drug in database
            final Vaccine savedVaccine = (Vaccine) service.findById(form.getId());
            if (savedVaccine == null) {
                return new ResponseEntity(
                        errorResponse("No vaccine found with name " + form.getName()),
                        HttpStatus.NOT_FOUND);
            }

            final Vaccine v = new Vaccine(form);

            // If the code was changed, make sure it is unique
            final Vaccine sameName = service.findByName(v.getName());
            if (sameName != null && !sameName.getId().equals(savedVaccine.getId())) {
                return new ResponseEntity(
                        errorResponse("Vaccine with name " + v.getName() + " already exists"),
                        HttpStatus.CONFLICT);
            }

            service.save(v); /* Overwrite existing drug */

            return new ResponseEntity(v, HttpStatus.OK);
        } catch (final Exception e) {

            return new ResponseEntity(
                    errorResponse("Could not update vaccine: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/vaccine/{id}")
    public ResponseEntity deleteVaccine(@PathVariable final String id) {
        try {
            final Vaccine v = (Vaccine) service.findById(Long.parseLong(id));
            if (v == null) {
//                loggerUtil.log(
//                        TransactionType.DRUG_DELETE,
//                        LoggerUtil.currentUser(),
//                        "Could not find drug with id " + id);
                return new ResponseEntity(
                        errorResponse("No vaccine found with id " + id), HttpStatus.NOT_FOUND);
            }
            service.delete(v);
//            loggerUtil.log(
//                    TransactionType.DRUG_DELETE,
//                    LoggerUtil.currentUser(),
//                    "Deleted drug with id " + drug.getId());
            return new ResponseEntity(id, HttpStatus.OK);
        } catch (final Exception e) {
//            loggerUtil.log(
//                    TransactionType.DRUG_DELETE, LoggerUtil.currentUser(), "Failed to delete drug");
            return new ResponseEntity(
                    errorResponse("Could not delete vaccine: " + e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/vaccine")
    public List<Vaccine> getVaccines() {
//        loggerUtil.log(
//                TransactionType.DRUG_VIEW, LoggerUtil.currentUser(), "Fetched list of drugs");
        return (List<Vaccine>) service.findAll();
    }


}
