package rtuit.lab.Models;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;


@Table(name = "events")
@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Setter
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String location;

}