package edu.ncsu.csc.itrust2.repositories;

import edu.ncsu.csc.itrust2.models.Review;
import edu.ncsu.csc.itrust2.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByHcpAndPatient(User hcp, User patient);
    Review findByHcpAndPatient(User hcp, User patient);

}
