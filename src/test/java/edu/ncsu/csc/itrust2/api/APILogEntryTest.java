package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.controllers.api.comm.LogEntryRequestBody;
import edu.ncsu.csc.itrust2.controllers.api.comm.LogEntryTableRow;
import edu.ncsu.csc.itrust2.models.security.LogEntry;
import edu.ncsu.csc.itrust2.services.*;
import edu.ncsu.csc.itrust2.models.*;
import edu.ncsu.csc.itrust2.forms.*;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.common.TestUtils;
import com.google.gson.reflect.TypeToken;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;

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
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APILogEntryTest {
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private UserService userService;

    /** Sets up test */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        final User patient = new Patient(new UserForm("patient", "123456", Role.ROLE_PATIENT, 1));

        final User hcp = new Personnel(new UserForm("hcp", "123456", Role.ROLE_HCP, 1));

        final User admin = new Personnel(new UserForm("admin", "123456", Role.ROLE_ADMIN, 1));

        userService.saveAll(List.of(patient, hcp, admin));
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testLogEntry() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        LogEntryRequestBody LERB = new LogEntryRequestBody();
        LERB.setStartDate(ZonedDateTime.of(2022, 6, 18, 20, 30, 0, 0, ZoneId.of("Asia/Seoul")).toString());
        LERB.setEndDate(ZonedDateTime.of(2022, 6, 30, 20, 30, 0, 0, ZoneId.of("Asia/Seoul")).toString());
        LERB.setPage(1);
        LERB.setPageLength(10);

        content =
                mvc.perform(post("/api/v1/logentries/range")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(LERB)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<LogEntry> dlist = gson.fromJson(content, new TypeToken<ArrayList<LogEntry>>() {}.getType());

        LERB.setStartDate(ZonedDateTime.of(2022, 7, 18, 20, 30, 0, 0, ZoneId.of("Asia/Seoul")).toString());
        mvc.perform(post("/api/v1/logentries/range")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(LERB)))
                            .andExpect(status().isNotAcceptable());
    }

    @Test()
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testLogEntry2() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        LogEntryRequestBody LERB = new LogEntryRequestBody();
        LERB.setStartDate("");
        LERB.setEndDate(ZonedDateTime.of(2022, 6, 30, 20, 30, 0, 0, ZoneId.of("Asia/Seoul")).toString());
        LERB.setPage(1);
        LERB.setPageLength(10);

        content =
                mvc.perform(post("/api/v1/logentries/range")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(LERB)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<LogEntry> dlist = gson.fromJson(content, new TypeToken<ArrayList<LogEntry>>() {}.getType());
    }

    @Test()
    @Transactional
    @WithMockUser(
            username = "hcp",
            roles = {"HCP"})
    public void testLogEntry3() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        LogEntryRequestBody LERB = new LogEntryRequestBody();
        LERB.setStartDate(ZonedDateTime.of(2022, 6, 30, 20, 30, 0, 0, ZoneId.of("Asia/Seoul")).toString());
        LERB.setEndDate("");
        LERB.setPage(1);
        LERB.setPageLength(10);

        content =
                mvc.perform(post("/api/v1/logentries/range")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(LERB)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<LogEntry> dlist = gson.fromJson(content, new TypeToken<ArrayList<LogEntry>>() {}.getType());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "patient",
            roles = {"PATIENT"})
    public void testLogEntry4() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        LogEntryRequestBody LERB = new LogEntryRequestBody();
        LERB.setStartDate(ZonedDateTime.of(2022, 6, 18, 20, 30, 0, 0, ZoneId.of("Asia/Seoul")).toString());
        LERB.setEndDate(ZonedDateTime.of(2022, 6, 30, 20, 30, 0, 0, ZoneId.of("Asia/Seoul")).toString());
        LERB.setPage(1);
        LERB.setPageLength(10);

        content =
                mvc.perform(post("/api/v1/logentries/range")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(LERB)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<LogEntry> dlist = gson.fromJson(content, new TypeToken<ArrayList<LogEntry>>() {}.getType());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "patient",
            roles = {"PATIENT"})
    public void testLogEntry5() throws Exception {
        final Gson gson = new GsonBuilder().create();
        String content;

        LogEntryRequestBody LERB = new LogEntryRequestBody();
        LERB.setStartDate(LocalDate.of(2022,6,30).toString());
        LERB.setEndDate(LocalDate.of(2022,7,30).toString());
        LERB.setPage(1);
        LERB.setPageLength(10);

        content =
                mvc.perform(post("/api/v1/logentries/range")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(TestUtils.asJsonString(LERB)))
                        .andReturn()
                        .getResponse()
                        .getContentAsString();
        List<LogEntry> dlist = gson.fromJson(content, new TypeToken<ArrayList<LogEntry>>() {}.getType());
    }

    @Test
    @Transactional
    @WithMockUser(
            username = "patient",
            roles = {"PATIENT"})
    public void testLogEntry6() throws Exception {
        LogEntryTableRow LETR = new LogEntryTableRow();
        LETR.setNumPages(1);
        LETR.setPatient(true);
        LETR.setPrimary("patient");
        LETR.setSecondary("hcp");
        LETR.setRole("PATIENT");
        LETR.setDateTime(LocalDate.of(2022,2,2).toString());
        LETR.setTransactionType("");

        assertEquals(1, LETR.getNumPages());
        assertEquals(true, LETR.isPatient());
        assertEquals("patient", LETR.getPrimary());
        assertEquals("hcp", LETR.getSecondary());
        assertEquals("PATIENT", LETR.getRole());
        assertEquals(LocalDate.of(2022,2,2).toString(), LETR.getDateTime());
        assertEquals("", LETR.getTransactionType());

    }

}

