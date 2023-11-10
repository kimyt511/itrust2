package edu.ncsu.csc.iTrust2.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.iTrust2.models.Procedure;
import edu.ncsu.csc.iTrust2.models.User;
import edu.ncsu.csc.iTrust2.models.enums.Priority;
import edu.ncsu.csc.iTrust2.models.enums.ProcedureStatus;
/**
 * A form for REST API communication. Contains fields for constructing Procedure
 * objects.
 *
 *
 */
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

    /**
     * Empty constructor for filling in fields without a Procedure object.
     */
    public ProcedureForm () {
    }

    /**
     * Constructs a new form with information from the given Procedure.
     *
     * @param Procedure
     *            the Procedure object
     */
    public ProcedureForm ( final Procedure Procedure ) {
        setId( Procedure.getId() );
        setCode( Procedure.getCode() );
        setName( Procedure.getName() );
        setHcp(Procedure.getHcp());
        setLabtech(Procedure.getLabtech());
        setPatient(Procedure.getPatient());
        setComment(Procedure.getComment());
        setPriority(Procedure.getPriority());
        setProcedureStatus(edu.ncsu.csc.iTrust2.models.enums.ProcedureStatus.Assigned);
    }


    public Long getId() {
        return id;
    }
    public void setId(final Long id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(final String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(final String name) {
        this.name = name;
    }
    public User getHcp() {
        return hcp;
    }
    public void setHcp(final User hcp) {
        this.hcp = hcp;
    }
    public User getLabtech() {
        return labtech;
    }
    public void setLabtech(final User labtech) {
        this.labtech = labtech;
    }
    public User getPatient() {
        return patient;
    }
    public void setPatient(final User patient) {
        this.patient = patient;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(final String comment) {
        this.comment = comment;
    }
    public edu.ncsu.csc.iTrust2.models.enums.Priority getPriority() {
        return priority;
    }
    public void setPriority(final Priority priority) {
        this.priority = priority;
    }
    public ProcedureStatus getProcedureStatus() {
        return procedureStatus;
    }
    public void setProcedureStatus(final ProcedureStatus procedureStatus) {
        this.procedureStatus = procedureStatus;
    }

}
