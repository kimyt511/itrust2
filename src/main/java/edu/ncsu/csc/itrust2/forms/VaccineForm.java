package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.Drug;
import edu.ncsu.csc.itrust2.models.Vaccine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class VaccineForm {

    private Long id;
    private String name;
    private String abbreviation;
    private String cptcode;
    private String comments;

    /**
     * Constructs a new form with information from the given drug.
     *
     * @param drug the drug object
     */
    public VaccineForm(@NotNull final Vaccine v) {
        if(v.getId() != null){
            this.id = v.getId();
        }
        this.name = v.getName();
        this.abbreviation = v.getAbbreviation();
        this.cptcode = v.getCptCode();
        this.comments = v.getComments();
    }
}
