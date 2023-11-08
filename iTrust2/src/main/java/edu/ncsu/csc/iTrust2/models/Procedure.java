package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.iTrust2.forms.ProcedureForm;
import org.hibernate.validator.constraints.Length;
import edu.ncsu.csc.iTrust2.models.enums.Priority;
import edu.ncsu.csc.iTrust2.models.enums.ProcedureStatus;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

/**
 * Represents a Procedure.
 */
@Entity
public class Procedure extends DomainObject {
    /** For Hibernate/Thymeleaf _must_ be an empty constructor */
    public Procedure(){

    }
    /**
     * Constructs a new form from the details in the given form
     *
     * @param form
     *            the form to base the new Procedure on
     */
    public Procedure ( final ProcedureForm form ) {
        setId( form.getId() );
        setLabtech(form.getLabtech());
        setPatient(form.getPatient());
        setComment(form.getComment());
        setPriority(form.getPriority());
        setProcedureStatus(ProcedureStatus.Assigned);
    }

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long   id;

    @NotEmpty
    private String labtech;

    @NotEmpty
    private String patient;

    @NotEmpty
    @Length( max = 500 )
    private String comment;

    @NotEmpty
    private Priority priority;

    @NotEmpty
    private ProcedureStatus procedureStatus;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
    public String getLabtech() {
        return labtech;
    }

    public void setLabtech(final String labtech) {
        this.labtech = labtech;
    }
    public String getPatient() {
        return patient;
    }

    public void setPatient(final String patient) {
        this.patient = patient;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public Priority getPriority() {
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
