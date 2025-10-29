package ApexCollectiveHibernateBE.ApexCollective.Repositories;


import ApexCollectiveHibernateBE.ApexCollective.Entity.User;
import ApexCollectiveHibernateBE.ApexCollective.Model.TokenModel;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRegisterRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
