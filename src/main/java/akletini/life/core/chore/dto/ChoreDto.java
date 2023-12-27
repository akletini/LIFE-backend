package akletini.life.core.chore.dto;

import akletini.life.core.chore.repository.entity.Interval;
import akletini.life.core.shared.dto.BaseDto;
import akletini.life.core.user.dto.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ChoreDto extends BaseDto {

    private String title;
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
