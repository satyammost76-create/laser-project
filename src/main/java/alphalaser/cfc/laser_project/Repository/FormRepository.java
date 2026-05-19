package alphalaser.cfc.laser_project.Repository;


import alphalaser.cfc.laser_project.Entity.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // Yeh import hona chahiye

public interface FormRepository extends JpaRepository<Data, Long> {
    Optional<Data> findByEmail(String email); // Return type Optional hona zaroori hai
}