package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.Vaccination;
import edu.ncsu.csc.itrust2.models.Vaccine;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@NoArgsConstructor
@Getter
public class VaccinationForm implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String vaccine;
    private String vaccinationDay;
    private String patient;
    private String comments;

    public VaccinationForm(final Vaccination v){
        this.id = v.getId();
        this.vaccine = v.getVaccine().getName();
        this.vaccinationDay = v.getVaccinationDay().toString();
        this.patient = v.getPatient().getUsername();
        this.comments = v.getComments();
    }

}
