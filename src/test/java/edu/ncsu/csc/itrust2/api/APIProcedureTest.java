package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.LoincForm;
import edu.ncsu.csc.itrust2.forms.ProcedureForm;
import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Loinc;
import edu.ncsu.csc.itrust2.models.Procedure;
import edu.ncsu.csc.itrust2.models.Hospital;
import edu.ncsu.csc.itrust2.models.ICDCode;
import edu.ncsu.csc.itrust2.models.OfficeVisit;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.AppointmentType;
import edu.ncsu.csc.itrust2.models.enums.HouseholdSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.PatientSmokingStatus;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.enums.Priority;
import edu.ncsu.csc.itrust2.models.enums.ProcedureStatus;
import edu.ncsu.csc.itrust2.services.ProcedureService;
import edu.ncsu.csc.itrust2.services.LoincService;
import edu.ncsu.csc.itrust2.services.HospitalService;
import edu.ncsu.csc.itrust2.services.ICDCodeService;
import edu.ncsu.csc.itrust2.services.OfficeVisitService;
import edu.ncsu.csc.itrust2.services.UserService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.UnsupportedEncodingException;
import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
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
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIProcedureTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private UserService userService;

    @Autowired private HospitalService hospitalService;

    @Autowired private LoincService loincService;

    @Autowired private ProcedureService procedureService;

    @Autowired private OfficeVisitService officeVisitService;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));

        final User labtech = new Personnel(new UserForm("labtech", "123456", Role.ROLE_LABTECH, 1));

        userService.saveAll(List.of(patient, hcp, labtech));

        final Hospital hospital =
                new Hospital("iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC");
        hospitalService.save(hospital);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"USER", "HCP"})
    public void testProcedureAPI() throws UnsupportedEncodingException, Exception {

        // create Loinc to use
        final Loinc loinc1 = new Loinc();
        loinc1.setCode("12345-0");
        loinc1.setName("test1");
        loinc1.setComponent("Test Component");
        loinc1.setProperty("Test Property");

        loincService.saveAll(List.of(loinc1));

        // Create an office visit
        final OfficeVisitForm form = new OfficeVisitForm();
        form.setDate("2048-04-16T09:50:00.000-04:00"); // 4/16/2048 9:50 AM
        form.setHcp("hcp");
        form.setPatient("patient");
        form.setNotes("Test office visit");
        form.setType(AppointmentType.GENERAL_CHECKUP.toString());
        form.setHospital("iTrust Test Hospital 2");
        form.setHdl(1);
        form.setHeight(1f);
        form.setWeight(1f);
        form.setLdl(1);
        form.setTri(100);
        form.setDiastolic(1);
        form.setSystolic(1);
        form.setHouseSmokingStatus(HouseholdSmokingStatus.NONSMOKING);
        form.setPatientSmokingStatus(PatientSmokingStatus.FORMER);

        final User patient = userService.findByName("patient");
        final User hcp = userService.findByName("hcp");
        final User labtech = userService.findByName("labtech");


        final List<ProcedureForm> list = new ArrayList<>();
        final ProcedureForm p = new ProcedureForm();
        p.setCode(loinc1.getCode());
        p.setName(loinc1.getName());
        p.setPatient(patient);
        p.setHcp(hcp);
        p.setLabtech(labtech);
        p.setComment("Test Comment");
        p.setPriority(Priority.HIGH);
        p.setProcedureStatus(ProcedureStatus.Assigned);
        list.add(p);

        form.setProcedures(list);


        // Add Office Visit to system
        final String content =
                mvc.perform(
                        post("/api/v1/officevisits")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        // Parse response and verify procedure has been added.
        final Gson gson = new GsonBuilder().create();
        final OfficeVisit visit1 = gson.fromJson(content, OfficeVisit.class);
        assertEquals(form.getProcedures().get(0).getCode(), visit1.getProcedures().get(0).getCode());


    }
}

