package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import java.time.LocalDate;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import com.google.gson.annotations.JsonAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters.LocalDateConverter;

/**
 * Represents a vaccination record in the system. Each vaccination is associated with a patient,
 * a vaccine, and an office visit.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "vaccinations")
public class Vaccination extends DomainObject {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vaccine_id")
    private Vaccine vaccine;

    @Setter
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "office_visit_id")
    private OfficeVisit officeVisit;

    @Setter
    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    @Setter
    @NotNull
    @Basic
    @Convert(converter = LocalDateConverter.class)
    @JsonAdapter(LocalDateAdapter.class)
    private LocalDate dateAdministered;
}
