package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import se.kth.iv1201.recruitment.RecruitmentBackendApplication;
import se.kth.iv1201.recruitment.service.ResetService;

@SpringBootTest(classes = RecruitmentBackendApplication.class)
@AutoConfigureMockMvc
@Transactional
public class ResetControllerTest {
    @Autowired
    private ResetService resetService;

    @Autowired
    private MockMvc mockMvc;
    @Mock
    private JavaMailSender javaMailSender;

    /**
     * Tests if a code can be generated
     * 
     * @throws Exception
     */
    @Test
    void testGenerateCodeWithCorrectEmail() throws Exception {
        mockMvc.perform(post("/api/reset/code").content("appltest@example.com")).andExpect(status().isOk());
    }

    /**
     * Tests that the same message comes even if an email that doesn't exist is used
     * 
     * @throws Exception
     */
    @Test
    void testGenerateCodeWithNonCorrectEmail() throws Exception {
        mockMvc.perform(post("/api/reset/code").content("appl11111test@example.com")).andExpect(status().isOk());
    }

    /**
     * Tests to see that the format checked of the backend works
     * 
     * @throws Exception
     */
    @Test
    void testGenerateCodeWithNonEmailFormat() throws Exception {
        mockMvc.perform(post("/api/reset/code").content("appl11111test.example.com"))
                .andExpect(status().isBadRequest());
    }

    /**
     * Tests that it is possible to use the password and username reset feature with
     * a valid code
     * 
     * @throws Exception
     */
    @Test
    void testUseCodeWithAllFieldsCorrect() throws Exception {
        mockMvc
                .perform(
                        post("/api/auth/login")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{\"username\":\"resetUsername\", \"password\":\"resetTestPass\"}"))
                .andExpect(status().isUnauthorized());
        int code = resetService.generateToken("appltest@example.com").getResetToken();
        mockMvc.perform(post("/api/reset/password").header("Content-Type", "application/json")
                .content("{\"email\":\"appltest@example.com\",\"password\":\"resetTestPass\",\"code\":\""
                        + code
                        + "\",\"username\":\"resetUsername\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reset successful")));
        mockMvc
                .perform(
                        post("/api/auth/login")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{\"username\":\"resetUsername\", \"password\":\"resetTestPass\"}"))
                .andExpect(status().isOk());

    }

    /**
     * Tests that an error is given if the email is wrong
     * 
     * @throws Exception
     */
    @Test
    void testUseCodeWithWrongEmail() throws Exception {
        int code = resetService.generateToken("appltest@example.com").getResetToken();
        mockMvc.perform(post("/api/reset/password").header("Content-Type", "application/json")
                .content("{\"email\":\"appl1111test@example.com\",\"password\":\"resetTestPass\",\"code\":\""
                        + code
                        + "\",\"username\":\"resetUsername\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email or code was incorrect")));

    }

    /**
     * Tests that an error is given if the email is wrong format
     * 
     * @throws Exception
     */
    @Test
    void testUseCodeWithWrongFormatEmail() throws Exception {
        int code = resetService.generateToken("appltest@example.com").getResetToken();
        mockMvc.perform(post("/api/reset/password").header("Content-Type", "application/json")
                .content("{\"email\":\"appltest.example.com\",\"password\":\"resetTestPass\",\"code\":\""
                        + code
                        + "\",\"username\":\"resetUsername\"}"))
                .andExpect(status().isBadRequest());

    }

    /**
     * Tests that an error is given if the code is wrong format right format is a
     * number between 100000-1000000
     * 
     * @throws Exception
     */
    @Test
    void testUseCodeWithWrongFormatCode() throws Exception {
        int code = 10;
        mockMvc.perform(post("/api/reset/password").header("Content-Type", "application/json")
                .content("{\"email\":\"appltest.example.com\",\"password\":\"resetTestPass\",\"code\":\""
                        + code
                        + "\",\"username\":\"resetUsername\"}"))
                .andExpect(status().isBadRequest());

    }

    /**
     * Tests that user can not set username to same as another user
     * 
     * @throws Exception
     */
    @Test
    void testUseCodeWithExistingUsername() throws Exception {
        int code = resetService.generateToken("appltest@example.com").getResetToken();
        mockMvc.perform(post("/api/reset/password").header("Content-Type", "application/json")
                .content("{\"email\":\"appltest@example.com\",\"password\":\"resetTestPass\",\"code\":\""
                        + code
                        + "\",\"username\":\"TestRecruiter\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Username already exists")));

    }

    /**
     * Tests that an error is given if the code is incorrect
     * 
     * @throws Exception
     */
    @Test
    void testUseCodeWithWrongCode() throws Exception {
        int code = 500000;
        mockMvc.perform(post("/api/reset/password").header("Content-Type", "application/json")
                .content("{\"email\":\"appltest@example.com\",\"password\":\"resetTestPass\",\"code\":\""
                        + code
                        + "\",\"username\":\"resetUsername\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Email or code was incorrect")));

    }

    /**
     * Tests that it is possible to only reset password
     * 
     * @throws Exception
     */
    @Test
    void testUseCodeWithOnlyPassword() throws Exception {
        mockMvc
                .perform(
                        post("/api/auth/login")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{\"username\":\"TestApplicant\", \"password\":\"resetTestPass\"}"))
                .andExpect(status().isUnauthorized());
        int code = resetService.generateToken("appltest@example.com").getResetToken();
        mockMvc.perform(post("/api/reset/password").header("Content-Type", "application/json")
                .content("{\"email\":\"appltest@example.com\",\"password\":\"resetTestPass\",\"code\":\""
                        + code
                        + "\",\"username\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reset successful")));
        mockMvc
                .perform(
                        post("/api/auth/login")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{\"username\":\"TestApplicant\", \"password\":\"resetTestPass\"}"))
                .andExpect(status().isOk());

    }

    /**
     * Tests that it is possible to only reset username
     * 
     * @throws Exception
     */
    @Test
    void testUseCodeWithOnlyUsername() throws Exception {
        mockMvc
                .perform(
                        post("/api/auth/login")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{\"username\":\"resetUsername\", \"password\":\"testpassword\"}"))
                .andExpect(status().isUnauthorized());
        int code = resetService.generateToken("appltest@example.com").getResetToken();
        mockMvc.perform(post("/api/reset/password").header("Content-Type", "application/json")
                .content("{\"email\":\"appltest@example.com\",\"password\":\"\",\"code\":\"" + code
                        + "\",\"username\":\"resetUsername\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Reset successful")));
        mockMvc
                .perform(
                        post("/api/auth/login")
                                .header("Content-Type", "application/json")
                                .content(
                                        "{\"username\":\"resetUsername\", \"password\":\"testpassword\"}"))
                .andExpect(status().isOk());

    }

}
