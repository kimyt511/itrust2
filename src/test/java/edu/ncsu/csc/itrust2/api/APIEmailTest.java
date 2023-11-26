package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.models.Email;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Patient;
import edu.ncsu.csc.itrust2.services.EmailService;
import edu.ncsu.csc.itrust2.services.UserService;
import edu.ncsu.csc.itrust2.models.Personnel;

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

import java.util.List;

/**
 * Class for testing drug API.
 *
 * @author Connor
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIEmailTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private EmailService service;

    @Autowired private UserService userService;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        service.deleteAll();

        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));

        final User admin = new Personnel(new UserForm("admin", "123456", Role.ROLE_ADMIN, 1));

        userService.saveAll(List.of(patient, hcp, admin));
    }

    /** Tests basic drug API functionality. */
    @Test
    @Transactional
    @WithMockUser(
            username = "admin",
            roles = {"USER", "ADMIN"})
    public void testDrugAPI() throws UnsupportedEncodingException, Exception {
        // Create drugs for testing
        final User patient = userService.findByName("patient");
        final User hcp = userService.findByName("hcp");

        final Email email1 = new Email("sender1@gmail.com", patient, "subject1", "msg1");
        final Email email2 = new Email("sender2@gmail.com", patient, "subject2", "msg2");
        final Email email3 = new Email("sender3@gmail.com", hcp, "subject3", "msg3");

        service.saveAll(List.of(email1, email2, email3));
        
        final List<Email> received_emails = service.findByReceiver(hcp);

        mvc.perform(get("/api/v1/emails"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString(email3.getSender())));

        final Email email4 = new Email();
        email4.setSender("sender4@gmail.com");
        email4.setReceiver(hcp);
        email4.setSubject("subject4");
        email4.setMessageBody("msg4");

        assertEquals(email4.getReceiver().getUsername(), "hcp");
        assertEquals(email4.getSubject(), "subject4");
        assertEquals(email4.getMessageBody(), "msg4");
    }
}
