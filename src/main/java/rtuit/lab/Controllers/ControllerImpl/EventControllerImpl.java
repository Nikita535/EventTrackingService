package rtuit.lab.Controllers.ControllerImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rtuit.lab.Controllers.EventController;
import rtuit.lab.DTO.EventDTO;
import rtuit.lab.Services.ServiceImpl.EventService;

import java.security.Principal;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventControllerImpl implements EventController {

    @Autowired
    EventService eventService;

    /**
     *
     * @return
     */
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEvents(@RequestParam(defaultValue = "0") Integer pageNumber){
        return eventService.getAllEvents(pageNumber);
    }

    /**
     *
     * @param eventDTO
     * @param principal
     * @return
     */
    @PostMapping("/addEvent")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<?> addEvent(@RequestBody EventDTO eventDTO,Principal principal){
        return eventService.addEvent(eventDTO,principal);
    }

    /**
     *
     * @param eventDTO
     * @param principal
     * @return
     */
    @PostMapping("/deleteEvent")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<?> deleteEvent(@RequestBody EventDTO eventDTO, Principal principal){
        return eventService.deleteEvent(eventDTO,principal);
    }

    /**
     *
     * @param tag
     * @param principal
     * @return
     */
    @PostMapping("/checkEventMembers")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<?> checkEventMembers(@RequestParam String tag,Principal principal,@RequestParam Integer pageNumber){
        return eventService.checkEventMembers(tag,principal,pageNumber);
    }
}
