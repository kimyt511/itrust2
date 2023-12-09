package edu.ncsu.csc.itrust2.models;

import edu.ncsu.csc.itrust2.forms.ReviewForm;
import edu.ncsu.csc.itrust2.services.UserService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Entity
public class Review extends DomainObject {

    /** The id of this review */
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The patient who left this review */
    @Setter
    @NotNull @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The hcp who has received this review */
    @Setter
    @ManyToOne
    @JoinColumn(name = "hcp_id", columnDefinition = "varchar(100)")
    private User hcp;

    /** The hospital which has received this review */
    @Setter
    @ManyToOne
    @JoinColumn(name = "hospital_id", columnDefinition = "varchar(100)")
    private Hospital hospital;

    @Getter
    @Setter
    @NotNull
    private double rate;

    @Setter
    @Length(max = 500)
    private String comment;

}
