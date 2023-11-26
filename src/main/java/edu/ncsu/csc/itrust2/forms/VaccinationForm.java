package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.Vaccination;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

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

    @NotEmpty
    private Long id; // Vaccination ID

    @NotEmpty
    @Pattern(regexp = "^90\\d{3}$", message = "CPT Code must start with 90 and be 5 digits long")
    private String vaccineCptCode; // ID of the vaccine used

    @NotEmpty
    @Size(max = 20)
    private String patientUserName; // Username of the patient

    @NotEmpty
    private LocalDate dateAdministered; // Date when the vaccine was administered

    @Length(max = 500)
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
            this.patientUserName = vaccination.getPatient().getUsername();
            this.dateAdministered = vaccination.getDateAdministered();
        }
    }
}
