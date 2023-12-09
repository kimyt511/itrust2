package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.Vaccine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
public class VaccineForm {

    @NotEmpty
    private Long id;

    @NotEmpty
    @Size(max = 250)
    private String name;

    @Size(max = 10)
    private String abbreviation;

    @NotEmpty
    @Pattern(regexp = "^90\\d{3}$", message = "CPT Code must start with 90 and be 5 digits long")
    private String cptCode;

    @Length(max = 500)
    private String comments;

    /**
     * Constructs a new form with information from the given vaccine.
     *
     * @param vaccine the vaccine object
     */
    public VaccineForm(final Vaccine vaccine) {
        if(vaccine.getId() != null){
            this.id = vaccine.getId();
        }
        this.name = vaccine.getName();
        this.abbreviation = vaccine.getAbbreviation();
        this.cptCode = vaccine.getCptCode();
        this.comments = vaccine.getComments();
    }
}
