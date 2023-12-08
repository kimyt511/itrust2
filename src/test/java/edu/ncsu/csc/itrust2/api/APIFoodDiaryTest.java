package edu.ncsu.csc.itrust2.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.FoodDiaryForm;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.models.enums.MealType;
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
import java.time.ZonedDateTime;
import java.time.ZoneId;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIFoodDiaryTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private UserService userService;

    @Autowired private HospitalService hospitalService;

    @Autowired private FoodDiaryService service;


    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        userService.deleteAll();
        service.deleteAll();

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
            username = "patient",
            roles = {"PATIENT"})
    public void testPatientFD() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;
        
        FoodDiaryForm fdf1 = new FoodDiaryForm();

        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());

        fdf1.setDate("2090-11-19T04:50:00.000-05:00");

        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        
        fdf1.setDate("2022-11-19T04:50:00.000-05:00");
        fdf1.setPatient("patient");
        // fdf1.setMealType(MealType.BREAKFAST.toString());
        // MealType.getInfo();
        MealType.BREAKFAST.toString();
        fdf1.setMealType("BREAKFAST");
        fdf1.setFoodName("cereal");
        fdf1.setServings(2.0f);
        fdf1.setCalories(10.0f);
        fdf1.setFat(10.0f);
        fdf1.setSodium(10.0f);
        fdf1.setCarb(10.0f);
        fdf1.setSugar(10.0f);
        fdf1.setFiber(10.0f);
        fdf1.setProtein(10.0f);

        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().isOk());
        mvc.perform(get("/api/v1/diary/mydiary")).andExpect(status().isOk());

        fdf1.setMealType("bla");
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setMealType("BREAKFAST");

        fdf1.setServings(-1.0f);
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setServings(2.0f);

        fdf1.setCalories(-1.0f);
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setCalories(2.0f);

        fdf1.setFat(-1.0f);
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setFat(2.0f);

        fdf1.setSodium(-1.0f);
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setSodium(2.0f);

        fdf1.setCarb(-1.0f);
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setCarb(2.0f);

        fdf1.setSugar(-1.0f);
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setSugar(2.0f);

        fdf1.setFiber(-1.0f);
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setFiber(2.0f);

        fdf1.setProtein(-1.0f);
        mvc.perform(post("/api/v1/diary/creatediary")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(fdf1))).andExpect(status().is4xxClientError());
        fdf1.setProtein(2.0f);
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testHCPFD() throws Exception {
        mvc.perform(get("/api/v1/diary/patient")).andExpect(status().isOk());
    }
}