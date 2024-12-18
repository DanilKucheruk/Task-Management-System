package com.tsm.integration.controller.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.tsm.integration.IntegrationTestBase;
import com.tsm.integration.annotation.IT;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@IT
@AutoConfigureMockMvc(printOnlyOnFailure = false)
public class AuthenticationControllerIT extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    private static final String VALID_USERNAME = "test@gmail.com";
    private static final String VALID_PASSWORD = "test";

    @Test
    void createAuthTokenReturnAuthToken() throws Exception {

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(VALID_USERNAME, VALID_PASSWORD));

        String requestBody = """
                {
                    "email": "test@gmail.com",
                    "password": "test"
                }
                    """;

        var requestBuilder = MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk());
    }

    @Test
    void createAuthTokenReturnisUnauthorized() throws Exception {

        String requestBody = """
                {
                    "email": "testUser",
                    "password": "wrong_pass"
                }
                    """;

        var requestBuilder = MockMvcRequestBuilders.post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isUnauthorized());
    }

    @Test
    public void createNewUserReturnStatus200() throws Exception {
        String requestBody = """
                {
                    "email": "testUser@test.com",
                    "password": "test",
                    "role": "ADMIN"
                }
                    """;

        var requestBuilder = MockMvcRequestBuilders.post("/api/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    public void createNewUserReturnStatus401AndMassage() throws Exception {
        String requestBody = """
                {
                    "email": "test@gmail.com",
                    "password": "test"
                }
                    """;

        var requestBuilder = MockMvcRequestBuilders.post("/api/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        mockMvc.perform(requestBuilder).andExpectAll(
                status().isBadRequest(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.message", is("The user with this email already exists")));
    }
}

