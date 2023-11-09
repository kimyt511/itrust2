package edu.ncsu.csc.iTrust2.models;

import javax.persistence.Basic;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import edu.ncsu.csc.iTrust2.forms.LoincForm;

/**
 * Represents a Loinc.
 */
@Entity
public class Loinc extends DomainObject {

    /** For Hibernate/Thymeleaf _must_ be an empty constructor */
    public Loinc () {
    }

    /**
     * Constructs a new form from the details in the given form
     *
     * @param form
     *            the form to base the new Loinc on
     */
    public Loinc ( final LoincForm form ) {
        setId( form.getId() );
        setCode( form.getCode() );
        setName( form.getName() );
        setComponent( form.getComponent() );
        setProperty( form.getProperty() );
    }

    @Id
    @GeneratedValue ( strategy = GenerationType.AUTO )
    private Long   id;

    @Pattern ( regexp = "^\\d{1,5}-\\d$" )
    private String code;

    @NotEmpty
    @Length ( max = 250 )
    private String name;

    @NotEmpty
    @Length ( max = 250 )
    private String component;

    @NotEmpty
    @Length ( max = 250 )
    private String property;



    public void setId ( final Long id ) {
        this.id = id;
    }

    public Long getId () {
        return id;
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

}
