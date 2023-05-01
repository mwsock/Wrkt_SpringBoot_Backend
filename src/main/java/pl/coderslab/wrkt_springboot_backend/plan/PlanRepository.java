package pl.coderslab.wrkt_springboot_backend.plan;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan,Long> {

    List<Plan> findAllByUser(String userName);
    @Query("select name from Plan where id = ?1")
    String findPlanNameById(Long id);
}
