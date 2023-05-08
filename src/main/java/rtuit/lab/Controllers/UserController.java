package rtuit.lab.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import rtuit.lab.Controllers.ControllerImpl.AuthControllerImpl;
import rtuit.lab.DTO.UserDTO;

import java.security.Principal;

public interface UserController {
    @Operation(
            tags = "Изменить данные учетной записи",
            summary = "Смена данных",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401")
            }
    )
    ResponseEntity<?> editController(@RequestBody UserDTO userDTO, Authentication authentication);
    @Operation(
            tags = "Удалить пользователя",
            summary = "Удаление пользователя",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            }
    )
    ResponseEntity<?> deleteUser(@RequestParam Long id);
    @Operation(
            tags = "Зарегистрироваться на событие",
            summary = "Регистрация проведена",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            }
    )
    ResponseEntity<?> registerOnEvent(@RequestParam String tag, Principal principal);
}
