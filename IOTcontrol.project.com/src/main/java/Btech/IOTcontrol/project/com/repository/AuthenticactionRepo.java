package Btech.IOTcontrol.project.com.repository;


import Btech.IOTcontrol.project.com.entity.AuthenticationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface AuthenticactionRepo extends JpaRepository<AuthenticationEntity, Long> {
    Optional<AuthenticationEntity> findByEmail(String email);
}
