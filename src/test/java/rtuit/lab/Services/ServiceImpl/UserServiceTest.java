package rtuit.lab.Services.ServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sun.security.auth.UserPrincipal;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.mail.MessagingException;
import javax.validation.Validator;

import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rtuit.lab.Config.JWT.JWTUtil;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.Models.ActivationToken;
import rtuit.lab.Models.Role;
import rtuit.lab.Models.User;
import rtuit.lab.Repositories.ActivationTokenRepository;
import rtuit.lab.Repositories.UserRepository;

@ContextConfiguration(classes = {UserService.class})
@ExtendWith(SpringExtension.class)
@PropertySource("classpath:application-test.properties")
@EnableConfigurationProperties
class UserServiceTest {
    @MockBean
    private ActivationTokenRepository activationTokenRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private EmailService emailService;

    @MockBean
    private JWTUtil jWTUtil;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private Validator validator;

    /**
     * Method under test: {@link UserService#existsByUserEmail(String)}
     */
    @Test
    void testExistsByUserEmail() {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByEmail((String) any())).thenReturn(ofResult);
        assertTrue(userService.existsByUserEmail("jane.doe@example.org"));
        verify(userRepository).findUserByEmail((String) any());
    }

    /**
     * Method under test: {@link UserService#existsByUserEmail(String)}
     */
    @Test
    void testExistsByUserEmail2() {
        when(userRepository.findUserByEmail((String) any())).thenReturn(Optional.empty());
        assertFalse(userService.existsByUserEmail("jane.doe@example.org"));
        verify(userRepository).findUserByEmail((String) any());
    }

    /**
     * Method under test: {@link UserService#existsByUserEmail(String)}
     */
    @Test
    void testExistsByUserEmail3() {
        when(userRepository.findUserByEmail((String) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.existsByUserEmail("jane.doe@example.org"));
        verify(userRepository).findUserByEmail((String) any());
    }

    /**
     * Method under test: {@link UserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult);
        assertSame(user, userService.loadUserByUsername("janedoe"));
        verify(userRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername2() throws UsernameNotFoundException {
        when(userRepository.findByUsername((String) any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("janedoe"));
        verify(userRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#loadUserByUsername(String)}
     */
    @Test
    void testLoadUserByUsername3() throws UsernameNotFoundException {
        when(userRepository.findByUsername((String) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.loadUserByUsername("janedoe"));
        verify(userRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#collectUserData(Principal)}
     */
    @Test
    void testCollectUserData() {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult);
        ResponseEntity<?> actualCollectUserDataResult = userService.collectUserData(new UserPrincipal("user"));
        assertTrue(actualCollectUserDataResult.hasBody());
        assertTrue(actualCollectUserDataResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualCollectUserDataResult.getStatusCode());
        assertEquals("jane.doe@example.org", ((UserDTO) actualCollectUserDataResult.getBody()).getEmail());
        assertTrue(((UserDTO) actualCollectUserDataResult.getBody()).getAuthorities() instanceof Object[]);
        assertNull(((UserDTO) actualCollectUserDataResult.getBody()).getPassword());
        assertEquals("janedoe", ((UserDTO) actualCollectUserDataResult.getBody()).getUsername());
        verify(userRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#collectUserData(Principal)}
     */
    @Test
    void testCollectUserData2() {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("jane.doe@example.org");
        when(user.getUsername()).thenReturn("janedoe");
        when((Collection<GrantedAuthority>) user.getAuthorities()).thenReturn(new ArrayList<>());
        doNothing().when(user).setActive((Boolean) any());
        doNothing().when(user).setAuthorities((Set<Role>) any());
        doNothing().when(user).setEmail((String) any());
        doNothing().when(user).setId((Long) any());
        doNothing().when(user).setPassword((String) any());
        doNothing().when(user).setUsername((String) any());
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult);
        ResponseEntity<?> actualCollectUserDataResult = userService.collectUserData(new UserPrincipal("user"));
        assertTrue(actualCollectUserDataResult.hasBody());
        assertTrue(actualCollectUserDataResult.getHeaders().isEmpty());
        assertEquals(HttpStatus.OK, actualCollectUserDataResult.getStatusCode());
        assertEquals("jane.doe@example.org", ((UserDTO) actualCollectUserDataResult.getBody()).getEmail());
        assertTrue(((UserDTO) actualCollectUserDataResult.getBody()).getAuthorities() instanceof Object[]);
        assertNull(((UserDTO) actualCollectUserDataResult.getBody()).getPassword());
        assertEquals("janedoe", ((UserDTO) actualCollectUserDataResult.getBody()).getUsername());
        verify(userRepository).findByUsername((String) any());
        verify(user).getEmail();
        verify(user).getUsername();
        verify(user).getAuthorities();
        verify(user).setActive((Boolean) any());
        verify(user).setAuthorities((Set<Role>) any());
        verify(user).setEmail((String) any());
        verify(user).setId((Long) any());
        verify(user).setPassword((String) any());
        verify(user).setUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#collectUserData(Principal)}
     */
    @Test
    void testCollectUserData3() {
        when(userRepository.findByUsername((String) any())).thenReturn(Optional.empty());
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("jane.doe@example.org");
        when(user.getUsername()).thenReturn("janedoe");
        when((Collection<GrantedAuthority>) user.getAuthorities()).thenReturn(new ArrayList<>());
        doNothing().when(user).setActive((Boolean) any());
        doNothing().when(user).setAuthorities((Set<Role>) any());
        doNothing().when(user).setEmail((String) any());
        doNothing().when(user).setId((Long) any());
        doNothing().when(user).setPassword((String) any());
        doNothing().when(user).setUsername((String) any());
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        assertThrows(UsernameNotFoundException.class, () -> userService.collectUserData(new UserPrincipal("user")));
        verify(userRepository).findByUsername((String) any());
        verify(user).setActive((Boolean) any());
        verify(user).setAuthorities((Set<Role>) any());
        verify(user).setEmail((String) any());
        verify(user).setId((Long) any());
        verify(user).setPassword((String) any());
        verify(user).setUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#collectUserData(Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCollectUserData4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "java.security.Principal.getName()" because "user" is null
        //       at rtuit.lab.Services.ServiceImpl.UserService.collectUserData(UserService.java:139)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = mock(User.class);
        when(user.getEmail()).thenReturn("jane.doe@example.org");
        when(user.getUsername()).thenReturn("janedoe");
        when((Collection<GrantedAuthority>) user.getAuthorities()).thenReturn(new ArrayList<>());
        doNothing().when(user).setActive((Boolean) any());
        doNothing().when(user).setAuthorities((Set<Role>) any());
        doNothing().when(user).setEmail((String) any());
        doNothing().when(user).setId((Long) any());
        doNothing().when(user).setPassword((String) any());
        doNothing().when(user).setUsername((String) any());
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult);
        userService.collectUserData(null);
    }

    /**
     * Method under test: {@link UserService#activateUser(String)}
     */
    @Test
    void testActivateUser() {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ActivationToken activationToken = new ActivationToken();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        doNothing().when(activationTokenRepository).deleteByToken((String) any());
        when(activationTokenRepository.findByToken((String) any())).thenReturn(activationToken);

        User user1 = new User();
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        when(userRepository.save((User) any())).thenReturn(user1);
        userService.activateUser("Code");
        verify(activationTokenRepository).findByToken((String) any());
        verify(activationTokenRepository).deleteByToken((String) any());
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserService#activateUser(String)}
     */
    @Test
    void testActivateUser2() {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ActivationToken activationToken = new ActivationToken();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        doNothing().when(activationTokenRepository).deleteByToken((String) any());
        when(activationTokenRepository.findByToken((String) any())).thenReturn(activationToken);
        when(userRepository.save((User) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.activateUser("Code"));
        verify(activationTokenRepository).findByToken((String) any());
        verify(activationTokenRepository).deleteByToken((String) any());
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserService#activateUser(String)}
     */
    @Test
    void testActivateUser3() {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        User user1 = mock(User.class);
        doNothing().when(user1).setActive((Boolean) any());
        doNothing().when(user1).setAuthorities((Set<Role>) any());
        doNothing().when(user1).setEmail((String) any());
        doNothing().when(user1).setId((Long) any());
        doNothing().when(user1).setPassword((String) any());
        doNothing().when(user1).setUsername((String) any());
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        ActivationToken activationToken = mock(ActivationToken.class);
        when(activationToken.getUser()).thenReturn(user1);
        doNothing().when(activationToken).setExpiryDate((Date) any());
        doNothing().when(activationToken).setId((Long) any());
        doNothing().when(activationToken).setToken((String) any());
        doNothing().when(activationToken).setUser((User) any());
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        doNothing().when(activationTokenRepository).deleteByToken((String) any());
        when(activationTokenRepository.findByToken((String) any())).thenReturn(activationToken);

        User user2 = new User();
        user2.setActive(true);
        user2.setAuthorities(new HashSet<>());
        user2.setEmail("jane.doe@example.org");
        user2.setId(123L);
        user2.setPassword("iloveyou");
        user2.setUsername("janedoe");
        when(userRepository.save((User) any())).thenReturn(user2);
        userService.activateUser("Code");
        verify(activationTokenRepository).findByToken((String) any());
        verify(activationTokenRepository).deleteByToken((String) any());
        verify(activationToken).getUser();
        verify(activationToken).setExpiryDate((Date) any());
        verify(activationToken).setId((Long) any());
        verify(activationToken).setToken((String) any());
        verify(activationToken).setUser((User) any());
        verify(user1, atLeast(1)).setActive((Boolean) any());
        verify(user1).setAuthorities((Set<Role>) any());
        verify(user1).setEmail((String) any());
        verify(user1).setId((Long) any());
        verify(user1).setPassword((String) any());
        verify(user1).setUsername((String) any());
        verify(userRepository).save((User) any());
    }

    /**
     * Method under test: {@link UserService#findUserByEmail(String)}
     */
    @Test
    void testFindUserByEmail() {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserByEmail((String) any())).thenReturn(ofResult);
        assertSame(user, userService.findUserByEmail("jane.doe@example.org"));
        verify(userRepository).findUserByEmail((String) any());
    }

    /**
     * Method under test: {@link UserService#findUserByEmail(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testFindUserByEmail2() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.NoSuchElementException: No value present
        //       at java.util.Optional.get(Optional.java:143)
        //       at rtuit.lab.Services.ServiceImpl.UserService.findUserByEmail(UserService.java:173)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.findUserByEmail((String) any())).thenReturn(Optional.empty());
        userService.findUserByEmail("jane.doe@example.org");
    }

    /**
     * Method under test: {@link UserService#findUserByEmail(String)}
     */
    @Test
    void testFindUserByEmail3() {
        when(userRepository.findUserByEmail((String) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.findUserByEmail("jane.doe@example.org"));
        verify(userRepository).findUserByEmail((String) any());
    }

    /**
     * Method under test: {@link UserService#createActivationCode(String)}
     */
    @Test
    void testCreateActivationCode() throws MessagingException {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ActivationToken activationToken = new ActivationToken();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        when(activationTokenRepository.save((ActivationToken) any())).thenReturn(activationToken);
        doNothing().when(emailService).sendSimpleMessage((String) any(), (String) any());

        User user1 = new User();
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user1);
        when(userRepository.findUserByEmail((String) any())).thenReturn(ofResult);
        userService.createActivationCode("jane.doe@example.org");
        verify(activationTokenRepository).save((ActivationToken) any());
        verify(emailService).sendSimpleMessage((String) any(), (String) any());
        verify(userRepository).findUserByEmail((String) any());
    }

    /**
     * Method under test: {@link UserService#createActivationCode(String)}
     */
    @Test
    void testCreateActivationCode2() throws MessagingException {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ActivationToken activationToken = new ActivationToken();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        when(activationTokenRepository.save((ActivationToken) any())).thenReturn(activationToken);
        doThrow(new RuntimeException()).when(emailService).sendSimpleMessage((String) any(), (String) any());

        User user1 = new User();
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user1);
        when(userRepository.findUserByEmail((String) any())).thenReturn(ofResult);
        assertThrows(RuntimeException.class, () -> userService.createActivationCode("jane.doe@example.org"));
        verify(activationTokenRepository).save((ActivationToken) any());
        verify(emailService).sendSimpleMessage((String) any(), (String) any());
        verify(userRepository).findUserByEmail((String) any());
    }

    /**
     * Method under test: {@link UserService#createActivationCode(String)}
     */
    @Test
    void testCreateActivationCode3() throws MessagingException {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ActivationToken activationToken = new ActivationToken();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        when(activationTokenRepository.save((ActivationToken) any())).thenReturn(activationToken);
        doNothing().when(emailService).sendSimpleMessage((String) any(), (String) any());
        User user1 = mock(User.class);
        when(user1.getUsername()).thenReturn("janedoe");
        when(user1.getEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(user1).setActive((Boolean) any());
        doNothing().when(user1).setAuthorities((Set<Role>) any());
        doNothing().when(user1).setEmail((String) any());
        doNothing().when(user1).setId((Long) any());
        doNothing().when(user1).setPassword((String) any());
        doNothing().when(user1).setUsername((String) any());
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user1);
        when(userRepository.findUserByEmail((String) any())).thenReturn(ofResult);
        userService.createActivationCode("jane.doe@example.org");
        verify(activationTokenRepository).save((ActivationToken) any());
        verify(emailService).sendSimpleMessage((String) any(), (String) any());
        verify(userRepository).findUserByEmail((String) any());
        verify(user1, atLeast(1)).getEmail();
        verify(user1).getUsername();
        verify(user1).setActive((Boolean) any());
        verify(user1).setAuthorities((Set<Role>) any());
        verify(user1).setEmail((String) any());
        verify(user1).setId((Long) any());
        verify(user1).setPassword((String) any());
        verify(user1).setUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#createActivationCode(String)}
     */
    @Test
    void testCreateActivationCode4() throws MessagingException {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ActivationToken activationToken = new ActivationToken();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        when(activationTokenRepository.save((ActivationToken) any())).thenReturn(activationToken);
        doNothing().when(emailService).sendSimpleMessage((String) any(), (String) any());
        User user1 = mock(User.class);
        when(user1.getUsername()).thenThrow(new RuntimeException());
        when(user1.getEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(user1).setActive((Boolean) any());
        doNothing().when(user1).setAuthorities((Set<Role>) any());
        doNothing().when(user1).setEmail((String) any());
        doNothing().when(user1).setId((Long) any());
        doNothing().when(user1).setPassword((String) any());
        doNothing().when(user1).setUsername((String) any());
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user1);
        when(userRepository.findUserByEmail((String) any())).thenReturn(ofResult);
        assertThrows(RuntimeException.class, () -> userService.createActivationCode("jane.doe@example.org"));
        verify(activationTokenRepository).save((ActivationToken) any());
        verify(userRepository).findUserByEmail((String) any());
        verify(user1).getEmail();
        verify(user1).getUsername();
        verify(user1).setActive((Boolean) any());
        verify(user1).setAuthorities((Set<Role>) any());
        verify(user1).setEmail((String) any());
        verify(user1).setId((Long) any());
        verify(user1).setPassword((String) any());
        verify(user1).setUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#createActivationCode(String)}
     */
    @Test
    void testCreateActivationCode5() throws MessagingException {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ActivationToken activationToken = new ActivationToken();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        when(activationTokenRepository.save((ActivationToken) any())).thenReturn(activationToken);
        doNothing().when(emailService).sendSimpleMessage((String) any(), (String) any());
        User user1 = mock(User.class);
        when(user1.getUsername()).thenReturn("janedoe");
        when(user1.getEmail()).thenReturn("");
        doNothing().when(user1).setActive((Boolean) any());
        doNothing().when(user1).setAuthorities((Set<Role>) any());
        doNothing().when(user1).setEmail((String) any());
        doNothing().when(user1).setId((Long) any());
        doNothing().when(user1).setPassword((String) any());
        doNothing().when(user1).setUsername((String) any());
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user1);
        when(userRepository.findUserByEmail((String) any())).thenReturn(ofResult);
        userService.createActivationCode("jane.doe@example.org");
        verify(activationTokenRepository).save((ActivationToken) any());
        verify(userRepository).findUserByEmail((String) any());
        verify(user1).getEmail();
        verify(user1).setActive((Boolean) any());
        verify(user1).setAuthorities((Set<Role>) any());
        verify(user1).setEmail((String) any());
        verify(user1).setId((Long) any());
        verify(user1).setPassword((String) any());
        verify(user1).setUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#createActivationCode(String)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testCreateActivationCode6() throws MessagingException {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.util.NoSuchElementException: No value present
        //       at java.util.Optional.get(Optional.java:143)
        //       at rtuit.lab.Services.ServiceImpl.UserService.findUserByEmail(UserService.java:173)
        //       at rtuit.lab.Services.ServiceImpl.UserService.createActivationCode(UserService.java:183)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ActivationToken activationToken = new ActivationToken();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        activationToken.setExpiryDate(Date.from(atStartOfDayResult.atZone(ZoneId.of("UTC")).toInstant()));
        activationToken.setId(123L);
        activationToken.setToken("ABC123");
        activationToken.setUser(user);
        when(activationTokenRepository.save((ActivationToken) any())).thenReturn(activationToken);
        doNothing().when(emailService).sendSimpleMessage((String) any(), (String) any());
        when(userRepository.findUserByEmail((String) any())).thenReturn(Optional.empty());
        User user1 = mock(User.class);
        when(user1.getUsername()).thenReturn("janedoe");
        when(user1.getEmail()).thenReturn("jane.doe@example.org");
        doNothing().when(user1).setActive((Boolean) any());
        doNothing().when(user1).setAuthorities((Set<Role>) any());
        doNothing().when(user1).setEmail((String) any());
        doNothing().when(user1).setId((Long) any());
        doNothing().when(user1).setPassword((String) any());
        doNothing().when(user1).setUsername((String) any());
        user1.setActive(true);
        user1.setAuthorities(new HashSet<>());
        user1.setEmail("jane.doe@example.org");
        user1.setId(123L);
        user1.setPassword("iloveyou");
        user1.setUsername("janedoe");
        userService.createActivationCode("jane.doe@example.org");
    }

    /**
     * Method under test: {@link UserService#getUserAuth(Principal)}
     */
    @Test
    void testGetUserAuth() {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult);
        assertSame(user, userService.getUserAuth(new UserPrincipal("principal")));
        verify(userRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#getUserAuth(Principal)}
     */
    @Test
    void testGetUserAuth2() {
        when(userRepository.findByUsername((String) any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.getUserAuth(new UserPrincipal("principal")));
        verify(userRepository).findByUsername((String) any());
    }

    /**
     * Method under test: {@link UserService#getUserAuth(Principal)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetUserAuth3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "java.security.Principal.getName()" because "principal" is null
        //       at rtuit.lab.Services.ServiceImpl.UserService.getUserAuth(UserService.java:203)
        //   See https://diff.blue/R013 to resolve this issue.

        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findByUsername((String) any())).thenReturn(ofResult);
        userService.getUserAuth(null);
    }

    /**
     * Method under test: {@link UserService#getAllUsers(Integer)}
     */
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        ResponseEntity<?> actualAllUsers = userService.getAllUsers(10);
        assertEquals("Что-то пошло не так", actualAllUsers.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, actualAllUsers.getStatusCode());
        assertTrue(actualAllUsers.getHeaders().isEmpty());
        verify(userRepository).findAll((Pageable) any());
    }

    /**
     * Method under test: {@link UserService#getAllUsers(Integer)}
     */
    @Test
    void testGetAllUsers2() {
        User user = new User();
        user.setActive(true);
        user.setAuthorities(new HashSet<>());
        user.setEmail("jane.doe@example.org");
        user.setId(123L);
        user.setPassword("iloveyou");
        user.setUsername("janedoe");

        ArrayList<User> userList = new ArrayList<>();
        userList.add(user);
        PageImpl<User> pageImpl = new PageImpl<>(userList);
        when(userRepository.findAll((Pageable) any())).thenReturn(pageImpl);
        ResponseEntity<?> actualAllUsers = userService.getAllUsers(10);
        assertTrue(actualAllUsers.hasBody());
        assertEquals(HttpStatus.OK, actualAllUsers.getStatusCode());
        assertTrue(actualAllUsers.getHeaders().isEmpty());
        verify(userRepository).findAll((Pageable) any());
    }

    /**
     * Method under test: {@link UserService#getAllUsers(Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAllUsers3() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.NullPointerException: Cannot invoke "org.springframework.data.domain.Page.hasContent()" because "pagedResult" is null
        //       at rtuit.lab.Services.ServiceImpl.UserService.getAllUsers(UserService.java:242)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.findAll((Pageable) any())).thenReturn(null);
        userService.getAllUsers(10);
    }

    /**
     * Method under test: {@link UserService#getAllUsers(Integer)}
     */
    @Test
    @Disabled("TODO: Complete this test")
    void testGetAllUsers4() {
        // TODO: Complete this test.
        //   Reason: R013 No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: Page index must not be less than zero
        //       at rtuit.lab.Services.ServiceImpl.UserService.getAllUsers(UserService.java:239)
        //   See https://diff.blue/R013 to resolve this issue.

        when(userRepository.findAll((Pageable) any())).thenReturn(new PageImpl<>(new ArrayList<>()));
        userService.getAllUsers(-1);
    }

    /**
     * Method under test: {@link UserService#getAllUsers(Integer)}
     */
    @Test
    void testGetAllUsers5() {
        when(userRepository.findAll((Pageable) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.getAllUsers(10));
        verify(userRepository).findAll((Pageable) any());
    }

    /**
     * Method under test: {@link UserService#deleteUser(Long)}
     */
    @Test
    void testDeleteUser() {
        when(userRepository.deleteUsersById((Long) any())).thenThrow(new RuntimeException());
        assertThrows(RuntimeException.class, () -> userService.deleteUser(123L));
        verify(userRepository).deleteUsersById((Long) any());
    }
}

