package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.forms.VaccineForm;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@NoArgsConstructor
@Getter
@Entity
@Table(name = "vaccine")
public class Vaccine extends DomainObject{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotEmpty
    @Length(max = 250) private String name;

    @Setter
    @NotEmpty
    @Length(max = 10) private String abbreviation;

    @Setter
    @NotEmpty
    @Length(max = 5) private String cptcode;

    @Setter
    @NotNull
    @Length(max = 500) private String comments;

    public Vaccine(String name, String abbreviation, String cptcode, String comments){
        this.name = name;
        this.abbreviation = abbreviation;
        this.cptcode = cptcode;
        this.comments = comments;
    }

    public Vaccine(VaccineForm v){
        this.name = v.getName();
        this.abbreviation = v.getAbbreviation();
        this.cptcode = v.getCptcode();
        this.comments = v.getComments();
    }


}
