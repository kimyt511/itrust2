package edu.ncsu.csc.iTrust2.controllers.api;

import java.util.List;

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

import edu.ncsu.csc.iTrust2.forms.LabProcedureForm;
import edu.ncsu.csc.iTrust2.models.LabProcedure;
import edu.ncsu.csc.iTrust2.models.enums.Priority;
import edu.ncsu.csc.iTrust2.models.enums.Status;
import edu.ncsu.csc.iTrust2.models.enums.TransactionType;
import edu.ncsu.csc.iTrust2.services.LabProcedureService;
import edu.ncsu.csc.iTrust2.utils.LoggerUtil;

/**
 * Provides REST endpoints that deal with labprocedure. Exposes functionality to add,
 * edit, fetch, and delete labprocedure.
 *
 * @author Connor
 * @author Kai Presler-Marshall
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
public class APILabProcedureController extends APIController {

    @Autowired
    private LabProcedureService service;

    @Autowired
    private LoggerUtil  loggerUtil;

    /**
     * Adds a new LabProcedure to the system. Requires admin permissions. Returns an
     * error message if something goes wrong.
     *
     * @param form
     *            the LabProcedure form
     * @return the created LabProcedure
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PostMapping ( BASE_PATH + "/labprocedure" )
    public ResponseEntity addLabProcedure ( @RequestBody final LabProcedureForm form ) {
        try {
            final LabProcedure LabProcedure = new LabProcedure( form );

            // Make sure code does not conflict with existing LabProcedure
            if ( service.existsByCode( LabProcedure.getCode() ) ) {
                loggerUtil.log( TransactionType.LABPROCEDURE_CREATE, LoggerUtil.currentUser(),
                        "Conflict: lab procedure with code " + LabProcedure.getCode() + " already exists" );
                return new ResponseEntity( errorResponse( "Lab Procedure with code " + LabProcedure.getCode() + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            service.save( LabProcedure );
            loggerUtil.log( TransactionType.LABPROCEDURE_CREATE, LoggerUtil.currentUser(),
                    "Lab procedure " + LabProcedure.getCode() + " created" );
            return new ResponseEntity( LabProcedure, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.LABPROCEDURE_CREATE, LoggerUtil.currentUser(), "Failed to create lab procedure" );
            return new ResponseEntity( errorResponse( "Could not add lab procedure: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Edits a lab procedure in the system. The id stored in the form must match an
     * existing lab procedure, and changes to NDCs cannot conflict with existing NDCs.
     * Requires admin permissions.
     *
     * @param form
     *            the edited lab procedure form
     * @return the edited lab procedure or an error message
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PutMapping ( BASE_PATH + "/labprocedure" )
    public ResponseEntity editLabProcedure ( @RequestBody final LabProcedureForm form ) {
        try {
            // Check for existing lab procedure in database
            final LabProcedure savedLabProcedure = (LabProcedure) service.findById( form.getId() );
            if ( savedLabProcedure == null ) {
                return new ResponseEntity( errorResponse( "No lab procedure found with code " + form.getCode() ),
                        HttpStatus.NOT_FOUND );
            }

            final LabProcedure LabProcedure = new LabProcedure( form );

            // If the code was changed, make sure it is unique
            final LabProcedure sameCode = service.findByCode( LabProcedure.getCode() );
            if ( sameCode != null && !sameCode.getId().equals( savedLabProcedure.getId() ) ) {
                return new ResponseEntity( errorResponse( "Lab procedure with code " + LabProcedure.getCode() + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            service.save( LabProcedure ); /* Overwrite existing drug */

            loggerUtil.log( TransactionType.LABPROCEDURE_EDIT, LoggerUtil.currentUser(),
                    "Lab procedure with id " + LabProcedure.getId() + " edited" );
            return new ResponseEntity( LabProcedure, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.LABPROCEDURE_EDIT, LoggerUtil.currentUser(), "Failed to edit lab procedure" );
            return new ResponseEntity( errorResponse( "Could not update lab procedure: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes the lab procedure with the id matching the given id. Requires admin
     * permissions.
     *
     * @param id
     *            the id of the lab procedure to delete
     * @return the id of the deleted lab procedure
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @DeleteMapping ( BASE_PATH + "/labprocedure/{id}" )
    public ResponseEntity deleteLabProcedure ( @PathVariable final String id ) {
        try {
            final LabProcedure LabProcedure = (LabProcedure) service.findById( Long.parseLong( id ) );
            if ( LabProcedure == null ) {
                loggerUtil.log( TransactionType.LABPROCEDURE_DELETE, LoggerUtil.currentUser(),
                        "Could not find lab procedure with id " + id );
                return new ResponseEntity( errorResponse( "No lab procedure found with id " + id ), HttpStatus.NOT_FOUND );
            }
            service.delete( LabProcedure );
            loggerUtil.log( TransactionType.LABPROCEDURE_DELETE, LoggerUtil.currentUser(),
                    "Deleted lab procedure with id " + LabProcedure.getId() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.LABPROCEDURE_DELETE, LoggerUtil.currentUser(), "Failed to delete lab procedure" );
            return new ResponseEntity( errorResponse( "Could not delete lab procedure: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Gets a list of all the drugs in the system.
     *
     * @return a list of drugs
     */
    @GetMapping ( BASE_PATH + "/labprocedure" )
    public List<LabProcedure> getLabProcedure () {
        loggerUtil.log( TransactionType.LABPROCEDURE_VIEW, LoggerUtil.currentUser(), "Fetched list of lab procedures" );
        return (List<LabProcedure>) service.findAll();
    }

}
