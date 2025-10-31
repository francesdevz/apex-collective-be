package ApexCollectiveHibernateBE.ApexCollective.Repositories;

import ApexCollectiveHibernateBE.ApexCollective.Entity.UserSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessions, Long> {
    Optional<UserSessions> findUserByUserId(Long user_id);
}
