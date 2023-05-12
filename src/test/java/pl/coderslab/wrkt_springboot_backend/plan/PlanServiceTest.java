package pl.coderslab.wrkt_springboot_backend.plan;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.wrkt_springboot_backend.exercise.*;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlanServiceTest {
    @InjectMocks
    private PlanService planService;
    @Mock
    private PlanRepository planRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    PlanMapper planMapper;
    @Mock
    InMemorySessionRegistry registry;
    @Test
    void should_not_return_plan_list_for_user_when_sessionId_is_null() {
        //given
        String sessionId = null;
        when(registry.getUserNameForSession(sessionId)).thenReturn(null);
        when(userRepository.findByName(registry.getUserNameForSession(sessionId))).thenReturn(null);
        //when
        List<PlanDTO> resultPlanList = planService.getPlans(null);
        //then
        assertTrue(resultPlanList.isEmpty());
    }

    @Test
    void should_not_add_plan_when_user_is_not_in_database() {
        //given
        User user = User.builder().name("Franek").build();
        PlanDTO planDTO = PlanDTO.builder().name("FBW").build();
        Plan plan = Plan.builder().name("FBW").user(user).build();
        when(userRepository.findByName(plan.getUser().getName())).thenReturn(null);
        when(planMapper.mapToPlan(planDTO)).thenReturn(plan);
        when(planRepository.save(plan)).thenThrow(NullPointerException.class);
        //when
        //then
        assertThrows(NullPointerException.class,() -> planService.addPlan(planDTO));
    }
}