package rtuit.lab.Services.ServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sun.security.auth.UserPrincipal;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import javax.mail.MessagingException;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rtuit.lab.DTO.EventDTO;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventAlreadyExistsException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventNotFoundException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.PermissionDeniedException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.UnusualException;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.EventRepository;
import rtuit.lab.Repositories.RegistrationRepository;
import rtuit.lab.Repositories.UserRepository;

@ContextConfiguration(classes = {EventService.class, EventDTO.class})
@ExtendWith(SpringExtension.class)
class EventServiceTest {
    @MockBean
    private EmailService emailService;

    @Autowired
    private EventDTO eventDTO;

    @MockBean
    private EventRepository eventRepository;

    @Autowired
    private EventService eventService;

    @MockBean
    private RegistrationRepository registrationRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    /**
     * Method under test: {@link EventService#getAllEvents()}
     */
    @Test
    void testGetAllEvents() {
        when(eventRepository.findAll()).thenReturn(new ArrayList<>());
        assertThrows(EventNotFoundException.class, () -> eventService.getAllEvents());
        verify(eventRepository).findAll();
    }

    /**
     * Method under test: {@link EventService#getAllEvents()}
     */
    @Test
    void testGetAllEvents2() {
        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("событий не найдено");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("событий не найдено");
        event.setTitle("Dr");
        event.setUser_id(1L);

        ArrayList<Event> eventList = new ArrayList<>();
        eventList.add(event);
        when(eventRepository.findAll()).thenReturn(eventList);
        ResponseEntity<?> actualAllEvents = eventService.getAllEvents();
        assertTrue(actualAllEvents.hasBody());
        assertEquals(HttpStatus.OK, actualAllEvents.getStatusCode());
        assertTrue(actualAllEvents.getHeaders().isEmpty());
        verify(eventRepository).findAll();
    }

    /**
     * Method under test: {@link EventService#getAllEvents()}
     */
    @Test
    void testGetAllEvents3() {
        when(eventRepository.findAll()).thenThrow(new EventNotFoundException("An error occurred"));
        assertThrows(EventNotFoundException.class, () -> eventService.getAllEvents());
        verify(eventRepository).findAll();
    }

    /**
     * Method under test: {@link EventService#getUserAuth(Principal)}
     */
    @Test
    void testGetUserAuth() throws UsernameNotFoundException {
        User user = new User();
        when(userService.loadUserByUsername((String) any())).thenReturn(user);
        assertSame(user, eventService.getUserAuth(new UserPrincipal("principal")));
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#getUserAuth(Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetUserAuth2() throws UsernameNotFoundException {
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        eventService.getUserAuth(null);
    }

