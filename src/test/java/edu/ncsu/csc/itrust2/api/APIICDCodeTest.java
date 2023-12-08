package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.ICDCodeForm;
import edu.ncsu.csc.itrust2.services.ICDCodeService;
import edu.ncsu.csc.itrust2.models.ICDCode;

import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIICDCodeTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private ICDCodeService service;

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
    public void testCodeAPI() throws Exception {
        final ICDCodeForm form = new ICDCodeForm();
        mvc.perform(
        post("/api/v1/icdcodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(form))).andExpect(status().is4xxClientError());

        form.setCode("T12");
        form.setDescription("Test Code");

        String content =
                mvc.perform(
                                post("/api/v1/icdcodes")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(TestUtils.asJsonString(form)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        final Gson gson = new GsonBuilder().create();
        ICDCodeForm code = gson.fromJson(content, ICDCodeForm.class);
        form.setId(code.getId()); // fill in the id of the code we just
        // created
        assertEquals(form.getCode(), code.getCode());

        content =
                mvc.perform(
                                get("/api/v1/icdcode/" + form.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        code = gson.fromJson(content, ICDCodeForm.class);
        ICDCode ic = gson.fromJson(content, ICDCode.class);
        ICDCode ic2 = gson.fromJson(content, ICDCode.class);
        assertTrue(ic.equals(ic2));
        assertTrue(!ic.equals(form));
        assertEquals(form.getCode(), code.getCode());

        // edit it
        form.setCode("T13");
        mvc.perform(
                put("/api/v1/icdcode/" + form.getId()+10L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestUtils.asJsonString(form)))
                        .andExpect(status().isNotFound());
        content =
                mvc.perform(
                                put("/api/v1/icdcode/" + form.getId())
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(TestUtils.asJsonString(form)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        code = gson.fromJson(content, ICDCodeForm.class);
        assertEquals(form.getCode(), code.getCode());
        content =
                mvc.perform(
                                get("/api/v1/icdcode/" + form.getId())
                                        .contentType(MediaType.APPLICATION_JSON))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        code = gson.fromJson(content, ICDCodeForm.class);
        assertEquals(form.getCode(), code.getCode());

        // then delete it and check that its gone.
        mvc.perform(
                        delete("/api/v1/icdcode/" + "TTTTTTTT")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
        mvc.perform(
                        delete("/api/v1/icdcode/" + form.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertNull(service.findById(form.getId()));
        mvc.perform(get("/api/v1/icdcode/" + form.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mvc.perform(
                        get("/api/v1/icdcodes"))
                .andExpect(status().isOk());
    }

    @Test()//(expected = IllegalArgumentException.class)
    @Transactional
    @WithMockUser(
            username = "admin",
            roles = {"USER", "ADMIN"})
    public void testICDCodeError() throws Exception {
        final ICDCodeForm form = new ICDCodeForm();
        form.setCode("T12");
        form.setDescription("1234567890".repeat(30));

        mvc.perform(
        post("/api/v1/icdcodes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtils.asJsonString(form))).andExpect(status().is4xxClientError());
    }
    
}
