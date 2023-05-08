package rtuit.lab.Controllers.ControllerImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rtuit.lab.Controllers.AuthController;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.DTO.ValidateDTO.RegisterRequestDTO;
import rtuit.lab.DTO.ValidateDTO.RegisterResponseDTO;
import rtuit.lab.Services.ServiceImpl.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthControllerImpl implements AuthController {
    private final UserService userService;


    /**
     *
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDto) {
        return userService.loginUser(userDto);
    }

    /**
     *
     * @param registerRequestDTO
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        if (userService.existsByUserEmail(registerRequestDTO.getEmail())) {
            return ResponseEntity.badRequest().body(new RegisterResponseDTO("Такой пользователь уже существует!"));
        }
        userService.registerUser(registerRequestDTO);
        return ResponseEntity.ok(new RegisterResponseDTO("Пользователь зарегистрирован!"));
    }

    /**
     *
     * @param user
     * @return
     */
    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Principal user){
        return userService.collectUserData(user);
    }


    /**
     *
     * @param jwt
     * @param id
     * @param email
     * @param username
     * @param authorities
     */
    public record JwtResponse(String jwt, Long id, String email, String username, List<String> authorities) {}
}
