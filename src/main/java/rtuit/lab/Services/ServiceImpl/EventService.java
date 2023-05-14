package rtuit.lab.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rtuit.lab.DTO.EventDTO;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventAlreadyExistsException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventNotFoundException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.PermissionDeniedException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.UnusualException;
import rtuit.lab.Logger.Loggable;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.Role;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.EventRepository;
import rtuit.lab.Repositories.RegistrationRepository;
import rtuit.lab.Repositories.UserRepository;

import javax.mail.MessagingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService implements rtuit.lab.Services.EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    EmailService emailService;

    /**
     *
     * @return
     */
    @Loggable
    public ResponseEntity<?> getAllEvents(Integer pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, 10, Sort.by("id"));
        Page<Event> pagedResultEvents = eventRepository.findAll(paging);
        if (!pagedResultEvents.hasContent()) {
            throw new EventNotFoundException("событий не найдено");
        } else {
            return ResponseEntity.ok(pagedResultEvents);
        }
    }

    /**
     *
     * @param principal
     * @return
     */
    @Loggable
    public User getUserAuth(Principal principal) {
        return (User) userService.loadUserByUsername(principal.getName());
    }

    /**
     *
     * @param eventDTO
     * @param principal
     * @return
     */
    @Loggable
    public ResponseEntity<?> addEvent(EventDTO eventDTO,Principal principal) {
        if(eventRepository.findEventByTag(eventDTO.getTag()).isPresent())
        {
            throw new EventAlreadyExistsException("Такое событие уже есть в нашей базе данных");
        }

        Event event = Event.builder()
                .title(eventDTO.getTitle())
                .description(eventDTO.getDescription())
                .location(eventDTO.getLocation())
                .tag(eventDTO.getTag())
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(7))
                .user_id(getUserAuth(principal).getId())
                .build();
        try {
            eventRepository.save(event);
            sendingMessage(event);
            return ResponseEntity.ok().body(event);
        }catch (Exception ex) {
            throw new UnusualException("Что-то пошло не так на стороне сервера. Сообщите о проблеме администрации.");
        }
    }

    /**
     *
     * @param eventDTO
     * @param principal
     * @return
     */
    @Transactional
    @Loggable
    public ResponseEntity<?> deleteEvent(EventDTO eventDTO,Principal principal) {
        User userAuth = getUserAuth(principal);
        Long id = eventRepository.findEventByTag(eventDTO.getTag()).orElseThrow().getId();
        if (userAuth.getId().equals(id)){
            Event event = eventRepository.findEventByTag(eventDTO.getTag()).get();
            registrationRepository.deleteAllByEvent(event);
            eventRepository.delete(event);
        }else{
            throw new PermissionDeniedException("У вас недостаточно прав для удаления этого события.");
        }
        return ResponseEntity.ok("Событие удалено");
    }

    /**
     *
     * @param event
     * @throws MessagingException
     */
    @Loggable
    public void sendingMessage(Event event) throws MessagingException {
        List<User> allUsualUsers = userRepository.findAll().stream()
                .filter(user -> user.getAuthorities().contains(Role.ROLE_USER)).toList();
        for(User user : allUsualUsers){
            String message = "Привет, " + user.getUsername() + "!" +
                    " Появилось новое событие - " + event.getTitle();
            emailService.sendSimpleMessage(user.getEmail(), message);
        }
    }

    /**
     *
     * @param tag
     * @param principal
     * @return
     */
    @Loggable
    public ResponseEntity<?> checkEventMembers(String tag, Principal principal,Integer pageNumber){

        User userAuth = getUserAuth(principal);
        Long id = eventRepository.findEventByTag(tag).orElseThrow().getUser_id();
        Pageable paging = PageRequest.of(pageNumber, 10, Sort.by("id"));
        if (userAuth.getId().equals(id)){
            return ResponseEntity.ok(registrationRepository.findAllByEvent_Tag(tag,paging));
        }else{
            throw new PermissionDeniedException("У вас недостаточно прав чтобы посмотреть список участников данного события");
        }
    }
}
