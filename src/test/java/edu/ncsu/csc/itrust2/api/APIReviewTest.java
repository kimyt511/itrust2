package edu.ncsu.csc.itrust2.api;


import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.ReviewForm;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIReviewTest {

    private MockMvc mvc;
    @Autowired private WebApplicationContext context;
    @Autowired private ReviewService service;
    @Autowired private UserService userService;
    @Autowired private HospitalService hospitalService;

    @Before
    public void setup(){
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        service.deleteAll();

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));
        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));
        final Hospital hospital = new Hospital("hospital", "123","12345", "NC");

        userService.saveAll(List.of(hcp, patient));
        hospitalService.save(hospital);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testAddHcpReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient(userService.findByName("patient"));
        form.setHcp(userService.findByName("hcp"));
        form.setRate(0.0);

        mvc.perform(
                MockMvcRequestBuilders.post("/api/v1/reviews/hcp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(1, service.count());

    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testAddHospitalReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient(userService.findByName("patient"));
        form.setHospital(hospitalService.findByName("hospital"));
        form.setRate(0.0);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hospital")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(1, service.count());

    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testAddInvalidReview() throws Exception{
        final ReviewForm form = new ReviewForm();
        form.setPatient(userService.findByName("patient"));

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hcp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/reviews/hospital")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
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
        form.setPatient(userService.findByName("patient"));
        form.setHcp(userService.findByName("hcp"));
        form.setRate(0.0);

        final Review review = new Review(form);
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
        form.setPatient(userService.findByName("patient"));
        form.setHospital(hospitalService.findByName("hospital"));
        form.setRate(0.0);

        final Review review = new Review(form);
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
        form.setPatient(userService.findByName("patient"));

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
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "test",
            roles = {"USER", "PATIENT"})
    public void testDeleteHcpReview() throws Exception{
        Assert.assertEquals(0, service.count());

        final ReviewForm form = new ReviewForm();
        form.setPatient(userService.findByName("patient"));
        form.setHcp(userService.findByName("hcp"));
        form.setRate(0.0);

        final Review review = new Review(form);
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
        form.setPatient(userService.findByName("patient"));
        form.setHospital(hospitalService.findByName("hospital"));
        form.setRate(0.0);

        final Review review = new Review(form);
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
    public void testDeleteInvalidReview() throws Exception{
        mvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/reviews/hcp/" + 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/reviews/hospital/" + 0))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(0, service.count());
    }

    @After
    public void deleteData(){
        service.deleteAll();
        userService.deleteAll();
        hospitalService.deleteAll();
    }
}
