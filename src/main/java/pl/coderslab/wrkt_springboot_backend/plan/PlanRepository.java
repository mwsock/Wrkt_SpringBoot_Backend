package pl.coderslab.wrkt_springboot_backend.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {

    List<Plan> findByUser(User user);
}
