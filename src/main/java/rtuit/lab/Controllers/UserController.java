package rtuit.lab.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.Services.UserService;

@RestController
@Secured({"ROLE_ADMIN","ROLE_USER"})
@RequestMapping("/api/user/")
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/edit")
    public ResponseEntity<?> editController(@RequestBody UserDTO userDTO, Authentication authentication){
        return userService.userEdit(userDTO,authentication);
    }
}
