package pl.coderslab.wrkt_springboot_backend.plan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.List;

@Service
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;

    @Autowired
    public PlanService(PlanRepository planRepository, UserRepository userRepository) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
    }

    public List<Plan> getPlans(){
        return planRepository.findAll().stream()
                .filter(plan -> !plan.isDeleted())
                .toList();
    }

    public String addPlan(Plan plan){
        User user = userRepository.findByName(plan.getUser().getName());
        plan.setUser(user);
        return "New plan saved: " + planRepository.save(plan);
    }

    public void removePlan(Long id){
        Plan plan = planRepository.findById(id).orElse(null);
        if(plan != null){
            plan.setDeleted(true);
            planRepository.save(plan);
            log.info("Usunięto: " + id);
        }
    }

}
