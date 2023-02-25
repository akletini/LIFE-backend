package akletini.life.chore.controller;

import akletini.life.chore.dto.ChoreDto;
import akletini.life.chore.dto.mapper.ChoreMapper;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.service.ChoreService;
import akletini.life.shared.response.HttpResponse;
import akletini.life.todo.dto.TodoDto;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/chores")
public class ChoreController {

    @Autowired
    private ChoreService choreService;

    private final ChoreMapper choreMapper = Mappers.getMapper(ChoreMapper.class);
    @PostMapping("/add")
    public ResponseEntity<ChoreDto> addChore(@RequestBody ChoreDto choreDto) {
        Chore storedChore = choreService.store(choreMapper.dtoToChore(choreDto));
        return ResponseEntity.status(OK).body(choreMapper.choreToDto(storedChore));
    }

    @PutMapping("/complete")
    public ResponseEntity<ChoreDto> completeChore(@RequestBody ChoreDto choreDto) {
        Chore completedChore = choreService.completeChore(choreMapper.dtoToChore(choreDto));
        return ResponseEntity.status(OK).body(choreMapper.choreToDto(completedChore));
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<TodoDto> deleteChore(@RequestBody ChoreDto choreDto) {
        Chore chore = choreMapper.dtoToChore(choreDto);
        choreService.delete(chore);
        return ResponseEntity.status(NO_CONTENT).build();
    }

    @PutMapping(value = "/update")
    public ResponseEntity<ChoreDto> updateTodo(@RequestBody ChoreDto choreDto) {
        Chore chore = choreMapper.dtoToChore(choreDto);
        choreService.getById(chore.getId());
        Chore updatedChore = choreService.store(chore);
        return ResponseEntity.status(HttpStatus.OK).body(choreMapper.choreToDto(updatedChore));
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ChoreDto> getById(@PathVariable Long id) {
        Chore chore = choreService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(choreMapper.choreToDto(chore));
    }

    @GetMapping(value = "/get", params = {"page", "size"})
    public ResponseEntity<HttpResponse> getChores(@RequestParam int page,
                                                  @RequestParam int size) {
        Page<Chore> chores = choreService.getChores(page, size);
        Page<ChoreDto> dtoPage = chores.map(choreMapper::choreToDto);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("page", dtoPage))
                        .message(OK.getReasonPhrase())
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
