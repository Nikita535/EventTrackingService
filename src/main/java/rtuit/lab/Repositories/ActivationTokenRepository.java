package rtuit.lab.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rtuit.lab.Models.ActivationToken;

@Repository
public interface ActivationTokenRepository extends JpaRepository<ActivationToken,Integer> {

    ActivationToken findByToken(String token);

    void deleteByToken(String token);
}