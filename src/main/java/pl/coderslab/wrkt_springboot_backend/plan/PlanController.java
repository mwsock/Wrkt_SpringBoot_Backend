package pl.coderslab.wrkt_springboot_backend.plan;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

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
    public List<PlanDTO> getPlans(HttpServletRequest request){
        return planService.getPlans(request);
    }

    @PostMapping("/add")
    public String addPlan(@Valid @RequestBody PlanDTO planDTO){
        log.info("New plan: " + planDTO.toString());
        return planService.addPlan(planDTO);
    }

    @DeleteMapping("/delete/{id}")
    public void removePlan(@PathVariable Long id){
        log.info("Id planu do usunięcia: " + id);
        planService.removePlan(id);
    }

}
