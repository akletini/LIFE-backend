package akletini.life.chore;

import akletini.life.chore.repository.api.ChoreRepository;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.user.repository.api.UserRepository;
import akletini.life.user.repository.entity.User;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static akletini.life.chore.TestChores.getNewChore;
import static akletini.life.user.structure.TestUsers.getDefaultCredentialUser;
import static org.apache.commons.lang3.time.DateUtils.addDays;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChoreRepositoryTest {

    @Autowired
    UserRepository userRepository;

    User user;
    @Autowired
    private ChoreRepository choreRepository;

    @BeforeEach
    public void initDB() {
        user = getDefaultCredentialUser();
        user = userRepository.save(user);
    }

    @Test
    public void filterByActive() {
        // GIVEN
        List<Chore> testData = createTestChores(4);
        createInactiveChores(6);

        // WHEN
        Page<Chore> activeChores = choreRepository.findFiltered(PageRequest.of(0, 5), true, null);
        List<Chore> choresFromDB = activeChores.toList();

        // THEN
        assertEquals(testData.size(), choresFromDB.size());
        List<Boolean> activeStates = choresFromDB.stream().map(Chore::isActive).toList();
        assertFalse(activeStates.contains(false));
    }

    @Test
    public void filterByDueDate() {
        // GIVEN
        List<Chore> testChores = createDueTestChores(10);
        final int[] counter = {0};
        testChores.forEach(chore -> {
            if (counter[0] < 5) {
                chore.setDueAt(addDays(chore.getDueAt(), 5));
                choreRepository.save(chore);
            }
            counter[0]++;
        });

        // WHEN
        List<Chore> foundChores =
                choreRepository.findFiltered(PageRequest.of(0, 5), null, new Date()).toList();

        // THEN
        assertEquals(5, foundChores.size());
        List<Chore> filteredChores =
                foundChores.stream().filter(chore -> DateUtils.truncatedCompareTo(chore.getDueAt(), new Date(), Calendar.DATE) >= 0).toList();
        assertTrue(filteredChores.isEmpty());
    }

    @Test
    public void filterByDueDateAndActive() {
        // GIVEN
        List<Chore> testChores = createDueTestChores(15);
        createInactiveChores(5);

        final int[] counter = {0};
        testChores.forEach(chore -> {
            if (counter[0] < 5) {
                chore.setDueAt(addDays(chore.getDueAt(), 5));
                choreRepository.save(chore);
            }
            counter[0]++;
        });
        // active: 10 due, 5 not due, 5 inactive

        // WHEN
        List<Chore> foundChores =
                choreRepository.findFiltered(PageRequest.of(0, 50), true, new Date()).toList();

        // THEN
        assertEquals(10, foundChores.size());
        List<Chore> filteredChores =
                foundChores.stream().filter(chore -> DateUtils.truncatedCompareTo(chore.getDueAt(), new Date(), Calendar.DATE) >= 0).toList();
        assertTrue(filteredChores.isEmpty());
        List<Boolean> activeStates = foundChores.stream().map(Chore::isActive).toList();
        assertFalse(activeStates.contains(false));
    }

    private List<Chore> createTestChores(int count) {
        List<Chore> chores = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Chore chore = getNewChore();
            chore.setAssignedUser(user);
            chores.add(choreRepository.save(chore));
        }
        return chores;
    }

    private List<Chore> createDueTestChores(int count) {
        List<Chore> chores = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Chore chore = getNewChore();
            chore.setAssignedUser(user);
            chore.setDueAt(addDays(new Date(), -3));
            chores.add(choreRepository.save(chore));
        }
        return chores;
    }

    private List<Chore> createInactiveChores(int count) {
        List<Chore> chores = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Chore chore = getNewChore();
            chore.setAssignedUser(user);
            chore.setActive(false);
            chores.add(choreRepository.save(chore));
        }
        return chores;
    }
}
