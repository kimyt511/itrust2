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

        // Parse response as Loinc object.
        final Gson gson = new GsonBuilder().create();
        final Loinc loinc1 = gson.fromJson(content, Loinc.class);
        final LoincForm loincForm1 = new LoincForm(loinc1);
        assertEquals(form.getCode(), loincForm1.getCode());
        assertEquals(form.getName(), loincForm1.getName());
        assertEquals(form.getComponent(), loincForm1.getComponent());
        assertEquals(form.getProperty(), loincForm1.getProperty());


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

        // Delete the loinc
        mvc.perform(delete("/api/v1/loinccodes/" + loinc1.getId()))
                .andExpect(status().isOk());

        // Verify that the LOINC record has been deleted
        mvc.perform(get("/api/v1/loinccodes"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.not(Matchers.containsString(form.getCode()))));
    }
}
