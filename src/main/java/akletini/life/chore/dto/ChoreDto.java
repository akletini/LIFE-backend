package akletini.life.chore.dto;

import akletini.life.chore.repository.entity.Interval;
import akletini.life.user.dto.UserDto;
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
    private boolean isActive;
    private int duration;
    private Interval interval;
    private UserDto assignedUser;
}
