package akletini.life.core.chore.dto;

import akletini.life.core.chore.repository.entity.Interval;
import akletini.life.core.user.dto.UserDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChoreDto {

    private Long id;
    private String title;
    private String createdAt;
    private String dueAt;
    private String description;
    private String startDate;
    private String lastCompleted;
    private boolean active;
    private boolean shiftInterval;
    private int duration;
    private Interval interval;
    private UserDto assignedUser;
}
