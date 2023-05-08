package rtuit.lab.Controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import rtuit.lab.DTO.EventDTO;
import rtuit.lab.Models.Event;
import rtuit.lab.Services.EventService;

import java.security.Principal;

@RestController
@RequestMapping("/api/event")
@RequiredArgsConstructor
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping("/getAll")
    @Secured({"ROLE_ADMIN","ROLE_USER","ROLE_ORGANIZER"})
    public ResponseEntity<?> getAllEvents(){
        return eventService.getAllEvents();
    }

    @PostMapping("/addEvent")
    @Secured({"ROLE_ORGANIZER"})
    public ResponseEntity<?> addEvent(@RequestBody EventDTO eventDTO,Principal principal){
        return eventService.addEvent(eventDTO,principal);
    }

    @PostMapping("/deleteEvent")
    @Secured({"ROLE_ORGANIZER"})
    public ResponseEntity<?> deleteEvent(@RequestBody EventDTO eventDTO, Principal principal){
        return eventService.deleteEvent(eventDTO,principal);
    }

}
