package edu.ncsu.csc.itrust2.controllers.api;

import edu.ncsu.csc.itrust2.forms.LoincForm;
import edu.ncsu.csc.itrust2.models.Loinc;
import edu.ncsu.csc.itrust2.models.enums.TransactionType;
import edu.ncsu.csc.itrust2.services.LoincService;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides REST endpoints that deal with loinc. Exposes functionality to add,
 * edit, fetch, and delete LOINC.
 *
 */
@SuppressWarnings ( { "unchecked", "rawtypes" } )
@RestController
@RequiredArgsConstructor
public class APILoincController extends APIController {

    @Autowired
    private LoincService service;

    @Autowired
    private LoggerUtil  loggerUtil;

    /**
     * Adds a new LOINC to the system. Requires admin permissions. Returns an
     * error message if something goes wrong.
     *
     * @param form
     *            the LOINC form
     * @return the created LOINC
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PostMapping ( "/loinccodes" )
    public ResponseEntity addLoinc ( @RequestBody final LoincForm form ) {
        try {
            final Loinc loinc = new Loinc( form );

            // Make sure code does not conflict with existing Loinc
            if ( service.existsByCode( loinc.getCode() ) ) throw new Exception("LOINC with code " + loinc.getCode() + "already exists");

            service.save( loinc );
            loggerUtil.log( TransactionType.LOINC_CREATE, LoggerUtil.currentUser(),
                    "LOINC " + loinc.getCode() + " created" );
            return new ResponseEntity( loinc, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.LOINC_CREATE, LoggerUtil.currentUser(), "Failed to create LOINC" );
            return new ResponseEntity( errorResponse( "Could not add LOINC: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Edits a LOINC in the system. The id stored in the form must match an
     * existing LOINC, and changes to NDCs cannot conflict with existing NDCs.
     * Requires admin permissions.
     *
     * @param form
     *            the edited LOINC form
     * @return the edited LOINC or an error message
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    @PutMapping ( "/loinccodes" ) // Keep an eye on nothing added after loinccodes.
    public ResponseEntity editLoinc ( @RequestBody final LoincForm form ) {
        try {
            // Check for existing LOINC in database
            final Loinc savedLoinc = (Loinc) service.findById( form.getId() );
            if ( savedLoinc == null ) throw new Exception("No LOINC found with code " + form.getCode());

            final Loinc loinc = new Loinc( form );

            // If the code was changed, make sure it is unique
            final Loinc sameCode = service.findByCode( loinc.getCode() );
            if ( sameCode != null && !sameCode.getId().equals( savedLoinc.getId() ) ) {
                return new ResponseEntity( errorResponse( "LOINC with code " + loinc.getCode() + " already exists" ),
                        HttpStatus.CONFLICT );
            }

            service.save( loinc ); /* Overwrite existing LOINC */

            loggerUtil.log( TransactionType.LOINC_EDIT, LoggerUtil.currentUser(),
                    "LOINC with id " + loinc.getId() + " edited" );
            return new ResponseEntity( loinc, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.LOINC_EDIT, LoggerUtil.currentUser(), "Failed to edit LOINC" );
            return new ResponseEntity( errorResponse( "Could not update LOINC: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Deletes the LOINC with the id matching the given id. Requires admin
     * permissions.
     *
     * @param id
     *            the id of the LOINC to delete
     * @return the id of the deleted LOINC
     */
    @PreAuthorize ( "hasRole('ROLE_ADMIN')" )
    // CHECK DELETE MAPPING FOR REFERENCE.
    // ALWAYS TAKE REFERENCE FROM API FORMATS
    @DeleteMapping ( "/loinccodes/{id}" ) //Keep an eye on {id}
    public ResponseEntity deleteLoinc ( @PathVariable final String id ) {
        try {
            final Loinc loinc = (Loinc) service.findById( Long.parseLong( id ) );
            if ( loinc == null ) throw new Exception("No LOINC found with id " + id);
            service.delete( loinc );
            loggerUtil.log( TransactionType.LOINC_DELETE, LoggerUtil.currentUser(),
                    "Deleted LOINC with id " + loinc.getId() );
            return new ResponseEntity( id, HttpStatus.OK );
        }
        catch ( final Exception e ) {
            loggerUtil.log( TransactionType.LOINC_DELETE, LoggerUtil.currentUser(), "Failed to delete LOINC" );
            return new ResponseEntity( errorResponse( "Could not delete LOINC: " + e.getMessage() ),
                    HttpStatus.BAD_REQUEST );
        }
    }

    /**
     * Gets a list of all the LOINC in the system.
     *
     * @return a list of LOINC
     */
    @GetMapping ( "/loinccodes" )
    public List<Loinc> getLoinc () {
        return (List<Loinc>) service.findAll();
    }

}