    /**
     * Method under test: {@link EventService#getUserAuth(Principal)}
     */
    @Test
    void testGetUserAuth3() throws UsernameNotFoundException {
        when(userService.loadUserByUsername((String) any())).thenThrow(new EventNotFoundException("An error occurred"));
        assertThrows(EventNotFoundException.class, () -> eventService.getUserAuth(new UserPrincipal("principal")));
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#addEvent(EventDTO, Principal)}
     */
    @Test
    void testAddEvent() {
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
        assertThrows(EventAlreadyExistsException.class,
                () -> eventService.addEvent(eventDTO, new UserPrincipal("principal")));
        verify(eventRepository).findEventByTag((String) any());
    }

    /**
     * Method under test: {@link EventService#addEvent(EventDTO, Principal)}
     */
    @Test
    void testAddEvent2() throws UsernameNotFoundException {
        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        when(eventRepository.save((Event) any())).thenReturn(event);
        when(eventRepository.findEventByTag((String) any())).thenReturn(Optional.empty());
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        ResponseEntity<?> actualAddEventResult = eventService.addEvent(eventDTO, new UserPrincipal("principal"));
        assertTrue(actualAddEventResult.hasBody());
        assertTrue(actualAddEventResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualAddEventResult.getStatusCode());
        assertNull(((Event) actualAddEventResult.getBody()).getTag());
        assertNull(((Event) actualAddEventResult.getBody()).getLocation());
        assertNull(((Event) actualAddEventResult.getBody()).getId());
        assertNull(((Event) actualAddEventResult.getBody()).getDescription());
        assertNull(((Event) actualAddEventResult.getBody()).getTitle());
        assertNull(((Event) actualAddEventResult.getBody()).getUser_id());
        verify(eventRepository).save((Event) any());
        verify(eventRepository).findEventByTag((String) any());
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#addEvent(EventDTO, Principal)}
     */
    @Test
    void testAddEvent3() throws UsernameNotFoundException {
        when(eventRepository.save((Event) any())).thenThrow(new EventNotFoundException("An error occurred"));
        when(eventRepository.findEventByTag((String) any())).thenReturn(Optional.empty());
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        assertThrows(UnusualException.class, () -> eventService.addEvent(eventDTO, new UserPrincipal("principal")));
        verify(eventRepository).save((Event) any());
        verify(eventRepository).findEventByTag((String) any());
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#addEvent(EventDTO, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testAddEvent4() throws UsernameNotFoundException {
        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        when(eventRepository.save((Event) any())).thenReturn(event);
        when(eventRepository.findEventByTag((String) any())).thenReturn(Optional.empty());
        when(userService.loadUserByUsername((String) any())).thenReturn(null);
        eventService.addEvent(eventDTO, new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#addEvent(EventDTO, Principal)}
     */
    @Test
    void testAddEvent5() throws UsernameNotFoundException {
        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        when(eventRepository.save((Event) any())).thenReturn(event);
        when(eventRepository.findEventByTag((String) any())).thenReturn(Optional.empty());
        User user = mock(User.class);
        when(user.getId()).thenReturn(123L);
        when(userService.loadUserByUsername((String) any())).thenReturn(user);
        ResponseEntity<?> actualAddEventResult = eventService.addEvent(eventDTO, new UserPrincipal("principal"));
        assertTrue(actualAddEventResult.hasBody());
        assertTrue(actualAddEventResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualAddEventResult.getStatusCode());
        assertNull(((Event) actualAddEventResult.getBody()).getTag());
        assertNull(((Event) actualAddEventResult.getBody()).getLocation());
        assertNull(((Event) actualAddEventResult.getBody()).getId());
        assertNull(((Event) actualAddEventResult.getBody()).getDescription());
        assertNull(((Event) actualAddEventResult.getBody()).getTitle());
        assertEquals(123L, ((Event) actualAddEventResult.getBody()).getUser_id().longValue());
        verify(eventRepository).save((Event) any());
        verify(eventRepository).findEventByTag((String) any());
        verify(userService).loadUserByUsername((String) any());
        verify(user).getId();
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testDeleteEvent() throws UsernameNotFoundException {

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
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        eventService.deleteEvent(eventDTO, new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    void testDeleteEvent2() throws UsernameNotFoundException {
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
        when(userService.loadUserByUsername((String) any())).thenThrow(new EventNotFoundException("An error occurred"));
        assertThrows(EventNotFoundException.class,
                () -> eventService.deleteEvent(eventDTO, new UserPrincipal("principal")));
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testDeleteEvent3() throws UsernameNotFoundException {

        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
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
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        eventService.deleteEvent(eventDTO, new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testDeleteEvent4() throws UsernameNotFoundException {
        when(eventRepository.findEventByTag((String) any())).thenReturn(Optional.empty());
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        eventService.deleteEvent(eventDTO, new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testDeleteEvent5() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
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
        when(userService.loadUserByUsername((String) any())).thenReturn(null);
        eventService.deleteEvent(eventDTO, new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    void testDeleteEvent6() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        Optional<Event> ofResult = Optional.of(event);
        doNothing().when(eventRepository).delete((Event) any());
        when(eventRepository.findEventByTag((String) any())).thenReturn(ofResult);
        doNothing().when(registrationRepository).deleteAllByEvent((Event) any());
        when(userService.loadUserByUsername((String) any()))
                .thenReturn(new User(123L, "janedoe", "iloveyou", "jane.doe@example.org", true, new HashSet<>()));
        ResponseEntity<?> actualDeleteEventResult = eventService.deleteEvent(eventDTO, new UserPrincipal("principal"));
        assertEquals("Событие удалено", actualDeleteEventResult.getBody());
        assertEquals(HttpStatus.OK, actualDeleteEventResult.getStatusCode());
        assertTrue(actualDeleteEventResult.getHeaders().isEmpty());
        verify(eventRepository, atLeast(1)).findEventByTag((String) any());
        verify(eventRepository).delete((Event) any());
        verify(event).getId();
        verify(event).setDescription((String) any());
        verify(event).setEndDate((LocalDateTime) any());
        verify(event).setId((Long) any());
        verify(event).setLocation((String) any());
        verify(event).setStartDate((LocalDateTime) any());
        verify(event).setTag((String) any());
        verify(event).setTitle((String) any());
        verify(event).setUser_id((Long) any());
        verify(registrationRepository).deleteAllByEvent((Event) any());
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    void testDeleteEvent7() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        Optional<Event> ofResult = Optional.of(event);
        doNothing().when(eventRepository).delete((Event) any());
        when(eventRepository.findEventByTag((String) any())).thenReturn(ofResult);
        doThrow(new EventNotFoundException("An error occurred")).when(registrationRepository)
                .deleteAllByEvent((Event) any());
        when(userService.loadUserByUsername((String) any()))
                .thenReturn(new User(123L, "janedoe", "iloveyou", "jane.doe@example.org", true, new HashSet<>()));
        assertThrows(EventNotFoundException.class,
                () -> eventService.deleteEvent(eventDTO, new UserPrincipal("principal")));
        verify(eventRepository, atLeast(1)).findEventByTag((String) any());
        verify(event).getId();
        verify(event).setDescription((String) any());
        verify(event).setEndDate((LocalDateTime) any());
        verify(event).setId((Long) any());
        verify(event).setLocation((String) any());
        verify(event).setStartDate((LocalDateTime) any());
        verify(event).setTag((String) any());
        verify(event).setTitle((String) any());
        verify(event).setUser_id((Long) any());
        verify(registrationRepository).deleteAllByEvent((Event) any());
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    void testDeleteEvent8() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(1L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        Optional<Event> ofResult = Optional.of(event);
        doNothing().when(eventRepository).delete((Event) any());
        when(eventRepository.findEventByTag((String) any())).thenReturn(ofResult);
        doNothing().when(registrationRepository).deleteAllByEvent((Event) any());
        when(userService.loadUserByUsername((String) any()))
                .thenReturn(new User(123L, "janedoe", "iloveyou", "jane.doe@example.org", true, new HashSet<>()));
        assertThrows(PermissionDeniedException.class,
                () -> eventService.deleteEvent(eventDTO, new UserPrincipal("principal")));
        verify(eventRepository).findEventByTag((String) any());
        verify(event).getId();
        verify(event).setDescription((String) any());
        verify(event).setEndDate((LocalDateTime) any());
        verify(event).setId((Long) any());
        verify(event).setLocation((String) any());
        verify(event).setStartDate((LocalDateTime) any());
        verify(event).setTag((String) any());
        verify(event).setTitle((String) any());
        verify(event).setUser_id((Long) any());
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#deleteEvent(EventDTO, Principal)}
     */
    @Test
    void testDeleteEvent9() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        Optional<Event> ofResult = Optional.of(event);
        doNothing().when(eventRepository).delete((Event) any());
        when(eventRepository.findEventByTag((String) any())).thenReturn(ofResult);
        doNothing().when(registrationRepository).deleteAllByEvent((Event) any());
        User user = mock(User.class);
        when(user.getId()).thenReturn(123L);
        when(userService.loadUserByUsername((String) any())).thenReturn(user);
        ResponseEntity<?> actualDeleteEventResult = eventService.deleteEvent(eventDTO, new UserPrincipal("principal"));
        assertEquals("Событие удалено", actualDeleteEventResult.getBody());
        assertEquals(HttpStatus.OK, actualDeleteEventResult.getStatusCode());
        assertTrue(actualDeleteEventResult.getHeaders().isEmpty());
        verify(eventRepository, atLeast(1)).findEventByTag((String) any());
        verify(eventRepository).delete((Event) any());
        verify(event).getId();
        verify(event).setDescription((String) any());
        verify(event).setEndDate((LocalDateTime) any());
        verify(event).setId((Long) any());
        verify(event).setLocation((String) any());
        verify(event).setStartDate((LocalDateTime) any());
        verify(event).setTag((String) any());
        verify(event).setTitle((String) any());
        verify(event).setUser_id((Long) any());
        verify(registrationRepository).deleteAllByEvent((Event) any());
        verify(userService).loadUserByUsername((String) any());
        verify(user).getId();
    }

    /**
     * Method under test: {@link EventService#sendingMessage(Event)}
     */
    @Test
    void testSendingMessage() throws MessagingException {
        when(userRepository.findAll()).thenReturn(new ArrayList<>());

        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        eventService.sendingMessage(event);
        verify(userRepository).findAll();
        assertEquals("The characteristics of someone or something", event.getDescription());
        assertEquals(1L, event.getUser_id().longValue());
        assertEquals("Dr", event.getTitle());
        assertEquals("Tag", event.getTag());
        assertEquals("Location", event.getLocation());
        assertEquals("01:01", event.getStartDate().toLocalTime().toString());
        assertEquals(123L, event.getId().longValue());
        assertEquals("0001-01-01", event.getEndDate().toLocalDate().toString());
    }

    /**
     * Method under test: {@link EventService#sendingMessage(Event)}
     */
    @Test
    void testSendingMessage2() throws MessagingException {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);

        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        eventService.sendingMessage(event);
        verify(userRepository).findAll();
        assertEquals("The characteristics of someone or something", event.getDescription());
        assertEquals(1L, event.getUser_id().longValue());
        assertEquals("Dr", event.getTitle());
        assertEquals("Tag", event.getTag());
        assertEquals("Location", event.getLocation());
        assertEquals("01:01", event.getStartDate().toLocalTime().toString());
        assertEquals(123L, event.getId().longValue());
        assertEquals("0001-01-01", event.getEndDate().toLocalDate().toString());
    }

    /**
     * Method under test: {@link EventService#sendingMessage(Event)}
     */
    @Test
    void testSendingMessage3() throws MessagingException {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        User user1 = new User();
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user);
        when(userRepository.findAll()).thenReturn(userList);

        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        eventService.sendingMessage(event);
        verify(userRepository).findAll();
        assertEquals("The characteristics of someone or something", event.getDescription());
        assertEquals(1L, event.getUser_id().longValue());
        assertEquals("Dr", event.getTitle());
        assertEquals("Tag", event.getTag());
        assertEquals("Location", event.getLocation());
        assertEquals("01:01", event.getStartDate().toLocalTime().toString());
        assertEquals(123L, event.getId().longValue());
        assertEquals("0001-01-01", event.getEndDate().toLocalDate().toString());
    }

    /**
     * Method under test: {@link EventService#sendingMessage(Event)}
     */
    @Test
    void testSendingMessage4() throws MessagingException {
        when(userRepository.findAll()).thenThrow(new EventNotFoundException("An error occurred"));

        Event event = new Event();
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        assertThrows(EventNotFoundException.class, () -> eventService.sendingMessage(event));
        verify(userRepository).findAll();
    }

    /**
     * Method under test: {@link EventService#checkEventMembers(String, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckEventMembers() throws UsernameNotFoundException {

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
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        eventService.checkEventMembers("Tag", new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#checkEventMembers(String, Principal)}
     */
    @Test
    void testCheckEventMembers2() throws UsernameNotFoundException {
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
        when(userService.loadUserByUsername((String) any())).thenThrow(new EventNotFoundException("An error occurred"));
        assertThrows(EventNotFoundException.class,
                () -> eventService.checkEventMembers("Tag", new UserPrincipal("principal")));
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#checkEventMembers(String, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckEventMembers3() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
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
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        eventService.checkEventMembers("Tag", new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#checkEventMembers(String, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckEventMembers4() throws UsernameNotFoundException {

        when(eventRepository.findEventByTag((String) any())).thenReturn(Optional.empty());
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
        event.setDescription("The characteristics of someone or something");
        event.setEndDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setId(123L);
        event.setLocation("Location");
        event.setStartDate(LocalDateTime.of(1, 1, 1, 1, 1));
        event.setTag("Tag");
        event.setTitle("Dr");
        event.setUser_id(1L);
        when(userService.loadUserByUsername((String) any())).thenReturn(new User());
        eventService.checkEventMembers("Tag", new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#checkEventMembers(String, Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCheckEventMembers5() throws UsernameNotFoundException {

        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
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
        when(userService.loadUserByUsername((String) any())).thenReturn(null);
        eventService.checkEventMembers("Tag", new UserPrincipal("principal"));
    }

    /**
     * Method under test: {@link EventService#checkEventMembers(String, Principal)}
     */
    @Test
    void testCheckEventMembers6() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
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
        when(registrationRepository.findAllByEvent_Tag((String) any())).thenReturn(new ArrayList<>());
        when(userService.loadUserByUsername((String) any()))
                .thenReturn(new User(123L, "janedoe", "iloveyou", "jane.doe@example.org", true, new HashSet<>()));
        ResponseEntity<?> actualCheckEventMembersResult = eventService.checkEventMembers("Tag",
                new UserPrincipal("principal"));
        assertTrue(actualCheckEventMembersResult.hasBody());
        assertEquals(HttpStatus.OK, actualCheckEventMembersResult.getStatusCode());
        assertTrue(actualCheckEventMembersResult.getHeaders().isEmpty());
        verify(eventRepository).findEventByTag((String) any());
        verify(event).getId();
        verify(event).setDescription((String) any());
        verify(event).setEndDate((LocalDateTime) any());
        verify(event).setId((Long) any());
        verify(event).setLocation((String) any());
        verify(event).setStartDate((LocalDateTime) any());
        verify(event).setTag((String) any());
        verify(event).setTitle((String) any());
        verify(event).setUser_id((Long) any());
        verify(registrationRepository).findAllByEvent_Tag((String) any());
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#checkEventMembers(String, Principal)}
     */
    @Test
    void testCheckEventMembers7() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(1L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
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
        when(registrationRepository.findAllByEvent_Tag((String) any())).thenReturn(new ArrayList<>());
        when(userService.loadUserByUsername((String) any()))
                .thenReturn(new User(123L, "janedoe", "iloveyou", "jane.doe@example.org", true, new HashSet<>()));
        assertThrows(PermissionDeniedException.class,
                () -> eventService.checkEventMembers("Tag", new UserPrincipal("principal")));
        verify(eventRepository).findEventByTag((String) any());
        verify(event).getId();
        verify(event).setDescription((String) any());
        verify(event).setEndDate((LocalDateTime) any());
        verify(event).setId((Long) any());
        verify(event).setLocation((String) any());
        verify(event).setStartDate((LocalDateTime) any());
        verify(event).setTag((String) any());
        verify(event).setTitle((String) any());
        verify(event).setUser_id((Long) any());
        verify(userService).loadUserByUsername((String) any());
    }

    /**
     * Method under test: {@link EventService#checkEventMembers(String, Principal)}
     */
    @Test
    void testCheckEventMembers8() throws UsernameNotFoundException {
        Event event = mock(Event.class);
        when(event.getId()).thenReturn(123L);
        doNothing().when(event).setDescription((String) any());
        doNothing().when(event).setEndDate((LocalDateTime) any());
        doNothing().when(event).setId((Long) any());
        doNothing().when(event).setLocation((String) any());
        doNothing().when(event).setStartDate((LocalDateTime) any());
        doNothing().when(event).setTag((String) any());
        doNothing().when(event).setTitle((String) any());
        doNothing().when(event).setUser_id((Long) any());
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
        when(registrationRepository.findAllByEvent_Tag((String) any())).thenReturn(new ArrayList<>());
        User user = mock(User.class);
        when(user.getId()).thenReturn(123L);
        when(userService.loadUserByUsername((String) any())).thenReturn(user);
        ResponseEntity<?> actualCheckEventMembersResult = eventService.checkEventMembers("Tag",
                new UserPrincipal("principal"));
        assertTrue(actualCheckEventMembersResult.hasBody());
        assertEquals(HttpStatus.OK, actualCheckEventMembersResult.getStatusCode());
        assertTrue(actualCheckEventMembersResult.getHeaders().isEmpty());
        verify(eventRepository).findEventByTag((String) any());
        verify(event).getId();
        verify(event).setDescription((String) any());
        verify(event).setEndDate((LocalDateTime) any());
        verify(event).setId((Long) any());
        verify(event).setLocation((String) any());
        verify(event).setStartDate((LocalDateTime) any());
        verify(event).setTag((String) any());
        verify(event).setTitle((String) any());
        verify(event).setUser_id((Long) any());
        verify(registrationRepository).findAllByEvent_Tag((String) any());
        verify(userService).loadUserByUsername((String) any());
        verify(user).getId();
    }
}

