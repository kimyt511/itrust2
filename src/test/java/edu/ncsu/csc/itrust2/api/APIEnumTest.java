package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.BloodType;
import edu.ncsu.csc.itrust2.models.enums.Ethnicity;
import edu.ncsu.csc.itrust2.models.enums.Gender;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.State;
import edu.ncsu.csc.itrust2.models.enums.Status;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.utils.LoggerUtil;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.Patient;

import java.io.UnsupportedEncodingException;
import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
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

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIEnumTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private UserService userService;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));

        final User oph = new Personnel(new UserForm("oph", "123456", Role.ROLE_OPH, 1));

        final User od = new Personnel(new UserForm("od", "123456", Role.ROLE_OD, 1));

        final User admin = new Personnel(new UserForm("admin", "123456", Role.ROLE_ADMIN, 1));

        userService.saveAll(List.of(patient, hcp, admin, oph, od));
    }

    /** Tests basic drug API functionality. */
    @Test
    @Transactional
    @WithMockUser(
            username = "admin",
            roles = {"USER", "ADMIN"})
    public void testEnumAPI() throws UnsupportedEncodingException, Exception {
        mvc.perform(get("/api/v1/state")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/bloodtype")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/ethnicity")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/gender")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/appointmenttype")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/appointmentstatus")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/housesmoking")).andExpect(status().isOk());
        mvc.perform(get("/api/v1/patientsmoking")).andExpect(status().isOk());

        Gender g1 = Gender.parse("bla");
        Ethnicity e1 = Ethnicity.parse("bla");
        BloodType b1 = BloodType.parse("bla");
        State s1 = State.parse("bla");

        assertEquals("INDOOR", HouseholdSmokingStatus.getName(3));

        HouseholdSmokingStatus hss1 = HouseholdSmokingStatus.parseValue(0);
        HouseholdSmokingStatus hss2 = HouseholdSmokingStatus.parseValue(1);
        HouseholdSmokingStatus hss3 = HouseholdSmokingStatus.parseValue(2);
        HouseholdSmokingStatus hss4 = HouseholdSmokingStatus.parseValue(9);

        assertEquals(PatientSmokingStatus.getName(1), "NEVER");
        PatientSmokingStatus pss1 = PatientSmokingStatus.parseValue(0);
        PatientSmokingStatus pss2 = PatientSmokingStatus.parseValue(1);
        PatientSmokingStatus pss3 = PatientSmokingStatus.parseValue(2);
        PatientSmokingStatus pss4 = PatientSmokingStatus.parseValue(3);
        PatientSmokingStatus pss5 = PatientSmokingStatus.parseValue(4);
        PatientSmokingStatus pss6 = PatientSmokingStatus.parseValue(9);
        PatientSmokingStatus pss7 = PatientSmokingStatus.parseValue(8);

    }

    /** Tests basic drug API functionality. */
    @Test
    @Transactional
    @WithMockUser(
            username = "od",
            roles = {"USER", "OD"})
    public void testEnum2API() throws UnsupportedEncodingException, Exception {
        mvc.perform(get("/api/v1/appointmenttype")).andExpect(status().isOk());
    }
    /** Tests basic drug API functionality. */
    @Test
    @Transactional
    @WithMockUser(
            username = "oph",
            roles = {"USER", "OPH"})
    public void testEnum3API() throws UnsupportedEncodingException, Exception {
        mvc.perform(get("/api/v1/appointmenttype")).andExpect(status().isOk());
    }
    /** Tests basic drug API functionality. */
    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"USER", "HCP"})
    public void testEnum4API() throws UnsupportedEncodingException, Exception {
        mvc.perform(get("/api/v1/appointmenttype")).andExpect(status().isOk());
    }
}