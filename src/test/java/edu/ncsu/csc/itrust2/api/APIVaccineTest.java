package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.VaccineForm;
import edu.ncsu.csc.itrust2.models.Vaccine;
import edu.ncsu.csc.itrust2.services.VaccineService;

import java.io.UnsupportedEncodingException;
import javax.transaction.Transactional;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class for testing Vaccine API.
 *
 * @author Connor
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIVaccineTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private VaccineService service;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        service.deleteAll();
    }

    /** Tests basic Vaccine API functionality. */
    @Test
    @Transactional
    @WithMockUser(
            username = "admin",
            roles = {"USER", "ADMIN"})
    public void testVaccineAPI() throws UnsupportedEncodingException, Exception {
        // Create Vaccines for testing
        final VaccineForm form1 = new VaccineForm();
        form1.setName("TEST1");
        form1.setAbbreviation("T1");
        form1.setCptCode("90000");
        form1.setComments("DESC1");


        // Add Vaccine1 to system
        final String content1 =
                mvc.perform(
                                post("/api/v1/vaccine")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(TestUtils.asJsonString(form1)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        // Parse response as Vaccine object
        final Gson gson = new GsonBuilder().create();
        final Vaccine Vaccine1 = gson.fromJson(content1, Vaccine.class);
        final VaccineForm vf1 = new VaccineForm(Vaccine1);
        assertEquals(form1.getCptCode(), vf1.getCptCode());
        assertEquals(form1.getName(), vf1.getName());

        // Verify Vaccines have been added
        mvc.perform(get("/api/v1/vaccine"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString(form1.getCptCode())));

        // Edit first Vaccine's description
        Vaccine1.setComments("This is a better description");
        final String editContent =
                mvc.perform(
                                put("/api/v1/vaccine")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(TestUtils.asJsonString(Vaccine1)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        final Vaccine editedVaccine = gson.fromJson(editContent, Vaccine.class);
        assertEquals(Vaccine1.getId(), editedVaccine.getId());
        assertEquals(Vaccine1.getCptCode(), editedVaccine.getCptCode());
        assertEquals(Vaccine1.getName(), editedVaccine.getName());
        assertEquals("This is a better description", editedVaccine.getComments());

        // Follow up with valid edit
        Vaccine1.setCptCode("90001");
        mvc.perform(
                        put("/api/v1/vaccine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(Vaccine1)))
                .andExpect(status().isOk());
    }
}
