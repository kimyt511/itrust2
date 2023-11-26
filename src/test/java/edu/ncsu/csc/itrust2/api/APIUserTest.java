package edu.ncsu.csc.itrust2.api;

import edu.ncsu.csc.itrust2.common.TestUtils;
import edu.ncsu.csc.itrust2.forms.UserForm;
import edu.ncsu.csc.itrust2.models.Personnel;
import edu.ncsu.csc.itrust2.models.User;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.services.UserService;

import java.util.Set;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class APIUserTest {

    private static final String USER_1 = "API_USER_1";

    private static final String USER_2 = "API_USER_2";

    private static final String PW = "123456";

    /** MockMvc uses Spring's testing framework to handle requests to the REST API */
    private MockMvc mvc;

    @Autowired private WebApplicationContext context;

    @Autowired private UserService service;

    /** Sets up the tests. */
    @Before
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
        service.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateUsers() throws Exception {

        Assert.assertEquals("There should be no Users in the system", 0, service.count());

        final UserForm u = new UserForm(USER_1, PW, Role.ROLE_PATIENT, 1);

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(u)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(
                "There should be one user in the system after creating a User", 1, service.count());

        // final User u_tmp = new User(USER_2, PW, Role.ROLE_HCP, 1);
        final UserForm u2 = new UserForm(USER_2, PW, Role.ROLE_HCP, 1);
        final User uu2 = new Personnel(u2);
        
        u2.addRole(Role.ROLE_VIROLOGIST.toString());
        u2.addRole(Role.ROLE_OPH.toString());

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(u2)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Assert.assertEquals(
                "It should be possible to create a user with multiple roles", 2, service.count());

        final User retrieved = service.findByName(USER_2);
        Assert.assertEquals(uu2.equals(retrieved), false);
        Assert.assertEquals(retrieved.equals(u2), false);

        final String USER_3 = "API_USER_3";
        final User retrieved2 = service.findByName(USER_3);
        Assert.assertEquals(retrieved.equals(retrieved2), false);

        final UserForm u3 = new UserForm(USER_3, PW, Role.ROLE_HCP, 1);
        final UserForm u4 = new UserForm(USER_3, PW, Role.ROLE_HCP, 1);
        final User uu3 = new Personnel(u3);
        final User uu4 = new Personnel(u4);
        uu4.setUsername(uu3.getUsername());
        uu4.setPassword(uu3.getPassword());
        uu4.setRoles(uu3.getRoles());
        uu4.setEnabled(uu3.getEnabled());

        Assert.assertEquals(uu4.equals(uu3), true);
        Assert.assertEquals(uu3.equals(uu2), false);

        uu4.setEnabled(null);
        int hc = uu4.hashCode();
        Assert.assertEquals(uu3.equals(uu4), false);
        Assert.assertEquals(uu4.equals(uu3), false);
        

        uu4.setEnabled(uu3.getEnabled());
        uu4.setUsername(null);
        hc = uu4.hashCode();
        Assert.assertEquals(uu3.equals(uu4), false);
        Assert.assertEquals(uu4.equals(uu3), false);

        uu4.setUsername("API_USER_00");
        hc = uu4.hashCode();
        Assert.assertEquals(uu3.equals(uu4), false);
        Assert.assertEquals(uu4.equals(uu3), false);

        uu4.setUsername(uu3.getUsername());
        uu4.setPassword(null);
        hc = uu4.hashCode();
        Assert.assertEquals(uu3.equals(uu4), false);
        Assert.assertEquals(uu4.equals(uu3), false);

        uu4.setPassword("000000");
        hc = uu4.hashCode();
        Assert.assertEquals(uu3.equals(uu4), false);
        Assert.assertEquals(uu4.equals(uu3), false);

        uu4.setPassword(uu3.getPassword());
        uu4.setRoles(Set.of(Role.ROLE_ER));
        hc = uu4.hashCode();
        Assert.assertEquals(uu3.equals(uu4), false);
        Assert.assertEquals(uu4.equals(uu3), false);



        Assert.assertNotNull("The created user should be retrievable from the database", retrieved);

        Assert.assertEquals(
                "The retrieved user should be a Personnel", Personnel.class, retrieved.getClass());

        Assert.assertEquals(
                "The retrieved user should have 3 roles", 3, retrieved.getRoles().size());
    }

    @Test
    @Transactional
    public void testCreateInvalidUsers() throws Exception {

        final UserForm u1 = new UserForm(USER_1, PW, Role.ROLE_ADMIN, 1);

        u1.addRole(Role.ROLE_ER.toString());

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(u1)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(
                "Trying to create an invalid user should not create any User record",
                0,
                service.count());

        final UserForm u2 = new UserForm(USER_2, PW, Role.ROLE_PATIENT, 1);

        u2.addRole(Role.ROLE_HCP.toString());

        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(u2)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());

        Assert.assertEquals(
                "Trying to create an invalid user should not create any User record",
                0,
                service.count());

        final String USER_4 = "API_USER_4";
        final UserForm u4 = new UserForm(USER_4, PW, Role.ROLE_HCP, 1);
        u4.setPassword2("0");
        mvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(u4)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @Transactional
    public void testUpdateUsers() throws Exception {

        final UserForm uf = new UserForm(USER_1, PW, Role.ROLE_HCP, 1);

        final User u1 = new Personnel(uf);

        service.save(u1);

        Assert.assertEquals(u1.getUsername(), service.findByName(USER_1).getUsername());

        uf.addRole(Role.ROLE_ER.toString());

        mvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/users/" + uf.getUsername())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(TestUtils.asJsonString(uf)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        final User retrieved = service.findByName(USER_1);

        Assert.assertEquals(
                "Updating a user should give them additional Roles",
                2,
                retrieved.getRoles().size());
    }
}
