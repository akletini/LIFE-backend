package akletini.life.mail;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.repository.entity.Interval;
import akletini.life.shared.utils.DateUtils;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.user.repository.entity.User;
import akletini.life.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

import static akletini.life.shared.utils.DateUtils.*;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Service
@AllArgsConstructor
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;
    private UserService userService;

    @Override
    public void sendMail() {
        List<User> users = userService.getAll();
        for (User user : users) {
            List<Todo> assignedTodos = user.getAssignedTodos();
            List<Chore> assignedChores = user.getAssignedChores();
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(user.getEmail());
            message.setSubject("Your weekly overview");
            message.setText(getTextFromEntities(assignedTodos, assignedChores));
            mailSender.send(message);
        }

    }

    private String getTextFromEntities(List<Todo> todos, List<Chore> chores) {
        StringBuilder sb = new StringBuilder();
        sb.append("Your week: \n\n");

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = dateToLocalDate(addDays(new Date(), 8));
        // CHORES
        sb.append("CHORES: \n");
        chores =
                chores.stream().filter(chore -> chore.getDueAt().isAfter(startDate) && chore.getDueAt().isBefore(endDate)).collect(Collectors.toList());
        chores = computeIntervals(chores, endDate);
        chores.sort(Comparator.comparing(Chore::getDueAt).thenComparing(Chore::getDuration));
        chores.forEach(chore -> sb.append("- ")
                .append(chore.getTitle())
                .append(" (")
                .append(chore.getDuration())
                .append(" min) on ")
                .append(getDayAsString(chore.getDueAt()))
                .append(", ")
                .append(DateTimeFormatter.ofPattern(DATE_FORMAT).format(chore.getDueAt()))
                .append("\n"));
        // TODOS
        sb.append("TODOS: \n");
        todos = todos.stream().filter(todo -> todo.getDueAt().isAfter(startDate) && todo.getDueAt().isBefore(endDate)).collect(Collectors.toList());
        todos.sort(Comparator.comparing(Todo::getDueAt));
        todos.forEach(todo -> sb.append("- ")
                .append(todo.getTitle())
                .append(" on ")
                .append(getDayAsString(todo.getDueAt()))
                .append(", ")
                .append(DateTimeFormatter.ofPattern(DATE_FORMAT)
                        .format(todo.getDueAt()))
                .append("\n"));

        sb.append("\n\n Enjoy!");
        return sb.toString();
    }

    private List<Chore> computeIntervals(List<Chore> chores, LocalDate endDate) {
        Date endDateAsDate = localDateToDate(endDate);
        List<Chore> computedChores = new ArrayList<>();
        for (Chore chore : chores) {
            Interval interval = chore.getInterval();
            Date date = addInterval(interval, interval.getValue(), localDateToDate(chore.getDueAt()));
            while (DateUtils.truncatedCompareTo(date, endDateAsDate, Calendar.DATE) < 0) {
                Chore computedChore = new Chore();
                computedChore.setDueAt(dateToLocalDate(date));
                computedChore.setTitle(chore.getTitle());
                computedChore.setDuration(chore.getDuration());
                computedChores.add(computedChore);
                date = addInterval(interval, interval.getValue(), date);
            }
        }
        chores.addAll(computedChores);
        return chores;
    }

    private String getDayAsString(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.GERMAN);
    }
}
