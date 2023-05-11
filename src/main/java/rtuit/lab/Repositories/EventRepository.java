package rtuit.lab.Repositories;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rtuit.lab.Models.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event,Long> {
    @NonNull Page<Event> findAll(@NonNull Pageable pageable);
    Optional<Event> findEventByTag(String tag);

}
