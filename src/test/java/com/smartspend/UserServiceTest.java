package com.smartspend;
import com.smartspend.model.User;
import com.smartspend.repository.UserRepository;
import com.smartspend.security.CustomUserDetails;
import com.smartspend.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
@Mock
private UserRepository userRepository;
@Mock
private PasswordEncoder passwordEncoder;
@InjectMocks
private UserService userService;
@Test
void getLoggedInUserReturnsUser(){
User user=new User();
user.setEmail("user@example.com");
CustomUserDetails details=new CustomUserDetails(user);
Authentication auth=mock(Authentication.class);
when(auth.getPrincipal()).thenReturn(details);
when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
User result=userService.getLoggedInUser(auth);
assertEquals("user@example.com",result.getEmail());
}
@Test
void getLoggedInUserWithoutAuthThrows(){
assertThrows(IllegalStateException.class,()->userService.getLoggedInUser(null));
}
}
