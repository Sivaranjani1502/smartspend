package com.smartspend;
import com.smartspend.model.Budget;
import com.smartspend.model.User;
import com.smartspend.repository.BudgetRepository;
import com.smartspend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
@DataJpaTest
public class BudgetRepositoryTest {
@Autowired
private BudgetRepository budgetRepository;
@Autowired
private UserRepository userRepository;
@Test
void findByUserReturnsBudgets(){
User user=new User();
user.setName("Test User");
user.setEmail("test@example.com");
user.setPassword("pass");
user.setRole("USER");
user=userRepository.save(user);
Budget budget=new Budget();
budget.setCategory("Food");
budget.setLimitAmount(1000.0);
budget.setUser(user);
budgetRepository.save(budget);
List<Budget> result=budgetRepository.findByUser(user);
assertFalse(result.isEmpty());
assertEquals("Food",result.get(0).getCategory());
}
}
