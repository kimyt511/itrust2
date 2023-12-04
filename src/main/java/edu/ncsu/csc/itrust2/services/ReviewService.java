package edu.ncsu.csc.itrust2.services;


import edu.ncsu.csc.itrust2.forms.ReviewForm;
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
   private final UserService userService;
   private final HospitalService hospitalService;

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

    public Review build(final ReviewForm rvf){
       final Review rv = new Review();

       rv.setPatient(userService.findByName(rvf.getPatient()));
       rv.setHcp(userService.findByName(rvf.getHcp()));
       rv.setHospital(hospitalService.findByName(rvf.getHospital()));

       if (rvf.getId() != null){
           rv.setId(rvf.getId());
       }
       rv.setRate(rvf.getRate());
       rv.setComment(rvf.getComment());

       return rv;
    }
    public Double averageHcp(final User hcp){
        List<Review> reviews = repository.findByHcp(hcp);
        Double rateSum = 0.0;
        for(Review review : reviews){
            rateSum += review.getRate();
        }
        return rateSum / reviews.size();
    }

    public Double averageHospital(final Hospital hospital){
        List<Review> reviews = repository.findByHospital(hospital);
        Double rateSum = 0.0;
        for(Review review : reviews){
            rateSum += review.getRate();
        }
        return rateSum / reviews.size();
    }
}
