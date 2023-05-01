package pl.coderslab.wrkt_springboot_backend.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateRepository extends JpaRepository<Template,Long> {
    List<Template> findByPlanId(Long planId);
    Template findByPlanIdAndDay(Long planId, Integer day);
}
