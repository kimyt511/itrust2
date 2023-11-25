package edu.ncsu.csc.itrust2.models;

import com.google.gson.annotations.JsonAdapter;
import edu.ncsu.csc.itrust2.adapters.LocalDateAdapter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "vaccination")
public class Vaccination extends DomainObject{

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @NotNull
    @ManyToOne private Vaccine vaccine;

    @Setter
    @NotNull @Basic
    @Convert(converter = Jsr310JpaConverters.LocalDateConverter.class)
    @JsonAdapter(LocalDateAdapter.class)
    private LocalDate vaccinationDay;

    @Setter
    @NotNull @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    @Setter
    @NotNull
    @Length(max = 500) private String comments;

}
