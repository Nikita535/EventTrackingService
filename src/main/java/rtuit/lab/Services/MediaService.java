package rtuit.lab.Services;

import rtuit.lab.Models.Media;

import java.util.Optional;

public interface MediaService {
    Optional<Media> findMediaById(Long id);
}
