package com.smartspend;
import com.smartspend.controller.DashboardController;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import static org.junit.jupiter.api.Assertions.assertEquals;
public class DashboardControllerTest {
@Test
void adminRedirectsToAdminDashboard(){
DashboardController controller=new DashboardController();
Authentication auth=new TestingAuthenticationToken("admin","pass","ROLE_ADMIN");
String view=controller.dashboard(auth);
assertEquals("redirect:/admin/dashboard",view);
}
@Test
void userSeesDashboardView(){
DashboardController controller=new DashboardController();
Authentication auth=new TestingAuthenticationToken("user","pass","ROLE_USER");
String view=controller.dashboard(auth);
assertEquals("dashboard",view);
}
}
