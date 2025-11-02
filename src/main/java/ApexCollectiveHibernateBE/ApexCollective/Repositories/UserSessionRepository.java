package ApexCollectiveHibernateBE.ApexCollective.Repositories;

import ApexCollectiveHibernateBE.ApexCollective.Entity.UserSessions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSessions, Long> {
    @Query("SELECT us FROM UserSessions us WHERE us.user_id = :user_id")
    Optional<UserSessions> findByUserId(@Param("user_id") Long user_id);
}
