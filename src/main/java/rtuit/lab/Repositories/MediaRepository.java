package rtuit.lab.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rtuit.lab.Models.Media;

import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, Long> {

    Optional<Media> findMediaById(Long id);
}