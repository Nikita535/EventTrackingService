package rtuit.lab.Services.ServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sun.security.auth.UserPrincipal;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import javax.mail.MessagingException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventNotFoundException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.UnusualException;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.Registration;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.EventRepository;
import rtuit.lab.Repositories.RegistrationRepository;
import rtuit.lab.Repositories.UserRepository;

@ContextConfiguration(classes = {RegistrationService.class})
@ExtendWith(SpringExtension.class)
class RegistrationServiceTest {
    @MockBean
    private EmailService emailService;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private RegistrationRepository registrationRepository;

    @Autowired
    private RegistrationService registrationService;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link RegistrationService#registerUserOnEvent(String, Principal)}
     */
    @Test
    void testRegisterUserOnEvent() {
        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        Optional<Event> ofResult = Optional.of(event);
        when(eventRepository.findEventByTag((String) any())).thenReturn(ofResult);

        Event event1 = new Event();
        event1.setDescription("The characteristics of someone or something");
        event1.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event1.setId(123L);
        event1.setLocation("Location");
        event1.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event1.setTag("Tag");
        event1.setTitle("Dr");
        event1.setUser_id(1L);

        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        Registration registration = new Registration();
        registration.setEvent(event1);
        registration.setId(123L);
        registration.setRegistrationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        registration.setUser(user);
        when(registrationRepository.save((Registration) any())).thenReturn(registration);

        User user1 = new User();
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        Optional<User> ofResult1 = Optional.of(user1);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult1);
        ResponseEntity<?> actualRegisterUserOnEventResult = registrationService.registerUserOnEvent("Tag",
                new UserPrincipal("principal"));
        assertTrue(actualRegisterUserOnEventResult.hasBody());
        assertTrue(actualRegisterUserOnEventResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualRegisterUserOnEventResult.getStatusCode());
        verify(eventRepository, atLeast(1)).findEventByTag((String) any());
        verify(registrationRepository).save((Registration) any());
        verify(userRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link RegistrationService#registerUserOnEvent(String, Principal)}
     */
    @Test
    void testRegisterUserOnEvent2() {
        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        Optional<Event> ofResult = Optional.of(event);
        when(eventRepository.findEventByTag((String) any())).thenReturn(ofResult);
        when(registrationRepository.save((Registration) any())).thenThrow(new UnusualException("An error occurred"));

        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult1 = Optional.of(user);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult1);
        assertThrows(UnusualException.class,
                () -> registrationService.registerUserOnEvent("Tag", new UserPrincipal("principal")));
        verify(eventRepository, atLeast(1)).findEventByTag((String) any());
        verify(registrationRepository).save((Registration) any());
        verify(userRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link RegistrationService#registerUserOnEvent(String, Principal)}
     */
    @Test
    void testRegisterUserOnEvent3() {
        when(eventRepository.findEventByTag((String) any())).thenReturn(Optional.empty());

        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);

        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        Registration registration = new Registration();
        registration.setEvent(event);
        registration.setId(123L);
        registration.setRegistrationDate(LocalDateTime.of(1, 1, 1, 1, 1));
        registration.setUser(user);
        when(registrationRepository.save((Registration) any())).thenReturn(registration);

        User user1 = new User();
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user1);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult);
        assertThrows(EventNotFoundException.class,
                () -> registrationService.registerUserOnEvent("Tag", new UserPrincipal("principal")));
        verify(eventRepository).findEventByTag((String) any());
    }

    /**
     * Method under test: {@link RegistrationService#sendMessageAboutRegisterOnEvent(User, Event)}
     */
    @Test
    void testSendMessageAboutRegisterOnEvent() throws MessagingException {
        doNothing().when(emailService).sendSimpleMessage((String) any(), (String) any());

        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        registrationService.sendMessageAboutRegisterOnEvent(user, event);
        verify(emailService).sendSimpleMessage((String) any(), (String) any());
    }

    /**
     * Method under test: {@link RegistrationService#sendMessageAboutRegisterOnEvent(User, Event)}
     */
    @Test
    void testSendMessageAboutRegisterOnEvent2() throws MessagingException {
        doThrow(new UnusualException("An error occurred")).when(emailService)
                .sendSimpleMessage((String) any(), (String) any());

        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        assertThrows(UnusualException.class, () -> registrationService.sendMessageAboutRegisterOnEvent(user, event));
        verify(emailService).sendSimpleMessage((String) any(), (String) any());
    }
}

