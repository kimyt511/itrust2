package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.PersonalRepresentative;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * Personal Representative form used to create representative relationship.
 *
 */
@Setter
@NoArgsConstructor
@Getter
public class PersonalRepresentativeForm implements Serializable {
    /** Name of the patient */
    @NotEmpty private String patient;

    /** Name of the representative */
    @NotEmpty private String representative;

    /** ID of the relationship */
    private String id;

    /**
     * Create a PersonalRepresentativeForm from the Personal Representative provided
     *
     */
    public PersonalRepresentativeForm(@NotNull final PersonalRepresentative pr) {
        setPatient(pr.getPatient().getUsername());
        setRepresentative(pr.getRepresentative().getUsername());
        setId(pr.getId().toString());
    }
}
