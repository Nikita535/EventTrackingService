package rtuit.lab.Controllers.ControllerImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rtuit.lab.Models.Media;
import rtuit.lab.Services.ServiceImpl.MediaService;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/media")
public class MediaController implements rtuit.lab.Controllers.MediaController {

    @Autowired
    private MediaService mediaService;


    @GetMapping("/getImage")
    public ResponseEntity<?> getImageById(@RequestParam Long id) {
        Media image = mediaService.findMediaById(id).orElse(null);
        return ResponseEntity.ok()
                .header("fileName", image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getMediaType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(image.getBytes())));
    }


}