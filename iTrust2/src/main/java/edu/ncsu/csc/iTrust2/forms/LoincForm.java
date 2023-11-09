package edu.ncsu.csc.iTrust2.forms;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import edu.ncsu.csc.iTrust2.models.Loinc;

/**
 * A form for REST API communication. Contains fields for constructing LOINC
 * objects.
 *
 * @author Connor
 */
public class LoincForm {

    private Long   id;
    private String code;
    private String name;
    private String component;
    private String property;

    /**
     * Empty constructor for filling in fields without a LOINC object.
     */
    public LoincForm () {
    }

    /**
     * Constructs a new form with information from the given Loinc.
     *
     * @param Loinc
     *            the Loinc object
     */
    public LoincForm ( final Loinc loinc ) {
        setId( loinc.getId() );
        setCode( loinc.getCode() );
        setName( loinc.getName() );
        setComponent( loinc.getComponent() );
        setProperty( loinc.getProperty() );
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

}
