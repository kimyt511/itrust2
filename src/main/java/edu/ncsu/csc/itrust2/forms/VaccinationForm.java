package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.Vaccination;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A form for REST API communication. Contains fields for constructing Vaccination objects.
 */
@Setter
@NoArgsConstructor
@Getter
public class VaccinationForm implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    private Long id; // Vaccination ID

    @NotNull
    private String vaccineCptCode; // ID of the vaccine used

    @NotNull
    private String patient; // Username of the patient

    @NotNull
    private LocalDate dateAdministered; // Date when the vaccine was administered

    private String comments; // Comments about the vaccination

    /**
     * Constructs a new form with information from the given vaccination.
     *
     * @param vaccination the vaccination object
     */
    public VaccinationForm(final Vaccination vaccination) {
        if (vaccination != null) {
            this.id = vaccination.getId();
            this.vaccineCptCode = vaccination.getVaccine().getCptCode();
            this.patient = vaccination.getPatient().getUsername();
            this.dateAdministered = vaccination.getDateAdministered();
            this.comments = vaccination.getComments();
        }
    }
}
