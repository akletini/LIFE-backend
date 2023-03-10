package akletini.life.mail;

import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.repository.entity.Interval;
import akletini.life.shared.utils.DateUtils;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.user.repository.entity.User;
import akletini.life.user.service.api.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
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
@Log4j2
public class MailServiceImpl implements MailService {

    private JavaMailSender mailSender;
    private UserService userService;

    @Override
    @Async
    @Transactional
    @Scheduled(cron = "0 0 8 * * SUN", zone = "Europe/Berlin")
    public void sendMail() {
        log.info("Sending out weekly mails");
        List<User> users = userService.getAll();
        for (User user : users) {
            List<Todo> assignedTodos = user.getAssignedTodos();
            List<Chore> assignedChores = user.getAssignedChores();
            MimeMessage message = mailSender.createMimeMessage();
            try {
                MimeMessageHelper helper = new MimeMessageHelper(message, false);
                helper.setTo(user.getEmail());
                helper.setSubject("Your weekly overview");
                helper.setText(getTextFromEntities(assignedTodos, assignedChores), true);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            mailSender.send(message);
        }

    }

    private String getTextFromEntities(List<Todo> todos, List<Chore> chores) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = dateToLocalDate(addDays(new Date(), 8));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);

        StringBuilder sb = new StringBuilder();
        sb.append("<h1> Your week: (")
                .append(dateToString(addDays(new Date(), 1)))
                .append(" - ")
                .append(dateToString(addDays(new Date(), 7)))
                .append(")")
                .append("</h1> \n\n");
        // CHORES
        sb.append("<h2> CHORES:  </h2> \n");
        chores = chores.stream().
                filter(chore -> chore.getDueAt().isAfter(startDate)
                        && chore.getDueAt().isBefore(endDate)).collect(Collectors.toList());
        computeIntervals(chores, endDate);
        chores.sort(Comparator.comparing(Chore::getDueAt).thenComparing(Chore::getDuration));
        sb.append("<ul>");
        chores.forEach(chore -> sb.append("<li> ")
                        .append(chore.getTitle())
                        .append(" (")
                        .append(chore.getDuration())
                        .append(" min) on ")
                        .append(getDayAsString(chore.getDueAt()))
                        .append(", ")
                        .append(formatter.format(chore.getDueAt()))
                .append("</li>"));
        sb.append("</ul>");

        // TODOS
        sb.append("<h2> TODOS:  </h2> \n");
        todos = todos.stream().filter(todo -> todo.getDueAt().isAfter(startDate) && todo.getDueAt().isBefore(endDate)).collect(Collectors.toList());
        todos.sort(Comparator.comparing(Todo::getDueAt));
        sb.append("<ul>");
        todos.forEach(todo -> sb.append("<li>")
                .append(todo.getTitle())
                .append(" on ")
                .append(getDayAsString(todo.getDueAt()))
                .append(", ")
                .append(formatter
                        .format(todo.getDueAt()))
                .append("</li>"));
        sb.append("</ul>");
        sb.append("<h3> Gl hf! </h3>");
        return sb.toString();
    }

    private void computeIntervals(List<Chore> chores, LocalDate endDate) {
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
    }

    private String getDayAsString(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.GERMAN);
    }
}
