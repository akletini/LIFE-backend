package akletini.life.vaadin.service.todo;

import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.todo.dto.TagDto;
import akletini.life.core.todo.dto.mapper.TagMapper;
import akletini.life.core.todo.repository.entity.Tag;
import akletini.life.core.todo.service.api.TagService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExposedTagService {

    private final TagMapper tagMapper;

    private final TagService tagService;

    public ExposedTagService(TagMapper tagMapper, TagService tagService) {
        this.tagMapper = tagMapper;
        this.tagService = tagService;
    }

    public TagDto store(TagDto tagDto) throws BusinessException {
        Tag tag = tagMapper.dtoToTag(tagDto);
        Tag storedTag = tagService.store(tag);
        return tagMapper.tagToDto(storedTag);
    }

    public List<TagDto> getAll() {
        return tagService.getAll().stream().map(tagMapper::tagToDto).collect(Collectors.toList());
    }

    public void delete(TagDto tagDto) {
        tagService.delete(tagMapper.dtoToTag(tagDto));
    }

}
