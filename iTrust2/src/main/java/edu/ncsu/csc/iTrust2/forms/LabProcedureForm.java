package edu.ncsu.csc.iTrust2.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.iTrust2.models.LabProcedure;
import edu.ncsu.csc.iTrust2.models.enums.Priority;
import edu.ncsu.csc.iTrust2.models.enums.Status;

/**
 * A form for REST API communication. Contains fields for constructing Drug
 * objects.
 *
 * @author Connor
 */
public class LabProcedureForm {

    private Long   id;
    private String code;
    private String name;
    private String component;
    private String property;
    private Priority priority;
    private String comment;
    private Status status;

    /**
     * Empty constructor for filling in fields without a Drug object.
     */
    public LabProcedureForm () {
    }

    /**
     * Constructs a new form with information from the given LabProcedure.
     *
     * @param LabProcedure
     *            the LabProcedure object
     */
    public LabProcedureForm ( final LabProcedure LabProcedure ) {
        setId( LabProcedure.getId() );
        setCode( LabProcedure.getCode() );
        setName( LabProcedure.getName() );
        setComponent( LabProcedure.getComponent() );
        setProperty( LabProcedure.getProperty() );
        setPriority( LabProcedure.getPriority() );
        setComment( LabProcedure.getComment() );
        setStatus( LabProcedure.getStatus() );
    }


    public Long getId () {
        return id;
    }

    public void setId ( final Long id ) {
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

    public String getComponent() {
        return component;
    }

    public void setComponent(final String component) {
        this.component = component;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(final String property) {
        this.property = property;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(final Status status) { this.status = status; }
}
