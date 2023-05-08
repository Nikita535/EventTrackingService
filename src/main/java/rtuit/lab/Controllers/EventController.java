package rtuit.lab.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rtuit.lab.DTO.EventDTO;
import rtuit.lab.Models.Event;
import rtuit.lab.Services.EventService;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEvents(){
        return eventService.getAllEvents();
    }

    @PostMapping("/addEvent")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<?> addEvent(@RequestBody EventDTO eventDTO,Principal principal){
        return eventService.addEvent(eventDTO,principal);
    }

    @PostMapping("/deleteEvent")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<?> deleteEvent(@RequestBody EventDTO eventDTO, Principal principal){
        return eventService.deleteEvent(eventDTO,principal);
    }

    @PostMapping("/checkEventMembers")
    @PreAuthorize("hasAuthority('ROLE_ORGANIZER')")
    public ResponseEntity<?> checkEventMembers(@RequestParam String tag,Principal principal){
        return eventService.checkEventMembers(tag,principal);
    }
}
