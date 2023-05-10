package rtuit.lab.Controllers.ControllerImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.DTO.ValidateDTO.RegisterRequestDTO;
import rtuit.lab.Services.ServiceImpl.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private  UserService userService;



    private static final ObjectMapper mapper = new ObjectMapper();


    @Test
    @Sql(value = {"/delete-test-users.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void registerUserSuccess() throws Exception {
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .username("testUser")
                .email("testemail@mail.ru")
                .password("12345678")
                .build();
        String body = mapper.writeValueAsString(registerRequestDTO);
        mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk());
    }


    @Test
    void registerUserUnSuccessWithBadMapping() throws Exception {
        RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                .username("testUser")
                .email("testemail@mail.ru")
                .password("12345678")
                .build();
        String body = mapper.writeValueAsString(registerRequestDTO);
        mockMvc.perform(post("/api/auth/somethingWrong").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNotFound());
    }

    @Nested
    class InnerTest {
        @Test
        void registerUserBadFields() throws Exception {
            RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                    .username("")
                    .email("testemail")
                    .password("123")
                    .build();
            String body = mapper.writeValueAsString(registerRequestDTO);
            mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @Sql(value = {"/add-test-user.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
        @Sql(value = {"/delete-test-users.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        void registerUserAlreadyExists() throws Exception {
            RegisterRequestDTO registerRequestDTO = RegisterRequestDTO.builder()
                    .username("testUser")
                    .email("testemail@mail.ru")
                    .password("12345678")
                    .build();
            String body = mapper.writeValueAsString(registerRequestDTO);
            mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON).content(body))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        void loginUserSuccess() throws Exception {
            UserDTO userDTO = UserDTO.builder()
                    .username("testUser")
                    .email("testing@mail.ru")
                    .password("12345678")
                    .build();
            String credentials = mapper.writeValueAsString(userDTO);
            mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(credentials))
                    .andDo(print())
                    .andExpect(status().is2xxSuccessful());
        }
    }
}
