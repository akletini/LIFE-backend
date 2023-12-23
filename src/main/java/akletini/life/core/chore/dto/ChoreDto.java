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
    private LocalDateTime createdAt;
    private LocalDate dueAt;
    private String description;
    private LocalDate startDate;
    private LocalDate lastCompleted;
    private boolean active;
    private boolean shiftInterval;
    private int duration;
    private Interval interval;
    private UserDto assignedUser;
}
