package ApexCollectiveHibernateBE.ApexCollective.Repositories;


import ApexCollectiveHibernateBE.ApexCollective.Entity.User;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegisterRepository extends JpaRepository<User, Long> {

}
