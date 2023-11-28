package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.Review;
import edu.ncsu.csc.itrust2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByPatient(User patient);
    List<Review> findByHcp(User hcp);
    List<Review> findByHospital(Hospital hospital);

}
