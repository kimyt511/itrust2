package edu.ncsu.csc.itrust2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "personal_representative")
public class PersonalRepresentative extends DomainObject {

    /** The patient */
    @Setter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id", columnDefinition = "varchar(100)")
    private User patient;

    /** The representative */
    @Setter
    @NotNull
    @ManyToOne
    @JoinColumn(name = "representative_id", columnDefinition = "varchar(100)")
    private User representative;

    /** Comments */
    @Setter
    private String comment;

    /** The id of this relationship */
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
