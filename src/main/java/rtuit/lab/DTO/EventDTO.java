package rtuit.lab.DTO;


import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDTO {
    private String title;
    private String description;
    private String location;
    private String tag;
}
