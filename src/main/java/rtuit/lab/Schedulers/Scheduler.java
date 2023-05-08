package rtuit.lab.Schedulers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.Registration;
import rtuit.lab.Models.Role;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.EventRepository;
import rtuit.lab.Repositories.RegistrationRepository;
import rtuit.lab.Repositories.UserRepository;
import rtuit.lab.Services.EmailService;

import javax.mail.MessagingException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


@Component
public class Scheduler {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    EmailService emailService;

    @Scheduled(fixedDelay = 172800000)
    public void sendMessageAboutNearEvent() throws MessagingException {
        List<User> allUsualUsers = userRepository.findAll().stream()
                .filter(user -> user.getAuthorities().contains(Role.ROLE_USER)).toList();
        for(User user : allUsualUsers){
            List<Event> userEvents = registrationRepository.findAllByUser(user).stream().map(Registration::getEvent).toList();
            for (Event userEvent : userEvents) {
                String message = "Привет, " + user.getUsername() + "!" +
                        " Это напоминание о том, что ты зарегистрирован на событие - " + userEvent.getTitle();
                emailService.sendSimpleMessage(user.getEmail(), message);
            }

        }
    }

    @Scheduled(fixedDelay = 60000)
    public void deleteExpiredEvents(){
        List<User> allUsualUsers = userRepository.findAll().stream()
                .filter(user -> user.getAuthorities().contains(Role.ROLE_USER)).toList();
        for(User user : allUsualUsers){
            List<Event> userEvents = registrationRepository.findAllByUser(user).stream().map(Registration::getEvent).toList();
            for (Event userEvent : userEvents) {
                if(Duration.between(LocalDateTime.now(),userEvent.getEndDate()).getSeconds() == 0){
                    registrationRepository.deleteAllByEvent(userEvent);
                    eventRepository.delete(userEvent);
                }
            }

        }
    }

}
