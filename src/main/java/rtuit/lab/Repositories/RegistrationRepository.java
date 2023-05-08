package rtuit.lab.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rtuit.lab.Models.Registration;
import rtuit.lab.Models.User;

import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration,Long> {
    List<Registration> findAllByEvent_Tag(String tag);
    List<Registration> findAllByUser(User user);
}
