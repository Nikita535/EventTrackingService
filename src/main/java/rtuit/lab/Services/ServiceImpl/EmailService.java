package rtuit.lab.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rtuit.lab.Logger.Loggable;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService implements rtuit.lab.Services.EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /**
     *
     * @param To
     * @param text
     * @throws MessagingException
     */
    @Async
    @Loggable
    public void sendSimpleMessage(String To, String text) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(To);
        helper.setText(text, true);
        helper.setSubject("Сообщение от команды разработчиков");
        mailSender.send(mimeMessage);

    }
}
