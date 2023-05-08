package rtuit.lab.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import rtuit.lab.Config.JWT.JWTUtil;
import rtuit.lab.Controllers.ControllerImpl.AuthControllerImpl;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.DTO.ValidateDTO.RegisterRequestDTO;
import rtuit.lab.Logger.Loggable;
import rtuit.lab.Models.ActivationToken;
import rtuit.lab.Models.Role;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.ActivationTokenRepository;
import rtuit.lab.Repositories.UserRepository;

import javax.mail.MessagingException;
import javax.validation.Validator;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UserService implements UserDetailsService, rtuit.lab.Services.UserService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    Validator validator;

    @Autowired
    private ActivationTokenRepository activationTokenRepository;

    /**
     *
     * @param user
     */
    @Loggable
    public void save(User user) {
        userRepo.save(user);
//        try {
//            createActivationCode(user.getEmail());
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
    }

    /**
     *
     * @param email
     * @return
     */
    @Loggable
    public boolean existsByUserEmail(String email) {
        return userRepo.findUserByEmail(email).isPresent();
    }

    /**
     *
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Loggable
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("No user with username = " + username));
    }

    /**
     *
     * @param registerRequestDTO
     */
    @Loggable
    public void registerUser(RegisterRequestDTO registerRequestDTO) {
        save(User.builder()
                .email(registerRequestDTO.getEmail())
                .username(registerRequestDTO.getUsername())
                .password(passwordEncoder.encode(registerRequestDTO.getPassword()))
                .authorities(Set.of(Role.ROLE_ORGANIZER))
                .active(false).build()
        );
    }

    /**
     *
     * @param userDto
     * @return
     */
    @Loggable
    public ResponseEntity<?> loginUser(UserDTO userDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = (User) authentication.getPrincipal();
        user.setEmail(userDto.getEmail());
        String jwt = jwtUtil.generateToken(user.getUsername());

        List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        return ResponseEntity.ok(new AuthControllerImpl.JwtResponse(jwt, user.getId(), user.getEmail(), user.getUsername(), authorities));
    }

    /**
     *
     * @param user
     * @return
     */
    @Loggable
    public ResponseEntity<?> collectUserData(Principal user) {
        User userObj = (User) loadUserByUsername(user.getName());

        return ResponseEntity.ok(
                UserDTO.builder()
                        .username(userObj.getUsername())
                        .email(userObj.getEmail())
                        .authorities(userObj.getAuthorities().toArray())
                        .build()
        );
    }


    /**
     *
     * @param code
     */
    @Loggable
    public void activateUser(String code) {
        User user = activationTokenRepository.findByToken(code).getUser();
        if (user == null) {
            return;
        }
        user.setActive(true);
        activationTokenRepository.deleteByToken(code);
        save(user);
    }

    /**
     *
     * @param email
     * @return
     */
    @Loggable
    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email).get();
    }

    /**
     *
     * @param userEmail
     * @throws MessagingException
     */
    @Loggable
    public void createActivationCode(String userEmail) throws MessagingException {
        User user = findUserByEmail(userEmail);
        String token = UUID.randomUUID().toString();
        ActivationToken myToken = new ActivationToken(token, user, new Date());
        activationTokenRepository.save(myToken);

        if (!ObjectUtils.isEmpty(user.getEmail())) {
            String message = "Привет, " + user.getUsername() + "!" +
                    " для активации аккаунта перейдите <a href='http://localhost:8080/activate/" + token + "'>по ссылке для подтверждения почты</a>"
                    + "а затем продолжите логин <a href='http://localhost:3000/login/'>по ссылке</a>";
            emailService.sendSimpleMessage(user.getEmail(), message);
        }
    }

    /**
     *
     * @param principal
     * @return
     */
    @Loggable
    public User getUserAuth(Principal principal) {
        return (User) loadUserByUsername(principal.getName());
    }


    /**
     *
     * @param userDTO
     * @param authentication
     * @return
     */
    @Loggable
    public ResponseEntity<?> userEdit(UserDTO userDTO, Authentication authentication) {
        User user = getUserAuth(authentication);
        try {
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            save(user);
            authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateToken(user.getUsername());

            List<String> authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            return ResponseEntity.ok(new AuthControllerImpl.JwtResponse(jwt, user.getId(), user.getEmail(), user.getUsername(), authorities));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Пользователь с такой почтой уже существует");
        }
    }

    /**
     *
     * @param pageNumber
     * @return
     */
    @Loggable
    public ResponseEntity<?> getAllUsers(Integer pageNumber){
        Pageable paging = PageRequest.of(pageNumber, 10, Sort.by("id"));
        Page<User> pagedResult = userRepo.findAll(paging);

        if(pagedResult.hasContent()) {
            return ResponseEntity.ok(pagedResult.getContent());
        } else {
            return ResponseEntity.badRequest().body("Что-то пошло не так");
        }
    }

    /**
     *
     * @param id
     * @return
     */
    @Loggable
    public ResponseEntity<?> deleteUser(Long id){
        return ResponseEntity.ok(userRepo.deleteUsersById(id));
    }

}