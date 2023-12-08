package edu.ncsu.csc.itrust2.api;


import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.ReviewForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.services.HospitalService;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;
import edu.ncsu.csc.itrust2.services.ReviewService;
import edu.ncsu.csc.itrust2.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIReviewTest {

    private MockMvc mvc;
    @Autowired private WebApplicationContext context;
    @Autowired private ReviewService service;
    @Autowired private UserService userService;
    @Autowired private HospitalService hospitalService;
    @Autowired private OfficeVisitService officeVisitService;

    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        service.deleteAll();

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));
        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));
        final Hospital hospital = new Hospital("hospital", "123","12345", "NC");

        userService.saveAll(List.of(hcp, patient));
        hospitalService.save(hospital);
        officeVisitService.deleteAll();
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testAddHcpReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHcp("hcp");
        form.setRate(0.0);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/reviews/hcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(1, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hcp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testAddHospitalReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHospital("hospital");
        form.setRate(0.0);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hospital")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(1, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hospital")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testAddInvalidReview() throws Exception{
        final ReviewForm form1 = new ReviewForm();
        form1.setPatient("patient");

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hcp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form1)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hospital")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form1)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        final ReviewForm form2 = new ReviewForm();
        form2.setHcp("patient");
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hcp")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(form2)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        form2.setHospital("");
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hospital")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(form2)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testEditHcpReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHcp("hcp");
        form.setRate(0.0);

        final Review review = service.build(form);
        service.save(review);
        Long id = service.findByPatient(userService.findByName("patient")).get(0).getId();
        form.setId(id);
        form.setRate(2.5);
        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/reviews/hcp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final Review retrieved = service.findByPatient(userService.findByName("patient")).get(0);

        Assert.assertEquals(2.5, retrieved.getRate(), 0.01);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testEditHospitalReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHospital("hospital");
        form.setRate(0.0);

        final Review review = service.build(form);
        service.save(review);

        Long id = service.findByPatient(userService.findByName("patient")).get(0).getId();
        form.setId(id);
        form.setRate(2.5);
        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/reviews/hospital")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final Review retrieved = service.findByPatient(userService.findByName("patient")).get(0);

        Assert.assertEquals(2.5, retrieved.getRate(), 0.01);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testEditInvalidReview() throws Exception{
        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/reviews/hcp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/reviews/hospital")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/reviews/hcp"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/reviews/hospital"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testDeleteHcpReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHcp("hcp");
        form.setRate(0.0);

        final Review review = service.build(form);
        service.save(review);

        Long id = service.findByPatient(userService.findByName("patient")).get(0).getId();

        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/reviews/hcp/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(0, service.count());
    }
    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testDeleteHospitalReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHospital("hospital");
        form.setRate(0.0);

        final Review review = service.build(form);
        service.save(review);

        Long id = service.findByPatient(userService.findByName("patient")).get(0).getId();

        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/reviews/hospital/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(0, service.count());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testDeleteInvalidReview() throws Exception{
        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/reviews/hcp/" + 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/reviews/hospital/" + 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/reviews/hcp/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/reviews/hospital/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());
    }

    @Test
    @Transactional
    public void testGetPatientReviews() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHcp("hcp");
        form.setRate(0.0);

        final Review review = service.build(form);
        service.save(review);
        Long id = service.findByPatient(userService.findByName("patient")).get(0).getId();
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/patient/" + "patient"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id));
    }

    @Test
    @Transactional
    public void testGetPatientInvalidReview() throws Exception{
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/patient/" + "patient2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/patient/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    public void testGetHcpReviews() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHcp("hcp");
        form.setRate(0.0);

        final Review review = service.build(form);
        service.save(review);
        Long id = service.findByPatient(userService.findByName("patient")).get(0).getId();
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hcp/" + "hcp"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id));
    }

    @Test
    @Transactional
    public void testGetHcpInvalidReview() throws Exception{
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hcp/" + "hcp2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hcp/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    public void testGetHospitalReviews() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHospital("hospital");
        form.setRate(0.0);

        final Review review = service.build(form);
        service.save(review);
        Long id = service.findByPatient(userService.findByName("patient")).get(0).getId();
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hospital/" + "hospital"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[0].id").value(id));
    }

    @Test
    @Transactional
    public void testGetHospitalInvalidReview() throws Exception{
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hospital/" + "hospital2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hospital/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    public void testGetAverageHcp() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHcp("hcp");
        form.setRate(0.0);

        final Review review1 = service.build(form);
        service.save(review1);
        form.setRate(5.0);
        final Review review2 = service.build(form);
        service.save(review2);

        Assert.assertEquals(2, service.count());

        Long id = service.findByPatient(userService.findByName("patient")).get(0).getId();
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/average/hcp/" + "hcp"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("2.5"));

    }
    @Test
    @Transactional
    public void testGetInvalidAverageHcp() throws Exception{
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/average/hcp/" + "hcp2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/average/hcp/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    public void testGetAverageHospital() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient("patient");
        form.setHospital("hospital");
        form.setRate(0.0);

        final Review review1 = service.build(form);
        service.save(review1);
        form.setRate(5.0);
        final Review review2 = service.build(form);
        service.save(review2);

        Assert.assertEquals(2, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/average/hospital/" + "hospital"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("2.5"));

    }
    @Test
    @Transactional
    public void testGetInvalidAverageHospital() throws Exception{
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/average/hospital/" + "hospital2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/average/hospital/"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testGetVisitedHcps() throws Exception{
        Assert.assertEquals(0, officeVisitService.count());

        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate("2030-11-19T04:50:00.000-05:00");
        visit.setHcp("hcp");
        visit.setPatient("patient");
        visit.setNotes("Test office visit");
        visit.setType(AppointmentType.GENERAL_CHECKUP.toString());
        visit.setHospital("hospital");

        OfficeVisit v = officeVisitService.build(visit);
        officeVisitService.save(v);
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hcps/" + "patient"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[0].username").value("hcp"));
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testGetInvalidVisitedHcps() throws Exception{
        Assert.assertEquals(0, officeVisitService.count());

        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hcps/" + "patient2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testGetVisitedHospitals() throws Exception{
        Assert.assertEquals(0, officeVisitService.count());

        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate("2030-11-19T04:50:00.000-05:00");
        visit.setHcp("hcp");
        visit.setPatient("patient");
        visit.setNotes("Test office visit");
        visit.setType(AppointmentType.GENERAL_CHECKUP.toString());
        visit.setHospital("hospital");

        OfficeVisit v = officeVisitService.build(visit);
        officeVisitService.save(v);
        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hospitals/" + "patient"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.[0].name").value("hospital"));
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testGetInvalidVisitedHospitals() throws Exception{
        Assert.assertEquals(0, officeVisitService.count());

        mvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/reviews/hospitals/" + "patient2"))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }


    @After
    public void deleteData(){
        service.deleteAll();
        userService.deleteAll();
        hospitalService.deleteAll();
        officeVisitService.deleteAll();
    }
}
