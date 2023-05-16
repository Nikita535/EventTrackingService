package rtuit.lab.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface MediaController {
    @Operation(
            tags = "Получить аватар пользователя",
            summary = "Смена данных",
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE)),
                    @ApiResponse(responseCode = "401")
            }
    )
    ResponseEntity<?> getImageById(@RequestParam Long id);
}
