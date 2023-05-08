package rtuit.lab.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import rtuit.lab.Controllers.ControllerImpl.AuthControllerImpl;
import rtuit.lab.DTO.EventDTO;

import java.security.Principal;

public interface EventController {
    @Operation(
            tags = "Получить все события",
            summary = "События получены",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            }
    )
    ResponseEntity<?> getAllEvents();
    @Operation(
            tags = "Добавить событие",
            summary = "Событие добавлено",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            }
    )
    ResponseEntity<?> addEvent(@RequestBody EventDTO eventDTO, Principal principal);
    @Operation(
            tags = "Удалить событие",
            summary = "Событие удалено",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            }
    )
    ResponseEntity<?> deleteEvent(@RequestBody EventDTO eventDTO, Principal principal);
    @Operation(
            tags = "Посмотреть участников события",
            summary = "Участники события получены",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    schema = @Schema(implementation = AuthControllerImpl.JwtResponse.class),
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401"),
                    @ApiResponse(responseCode = "403")
            }
    )
    ResponseEntity<?> checkEventMembers(@RequestParam String tag, Principal principal);
}
