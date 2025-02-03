package se.kth.iv1201.recruitment.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import se.kth.iv1201.recruitment.security.JwtUtil;


@SpringBootTest
@AutoConfigureMockMvc
@Import(JwtUtil.class)
public class ContentControllerTest {
    
    @Autowired
	private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    

    @Test
    void testCorrectApiLoginGetWithNotLoggedIn() throws Exception{
        mockMvc.perform(get("/api/login")).andExpect(status().isOk()).andExpect(content().string(containsString("login")));
    }
    @Test
    void testCorrectApiLoginGetWithLoggedIn() throws Exception{
        String token = jwtUtil.generateToken("Mockrecruiter");
        mockMvc.perform(get("/api/login").header("Authorization", "Bearer "+token)).andExpect(status().isOk()).andExpect(content().string(containsString("login")));
    }
    @Test
    void testCorrectApiRegisterGetWithNotLoggedIn() throws Exception{
        mockMvc.perform(get("/api/register")).andExpect(status().isOk()).andExpect(content().string(containsString("register")));
    }
    @Test
    void testCorrectApiRegisterGetWithLoggedIn() throws Exception{
        String token = jwtUtil.generateToken("Mockrecruiter");
        mockMvc.perform(get("/api/register").header("Authorization", "Bearer "+token)).andExpect(status().isOk()).andExpect(content().string(containsString("register")));

    }
    @Test 
    void testCorrectApiProtectedGetWithNotLoggedIn() throws Exception{
        mockMvc.perform(get("/api/protected")).andExpect(status().isFound());


    }

    @Test 
    void testCorrectApiProtectedGetWithLoggedIn() throws Exception{
        String token = jwtUtil.generateToken("Mockrecruiter");
        mockMvc.perform(get("/api/protected").header("Authorization", "Bearer "+token)).andExpect(status().isOk()).andExpect(content().string(containsString("Hello Mockrecruiter, you accessed a protected resource!")));       

    }


}
