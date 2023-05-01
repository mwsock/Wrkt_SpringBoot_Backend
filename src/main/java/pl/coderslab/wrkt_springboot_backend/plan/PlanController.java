package pl.coderslab.wrkt_springboot_backend.plan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/plan")
@Slf4j
public class PlanController {

    private final PlanRepository planRepository;
    private final  UserRepository userRepository;

    @Autowired
    public PlanController(PlanRepository repository, PlanRepository planRepository, UserRepository userRepository) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Plan> getPlans(){
        return planRepository.findAll().stream()
                .filter(plan -> !plan.isDeleted())
                .toList();
    }

    @PostMapping("/add")
    public String addPlan(@RequestBody Plan plan){
        log.info("New plan: " + plan.toString());
        User user = userRepository.findByName(plan.getUser().getName());
        plan.setUser(user);
        return "New plan saved: " + planRepository.save(plan);
    }

    @DeleteMapping("/delete/{id}")
    public void removePlan(@PathVariable Long id){
        log.info("Id planu do usunięcia: " + id);
        Plan plan = planRepository.findById(id).orElse(null);
        if(plan != null){
            plan.setDeleted(true);
            planRepository.save(plan);
            log.info("Usunięto: " + id);
        }
    }

}
