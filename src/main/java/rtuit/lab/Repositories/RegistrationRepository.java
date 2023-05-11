package rtuit.lab.Repositories;

import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rtuit.lab.Models.Event;
import rtuit.lab.Models.Registration;
import rtuit.lab.Models.User;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    Page<Registration> findAllByEvent_Tag(String tag, @NonNull Pageable pageable);
    List<Registration> findAllByUser(User user);

    void deleteAllByEvent(Event event);
}
