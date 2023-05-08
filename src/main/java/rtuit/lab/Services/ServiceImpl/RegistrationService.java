package rtuit.lab.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.EventNotFoundException;
import rtuit.lab.Exceptions.ModelsExceptions.EventServiceException.UnusualException;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.Registration;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.EventRepository;
import rtuit.lab.Repositories.RegistrationRepository;
import rtuit.lab.Repositories.UserRepository;

import javax.mail.MessagingException;
import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class RegistrationService implements rtuit.lab.Services.RegistrationService {


    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    /**
     *
     * @param tag
     * @param principal
     * @return
     */
    public ResponseEntity<?> registerUserOnEvent(String tag, Principal principal){
        eventRepository.findEventByTag(tag).orElseThrow(() -> new EventNotFoundException("Такого события нет."));

        try {
            User user = userRepository.findByUsername(principal.getName()).get();
            Event event = eventRepository.findEventByTag(tag).get();
//            sendMessageAboutRegisterOnEvent(user,event);
           return ResponseEntity.ok(registrationRepository.save(Registration.builder()
                    .user(user)
                    .event(event)
                    .registrationDate(LocalDateTime.now())
                    .build()));

        }catch (Exception e){
            throw new UnusualException("Что-то пошло не так на стороне сервера. Обратитесь в поддержку.");
        }
    }

    /**
     *
     * @param user
     * @param event
     * @throws MessagingException
     */

    public void sendMessageAboutRegisterOnEvent(User user, Event event) throws MessagingException {
        String message = "Привет, " + user.getUsername() + "!" +
                " Ты зарегистрировался на событие - " + event.getTitle();
        emailService.sendSimpleMessage(user.getEmail(), message);
    }
}
