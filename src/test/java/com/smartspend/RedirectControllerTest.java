package com.smartspend;
import com.smartspend.controller.RedirectController;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class RedirectControllerTest {
@Test
void adminRedirectsToAdminDashboard(){
RedirectController controller=new RedirectController();
Authentication auth=new TestingAuthenticationToken("admin","pass","ROLE_ADMIN");
String view=controller.redirectAfterLogin(auth);
assertEquals("redirect:/admin/dashboard",view);
}
@Test
void userRedirectsToDashboard(){
RedirectController controller=new RedirectController();
Authentication auth=new TestingAuthenticationToken("user","pass","ROLE_USER");
String view=controller.redirectAfterLogin(auth);
assertEquals("redirect:/dashboard",view);
}
}
