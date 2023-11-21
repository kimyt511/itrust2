package edu.ncsu.csc.itrust2.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import edu.ncsu.csc.itrust2.forms.ProcedureForm;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import edu.ncsu.csc.itrust2.models.enums.Priority;
import edu.ncsu.csc.itrust2.models.enums.ProcedureStatus;
import edu.ncsu.csc.itrust2.models.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Represents a Procedure.
 */
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

    @Getter
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private Long   id;
    @Getter
    @Pattern( regexp = "^\\d{1,5}-\\d$" )
    private String code;
    @Getter
    @NotEmpty
    @Length ( max = 250 )
    private String name;

    @Getter
    @ManyToOne
    @JoinColumn ( name = "hcp_id", columnDefinition = "varchar(100)" )
    private User hcp;
    @Getter
    @NotNull
    @ManyToOne
    @JoinColumn ( name = "labtech_id", columnDefinition = "varchar(100)" )
    private User labtech;

    @Getter
    @ManyToOne
    @JoinColumn ( name = "patient_id", columnDefinition = "varchar(100)" )
    private User patient;

    @Setter @Getter
//    @NotNull
    @ManyToOne
    @JoinColumn(name = "visit_id", nullable = false)
    @JsonBackReference
    private OfficeVisit visit;

    @Getter
    @NotEmpty
    @Length( max = 500 )
    private String comment;
    @Getter
    @Enumerated ( EnumType.STRING )
    private Priority priority;
    @Getter
    @Enumerated ( EnumType.STRING )
    private ProcedureStatus procedureStatus;

    public void setId(final Long id) {
        this.id = id;
    }

    public void setCode(final String code) {
        this.code = code;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setHcp(final User hcp) {
        this.hcp = hcp;
    }

    public void setLabtech(final User labtech) {
        this.labtech = labtech;
    }

    public void setPatient(final User patient) {
        this.patient = patient;
    }

    public void setComment(final String comment) {
        this.comment = comment;
    }

    public void setPriority(final Priority priority) {
        this.priority = priority;
    }

    public void setProcedureStatus(final ProcedureStatus procedureStatus) {
        this.procedureStatus = procedureStatus;
    }
}
