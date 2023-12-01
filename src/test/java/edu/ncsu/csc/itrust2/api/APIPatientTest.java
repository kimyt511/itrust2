package edu.ncsu.csc.itrust2.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.PatientForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.services.PatientService;
import edu.ncsu.csc.itrust2.dto.PatientDto;
import edu.ncsu.csc.itrust2.dto.EhrDto;

import javax.transaction.Transactional;
import java.time.LocalDate;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

/**
 * Test for API functionality for interacting with Patients
 *
 * @author Kai Presler-Marshall
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIPatientTest {

    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private PatientService service;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        service.deleteAll();
    }

    /** Tests that getting a patient that doesn't exist returns the proper status. */
    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testGetNonExistentPatient() throws Exception {
        mvc.perform(get("/api/v1/patients/-1")).andExpect(status().isNotFound());
    }

    /** Tests PatientAPI */
    @Test
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    @Transactional
    public void testPatientAPI() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        final PatientForm patient = new PatientForm();
        patient.setAddress1("1 Test Street");
        patient.setAddress2("Some Location");
        patient.setBloodType(BloodType.APos.toString());
        patient.setCity("Viipuri");
        patient.setDateOfBirth("1977-06-15");
        patient.setEmail("antti@itrust.fi");
        patient.setEthnicity(Ethnicity.Caucasian.toString());
        patient.setFirstName("Antti");
        patient.setGender(Gender.Male.toString());
        patient.setLastName("Walhelm");
        patient.setPhone("123-456-7890");
        patient.setUsername("antti");
        patient.setState(State.NC.toString());
        patient.setZip("27514");

        // Editing the patient before they exist should fail
        mvc.perform(
                        put("/api/v1/patients/antti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(patient)))
                .andExpect(status().isNotFound());

        final User antti = new Patient(new UserForm("antti", "123456", Role.ROLE_PATIENT, 1));

        service.save(antti);

        // Creating a User should create the Patient record automatically
        mvc.perform(get("/api/v1/patients/antti")).andExpect(status().isOk());

        // mvc.perform(get("/api/v1/patients/ehr/antti")).andExpect(status().isOk());
        // get all patients
        mvc.perform(get("/api/v1/patients")).andExpect(status().isOk());

        // get wrong patients
        mvc.perform(get("/api/v1/patients/bees")).andExpect(status().isNotFound());

        // Should also now be able to edit existing record with new information
        mvc.perform(
                        put("/api/v1/patients/antti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(patient)))
                .andExpect(status().isOk());

        content =
                mvc.perform(
                                get("/api/v1/patients/ehr/antti"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        EhrDto ehrdto =
                gson.fromJson(content, new TypeToken<EhrDto>() {}.getType());
        Assert.assertEquals(ehrdto.getUsername(), "antti");
        Assert.assertEquals(ehrdto.getFirstName(), "Antti");
        Assert.assertEquals(ehrdto.getLastName(), "Walhelm");
        Assert.assertTrue(ehrdto.getDateOfBirth().equals(LocalDate.of(1977,06,15)));
        Assert.assertEquals(ehrdto.getGender(), Gender.Male);
        Assert.assertEquals(ehrdto.getBloodType(), BloodType.APos);

        Patient anttiRetrieved = (Patient) service.findByName("antti");


        // Test a few fields to be reasonably confident things are working
        Assert.assertEquals("Walhelm", anttiRetrieved.getLastName());
        Assert.assertEquals(Gender.Male, anttiRetrieved.getGender());
        Assert.assertEquals("Viipuri", anttiRetrieved.getCity());

        content =
                mvc.perform(
                                get("/api/v1/patients/search/" + "Walh"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<PatientDto> plist =
                gson.fromJson(content, new TypeToken<ArrayList<PatientDto>>() {}.getType());
        boolean flag = false;
        for (final PatientDto pp : plist) {
            if (pp.getUsername().equals("antti")) {
                flag = true;
            }
        }
        Assert.assertTrue(flag);

        content =
                mvc.perform(
                                get("/api/v1/patients/searchmid/" + "an"))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        plist =
                gson.fromJson(content, new TypeToken<ArrayList<PatientDto>>() {}.getType());
        flag = false;
        for (final PatientDto pp : plist) {
            if (pp.getUsername().equals("antti")) {
                flag = true;
            }
        }
        Assert.assertTrue(flag);

        // Also test a field we haven't set yet
        Assert.assertNull(anttiRetrieved.getPreferredName());

        patient.setPreferredName("Antti Walhelm");

        mvc.perform(
                        put("/api/v1/patients/antti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(patient)))
                .andExpect(status().isOk());

        anttiRetrieved = (Patient) service.findByName("antti");

        Assert.assertNotNull(anttiRetrieved.getPreferredName());

        // Editing with the wrong username should fail.
        mvc.perform(
                        put("/api/v1/patients/badusername")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(patient)))
                .andExpect(status().is4xxClientError());
    }

    /** Test accessing the patient PUT request unauthenticated */
    @Test
    @Transactional
    public void testPatientUnauthenticated() throws Exception {
        final PatientForm patient = new PatientForm();
        patient.setAddress1("1 Test Street");
        patient.setAddress2("Some Location");
        patient.setBloodType(BloodType.APos.toString());
        patient.setCity("Viipuri");
        patient.setDateOfBirth("1977-06-15");
        patient.setEmail("antti@itrust.fi");
        patient.setEthnicity(Ethnicity.Caucasian.toString());
        patient.setFirstName("Antti");
        patient.setGender(Gender.Male.toString());
        patient.setLastName("Walhelm");
        patient.setPhone("123-456-7890");
        patient.setUsername("antti");
        patient.setState(State.NC.toString());
        patient.setZip("27514");

        final User antti = new Patient(new UserForm("antti", "123456", Role.ROLE_PATIENT, 1));

        service.save(antti);

        mvc.perform(
                        put("/api/v1/patients/antti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(patient)))
                .andExpect(status().isUnauthorized());
    }

    /** Test accessing the patient PUT request as a patient */
    @Test
    @Transactional
    @WithMockUser(
            username = "antti",
            roles = {"PATIENT"})
    public void testPatientAsPatient() throws Exception {
        final User antti = new Patient(new UserForm("antti", "123456", Role.ROLE_PATIENT, 1));

        service.save(antti);

        mvc.perform(get("/api/v1/patient/findexperts/getzip")).andExpect(status().isNoContent());

        final PatientForm patient = new PatientForm();
        patient.setAddress1("1 Test Street");
        patient.setAddress2("Some Location");
        patient.setBloodType(BloodType.APos.toString());
        patient.setCity("Viipuri");
        patient.setDateOfBirth("1977-06-15");
        patient.setEmail("antti@itrust.fi");
        patient.setEthnicity(Ethnicity.Caucasian.toString());
        patient.setFirstName("Antti");
        patient.setGender(Gender.Male.toString());
        patient.setLastName("Walhelm");
        patient.setPhone("123-456-7890");
        patient.setUsername("antti");
        patient.setState(State.NC.toString());
        patient.setZip("27514");

        // a patient can edit their own info
        mvc.perform(
                        put("/api/v1/patients/antti")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(patient)))
                .andExpect(status().isOk());

        final Patient anttiRetrieved = (Patient) service.findByName("antti");

        // Test a few fields to be reasonably confident things are working
        Assert.assertEquals("Walhelm", anttiRetrieved.getLastName());
        Assert.assertEquals(Gender.Male, anttiRetrieved.getGender());
        Assert.assertEquals("Viipuri", anttiRetrieved.getCity());

        // Also test a field we haven't set yet
        Assert.assertNull(anttiRetrieved.getPreferredName());

        // but they can't edit someone else's
        patient.setUsername("patient");
        mvc.perform(
                        put("/api/v1/patients/patient")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(patient)))
                .andExpect(status().isUnauthorized());

        // we should be able to update our record too
        mvc.perform(get("/api/v1/patient/")).andExpect(status().isOk());

        mvc.perform(get("/api/v1/patient/findexperts/getzip")).andExpect(status().isOk());


    }

}
