package pl.coderslab.wrkt_springboot_backend.plan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanMapper planMapper;

    @Autowired
    public PlanService(PlanRepository planRepository, UserRepository userRepository, PlanMapper planMapper) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
        this.planMapper = planMapper;
    }

    public List<PlanDTO> getPlans(){
        String userName = "skarpeta";
        User user = userRepository.findByName(userName);
        List<PlanDTO> planDTOList = new ArrayList<>();
        if(user!=null){
            planDTOList = planRepository.findByUser(user).stream()
                .filter(plan -> !plan.isDeleted())
                .map(planMapper::mapToPlanDTO)
                .collect(Collectors.toList());
        }
        return planDTOList;
    }

    public void addPlan(PlanDTO planDTO){
        Plan plan = planMapper.mapToPlan(planDTO);
        User user = userRepository.findByName(plan.getUser().getName());
        plan.setUser(user);
        planRepository.save(plan);
    }

    public void removePlan(Long id){
        Optional<Plan> plan = planRepository.findById(id);
        if(plan.isPresent()){
            Plan planToDelete = plan.get();
            planToDelete.setDeleted(true);
            planRepository.save(planToDelete);
            log.info("Usunięto: " + id);
        }
    }

}
