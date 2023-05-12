package pl.coderslab.wrkt_springboot_backend.plan;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlanService {

    private final PlanRepository planRepository;
    private final UserRepository userRepository;
    private final PlanMapper planMapper;
    private final InMemorySessionRegistry registry;

    @Autowired
    public PlanService(PlanRepository planRepository, UserRepository userRepository, PlanMapper planMapper, InMemorySessionRegistry registry) {
        this.planRepository = planRepository;
        this.userRepository = userRepository;
        this.planMapper = planMapper;
        this.registry = registry;
    }

    public List<PlanDTO> getPlans(String sessionId){
        String userName = registry.getUserNameForSession(sessionId);
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

    public String addPlan(PlanDTO planDTO){
        Plan plan = planMapper.mapToPlan(planDTO);
        User user = userRepository.findByName(plan.getUser().getName());
        plan.setUser(user);
        return "New plan saved: " + planRepository.save(plan).getName();
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
