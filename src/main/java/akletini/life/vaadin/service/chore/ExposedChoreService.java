package akletini.life.vaadin.service.chore;

import akletini.life.core.chore.dto.ChoreDto;
import akletini.life.core.chore.dto.mapper.ChoreMapper;
import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.chore.service.ChoreService;
import akletini.life.core.shared.validation.exception.BusinessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExposedChoreService {

    private final ChoreService choreService;
    private final ChoreMapper choreMapper;


    public ExposedChoreService(ChoreService choreService, ChoreMapper choreMapper) {
        this.choreService = choreService;
        this.choreMapper = choreMapper;
    }

    public ChoreDto store(ChoreDto choreDto) throws BusinessException {
        Chore chore = choreMapper.dtoToChore(choreDto);
        Chore storedChore = choreService.store(chore);
        return choreMapper.choreToDto(storedChore);
    }

    public Page<ChoreDto> getChores(int page, int pageSize, Optional<String> sortBy,
                                    Optional<List<String>> filterBy) {
        Page<Chore> chores = choreService.getChores(page, pageSize, sortBy, filterBy);
        return chores.map(choreMapper::choreToDto);
    }

    public void delete(ChoreDto choreDto) {
        choreService.delete(choreMapper.dtoToChore(choreDto));
    }

    public void completeChore(ChoreDto choreDto) throws BusinessException {
        choreService.completeChore(choreMapper.dtoToChore(choreDto));
    }
}
