package rtuit.lab.Services.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rtuit.lab.Models.Media;
import rtuit.lab.Repositories.MediaRepository;

import java.util.Optional;

@Service
public class MediaService implements rtuit.lab.Services.MediaService {

    @Autowired
    private MediaRepository mediaRepository;

    public Optional<Media> findMediaById(Long id) {
        return mediaRepository.findMediaById(id);
    }
}
