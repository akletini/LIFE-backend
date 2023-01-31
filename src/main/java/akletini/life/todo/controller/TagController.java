package akletini.life.todo.controller;

import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.service.api.TagService;
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

    @PostMapping(value = "/add")
    public ResponseEntity<Tag> addTag(@RequestBody Tag tag) {
        Tag storedTag = tagService.store(tag);
        return ResponseEntity.status(HttpStatus.OK).body(storedTag);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<Tag> updateTag(@RequestBody Tag tag) {
        tagService.getById(tag.getId());
        Tag updatedTag = tagService.store(tag);
        return ResponseEntity.status(HttpStatus.OK).body(updatedTag);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Tag> deleteTag(@PathVariable Long id) {
        tagService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.status(HttpStatus.OK).body(tagService.getAll());
    }

    @GetMapping(value = "/get/{id}")
    public ResponseEntity<Tag> getById(@PathVariable Long id) {
        Tag tagById = tagService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(tagById);
    }
}
