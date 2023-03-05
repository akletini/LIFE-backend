package akletini.life.chore;

import akletini.life.chore.exception.custom.ChoreStoreException;
import akletini.life.chore.repository.api.ChoreRepository;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.repository.entity.Interval;
import akletini.life.chore.service.ChoreService;
import akletini.life.user.repository.api.UserRepository;
import akletini.life.user.repository.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static akletini.life.chore.TestChores.getNewChore;
import static akletini.life.shared.utils.DateUtils.dateToLocalDate;
import static akletini.life.shared.validation.Errors.CHORE.POSITIVE_INTERVAL;
import static akletini.life.shared.validation.Errors.CHORE.START_IN_THE_PAST;
import static akletini.life.shared.validation.Errors.getError;
import static akletini.life.user.structure.TestUsers.getDefaultCredentialUser;
import static org.apache.commons.lang3.time.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ChoreServiceTest {

    @Autowired
    ChoreService choreService;

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
    public void updateChore() {
        // GIVEN
        Chore chore = getNewChore();
        chore.setDueAt(LocalDate.now());
        chore.setAssignedUser(user);
        chore = choreRepository.save(chore);

        // WHEN
        String differentTitle = "a different title";
        chore.setTitle(differentTitle);

        // THEN
        Chore finalChore = chore;
        assertDoesNotThrow(() -> choreService.store(finalChore));
        Optional<Chore> byId = choreRepository.findById(chore.getId());
        assertTrue(byId.isPresent() && byId.get().getTitle().equals(differentTitle));
    }

    @Test
    public void cannotModifyUser() {
        // GIVEN
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        String userEmail = user.getEmail();

        // WHEN
        chore.getAssignedUser().setEmail("");
        choreService.store(chore);

        // THEN
        Optional<User> byId = userRepository.findById(user.getId());
        assertTrue(byId.isPresent());
        assertEquals(byId.get().getEmail(), userEmail);
    }

    @Test
    public void completeWithShift() {
        // Given chore with due date today
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        // the next due date will be shifted
        chore.setShiftInterval(true);
        Date currentDate = new Date();
        chore.setDueAt(LocalDate.now());

        Chore completedChore = choreService.completeChore(chore);

        Date targetDate = addDays(currentDate, 5);
        assertEquals(dateToLocalDate(targetDate).toString(), completedChore.getDueAt().toString());
    }

    @Test
    public void completeWithShiftTooEarly() {
        // Given chore with due date today
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        // the next due date will be shifted
        chore.setShiftInterval(true);
        Date currentDate = new Date();
        chore.setDueAt(dateToLocalDate(addDays(currentDate, 2)));

        Chore completedChore = choreService.completeChore(chore);

        Date targetDate = addDays(currentDate, 5);
        assertEquals(dateToLocalDate(targetDate).toString(), completedChore.getDueAt().toString());
    }

    @Test
    public void completeWithShiftTooLate() {
        // Given chore with due date today
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        // the next due date will be shifted
        chore.setShiftInterval(true);
        Date currentDate = new Date();
        chore.setDueAt(dateToLocalDate(addDays(currentDate, -2)));

        Chore completedChore = choreService.completeChore(chore);

        Date targetDate = addDays(currentDate, 5);
        assertEquals(dateToLocalDate(targetDate).toString(), completedChore.getDueAt().toString());
    }

    @Test
    public void completeWithoutShift() {
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        chore.setShiftInterval(false);
        Date currentDate = new Date();
        chore.setDueAt(dateToLocalDate(currentDate));

        Chore completedChore = choreService.completeChore(chore);

        Date targetDate = addDays(currentDate, 5);
        assertEquals(dateToLocalDate(targetDate).toString(), completedChore.getDueAt().toString());
    }

    @Test
    public void completeWithoutShiftTooEarly() {
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        chore.setShiftInterval(false);
        Date currentDate = new Date();
        chore.setDueAt(dateToLocalDate(addDays(currentDate, 2)));

        Chore completedChore = choreService.completeChore(chore);

        Date targetDate = addDays(currentDate, 7);
        assertEquals(dateToLocalDate(targetDate).toString(), completedChore.getDueAt().toString());
    }

    @Test
    public void completeWithoutShiftTooLate() {
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        chore.setShiftInterval(false);
        Date currentDate = new Date();
        int delay = -6;
        chore.setDueAt(dateToLocalDate(addDays(currentDate, delay)));

        Chore completedChore = choreService.completeChore(chore);

        Date targetDate = addDays(currentDate, chore.getInterval().getValue() * 2 + delay);
        assertEquals(dateToLocalDate(targetDate).toString(), completedChore.getDueAt().toString());
    }

    @Test
    public void startDateInThePast() {
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        chore.setStartDate(dateToLocalDate(addDays(new Date(), -1)));

        ChoreStoreException choreStoreException = assertThrows(ChoreStoreException.class, () -> choreService.store(chore));
        assertEquals(choreStoreException.getMessage(), getError(START_IN_THE_PAST));
    }

    @Test
    public void testStartDate() {
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        chore.setStartDate(dateToLocalDate(addDays(new Date(), 5)));

        Chore storedChore = choreService.store(chore);

        assertEquals(dateToLocalDate(addDays(new Date(), 5)).toString(),
                storedChore.getDueAt().toString());
    }

    @Test
    public void negativeInterval() {
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        chore.setInterval(new Interval(-1, Interval.DateUnit.DAYS));

        ChoreStoreException choreStoreException = assertThrows(ChoreStoreException.class, () -> choreService.store(chore));
        assertEquals(choreStoreException.getMessage(), getError(POSITIVE_INTERVAL));
    }

    @Test
    public void correctDueDateWeeks() {
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        Date currentDate = new Date();
        chore.setStartDate(LocalDate.now());
        chore.setDueAt(dateToLocalDate(addDays(new Date(), -3)));
        chore.setShiftInterval(true);
        chore.setInterval(new Interval(3, Interval.DateUnit.WEEKS));

        Chore storedChore = choreService.store(chore);

        assertEquals(storedChore.getDueAt().toString(), dateToLocalDate(addWeeks(currentDate, 3)).toString());
    }

    @Test
    public void correctDueDateMonths() {
        Chore chore = getNewChore();
        chore.setAssignedUser(user);
        Date currentDate = new Date();
        chore.setStartDate(LocalDate.now());
        chore.setDueAt(dateToLocalDate(addDays(currentDate, -3)));
        chore.setShiftInterval(true);
        chore.setInterval(new Interval(3, Interval.DateUnit.MONTHS));

        Chore storedChore = choreService.store(chore);

        assertEquals(storedChore.getDueAt().toString(), dateToLocalDate(addMonths(currentDate, 3)).toString());
    }
}
