package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Prescription;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.Drug;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.services.PrescriptionService;
import edu.ncsu.csc.itrust2.services.HospitalService;
import edu.ncsu.csc.itrust2.services.DrugService;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.services.PersonnelService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDate;
import javax.transaction.Transactional;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ERControllerTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private PersonnelService service;

    @Autowired private UserService userService;

    @Autowired private HospitalService hospitalService;

    @Autowired private PrescriptionService prescriptionService;

    @Autowired private DrugService drugService;

    @Autowired private OfficeVisitService officeVisitService;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        service.deleteAll();

        // final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));

        // final User hcp = new Personnel(new UserForm("er", "123456", Role.ROLE_ER, 1));

        // final User admin = new Personnel(new UserForm("admin", "123456", Role.ROLE_ADMIN, 1));

        // userService.saveAll(List.of(patient, hcp, admin));

        // final Hospital hospital =
        //         new Hospital("iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC");
        // hospitalService.save(hospital);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "er",
            roles = {"ER"})
    public void testERController() throws Exception {
        String content =
            mvc.perform(get("/er/index"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        System.out.println(content);
        content =
            mvc.perform(get("/er/EmergencyHealthRecords"))
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        System.out.println(content);
    }
}