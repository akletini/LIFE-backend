package akletini.life.core.chore.dto;

import akletini.life.core.chore.repository.entity.Interval;
import akletini.life.core.user.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ChoreDto {

    private Long id;
    private String title;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDate dueAt;
    private String description;
    private LocalDate startDate = LocalDate.now();
    private LocalDate lastCompleted;
    private boolean active = true;
    private boolean shiftInterval;
    private Integer duration;
    private Interval interval = new Interval();
    private UserDto assignedUser;
}
