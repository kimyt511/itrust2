package edu.ncsu.csc.itrust2.services;


import edu.ncsu.csc.itrust2.models.DomainObject;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.Review;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class ReviewService extends Service {


   private final ReviewRepository repository;

   @Override protected JpaRepository getRepository(){ return repository; }

   public List<Review> findByPatient(final User patient){
       return repository.findByPatient(patient);
   }
    public List<Review> findByHcp(final User hcp){
        return repository.findByHcp(hcp);
    }
    public List<Review> findByHospital(final Hospital hospital){
        return repository.findByHospital(hospital);
    }
}
