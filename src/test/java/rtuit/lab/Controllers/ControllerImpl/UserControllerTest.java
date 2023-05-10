package rtuit.lab.Controllers.ControllerImpl;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.Services.ServiceImpl.UserService;

import java.security.Principal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private UserService userService;

    private static final ObjectMapper mapper = new ObjectMapper();


    @Test
    @Sql(value = {"/delete-test-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/add-test-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "NoUser")
    void changeUserInfoUnauthorized() throws Exception {
        Authentication authentication = new TestingAuthenticationToken("NoUser", "123456","ROLE_USER");
        UserDTO  userDTO= UserDTO.builder()
                .username("testUser")
                .email("testemail@mail.ru")
                .password("12345678")
                .build();
        String body = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/api/user/edit").contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Sql(value = {"/delete-test-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Sql(value = {"/add-test-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @WithMockUser(username = "testUser")
    void changeUserInfoSuccess() throws Exception {
        Authentication authentication = new TestingAuthenticationToken("testUser", "12345678","ROLE_USER");
        UserDTO  userDTO= UserDTO.builder()
                .username("testUser")
                .email("testemail@mail.ru")
                .password("12345678")
                .build();
        String body = mapper.writeValueAsString(userDTO);
        mockMvc.perform(post("/api/user/edit").contentType(MediaType.APPLICATION_JSON).content(body))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}
