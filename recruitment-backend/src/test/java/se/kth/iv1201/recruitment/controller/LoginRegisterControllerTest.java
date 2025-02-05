package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import se.kth.iv1201.recruitment.security.JwtUtil;


@SpringBootTest
@AutoConfigureMockMvc
@Import(JwtUtil.class)
public class LoginRegisterControllerTest {
    
    @Autowired
	private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void testCorrectApiLoginPostWithNotLoggedIn() throws Exception{
        mockMvc.perform(post("/api/login").header("Content-Type", "application/json").content("{\"username\":\"Mockrecruiter\"}")).andExpect(status().isOk()).andExpect(content().string(containsString("Bearer")));
    }
    
    @Test
    void testCorrectApiLoginPostWithLoggedIn() throws Exception{
        String token = jwtUtil.generateToken("Mockrecruiter");
        mockMvc.perform(post("/api/login").header("Content-Type", "application/json").header("Authorization", "Bearer "+token).content("{\"username\":\"Mockrecruiter\"}")).andExpect(status().isOk()).andExpect(content().string(containsString("Bearer")));
    }
    @Test
    void testCorrectApiAuthenticationWhenLoggedInUsingLogin() throws Exception{
        MvcResult apiLoginResult = mockMvc.perform(post("/api/login").header("Content-Type", "application/json").content("{\"username\":\"Mockrecruiter\"}")).andExpect(status().isOk()).andExpect(content().string(containsString("Bearer"))).andReturn();
        String returnedToken = apiLoginResult.getResponse().getContentAsString();
        mockMvc.perform(get("/api/protected").header("Content-Type","application/json").header("Authorization", returnedToken)).andExpect(status().isOk()).andExpect(content().string(containsString("Hello Mockrecruiter, you accessed a protected resource!")));
    }
    
    
    
    



}
