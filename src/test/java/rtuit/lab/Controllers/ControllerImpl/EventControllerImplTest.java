package rtuit.lab.Controllers.ControllerImpl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.security.Principal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rtuit.lab.DTO.EventDTO;
import rtuit.lab.Services.ServiceImpl.EventService;

@ContextConfiguration(classes = {EventControllerImpl.class, EventDTO.class})
@ExtendWith(SpringExtension.class)
class EventControllerImplTest {
    @Autowired
    private EventControllerImpl eventControllerImpl;

    @Autowired
    private EventDTO eventDTO;

    @MockBean
    private EventService eventService;

    /**
     * Method under test: {@link EventControllerImpl#getAllEvents()}
     */
    @Test
    void testGetAllEvents() throws Exception {
        when((ResponseEntity<Object>) eventService.getAllEvents()).thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/event/getAll");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(eventControllerImpl)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link EventControllerImpl#addEvent(EventDTO, Principal)}
     */
    @Test
    void testAddEvent() throws Exception {
        when((ResponseEntity<Object>) eventService.addEvent((EventDTO) any(), (Principal) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        EventDTO eventDTO1 = new EventDTO();
        eventDTO1.setDescription("The characteristics of someone or something");
        eventDTO1.setLocation("Location");
        eventDTO1.setTag("Tag");
        eventDTO1.setTitle("Dr");
        String content = (new ObjectMapper()).writeValueAsString(eventDTO1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/event/addEvent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(eventControllerImpl)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link EventControllerImpl#deleteEvent(EventDTO, Principal)}
     */
    @Test
    void testDeleteEvent() throws Exception {
        when((ResponseEntity<Object>) eventService.deleteEvent((EventDTO) any(), (Principal) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        EventDTO eventDTO1 = new EventDTO();
        eventDTO1.setDescription("The characteristics of someone or something");
        eventDTO1.setLocation("Location");
        eventDTO1.setTag("Tag");
        eventDTO1.setTitle("Dr");
        String content = (new ObjectMapper()).writeValueAsString(eventDTO1);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/event/deleteEvent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(eventControllerImpl)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }

    /**
     * Method under test: {@link EventControllerImpl#checkEventMembers(String, Principal)}
     */
    @Test
    void testCheckEventMembers() throws Exception {
        when((ResponseEntity<Object>) eventService.checkEventMembers((String) any(), (Principal) any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/event/checkEventMembers")
                .param("tag", "foo");
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(eventControllerImpl)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(100));
    }
}

