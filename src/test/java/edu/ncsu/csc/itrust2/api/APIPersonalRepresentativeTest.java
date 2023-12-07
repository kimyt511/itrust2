package edu.ncsu.csc.itrust2.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.PersonalRepresentativeForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.services.*;
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

import edu.ncsu.csc.itrust2.controllers.api.comm.LogEntryRequestBody;
import edu.ncsu.csc.itrust2.controllers.api.comm.LogEntryTableRow;
import edu.ncsu.csc.itrust2.models.security.LogEntry;
import java.time.ZonedDateTime;
import java.time.ZoneId;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIPersonalRepresentativeTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private UserService userService;

    @Autowired private HospitalService hospitalService;

    @Autowired private PersonalRepresentativeService  PRService;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        userService.deleteAll();
        PRService.deleteAll();
        hospitalService.deleteAll();

        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));

        final User patient2 = new Patient(new UserForm("patient2", "123456", Role.ROLE_PATIENT, 1));

        final User patient3 = new Patient(new UserForm("patient3", "123456", Role.ROLE_PATIENT, 1));

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));

        final User admin = new Personnel(new UserForm("admin", "123456", Role.ROLE_ADMIN, 1));

        userService.saveAll(List.of(patient, hcp, admin, patient2, patient3));

        final Hospital hospital =
                new Hospital("iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC");
        hospitalService.save(hospital);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "patient",
            roles = {"PATIENT"})
    public void testPatientPR() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        PersonalRepresentativeForm PRForm1 = new PersonalRepresentativeForm();
        // mvc.perform(
        //                 post("/api/v1/pr/declare")
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content(TestUtils.asJsonString(PRForm1)))
        //         .andExpect(status().is4xxClientError());
            
        PRForm1.setPatient("patient");
        PRForm1.setRepresentative("patient2");
        PRForm1.setComment("comment1");

        mvc.perform(
                        post("/api/v1/pr/declare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(PRForm1)))
                .andExpect(status().isOk());
        mvc.perform(
                        post("/api/v1/pr/declare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(PRForm1)))
                .andExpect(status().isConflict());
        
        content =
                mvc.perform(
                                get("/api/v1/pr/myrepresentatives")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<PersonalRepresentative> prlist =
                gson.fromJson(content, new TypeToken<ArrayList<PersonalRepresentative>>() {}.getType());
        boolean flag = false;
        long id=0;
        for (final PersonalRepresentative pr : prlist) {
            if (pr.getRepresentative().getUsername().equals(PRForm1.getRepresentative())) {
                flag = true;
                id = pr.getId();
            }
        }
        assertTrue(flag);

        PersonalRepresentativeForm PRForm2 = new PersonalRepresentativeForm(prlist.get(0));

        mvc.perform(get("/api/v1/pr/logentries/patient2")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/pr/logentries/patient3")).andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/pr/diagnoses/patient2")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/pr/diagnoses/patient3")).andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/pr/officevisits/patient2")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/pr/officevisits/patient3")).andExpect(status().isForbidden());
        mvc.perform(get("/api/v1/pr/appointmentrequest/patient2")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/pr/appointmentrequest/patient3")).andExpect(status().isForbidden());

        mvc.perform(
            delete("/api/v1/pr/undeclare/"+Long.toString(id))).andExpect(status().isOk());
        mvc.perform(
            delete("/api/v1/pr/undeclare/patient21")).andExpect(status().is4xxClientError());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testHCPPR() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        PersonalRepresentativeForm PRForm1 = new PersonalRepresentativeForm();
        PRForm1.setPatient("patient");
        PRForm1.setRepresentative("patient2");
        PRForm1.setComment("comment1");

        mvc.perform(
                        post("/api/v1/pr/declare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(PRForm1)))
                .andExpect(status().isOk());

        content =
            mvc.perform(
                            get("/api/v1/pr/patient")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        List<PersonalRepresentative> prlist =
                gson.fromJson(content, new TypeToken<ArrayList<PersonalRepresentative>>() {}.getType());
        boolean flag = false;
        long id=0;
        for (final PersonalRepresentative pr : prlist) {
            if (pr.getRepresentative().getUsername().equals(PRForm1.getRepresentative())) {
                flag = true;
                id = pr.getId();
            }
        }
        assertTrue(flag);

        content =
            mvc.perform(
                            get("/api/v1/pr/declared/patient2")
                                    .contentType(MediaType.APPLICATION_JSON))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        prlist =
                gson.fromJson(content, new TypeToken<ArrayList<PersonalRepresentative>>() {}.getType());
        flag = false;
        for (final PersonalRepresentative pr : prlist) {
            if (pr.getRepresentative().getUsername().equals(PRForm1.getRepresentative())) {
                flag = true;
            }
        }
        assertTrue(flag);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "patient",
            roles = {"PATIENT"})
    public void testPatientPR2() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        PersonalRepresentativeForm PRForm1 = new PersonalRepresentativeForm();
        // mvc.perform(
        //                 post("/api/v1/pr/declare")
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content(TestUtils.asJsonString(PRForm1)))
        //         .andExpect(status().is4xxClientError());
            
        PRForm1.setPatient("patient2");
        PRForm1.setRepresentative("patient");
        PRForm1.setComment("comment1");

        mvc.perform(
                        post("/api/v1/pr/declare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(PRForm1)))
                .andExpect(status().isOk());

        PersonalRepresentativeForm PRForm2 = new PersonalRepresentativeForm();
        // mvc.perform(
        //                 post("/api/v1/pr/declare")
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content(TestUtils.asJsonString(PRForm1)))
        //         .andExpect(status().is4xxClientError());
            
        PRForm2.setPatient("patient");
        PRForm2.setRepresentative("patient2");
        PRForm2.setComment("comment2");

        mvc.perform(
                        post("/api/v1/pr/declare")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(PRForm2)))
                .andExpect(status().isOk());

        
        content =
                mvc.perform(
                                get("/api/v1/pr/mypatients")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<PersonalRepresentative> prlist =
                gson.fromJson(content, new TypeToken<ArrayList<PersonalRepresentative>>() {}.getType());
        boolean flag = false;
        long id=0;
        for (final PersonalRepresentative pr : prlist) {
            if (pr.getRepresentative().getUsername().equals(PRForm1.getRepresentative())) {
                flag = true;
                id = pr.getId();
            }
        }
        assertTrue(flag);
        mvc.perform(get("/api/v1/pr/logentries/patient2")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/pr/logentries/patient")).andExpect(status().is4xxClientError());
        mvc.perform(get("/api/v1/pr/logentries/patient5")).andExpect(status().is4xxClientError());

        mvc.perform(
            delete("/api/v1/pr/undeclare/"+Long.toString(id))).andExpect(status().isOk());
        mvc.perform(
            delete("/api/v1/pr/undeclare/patient21")).andExpect(status().is4xxClientError());


        LogEntryRequestBody LERB = new LogEntryRequestBody();
        LERB.setStartDate(LocalDate.of(2022,6,30).toString());
        LERB.setEndDate(LocalDate.of(2022,7,30).toString());
        LERB.setPage(1);
        LERB.setPageLength(10);

        mvc.perform(post("/api/v1/logentries/range")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(LERB))).andExpect(status().isOk());
    }

}