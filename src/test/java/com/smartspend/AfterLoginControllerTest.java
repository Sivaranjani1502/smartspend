package com.smartspend;
import com.smartspend.controller.AfterLoginController;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class AfterLoginControllerTest {
@Test
void adminIsRedirectedToAdminDashboard(){
AfterLoginController controller=new AfterLoginController();
Authentication auth=new TestingAuthenticationToken("admin","pass","ADMIN");
String view=controller.afterLogin(auth);
assertEquals("redirect:/admin/dashboard",view);
}
@Test
void normalUserIsRedirectedToDashboard(){
AfterLoginController controller=new AfterLoginController();
Authentication auth=new TestingAuthenticationToken("user","pass","ROLE_USER");
String view=controller.afterLogin(auth);
assertEquals("redirect:/dashboard",view);
}
}
