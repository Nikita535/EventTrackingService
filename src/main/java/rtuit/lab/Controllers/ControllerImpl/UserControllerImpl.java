package rtuit.lab.Controllers.ControllerImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rtuit.lab.Controllers.UserController;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.Services.ServiceImpl.RegistrationService;
import rtuit.lab.Services.ServiceImpl.UserService;

import java.security.Principal;

@RestController
@Secured({"ROLE_ADMIN","ROLE_USER","ROLE_ORGANIZER"})
@RequestMapping("/api/user/")
public class UserControllerImpl implements UserController {

    @Autowired
    UserService userService;

    @Autowired
    RegistrationService registrationService;

    /**
     *
     * @param userDTO
     * @param authentication
     * @return
     */
    @PostMapping("/edit")
    public ResponseEntity<?> editController(@RequestBody UserDTO userDTO, Authentication authentication){
        return userService.userEdit(userDTO,authentication);
    }


    /**
     *
     * @param id
     * @return
     */
    @PostMapping("/deleteUser")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam Long id){
        return userService.deleteUser(id);
    }


    @PostMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> allUsers(Integer pageNumber){
        return userService.getAllUsers(pageNumber);
    }

    /**
     *
     * @param tag
     * @param principal
     * @return
     */
    @PostMapping("/register")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> registerOnEvent(@RequestParam String tag, Principal principal){
        return registrationService.registerUserOnEvent(tag,principal);
    }
}
