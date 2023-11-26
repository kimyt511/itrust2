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
    private Long vaccineId; // ID of the vaccine used

    @NotNull
    private Long officeVisitId; // ID of the office visit during which the vaccine was administered

    @NotNull
    private String patientUsername; // Username of the patient

    @NotNull
    private LocalDate dateAdministered; // Date when the vaccine was administered

    /**
     * Constructs a new form with information from the given vaccination.
     *
     * @param vaccination the vaccination object
     */
    public VaccinationForm(final Vaccination vaccination) {
        if (vaccination != null) {
            this.id = vaccination.getId();
            this.vaccineId = vaccination.getVaccine().getId();
            this.officeVisitId = vaccination.getOfficeVisit().getId();
            this.patientUsername = vaccination.getPatient().getUsername();
            this.dateAdministered = vaccination.getDateAdministered();
        }
    }
}
