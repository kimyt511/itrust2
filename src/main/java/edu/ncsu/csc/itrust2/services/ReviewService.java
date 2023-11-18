package edu.ncsu.csc.itrust2.services;


import edu.ncsu.csc.itrust2.models.DomainObject;
import edu.ncsu.csc.itrust2.models.Review;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
@Component
@Transactional
@RequiredArgsConstructor
public class ReviewService extends Service {


   private final ReviewRepository repository;

   @Override protected JpaRepository getRepository(){ return repository; }

   public boolean existsByHcpAndPatient(final User hcp, final User patient){
       return repository.existsByHcpAndPatient(hcp, patient);
   }
   public Review findByHcpAndPatient(final User hcp, final User patient){
       return repository.findByHcpAndPatient(hcp, patient);
   }
}
