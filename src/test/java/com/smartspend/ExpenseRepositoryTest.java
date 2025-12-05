package com.smartspend;
import com.smartspend.model.Expense;
import com.smartspend.model.User;
import com.smartspend.repository.ExpenseRepository;
import com.smartspend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
@DataJpaTest
public class ExpenseRepositoryTest {
@Autowired
private ExpenseRepository expenseRepository;
@Autowired
private UserRepository userRepository;
@Test
void findByUserAndCategoryWorks(){
User user=new User();
user.setName("Test User");
user.setEmail("user@example.com");
user.setPassword("pass");
user.setRole("USER");
user=userRepository.save(user);
Expense e1=new Expense();
e1.setUser(user);
e1.setCategory("Food");
e1.setAmount(100.0);
e1.setDate(LocalDate.now());
expenseRepository.save(e1);
Expense e2=new Expense();
e2.setUser(user);
e2.setCategory("Travel");
e2.setAmount(200.0);
e2.setDate(LocalDate.now());
expenseRepository.save(e2);
List<Expense> result=expenseRepository.findByUserAndCategory(user,"Food");
assertFalse(result.isEmpty());
assertEquals("Food",result.get(0).getCategory());
}
}
