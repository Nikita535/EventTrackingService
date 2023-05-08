package rtuit.lab.Services;

import org.springframework.http.ResponseEntity;
import rtuit.lab.DTO.EventDTO;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.User;

import javax.mail.MessagingException;
import java.security.Principal;

public interface EventService {
    ResponseEntity<?> getAllEvents();
    User getUserAuth(Principal principal);
    ResponseEntity<?> addEvent(EventDTO eventDTO, Principal principal);
    ResponseEntity<?> deleteEvent(EventDTO eventDTO,Principal principal);
    void sendingMessage(Event event) throws MessagingException;
    ResponseEntity<?> checkEventMembers(String tag, Principal principal);

}
