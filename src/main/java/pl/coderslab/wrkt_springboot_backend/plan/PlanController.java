package pl.coderslab.wrkt_springboot_backend.plan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/plan")
@Slf4j
public class PlanController {

    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping
    public List<Plan> getPlans(){
        return planService.getPlans();
    }

    @PostMapping("/add")
    public String addPlan(@RequestBody Plan plan){
        log.info("New plan: " + plan.toString());
        return planService.addPlan(plan);
    }

    @DeleteMapping("/delete/{id}")
    public void removePlan(@PathVariable Long id){
        log.info("Id planu do usunięcia: " + id);
        planService.removePlan(id);
    }

}
