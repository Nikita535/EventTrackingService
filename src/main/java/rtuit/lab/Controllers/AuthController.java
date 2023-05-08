package rtuit.lab.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import rtuit.lab.Controllers.ControllerImpl.AuthControllerImpl;
import rtuit.lab.DTO.UserDTO;
import rtuit.lab.DTO.ValidateDTO.RegisterRequestDTO;

import javax.validation.Valid;
import java.security.Principal;

public interface AuthController {
    @Operation(
            tags = "Войти в систему",
            summary = "Вход в систему",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400")
            }
    )
    ResponseEntity<?> login(@RequestBody UserDTO userDto);

    @Operation(
            tags = "Зарегистрироваться в системе",
            summary = "Регистрация",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "400")
            }
    )
    ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO);

    @Operation(
            tags = "Собрать данные о пользователя в системе",
            summary = "Сбор данных",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401")
            }
    )
    ResponseEntity<?> getUserInfo(Principal user);
}
