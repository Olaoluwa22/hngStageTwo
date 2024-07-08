package com.hng.stagetwo.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hng.stagetwo.model.Organization;
import com.hng.stagetwo.model.User;
import com.hng.stagetwo.repository.OrganizationRepository;
import com.hng.stagetwo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class spec {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserSuccessfullyWithDefaultOrganization() throws Exception {
        String userJson = "{ \"firstName\": \"John\", \"lastName\": \"Smith\", \"email\": \"johnsmith@example.com\", \"password\": \"password\", \"phone\": \"09012345678\" }";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Registration Successful"))
                .andExpect(jsonPath("$.data.user.userId").isNotEmpty())
                .andExpect(jsonPath("$.data.user.firstName").value("John"))
                .andExpect(jsonPath("$.data.user.lastName").value("Smith"))
                .andExpect(jsonPath("$.data.user.email").value("johnsmith@example.com"))
                .andExpect(jsonPath("$.data.user.phone").value("09012345678"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());

        User user = userRepository.findByEmail("johnsmith@example.com").orElse(null);
        List<Organization> organization = organizationRepository.findByUsers_Id(user.getId());

        assertNotNull(user);
        assertNotNull(organization);
        assertEquals("John's organization", organization.get(0).getName());
    }
    @Test
    void shouldFailIfRequiredFieldsAreMissing() throws Exception {
        String[] requiredFields = {"firstName", "lastName", "email", "password"};
        for (String field : requiredFields) {
            String userJson = "{ \"firstName\": \"John\", \"lastName\": \"Smith\", \"email\": \"johnsmith@example.com\", \"password\": \"password\" }";
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(userJson);
            ((ObjectNode) node).remove(field);

            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(node)))
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath("$.errors").isArray())
                    .andExpect(jsonPath("$.errors[?(@.field == '" + field + "')].message").value("must not be null"));
        }
    }
    @Test
    void shouldFailIfDuplicateEmailIsUsed() throws Exception {
        String userJson = "{ \"firstName\": \"John\", \"lastName\": \"Smith\", \"email\": \"johnsmith@example.com\", \"password\": \"password\", \"phone\": \"09012345678\" }";
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        String duplicateUserJson = "{ \"firstName\": \"Jane\", \"lastName\": \"Mary\", \"email\": \"johnsmith@example.com\", \"password\": \"password\",  \"phone\": \"09012345678\" }";
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(duplicateUserJson))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").value("Registration unsuccessful"));
    }
    @Test
    void shouldLoginUserSuccessfully() throws Exception {
        // First register the user
        String userJson = "{ \"firstName\": \"John\", \"lastName\": \"Smith\", \"email\": \"johnsmith@example.com\", \"password\": \"password\", \"phone\": \"09012345678\" }";
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isCreated());

        // Then try to log in
        String loginJson = "{ \"email\": \"johnsmith@example.com\", \"password\": \"password\" }";
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.user.userId").isNotEmpty())
                .andExpect(jsonPath("$.data.user.firstName").value("John"))
                .andExpect(jsonPath("$.data.user.lastName").value("Smith"))
                .andExpect(jsonPath("$.data.user.email").value("johnsmith@example.com"))
                .andExpect(jsonPath("$.data.user.phone").value("09012345678"))
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());
    }
    @Test
    void shouldFailLoginWithInvalidCredentials() throws Exception {
        String loginJson = "{ \"email\": \"wrongemail@example.com\", \"password\": \"wrongpassword\" }";
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Authentication failed"));
    }
}
