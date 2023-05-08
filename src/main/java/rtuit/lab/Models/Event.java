package rtuit.lab.Models;

import lombok.*;
import org.springframework.lang.Nullable;

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

    @Column(name = "tag",unique = true)
    private String tag;

    @Column(name = "user_id",unique = false, nullable = true)
    private Long user_id;
}