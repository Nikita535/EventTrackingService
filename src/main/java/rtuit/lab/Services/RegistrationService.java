package rtuit.lab.Services;

import org.springframework.http.ResponseEntity;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.User;

import javax.mail.MessagingException;
import java.security.Principal;

public interface RegistrationService {
    ResponseEntity<?> registerUserOnEvent(String tag, Principal principal);
    void sendMessageAboutRegisterOnEvent(User user, Event event) throws MessagingException;
}
