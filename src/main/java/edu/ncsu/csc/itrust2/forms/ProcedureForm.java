package edu.ncsu.csc.itrust2.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.Procedure;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.Priority;
import edu.ncsu.csc.itrust2.models.enums.ProcedureStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * A form for REST API communication. Contains fields for constructing Procedure
 * objects.
 *
 *
 */

@Setter
@Getter
@NoArgsConstructor
public class ProcedureForm {

    private Long   id;

    private String code;
    private String name;
    private User hcp;
    private User labtech;
    private User patient;
    private String comment;
    private Priority priority;
    private ProcedureStatus procedureStatus;
    private Long visit;

    /**
     * Constructs a new form with information from the given Procedure.
     *
     * @param procedure
     *            the Procedure object
     */
    public ProcedureForm ( final Procedure procedure ) {
        setId( procedure.getId() );
        setCode( procedure.getCode() );
        setName( procedure.getName() );
        setHcp(procedure.getHcp());
        setLabtech(procedure.getLabtech());
        setPatient(procedure.getPatient());
        setComment(procedure.getComment());
        setPriority(procedure.getPriority());
        setProcedureStatus(procedure.getProcedureStatus());

        if(procedure.getVisit() != null){
            setVisit(procedure.getVisit().getId());
        }
    }


}
