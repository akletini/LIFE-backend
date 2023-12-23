package akletini.life.core.todo.controller;

import akletini.life.core.todo.dto.TagDto;
import akletini.life.core.todo.dto.mapper.TagMapper;
import akletini.life.core.todo.repository.entity.Tag;
import akletini.life.core.todo.service.api.TagService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos/tags")
@AllArgsConstructor
public class TagController {

    private TagService tagService;

    private final TagMapper tagMapper;

    @PostMapping(value = "/add")
    public ResponseEntity<TagDto> addTag(@RequestBody TagDto tagDto) {
        Tag tag = tagMapper.dtoToTag(tagDto);
        Tag storedTag = tagService.store(tag);
        return ResponseEntity.status(HttpStatus.OK).body(tagMapper.tagToDto(storedTag));
    }

    @PutMapping(value = "/update")
    public ResponseEntity<TagDto> updateTag(@RequestBody TagDto tagDto) {
        Tag tag = tagMapper.dtoToTag(tagDto);
        tagService.getById(tag.getId());
        Tag updatedTag = tagService.store(tag);
        return ResponseEntity.status(HttpStatus.OK).body(tagMapper.tagToDto(updatedTag));
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<TagDto> deleteTag(@PathVariable TagDto tagDto) {
        tagService.delete(tagMapper.dtoToTag(tagDto));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<Tag> allTags = tagService.getAll();
        List<TagDto> allToDto = allTags.stream().map(tagMapper::tagToDto).toList();
        return ResponseEntity.status(HttpStatus.OK).body(allToDto);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable Long id) {
        Tag tagById = tagService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(tagMapper.tagToDto(tagById));
    }
}
