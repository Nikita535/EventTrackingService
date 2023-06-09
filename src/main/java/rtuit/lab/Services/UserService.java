package rtuit.lab.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.DTO.ValidateDTO.RegisterRequestDTO;
import rtuit.lab.Models.User;

import javax.mail.MessagingException;
import java.security.Principal;

public interface UserService {
    void save(User user);
    boolean existsByUserEmail(String email);
    ResponseEntity<?> userEdit(String JsonUserDTO, Authentication authentication, MultipartFile multipartFile) throws JsonProcessingException;
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    void registerUser(RegisterRequestDTO registerRequestDTO);
    ResponseEntity<?> loginUser(UserDTO userDto);
    ResponseEntity<?> collectUserData(Principal user);
    void activateUser(String code);
    User findUserByEmail(String email);
    void createActivationCode(String userEmail) throws MessagingException;
    User getUserAuth(Principal principal);
    ResponseEntity<?> getAllUsers(Integer pageNumber);
    ResponseEntity<?> deleteUser(Long id);
}
