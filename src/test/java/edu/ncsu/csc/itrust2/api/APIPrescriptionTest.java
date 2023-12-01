package edu.ncsu.csc.itrust2.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.PrescriptionForm;
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

import javax.transaction.Transactional;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIPrescriptionTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private UserService userService;

    @Autowired private HospitalService hospitalService;

    @Autowired private PrescriptionService prescriptionService;

    @Autowired private DrugService drugService;

    @Autowired private OfficeVisitService officeVisitService;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));

        final User admin = new Personnel(new UserForm("admin", "123456", Role.ROLE_ADMIN, 1));

        userService.saveAll(List.of(patient, hcp, admin));

        final Hospital hospital =
                new Hospital("iTrust Test Hospital 2", "1 iTrust Test Street", "27607", "NC");
        hospitalService.save(hospital);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testPrescriptions() throws Exception {

        final Gson gson = new GsonBuilder().create();
        String content;

        // create 2 drugs to use
        final Drug drug1 = new Drug("0000-0000-01", "TEST1", "Desc1");

        final Drug drug2 = new Drug("0000-0000-02", "TEST2", "Desc2");

        drugService.saveAll(List.of(drug1, drug2));

        // Create an office visit with two diagnoses
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

        final List<Prescription> list = new ArrayList<>();
        final Prescription p = new Prescription();
        p.setDrug(drug1);
        p.setDosage(10);
        p.setStartDate(LocalDate.of(2023,06,15));
        p.setEndDate(LocalDate.of(2023,06,23));
        p.setRenewals(1);
        p.setPatient(patient);
        list.add(p);
        final Prescription p2 = new Prescription();
        p2.setDrug(drug2);
        p2.setDosage(10);
        p2.setStartDate(LocalDate.of(2023,06,16));
        p2.setEndDate(LocalDate.of(2023,06,24));
        p2.setRenewals(2);
        p2.setPatient(patient);
        list.add(p2);

        form.setPrescriptions(list.stream().map(PrescriptionForm::new).collect(Collectors.toList()));

        final OfficeVisit visit = officeVisitService.build(form);

        officeVisitService.save(visit);

        final OfficeVisit retrieved = (OfficeVisit) officeVisitService.findAll().get(0);

        final Serializable id = retrieved.getId();

        // get the list of diagnoses for this office visit and make sure both
        // are there
        content =
                mvc.perform(
                                get("/api/v1/prescriptions/ehr/search/" + "patient")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<Prescription> plist =
                gson.fromJson(content, new TypeToken<ArrayList<Prescription>>() {}.getType());
        boolean flag = false;
        for (final Prescription pp : plist) {
            if (pp.getDosage() == p.getDosage() && pp.getDrug().getCode().equals(p.getDrug().getCode())) {
                flag = true;
                p.setId(pp.getId());
            }
                
        }
        assertTrue(flag);
        flag = false;
        for (final Prescription pp : plist) {
            if (pp.getDosage() == p2.getDosage() && pp.getDrug().getCode().equals(p2.getDrug().getCode())) {
                flag = true;
                p2.setId(pp.getId());
            }
        }
        assertTrue(flag);

        content =
                mvc.perform(
                                get("/api/v1/prescriptions/search/" + "patient")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        plist =
                gson.fromJson(content, new TypeToken<ArrayList<Prescription>>() {}.getType());
        flag = false;
        for (final Prescription pp : plist) {
            if (pp.getDosage() == p.getDosage() && pp.getDrug().getCode().equals(p.getDrug().getCode())) {
                flag = true;
                p.setId(pp.getId());
            }

        }
        assertTrue(flag);
        flag = false;
        for (final Prescription pp : plist) {
            if (pp.getDosage() == p2.getDosage() && pp.getDrug().getCode().equals(p2.getDrug().getCode())) {
                flag = true;
                p2.setId(pp.getId());
            }
        }
        assertTrue(flag);

        // get the list of diagnoses for this patient and make sure both are
        // there
        final List<Prescription> forPatient =
                prescriptionService.findByPatient(userService.findByName("patient"));
        flag = false;
        for (final Prescription pp : forPatient) {
            if (pp.getDosage() == p.getDosage() && pp.getDrug().getCode().equals(p.getDrug().getCode())) {
                flag = true;
            }
        }
        assertTrue(flag);
        flag = false;
        for (final Prescription pp : forPatient) {
            if (pp.getDosage() == p2.getDosage() && pp.getDrug().getCode().equals(p2.getDrug().getCode())) {
                flag = true;
            }
        }
        assertTrue(flag);

        // edit a diagnosis within the editing of office visit and check they
        // work.
        form.setId(id + "");
        p.setEndDate(LocalDate.of(2023,06,26));
        form.setPrescriptions(list.stream().map(PrescriptionForm::new).collect(Collectors.toList()));
        content =
                mvc.perform(
                                put("/api/v1/officevisits/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(TestUtils.asJsonString(form)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        content =
                mvc.perform(
                                get("/api/v1/prescriptions/search/" + "patient")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        plist =
                gson.fromJson(content, new TypeToken<ArrayList<Prescription>>() {}.getType());
        flag = false;
        for (final Prescription pp : plist) {
            if (pp.getDosage() == p.getDosage() && pp.getDrug().getCode().equals(p.getDrug().getCode())) {
                flag = true;
            }
        }
        assertTrue(flag);
        flag = false;
        for (final Prescription pp : plist) {
            if (pp.getDosage() == p2.getDosage() && pp.getDrug().getCode().equals(p2.getDrug().getCode())) {
                flag = true;
            }
        }
        assertTrue(flag);

        // edit the office visit and remove a diagnosis

        list.remove(p);
        form.setPrescriptions(list.stream().map(PrescriptionForm::new).collect(Collectors.toList()));
        content =
                mvc.perform(
                                put("/api/v1/officevisits/" + id)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(TestUtils.asJsonString(form)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        // check that the removed one is gone
        content =
                mvc.perform(
                                get("/api/v1/prescriptions/search/" + "patient")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        plist =
                gson.fromJson(content, new TypeToken<ArrayList<Prescription>>() {}.getType());
        for (final Prescription pp : plist) {
            if (pp.getDosage() == p.getDosage() && pp.getDrug().getCode().equals(p.getDrug().getCode())) {
                Assert.fail("Was not deleted!");
            }
        }
        flag = false;
        for (final Prescription pp : plist) {
            if (pp.getDosage() == p2.getDosage() && pp.getDrug().getCode().equals(p2.getDrug().getCode())) {
                flag = true;
            }
        }
        assertTrue(flag);

        /* Make sure all the editing didn't create any duplicates */
        Assert.assertEquals(2, prescriptionService.count());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testPrescriptionsCtr() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        final Drug drug1 = new Drug("0000-0000-01", "TEST1", "Desc1");

        final Drug drug2 = new Drug("0000-0000-02", "TEST2", "Desc2");

        drugService.saveAll(List.of(drug1, drug2));

        PrescriptionForm pf = new PrescriptionForm();
        pf.setDrug("0000-0000-01");
        pf.setDosage(10);
        pf.setStartDate(LocalDate.of(2023,06,15).toString());
        pf.setEndDate(LocalDate.of(2023,06,23).toString());
        pf.setRenewals(1);
        pf.setPatient("patient");

        mvc.perform(
                post("/api/v1/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(pf)))
        .andExpect(status().isOk());

        // pf.setDrug("0000-0000-03");
        // mvc.perform(
        //         post("/api/v1/prescriptions")
        //                 .contentType(MediaType.APPLICATION_JSON)
        //                 .content(TestUtils.asJsonString(pf)))
        // .andExpect(status().is4xxClientError());

        content =
                mvc.perform(
                                get("/api/v1/prescriptions")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<Prescription> plist =
                gson.fromJson(content, new TypeToken<ArrayList<Prescription>>() {}.getType());
        boolean flag = false;
        for (final Prescription pp : plist) {
            if (pp.getDosage() == pf.getDosage()) {
                flag = true;
                pf.setId(pp.getId());
            }
        }
        assertTrue(flag);

        mvc.perform(
            get("/api/v1/prescriptions/"+pf.getId())
        ).andExpect(status().isOk());

        pf.setDosage(20);
        mvc.perform(
                put("/api/v1/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(pf)))
        .andExpect(status().isOk());

        long id = pf.getId();
        pf.setId(2L);
        mvc.perform(
                put("/api/v1/prescriptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(pf)))
        .andExpect(status().isNotFound());

        mvc.perform(
            delete("/api/v1/prescriptions/"+id)
        ).andExpect(status().isOk());

        mvc.perform(
            get("/api/v1/prescriptions/"+1L)
        ).andExpect(status().isNotFound());

        mvc.perform(
            delete("/api/v1/prescriptions/"+1L)
        ).andExpect(status().isNotFound());

    }
}
