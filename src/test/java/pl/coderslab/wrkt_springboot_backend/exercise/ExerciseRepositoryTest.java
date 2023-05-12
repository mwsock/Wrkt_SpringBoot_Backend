package pl.coderslab.wrkt_springboot_backend.exercise;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ExerciseRepositoryTest {

    @Autowired
    private TestEntityManager testEntityManager;
    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    void should_return_exercises_for_given_user() {
        //given
        User userOne = testEntityManager.persistAndFlush(User.builder().name("TestUser1").password("TestPassword1").build());
        User userTwo = testEntityManager.persistAndFlush(User.builder().name("TestUser2").password("TestPassword2").build());

        List<Exercise> exercisesForOne = Arrays.asList(
                testEntityManager.persistAndFlush(Exercise.builder().user(userOne).name("Deadlift").build()),
                testEntityManager.persistAndFlush(Exercise.builder().user(userOne).name("Barbell Curl").build())
        );
        List<Exercise> exercisesForTwo = Arrays.asList(
                testEntityManager.persistAndFlush(Exercise.builder().user(userTwo).name("Front Squat").build()),
                testEntityManager.persistAndFlush(Exercise.builder().user(userTwo).name("Low Bar Squat").build())
        );

        //when
        List<Exercise> result = exerciseRepository.findByUser(userOne);
        //then
        assertEquals(exercisesForOne,result);
        assertEquals(userOne,result.get(0).getUser());
        assertEquals(userOne,result.get(1).getUser());

        assertNotEquals(exercisesForTwo,result);
        assertNotEquals(userTwo,result.get(0).getUser());
        assertNotEquals(userTwo,result.get(1).getUser());
    }
}