package rtuit.lab.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rtuit.lab.Models.Role;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.UserRepository;

import java.util.Collections;
import java.util.HashSet;

@Service
public class AdminInitService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;


    public AdminInitService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                            UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        String password = passwordEncoder.encode("ADMIN");
        if (userRepository.findByUsername("ADMIN").isEmpty()) {
            User admin = new User("admin@bk.ru", "ADMIN" , password,
                    new HashSet<Role>(Collections.singleton(Role.ROLE_ADMIN)), true);
            userRepository.save(admin);
        }
        if (userRepository.findByUsername("ORGANIZER").isEmpty()) {
            User admin = new User("organizer@bk.ru", "ORGANIZER" , password,
                    new HashSet<Role>(Collections.singleton(Role.ROLE_ORGANIZER)), true);
            userRepository.save(admin);
        }
        if (userRepository.findByUsername("USER").isEmpty()) {
            User admin = new User("user@bk.ru", "USER" , password,
                    new HashSet<Role>(Collections.singleton(Role.ROLE_USER)), true);
            userRepository.save(admin);
        }

    }
}