package edu.ncsu.csc.itrust2.forms;

import edu.ncsu.csc.itrust2.models.Review;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.Hospital;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.SpringVersion;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ReviewForm {

    private Long id;
    private User patient;
    private User hcp;
    private Hospital hospital;
    private double rate;
    private String comment;

    /**
     * Constructs a new form with information from the given review.
     *
     * @param review the review object
     */
    public ReviewForm(@NotNull final Review review){
        setId(review.getId());
        setPatient(review.getPatient());
        setHcp(review.getHcp());
        setHospital(review.getHospital());
        setRate(review.getRate());
        setComment(review.getComment());
    }
}
