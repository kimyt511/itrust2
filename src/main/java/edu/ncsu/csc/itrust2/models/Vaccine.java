package edu.ncsu.csc.itrust2.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a vaccine in the system.
 */
@NoArgsConstructor
@Getter
@Entity
@Table(name = "vaccines")
public class Vaccine extends DomainObject {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotEmpty
    @Size(max = 250)
    private String name;

    @Setter
    @NotEmpty
    @Size(max = 10)
    private String abbreviation;

    @Setter
    @NotEmpty
    @Pattern(regexp = "90\\d{3}")
    private String cptCode;

    @Setter
    @Size(max = 500)
    private String comments;
}
