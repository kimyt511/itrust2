package edu.ncsu.csc.itrust2.unit;


import edu.ncsu.csc.itrust2.TestConfig;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.services.HospitalService;
import edu.ncsu.csc.itrust2.services.ReviewService;
import edu.ncsu.csc.itrust2.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(classes = TestConfig.class)
public class ReviewTest {

    @Autowired private ReviewService reviewService;
    @Autowired private UserService userService;
    @Autowired private HospitalService hospitalService;

    @Before
    public void setup(){
        reviewService.deleteAll();

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));
        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));
        final Hospital hospital = new Hospital("hospital", "123","12345", "NC");

        userService.saveAll(List.of(hcp, patient));
        hospitalService.save(hospital);
    }

    @Test
    @Transactional
    public void testReviewHCP(){
        Assert.assertEquals(0, reviewService.count());

        final Review review1 = new Review();
        review1.setPatient(userService.findByName("patient"));
        review1.setHcp(userService.findByName("hcp"));
        review1.setRate(0.0);
        reviewService.save(review1);

        Assert.assertEquals(1, reviewService.count());

        Review retrieved = (Review) reviewService.findAll().get(0);

        Assert.assertEquals("patient", retrieved.getPatient().getId());
        Assert.assertEquals("hcp", retrieved.getHcp().getId());
        Assert.assertEquals(0.0, retrieved.getRate(),0.01);
    }

    @Test
    @Transactional
    public void testReviewHospital(){
        Assert.assertEquals(0, reviewService.count());

        final Review review1 = new Review();
        review1.setPatient(userService.findByName("patient"));
        review1.setHospital(hospitalService.findByName("hospital"));
        review1.setRate(0.0);
        reviewService.save(review1);

        Assert.assertEquals(1, reviewService.count());

        Review retrieved = (Review) reviewService.findAll().get(0);

        Assert.assertEquals("patient", retrieved.getPatient().getId());
        Assert.assertEquals("hospital", retrieved.getHospital().getId());
        Assert.assertEquals(0.0, retrieved.getRate(),0.01);
    }

    @After
    public void deleteData(){
        reviewService.deleteAll();
        userService.deleteAll();
        hospitalService.deleteAll();
    }


}
