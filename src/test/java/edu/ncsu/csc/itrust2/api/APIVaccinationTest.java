package edu.ncsu.csc.itrust2.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.OfficeVisitForm;
import edu.ncsu.csc.itrust2.forms.VaccinationForm;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIVaccinationTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private UserService userService;

    @Autowired private HospitalService hospitalService;

    @Autowired private VaccinationService vaccinationService;

    @Autowired private VaccineService vaccineService;

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
    public void testVaccinations() throws Exception {

        final Gson gson = new GsonBuilder().create();
        String content;

        // create 2 vaccines to use
        final Vaccine vaccine1 = new Vaccine();
        vaccine1.setName("test1");
        vaccine1.setAbbreviation("t1");
        vaccine1.setCptCode("90000");
        vaccine1.setComments("com1");

        final Vaccine vaccine2 = new Vaccine();
        vaccine2.setName("test2");
        vaccine2.setAbbreviation("t2");
        vaccine2.setCptCode("90001");
        vaccine2.setComments("com2");

        vaccineService.saveAll(List.of(vaccine1, vaccine2));

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

        final List<Vaccination> list = new ArrayList<>();
        final Vaccination v = new Vaccination();
        v.setVaccine(vaccine1);
        v.setDateAdministered(LocalDate.of(2023,06,15));
        v.setPatient(patient);
        list.add(v);
        final Vaccination v2 = new Vaccination();
        v2.setVaccine(vaccine2);
        v2.setDateAdministered(LocalDate.of(2023,06,15));
        v2.setPatient(patient);
        list.add(v2);

        form.setVaccinations(list.stream().map(VaccinationForm::new).collect(Collectors.toList()));

        final OfficeVisit visit = officeVisitService.build(form);

        officeVisitService.save(visit);

        final OfficeVisit retrieved = (OfficeVisit) officeVisitService.findAll().get(0);

        final Serializable id = retrieved.getId();

        // get the list of diagnoses for this office visit and make sure both
        // are there
        mvc.perform(get("/api/v1/vaccinations/patient/haha")).andExpect(status().isNotFound());
        content =
                mvc.perform(
                                get("/api/v1/vaccinations/patient/" + "patient")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<Vaccination> plist =
                gson.fromJson(content, new TypeToken<ArrayList<Vaccination>>() {}.getType());
        boolean flag = false;
        for (final Vaccination pp : plist) {
            if (pp.getVaccine().getCptCode().equals(v.getVaccine().getCptCode())) {
                flag = true;
                v.setId(pp.getId());
            }
                
        }
        assertTrue(flag);
        flag = false;
        for (final Vaccination pp : plist) {
            if (pp.getVaccine().getCptCode().equals(v2.getVaccine().getCptCode())) {
                flag = true;
                v2.setId(pp.getId());
            }
        }
        assertTrue(flag);

        // edit a diagnosis within the editing of office visit and check they
        // work.
        form.setId(id + "");
        v.setDateAdministered(LocalDate.of(2023,06,26));
        form.setVaccinations(list.stream().map(VaccinationForm::new).collect(Collectors.toList()));
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
                                get("/api/v1/vaccinations/patient/" + "patient")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        plist =
                gson.fromJson(content, new TypeToken<ArrayList<Vaccination>>() {}.getType());
        flag = false;
        for (final Vaccination pp : plist) {
            if (pp.getPatient().getUsername().equals(v2.getPatient().getUsername())) {
                flag = true;
            }
        }
        assertTrue(flag);

        // edit the office visit and remove a diagnosis

        // list.remove(v);
        // form.setVaccinations(list.stream().map(VaccinationForm::new).collect(Collectors.toList()));
        // content =
        //         mvc.perform(
        //                         put("/api/v1/officevisits/" + id)
        //                                 .contentType(MediaType.APPLICATION_JSON)
        //                                 .content(TestUtils.asJsonString(form)))
        //                 .andReturn()
        //                 .getResponse()
        //                 .getContentAsString();

        // // check that the removed one is gone
        // content =
        //         mvc.perform(
        //                         get("/api/v1/vaccinations/patient/" + "patient")
        //                                 .contentType(MediaType.APPLICATION_JSON))
        //                 .andReturn()
        //                 .getResponse()
        //                 .getContentAsString();
        // plist =
        //         gson.fromJson(content, new TypeToken<ArrayList<Vaccination>>() {}.getType());
        // for (final Vaccination pp : plist) {
        //     if (pp.getVaccine().getCptCode().equals(v.getVaccine().getCptCode())) {
        //         Assert.fail("Was not deleted!");
        //     }
        // }
        // flag = false;
        // for (final Vaccination pp : plist) {
        //     if (pp.getVaccine().getCptCode().equals(v2.getVaccine().getCptCode()) && pp.getDateAdministered().equals(v2.getDateAdministered())) {
        //         flag = true;
        //     }
        // }
        // assertTrue(flag);

    }

    @Test
    @Transactional
    @WithMockUser(
            username = "patient",
            roles = {"PATIENT"})
    public void testVaccinations2() throws Exception {
        mvc.perform(get("/api/v1/vaccinations")).andExpect(status().isOk());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "patient2",
            roles = {"PATIENT"})
    public void testVaccinations3() throws Exception {
        mvc.perform(get("/api/v1/vaccinations")).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testVaccinations4() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;
        
        VaccinationForm vf1 = new VaccinationForm();
        vf1.setVaccineCptCode("90000");
        vf1.setPatientUserName("patient");
        vf1.setDateAdministered(LocalDate.of(2023,06,26).toString());

        // mvc.perform(
        //                 post("/api/v1/vaccinations")
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content(TestUtils.asJsonString(vf1)))
        //         .andExpect(status().is4xxClientError());

        final Vaccine vaccine1 = new Vaccine();
        vaccine1.setName("test1");
        vaccine1.setAbbreviation("t1");
        vaccine1.setCptCode("90000");
        vaccine1.setComments("com1");

        final Vaccine vaccine2 = new Vaccine();
        vaccine2.setName("test2");
        vaccine2.setAbbreviation("t2");
        vaccine2.setCptCode("90001");
        vaccine2.setComments("com2");

        vaccineService.saveAll(List.of(vaccine1, vaccine2));

        mvc.perform(
                        post("/api/v1/vaccinations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(vf1)))
                .andExpect(status().isOk());

        content =
                mvc.perform(
                                get("/api/v1/vaccinations/patient/" + "patient")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<Vaccination> plist =
                gson.fromJson(content, new TypeToken<ArrayList<Vaccination>>() {}.getType());
        boolean flag = false;
        long id=0;
        for (final Vaccination pp : plist) {
            if (pp.getVaccine().getCptCode().equals(vf1.getVaccineCptCode())) {
                flag = true;
                id = pp.getId();
            }
                
        }
        assertTrue(flag);
        
        vf1.setVaccineCptCode("90001");
        mvc.perform(
                        put("/api/v1/vaccinations/" + 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(vf1)))
                .andExpect(status().isNotFound());
        mvc.perform(
                        put("/api/v1/vaccinations/" + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(vf1)))
                .andExpect(status().isOk());

        mvc.perform(
            delete("/api/v1/vaccinations/"+id)).andExpect(status().isOk());
        mvc.perform(
            delete("/api/v1/vaccinations/"+id)).andExpect(status().isNotFound());
    }

    
    @Test()
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testVaccinations5() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;
        
        VaccinationForm vf1 = new VaccinationForm();
        // vf1.setVaccineCptCode("90000");
        // vf1.setPatientUserName("patient");
        // vf1.setDateAdministered(LocalDate.of(2023,06,26).toString());

        // mvc.perform(
        //                 post("/api/v1/vaccinations")
        //                         .contentType(MediaType.APPLICATION_JSON)
        //                         .content(TestUtils.asJsonString(vf1)))
        //         .andExpect(status().is4xxClientError());

        final Vaccine vaccine1 = new Vaccine();
        vaccine1.setName("test1");
        vaccine1.setAbbreviation("t1");
        vaccine1.setCptCode("90000");
        vaccine1.setComments("com1");

        final Vaccine vaccine2 = new Vaccine();
        vaccine2.setName("test2");
        vaccine2.setAbbreviation("t2");
        vaccine2.setCptCode("90001");
        vaccine2.setComments("com2");

        vaccineService.saveAll(List.of(vaccine1, vaccine2));

        mvc.perform(
                        post("/api/v1/vaccinations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(vf1)))
                .andExpect(status().is4xxClientError());

        // VaccinationForm vf1 = new VaccinationForm();
        vf1.setVaccineCptCode("90000");
        vf1.setPatientUserName("patient");
        vf1.setDateAdministered(LocalDate.of(2023,06,26).toString());
        mvc.perform(
                        post("/api/v1/vaccinations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(vf1)))
                .andExpect(status().isOk());

        content =
                mvc.perform(
                                get("/api/v1/vaccinations/patient/" + "patient")
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<Vaccination> plist =
                gson.fromJson(content, new TypeToken<ArrayList<Vaccination>>() {}.getType());
        boolean flag = false;
        long id=0;
        for (final Vaccination pp : plist) {
            if (pp.getVaccine().getCptCode().equals(vf1.getVaccineCptCode())) {
                flag = true;
                vf1.setId(pp.getId());
                id = pp.getId();
            }       
        }
        assertTrue(flag);
        
        VaccinationForm vf2 = new VaccinationForm();
        vf2.setId(id);
        mvc.perform(
                put("/api/v1/vaccinations/"+id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(vf2)))
        .andExpect(status().is4xxClientError());
        
    }
}
