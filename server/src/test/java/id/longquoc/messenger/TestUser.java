package id.longquoc.messenger;

import com.github.javafaker.Faker;
import id.longquoc.messenger.enums.Role;
import id.longquoc.messenger.enums.UserState;
import id.longquoc.messenger.model.User;
import id.longquoc.messenger.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration("/test-context.xml")
@RunWith(SpringRunner.class)
@DataJpaTest
public class TestUser {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private Faker faker;
    @Mock
    private TestEntityManager entityManager;
    @Test
    public void testSaveUser(){
        User user = User.builder()
                .fullName(faker.name().fullName())
                .username(faker.name().username())
                .email(faker.name().username() + "@gmail.com")
                .password("123456")
                .roles(List.of(Role.ROLE_BASIC))
                .userState(UserState.OFFLINE)
                .phone(faker.phoneNumber().cellPhone())
                .build();
        User savedUser = userRepository.save(user);

        Assertions.assertNotNull(savedUser);
    }

}
