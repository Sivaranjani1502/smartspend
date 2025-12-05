package com.smartspend;
import com.smartspend.model.PasswordResetToken;
import com.smartspend.model.User;
import com.smartspend.repository.PasswordResetTokenRepository;
import com.smartspend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@DataJpaTest
public class PasswordResetTokenRepositoryTest {
@Autowired
private PasswordResetTokenRepository tokenRepository;
@Autowired
private UserRepository userRepository;
@Test
void findByTokenReturnsToken(){
User user=new User();
user.setName("Token User");
user.setEmail("reset@example.com");
user.setPassword("pass");
user.setRole("USER");
user=userRepository.save(user);
PasswordResetToken token=new PasswordResetToken();
token.setToken("abc123");
token.setUser(user);
token.setExpiry(LocalDateTime.now().plusHours(1));
token=tokenRepository.save(token);
PasswordResetToken found=tokenRepository.findByToken("abc123");
assertNotNull(found);
assertEquals("abc123",found.getToken());
assertEquals(user.getId(),found.getUser().getId());
}
}
