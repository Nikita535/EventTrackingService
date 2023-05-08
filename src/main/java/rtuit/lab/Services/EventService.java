package rtuit.lab.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rtuit.lab.DTO.EventDTO;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventAlreadyExistsException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventNotFoundException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.PermissionDeniedException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.UnusualException;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.EventRepository;

import java.security.Principal;
import java.security.PrivilegedActionException;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserService userService;
    public ResponseEntity<?> getAllEvents() {
        List<Event> allEvents = eventRepository.findAll();
        if (allEvents.isEmpty()) {
            throw new EventNotFoundException("событий не найдено");
        } else {
            return ResponseEntity.ok(allEvents);
        }
    }

    public User getUserAuth(Principal principal) {
        return (User) userService.loadUserByUsername(principal.getName());
    }

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
            return ResponseEntity.ok().body(event);
        }catch (Exception ex) {
            throw new UnusualException("Что-то пошло не так на стороне сервера. Сообщите о проблеме администрации.");
        }
    }

    public ResponseEntity<?> deleteEvent(EventDTO eventDTO,Principal principal) {
        User userAuth = getUserAuth(principal);
        Long id = eventRepository.findEventByTag(eventDTO.getTag()).orElseThrow().getId();
        if (userAuth.getId().equals(id)){
            eventRepository.delete(eventRepository.findEventByTag(eventDTO.getTag()).get());
        }else{
            throw new PermissionDeniedException("У вас недостаточно прав для удаления этого события.");
        }
        return ResponseEntity.ok("Событие удалено");
    }

}
