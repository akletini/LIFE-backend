package akletini.life.todo.controller;

import akletini.life.todo.dto.TagDto;
import akletini.life.todo.dto.mapper.TagMapper;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.service.api.TagService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    private final TagMapper tagMapper = Mappers.getMapper(TagMapper.class);

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
    public ResponseEntity<TagDto> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<TagDto>> getAllTags() {
        List<Tag> allTags = tagService.getAll();
        List<TagDto> allToDto = allTags.stream().map(tag -> tagMapper.tagToDto(tag)).toList();
        return ResponseEntity.status(HttpStatus.OK).body(allToDto);
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<TagDto> getById(@PathVariable Long id) {
        Tag tagById = tagService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(tagMapper.tagToDto(tagById));
    }
}
