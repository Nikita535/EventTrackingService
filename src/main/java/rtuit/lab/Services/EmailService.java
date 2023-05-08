package rtuit.lab.Services;

import javax.mail.MessagingException;

public interface EmailService {
    void sendSimpleMessage(String To, String text) throws MessagingException;
}
