package com.smartspend;
import com.smartspend.service.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
@Mock
private JavaMailSender mailSender;
@InjectMocks
private EmailService emailService;
@Test
void sendEmailShouldUseMailSender(){
emailService.sendEmail("test@example.com","Subject","Body");
verify(mailSender).send(any(SimpleMailMessage.class));
}
}
