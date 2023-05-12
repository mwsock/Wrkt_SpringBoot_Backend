package pl.coderslab.wrkt_springboot_backend.plan;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlanRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private PlanRepository planRepository;

    @Test
    void should_return_plan_defined_for_specific_user() {
        User userOne = entityManager.persistAndFlush(User.builder().name("TestUser1").password("TestPassword1").build());
        User userTwo = entityManager.persistAndFlush(User.builder().name("TestUser2").password("TestPassword2").build());

        List<Plan> plansForOne = Arrays.asList(
                entityManager.persistAndFlush(Plan.builder().user(userOne).name("FBW").build()),
                entityManager.persistAndFlush(Plan.builder().user(userOne).name("Split").build())
        );
        List<Plan> plansForTwo = Arrays.asList(
                entityManager.persistAndFlush(Plan.builder().user(userTwo).name("5/3/1").build()),
                entityManager.persistAndFlush(Plan.builder().user(userTwo).name("Push Pull").build())
        );

        //when
        List<Plan> result = planRepository.findByUser(userOne);
        //then
        assertEquals(plansForOne,result);
        assertNotEquals(plansForTwo,result);
        assertNotNull(result);
    }

    @Test
    void should_not_return_any_plan_for_specific_user(){
        //given
        User userOne = entityManager.persistAndFlush(User.builder().name("TestUser1").password("TestPassword1").build());
        //when
        List<Plan> result = planRepository.findByUser(userOne);
        //then
        assertTrue(result.isEmpty());
        assertFalse(result.size() > 0);

    }
}