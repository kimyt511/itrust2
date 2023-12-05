package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.models.Loinc;
import edu.ncsu.csc.itrust2.forms.LoincForm;
import edu.ncsu.csc.itrust2.services.LoincService;

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
public class APILoincTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private LoincService service;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        service.deleteAll();
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "admin",
            roles = {"USER", "ADMIN"})
    public void testLoincAPI() throws UnsupportedEncodingException, Exception {
        // Create LoincForm for testing
        final LoincForm form = new LoincForm();
        form.setCode("12345-0");
        form.setName("Test Name");
        form.setComponent("Test Component");
        form.setProperty("Test Property");

        final LoincForm form2 = new LoincForm();
        form2.setCode("12345-0");
        form2.setName("Test Name 2");
        form2.setComponent("Test Component 2");
        form2.setProperty("Test Property 2");

        final LoincForm form3 = new LoincForm();
        form3.setCode("12346-0");
        form3.setName("Test Name 3");
        form3.setComponent("Test Component 3");
        form3.setProperty("Test Property 3");

        // Add Loinc to system
        final String content =
                mvc.perform(
                                post("/api/v1/loinccodes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(TestUtils.asJsonString(form)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        // Try to add Loinc with same Code to system
        final String content2 =
                mvc.perform(
                        post("/api/v1/loinccodes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form2)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        final String content3 =
                mvc.perform(
                        post("/api/v1/loinccodes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(form3)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        mvc.perform(
                post("/api/v1/loinccodes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString("wrongForm")))
                .andExpect(status().isBadRequest());

        // Parse response as Loinc object.
        final Gson gson = new GsonBuilder().create();
        final Loinc loinc1 = gson.fromJson(content, Loinc.class);
        final LoincForm loincForm1 = new LoincForm(loinc1);
        assertEquals(form.getCode(), loincForm1.getCode());
        assertEquals(form.getName(), loincForm1.getName());
        assertEquals(form.getComponent(), loincForm1.getComponent());
        assertEquals(form.getProperty(), loincForm1.getProperty());
        final Loinc loinc3 = gson.fromJson(content3, Loinc.class);
        final LoincForm loincForm3 = new LoincForm(loinc3);


        // Verify loinc have been added
        mvc.perform(get("/api/v1/loinccodes"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString(form.getCode())));

        // Edit loinc's Property
        loinc1.setProperty("This is a better Property");
        final String editContent =
                mvc.perform(
                        put("/api/v1/loinccodes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(loinc1)))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        final Loinc editedLoinc = gson.fromJson(editContent, Loinc.class);
        assertEquals(loinc1.getId(), editedLoinc.getId());
        assertEquals(loinc1.getCode(), editedLoinc.getCode());
        assertEquals(loinc1.getName(), editedLoinc.getName());
        assertEquals(loinc1.getComponent(), editedLoinc.getComponent());
        assertEquals("This is a better Property", editedLoinc.getProperty());

        // Try to edit loinc with existing code
        loinc3.setCode("12345-0");
        final String editIntoExistingCode =
                mvc.perform(
                        put("/api/v1/loinccodes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(loinc3)))
                        .andExpect(status().isConflict())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();


        // Delete the loinc
        mvc.perform(delete("/api/v1/loinccodes/" + loinc1.getId()))
                .andExpect(status().isOk());

        // Try to delete loinc with non existing id
        mvc.perform(delete("/api/v1/loinccodes/" + 0))
                .andExpect(status().isBadRequest());

        // Try to delete loinc with wrong id
        mvc.perform(delete("/api/v1/loinccodes/" + "wrongId"))
                .andExpect(status().isBadRequest());

        // Verify that the LOINC record has been deleted
        mvc.perform(get("/api/v1/loinccodes"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.not(Matchers.containsString(form.getCode()))));

        // Try to edit non existing loinc
        final String editNonexistingContent =
                mvc.perform(
                        put("/api/v1/loinccodes")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(loinc1)))
                        .andExpect(status().isBadRequest())
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
    }
}
