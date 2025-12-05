package com.smartspend;
import com.smartspend.controller.AuthController;
import com.smartspend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
@Mock
private UserRepository userRepository;
@InjectMocks
private AuthController controller;
@Test
void showLoginPageReturnsLoginView(){
String view=controller.showLoginPage();
assertEquals("login",view);
}
}
