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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        final User labtech2 = new Personnel(new UserForm("labtech2", "123456", Role.ROLE_LABTECH, 1));

        userService.saveAll(List.of(patient, hcp, labtech, labtech2));

        final Hospital hospital =
                new Hospital("iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC");
        hospitalService.save(hospital);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"USER", "HCP"})
    public void testHcpProcedure() throws Exception{
        final User patient = userService.findByName("patient");
        final User hcp = userService.findByName("hcp");
        final User labtech = userService.findByName("labtech");

        final Loinc loinc1 = new Loinc();
        loinc1.setCode("12345-0");
        loinc1.setName("test1");
        loinc1.setComponent("Test Component");
        loinc1.setProperty("Test Property");

        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate("2030-11-19T04:50:00.000-05:00");
        visit.setHcp("hcp");
        visit.setPatient("patient");
        visit.setNotes("Test office visit");
        visit.setType(AppointmentType.GENERAL_CHECKUP.toString());
        visit.setHospital("iTrust Test Hospital 2");
        final OfficeVisit ov = officeVisitService.build(visit);
        officeVisitService.save(ov);
        Long ovId = officeVisitService.findByHcp(hcp).get(0).getId();
        ov.setId(ovId);
        final ProcedureForm p = new ProcedureForm();
        p.setCode(loinc1.getCode());
        p.setName(loinc1.getName());
        p.setPatient(patient);
        p.setHcp(hcp);
        p.setComment("Test Comment");
        p.setPriority(Priority.HIGH);
        p.setProcedureStatus(ProcedureStatus.Assigned);

        mvc.perform(
                        post("/api/v1/procedure")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(p)))
                .andExpect(status().isBadRequest());
        p.setLabtech(labtech);
        p.setVisit(ovId);
        final String content =
                mvc.perform(
                        post("/api/v1/procedure")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(p)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        final Gson gson = new GsonBuilder().create();
        final Procedure procedure1 = gson.fromJson(content, Procedure.class);
        final ProcedureForm procedureForm1 = new ProcedureForm(procedure1);
        procedureForm1.setProcedureStatus(ProcedureStatus.InProgress);
        procedureForm1.setComment("This is a better Comment");
        procedureForm1.setVisit(ovId);
        final String editContent =
                mvc.perform(
                        put("/api/v1/procedure")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(procedureForm1)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        final Procedure editedProcedure = gson.fromJson(editContent, Procedure.class);
        assertEquals("This is a better Comment", editedProcedure.getComment());

        mvc.perform(
                get("/api/v1/procedure")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(
                get("/api/v1/procedure/" + procedure1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(
                get("/api/v1/procedureForHcp/" + "patient")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(delete("/api/v1/procedure/" + procedure1.getId()))
                .andExpect(status().isBadRequest());

        procedureForm1.setProcedureStatus(ProcedureStatus.Assigned);
        mvc.perform(
                        put("/api/v1/procedure")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(procedureForm1)))
                .andExpect(status().isOk());
        mvc.perform(delete("/api/v1/procedure/" + procedure1.getId()))
                .andExpect(status().isOk());

        mvc.perform(
                put("/api/v1/procedure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(procedure1)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        mvc.perform(
                get("/api/v1/procedure/" + procedure1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mvc.perform(
                get("/api/v1/procedureForHcp/" + "wrongPatientName")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        p.setProcedureStatus(ProcedureStatus.InProgress);

        mvc.perform(
                post("/api/v1//procedure")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(p)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //Try to delete ongoing procedure
        mvc.perform(delete("/api/v1/procedure/" + procedure1.getId()))
                .andExpect(status().isNotFound());

        // Try to delete loinc with non existing id
        mvc.perform(delete("/api/v1/procedure/" + 0))
                .andExpect(status().isNotFound());

        // Try to delete loinc with wrong id
        mvc.perform(delete("/api/v1/procedure/" + "wrongId"))
                .andExpect(status().isBadRequest());

    }

    @Test
    @Transactional
    @WithMockUser(
            username = "labtech",
            roles = {"USER", "LABTECH"})
    public void testLabtechProcedure() throws Exception{
        final User patient = userService.findByName("patient");
        final User hcp = userService.findByName("hcp");
        final User labtech = userService.findByName("labtech");
        final User labtech2 = userService.findByName("labtech2");

        final Loinc loinc2 = new Loinc();
        loinc2.setCode("12346-0");
        loinc2.setName("test2");
        loinc2.setComponent("Test Component");
        loinc2.setProperty("Test Property");

        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate("2030-11-19T04:50:00.000-05:00");
        visit.setHcp("hcp");
        visit.setPatient("patient");
        visit.setNotes("Test office visit");
        visit.setType(AppointmentType.GENERAL_CHECKUP.toString());
        visit.setHospital("iTrust Test Hospital 2");
        final OfficeVisit ov = officeVisitService.build(visit);
        officeVisitService.save(ov);
        Long ovId = officeVisitService.findByHcp(hcp).get(0).getId();
        ov.setId(ovId);

        final ProcedureForm p2 = new ProcedureForm();
        p2.setCode(loinc2.getCode());
        p2.setName(loinc2.getName());
        p2.setPatient(patient);
        p2.setHcp(hcp);
        p2.setLabtech(labtech);
        p2.setComment("Test Comment");
        p2.setPriority(Priority.HIGH);
        p2.setProcedureStatus(ProcedureStatus.Assigned);
        p2.setVisit(ovId);

        final Procedure procedure2 = procedureService.build(p2);

        final ProcedureForm procedureForm = new ProcedureForm();
        procedureForm.setId(0L);
        // Trying to edit with invalid it
        mvc.perform(
                        put("/api/v1/procedure/")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(procedureForm)))
                .andExpect(status().isBadRequest());

        // Trying to reassign with no procedure
        assertEquals(procedureService.count(), 0);
        procedure2.setId(0L);
        mvc.perform(
                put("/api/v1/procedureReassign/" + labtech2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(procedureForm)))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        procedureService.saveAll(List.of(procedure2));
        Long pid = procedureService.findByHcp(hcp).get(0).getId();
        p2.setId(pid);
        final Gson gson = new GsonBuilder().create();

        p2.setComment("This is a better Comment");
        final String editContent =
                mvc.perform(
                        put("/api/v1/procedure")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(p2)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        final Procedure editedProcedure = gson.fromJson(editContent, Procedure.class);
        assertEquals("This is a better Comment", editedProcedure.getComment());

        mvc.perform(
                get("/api/v1/procedureForLabtech")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Reassign procedure to another labtech
        mvc.perform(
                put("/api/v1/procedureReassign/" + labtech2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(p2)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }

    @Test
    @Transactional
    @WithMockUser(
            username = "patient",
            roles = {"USER", "PATIENT"})
    public void testPatientProcedure() throws Exception{
        final User patient = userService.findByName("patient");
        final User hcp = userService.findByName("hcp");
        final User labtech = userService.findByName("labtech");
        final User labtech2 = userService.findByName("labtech2");

        final Loinc loinc2 = new Loinc();
        loinc2.setCode("12346-0");
        loinc2.setName("test2");
        loinc2.setComponent("Test Component");
        loinc2.setProperty("Test Property");

        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate("2030-11-19T04:50:00.000-05:00");
        visit.setHcp("hcp");
        visit.setPatient("patient");
        visit.setNotes("Test office visit");
        visit.setType(AppointmentType.GENERAL_CHECKUP.toString());
        visit.setHospital("iTrust Test Hospital 2");
        final OfficeVisit ov = officeVisitService.build(visit);
        officeVisitService.save(ov);
        Long ovId = officeVisitService.findByHcp(hcp).get(0).getId();
        ov.setId(ovId);

        final ProcedureForm p2 = new ProcedureForm();
        p2.setCode(loinc2.getCode());
        p2.setName(loinc2.getName());
        p2.setPatient(patient);
        p2.setHcp(hcp);
        p2.setLabtech(labtech);
        p2.setComment("Test Comment");
        p2.setPriority(Priority.HIGH);
        p2.setProcedureStatus(ProcedureStatus.Assigned);
        p2.setVisit(ovId);
        final Procedure procedure2 = procedureService.build(p2);

        procedureService.saveAll(List.of(procedure2));

        mvc.perform(
                get("/api/v1/procedureForPatient")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }






    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"USER", "HCP"})
    public void testProcedureWithOV() throws UnsupportedEncodingException, Exception {

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

        mvc.perform(
                get("/api/v1/procedureforvisit/" + visit1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mvc.perform(
                        get("/api/v1/procedureforvisit/" + "0")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").doesNotExist());

    }

    @Test
    @Transactional
    public void testGetOfficeVisitByProcedure() throws Exception{
        final User patient = userService.findByName("patient");
        final User hcp = userService.findByName("hcp");
        final User labtech = userService.findByName("labtech");

        final Loinc loinc1 = new Loinc();
        loinc1.setCode("12345-0");
        loinc1.setName("test1");
        loinc1.setComponent("Test Component");
        loinc1.setProperty("Test Property");

        final OfficeVisitForm visit = new OfficeVisitForm();
        visit.setDate("2030-11-19T04:50:00.000-05:00");
        visit.setHcp("hcp");
        visit.setPatient("patient");
        visit.setNotes("Test office visit");
        visit.setType(AppointmentType.GENERAL_CHECKUP.toString());
        visit.setHospital("iTrust Test Hospital 2");

        final ProcedureForm p = new ProcedureForm();
        p.setCode(loinc1.getCode());
        p.setName(loinc1.getName());
        p.setPatient(patient);
        p.setHcp(hcp);
        p.setLabtech(labtech);
        p.setComment("Test Comment");
        p.setPriority(Priority.HIGH);
        p.setProcedureStatus(ProcedureStatus.Assigned);

        List<ProcedureForm> procedures = new ArrayList<>();
        procedures.add(p);
        visit.setProcedures(procedures);
        final OfficeVisit ov = officeVisitService.build(visit);
        officeVisitService.save(ov);

        assertEquals(procedureService.count(), 1);
        Long pid = procedureService.findByHcp(hcp).get(0).getId();

        mvc.perform(get("/api/v1/officevisit/withprocedure/" + 0))
        .andExpect(status().isNotFound());

        mvc.perform(get("/api/v1/officevisit/withprocedure/" + pid))
                .andExpect(status().isOk());
    }
}

