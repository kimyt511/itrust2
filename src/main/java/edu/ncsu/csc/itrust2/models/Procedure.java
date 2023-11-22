package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.forms.ProcedureForm;
import org.hibernate.validator.constraints.Length;
import edu.ncsu.csc.itrust2.models.enums.Priority;
import edu.ncsu.csc.itrust2.models.enums.ProcedureStatus;
import edu.ncsu.csc.itrust2.models.User;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Represents a Procedure.
 */

@Getter
@Entity
@Table(name="lab_procedure")
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
    /*
    public Procedure ( final ProcedureForm form ) {
        setId( form.getId() );
        setCode (form.getCode() );
        setName( form.getName());
        setHcp(form.getHcp());
        setLabtech(form.getLabtech());
        setPatient(form.getPatient());
        setComment(form.getComment());
        setPriority(form.getPriority());
        setProcedureStatus(form.getProcedureStatus());
    }
    */

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long   id;
    @Pattern( regexp = "^\\d{1,5}-\\d$" )
    private String code;
    @NotEmpty
    @Length ( max = 250 )
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User hcp;
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "labtech_id", columnDefinition = "varchar(100)" )
    private User labtech;

    @NotNull
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User patient;

    @NotEmpty
    @Length( max = 500 )
    private String comment;
    @Enumerated ( EnumType.STRING )
    private Priority priority;
    @Enumerated ( EnumType.STRING )
    private ProcedureStatus procedureStatus;

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
