package edu.ncsu.csc.itrust2.controllers.api;

import java.util.List;
import java.util.logging.Logger;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.DomainObject;
import edu.ncsu.csc.itrust2.models.enums.*;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc.itrust2.forms.ProcedureForm;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.Procedure;
import edu.ncsu.csc.itrust2.services.ProcedureService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;

/**
 * Provides REST endpoints that deal with loinc. Exposes functionality to add,
 * edit, fetch, and delete LOINC.
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
@RequiredArgsConstructor
public class APIProcedureController extends APIController {

    @Autowired
    private ProcedureService procedureService;

    @Autowired
    private LoggerUtil  loggerUtil;
    @Autowired
    private UserService  userService;
    @Autowired
    private OfficeVisitService officeVisitService;

    /**
     * Adds a new Procedure to the system. Returns an
     * error message if something goes wrong.
     *
     * @param form
     *            the Procedure form
     * @return the created Procedure
     */
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    @PostMapping ( "/procedure" )
    public ResponseEntity addProcedure ( @RequestBody final ProcedureForm form ) {
        try {
            if (form.getLabtech() == null) throw new Exception("Labtech should be assigned");
            final Procedure procedure = procedureService.build(form);
            procedureService.save( procedure );
            loggerUtil.log( TransactionType.HCP_CREATE_PROC, LoggerUtil.currentUser(),
                    "Procedure created" );
            return new ResponseEntity(procedure, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.HCP_CREATE_PROC, LoggerUtil.currentUser(), "Failed to create Procedure" );
            return new ResponseEntity( errorResponse( "Could not add Procedure: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Edits a Procedure in the system. The id stored in the form must match an
     * existing Procedure, and changes to NDCs cannot conflict with existing NDCs.
     *
     *
     * @param form
     *            the edited Procedure form
     * @return the edited Procedure or an error message
     */
    @PreAuthorize ( "hasAnyRole('ROLE_HCP', 'ROLE_LABTECH')" )
    @PutMapping ( "/procedure" )
    public ResponseEntity editProcedure ( @RequestBody final ProcedureForm form ) {
        final User self = userService.findByName(LoggerUtil.currentUser());
        try {
            // Check for existing Procedure in database
            final Procedure savedProcedure = (Procedure) procedureService.findById( form.getId() );
            if ( savedProcedure == null ) throw new Exception("No procedure found");

            final Procedure procedure = procedureService.build(form);

            procedureService.save( procedure ); /* Overwrite existing Procedure */
            if (self.getRoles().contains(Role.ROLE_HCP)){
                loggerUtil.log( TransactionType.HCP_EDIT_PROC, LoggerUtil.currentUser(),
                        "Procedure with id " + procedure.getId() + " edited" );
            }else{
                loggerUtil.log( TransactionType.LABTECH_EDIT_PROC, LoggerUtil.currentUser(),
                        "Procedure with id " + procedure.getId() + " edited" );
            }

            return new ResponseEntity( procedure, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            if (self.getRoles().contains(Role.ROLE_HCP)){
                loggerUtil.log( TransactionType.HCP_EDIT_PROC, LoggerUtil.currentUser(), "Failed to edit Procedure" );
            }else{
                loggerUtil.log( TransactionType.LABTECH_EDIT_PROC, LoggerUtil.currentUser(), "Failed to edit Procedure" );
            }

            loggerUtil.log( TransactionType.HCP_EDIT_PROC, LoggerUtil.currentUser(), "Failed to edit Procedure" );
            return new ResponseEntity( errorResponse( "Could not update Procedure: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes the Procedure with the id matching the given id.
     * Procedure should in assigned progress.
     * Requires hcp permissions.
     *
     * @param id
     *            the id of the Procedure to delete
     * @return the id of the deleted Procedure
     */
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    @DeleteMapping ( "/procedure/{id}" )
    public ResponseEntity deleteProcedure ( @PathVariable final String id ) {
        try {
            final Procedure procedure= (Procedure) procedureService.findById( Long.parseLong( id ) );
            if ( procedure == null ) {
                loggerUtil.log( TransactionType.HCP_DELETE_PROC, LoggerUtil.currentUser(),
                        "Could not find Procedure with id " + id );
                return new ResponseEntity( errorResponse( "No Procedure found with id " + id ), HttpStatus.NOT_FOUND );
            }
            if (procedure.getProcedureStatus() != ProcedureStatus.Assigned){
                loggerUtil.log( TransactionType.HCP_DELETE_PROC, LoggerUtil.currentUser(),
                        "Could not delete Procedure with progress " + ProcedureStatus.getName(procedure.getProcedureStatus().ordinal()) );
                return new ResponseEntity( errorResponse( "Could not delete Procedure with progress " + ProcedureStatus.getName(procedure.getProcedureStatus().ordinal()) ), HttpStatus.BAD_REQUEST );
            }
            procedureService.delete( procedure );
            loggerUtil.log( TransactionType.HCP_DELETE_PROC, LoggerUtil.currentUser(),
                    "Deleted Procedure with id " + procedure.getId() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.HCP_DELETE_PROC, LoggerUtil.currentUser(), "Failed to delete Procedure" );
            return new ResponseEntity( errorResponse( "Could not delete Procedure: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Gets a list of all the Procedure in the system.
     *
     * @return a list of Procedure
     */
    @GetMapping ( "/procedure" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public List<Procedure> getProcedure () {
        loggerUtil.log( TransactionType.HCP_VIEW_PROCS, LoggerUtil.currentUser(), "Fetched list of Procedures" );
        return (List<Procedure>) procedureService.findAll();
    }

    /**
     * Gets a list of Procedure associated with the labtech.
     *
     * @return a list of Procedure
     *
     * KEEP IN MIND the path is BASE_PATH + "/procedureForLabtech"
     */
    @GetMapping ( "/procedureForLabtech" )
    @PreAuthorize ( "hasRole('ROLE_LABTECH')" )
    public List<Procedure> getProcedureForLabtech () {
        final User labtech = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.LABTECH_VIEW_PROCS, LoggerUtil.currentUser(), "Fetched list of Procedures" );
        return procedureService.findByLabtech( labtech );
    }

    /**
     * Gets a list of Procedure associated with the patient.
     *
     * @return a list of Procedure
     *
     * KEEP IN MIND the path is BASE_PATH + "/procedureForPatient"
     */
    @GetMapping ( "/procedureForPatient" )
    @PreAuthorize ( "hasRole('ROLE_PATIENT')" )
    public List<Procedure> getProcedureForPatient () {
        final User patient = userService.findByName( LoggerUtil.currentUser() );
        loggerUtil.log( TransactionType.PATIENT_VIEW_PROCS, LoggerUtil.currentUser(), "Fetched list of Procedures" );
        return procedureService.findByPatient( patient );
    }

    /**
     * Gets a list of Procedure associated with hcp and patient
     *
     * @return a list of Procedure
     */
    @GetMapping ( "/procedureForHcp/{id}" )
    @PreAuthorize ( "hasRole('ROLE_HCP')" )
    public ResponseEntity getProcedureForHcpAndPatient (@PathVariable ( "id" ) final String id ) {
        final User hcp = userService.findByName( LoggerUtil.currentUser() );
        final User patient = userService.findByName( id );
        if ( patient == null ) {
            return new ResponseEntity( errorResponse( "No Patient found with id " + id ), HttpStatus.NOT_FOUND );
        }
        else{
            loggerUtil.log( TransactionType.HCP_VIEW_PROCS, LoggerUtil.currentUser(), "Fetched list of Procedures" );
            return new ResponseEntity( procedureService.findByHcpAndPatient(hcp, patient), HttpStatus.OK);
        }

    }
    /**
     * Reassign Labtech for Procedure. The id stored in the form must match an
     * existing Procedure, reassign to Labtech who has id in parameter
     *
     * @param id
     *            reassigned Labtech id
     * @param form
     *            the Procedure form
     * @return the edited Procedure or an error message
     */
    @PutMapping ( "/procedureReassign/{id}" )
    @PreAuthorize ( "hasRole('ROLE_LABTECH')" )
    public ResponseEntity reassignProcedure ( @PathVariable ( "id" ) final String id, @RequestBody final ProcedureForm form ) {
        try {
            final Procedure savedProcedure = (Procedure) procedureService.findById( form.getId() );
            if ( savedProcedure == null ) throw new Exception("No procedure found");
            final User labtech = userService.findByName( id );
            form.setLabtech( labtech );

            final Procedure procedure = procedureService.build(form);

            procedureService.save( procedure ); /* Overwrite existing Procedure */

            loggerUtil.log( TransactionType.LABTECH_REASSIGN_PROC, LoggerUtil.currentUser(),
                    "Labtech reassign to " + id);
            return new ResponseEntity( procedure, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.LABTECH_REASSIGN_PROC, LoggerUtil.currentUser(), "Failed to reassign Labtech" );
            return new ResponseEntity( errorResponse( "Could not reassigned Labtech: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Returns the Procedure with the specified ID.
     *
     * @param id The id of the Procedure to retrieved
     * @return Response Entity containing the diagnosis if it exists
     */
    @GetMapping("/procedure/{id}")
    public ResponseEntity getProcedure(@PathVariable("id") final Long id) {
        final Procedure d = (Procedure) procedureService.findById(id);
        loggerUtil.log(
                TransactionType.DIAGNOSIS_VIEW_BY_ID,
                LoggerUtil.currentUser(),
                "Retrieved procedure with id " + id);
        return null == d
                ? new ResponseEntity(
                errorResponse("No procedure found for id " + id), HttpStatus.NOT_FOUND)
                : new ResponseEntity(d, HttpStatus.OK);
    }

    /**
     * Returns a list of procedure for a specified office visit
     *
     * @param id The ID of the office visit to get procedure for
     * @return List of procedure objects for the given visit
     */
    @GetMapping("/procedureforvisit/{id}")
    public List<Procedure> getProcedureForVisit(@PathVariable("id") final Long id) {
        // Check if office visit exists
        if (!officeVisitService.existsById(id)) {
            return null;
        }

        final OfficeVisit visit = (OfficeVisit) officeVisitService.findById(id);

        loggerUtil.log(
                TransactionType.DIAGNOSIS_VIEW_BY_OFFICE_VISIT,
                LoggerUtil.currentUser(),
                (visit).getPatient().getUsername(),
                "Retrieved procedures for office visit with id " + id);
        return visit.getProcedures();
    }

    @GetMapping("/officevisit/withprocedure/{procedureId}")
    public ResponseEntity getOfficeVisitByProcedure(@PathVariable Long procedureId) {
        List<? extends DomainObject> domainObjects = officeVisitService.findAll();
        for (DomainObject domainObject : domainObjects) {
            if (domainObject instanceof OfficeVisit) {
                OfficeVisit visit = (OfficeVisit) domainObject;
                if (visit.getProcedures().stream().anyMatch(proc -> proc.getId().equals(procedureId))) {
                    return ResponseEntity.ok(visit);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No OfficeVisit found containing the specified procedure.");
    }

}
