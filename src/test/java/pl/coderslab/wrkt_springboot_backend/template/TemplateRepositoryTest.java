package pl.coderslab.wrkt_springboot_backend.template;

import org.aspectj.weaver.bcel.ExceptionRange;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.plan.Plan;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class TemplateRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TemplateRepository templateRepository;

    @Test
    void should_return_template_for_provided_planId() {
        //given
        User userOne = entityManager.persistAndFlush(User.builder().name("TestUser1").password("TestPassword1").build());
        Plan plan = entityManager.persistAndFlush((Plan.builder().name("FBW").user(userOne).build()));
        List<Exercise> exercises = Arrays.asList(entityManager.persistAndFlush(Exercise.builder().user(userOne).name("Deadlift").build()));

        entityManager.persistAndFlush(Template.builder().day(1).plan(plan).exercises(exercises).user(userOne).build());
        //when
        List<Template> resultList = templateRepository.findByPlanId(plan.getId());
        //then
        assertNotNull(resultList);
        assertEquals(resultList.get(0).getPlan().getId(),plan.getId());
    }

    @Test
    void should_not_return_template_for_provided_planId_and_day() {
        //given
        User userOne = entityManager.persistAndFlush(User.builder().name("TestUser1").password("TestPassword1").build());
        Plan firstPlan = entityManager.persistAndFlush((Plan.builder().name("FBW").user(userOne).build()));
        Plan secondPlan = entityManager.persistAndFlush((Plan.builder().name("Split").user(userOne).build()));
        List<Exercise> exercises = Arrays.asList(entityManager.persistAndFlush(Exercise.builder()
                .user(userOne)
                .name("Deadlift")
                .build()));

        entityManager.persistAndFlush(Template.builder().day(1).plan(firstPlan).exercises(exercises).user(userOne).build());
        //when
        Template result = templateRepository.findByPlanIdAndDay(firstPlan.getId(),2);
        //then
        assertNull(result);
    }
}