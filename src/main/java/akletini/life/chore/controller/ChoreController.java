package akletini.life.chore.controller;

import akletini.life.chore.repository.api.ChoreRepository;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.chore.service.ChoreService;
import akletini.life.shared.response.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/chores")
public class ChoreController {

    @Autowired
    private ChoreService choreService;
    @Autowired
    private ChoreRepository choreRepository;

    @GetMapping("/createData/{count}")
    public void createData(@PathVariable int count) {
        for (int i = 0; i < count; i++) {
            Chore chore = new Chore();
            chore.setTitle(String.valueOf((char)('a' + i)));
            choreRepository.save(chore);
        }
    }

    @GetMapping(value = "/get", params = {"page", "size"})
    public ResponseEntity<HttpResponse> getChores(@RequestParam int page,
                                                  @RequestParam int size) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("page", choreService.getChores(page,size)))
                        .message(OK.getReasonPhrase())
                        .status(OK)
                        .statusCode(OK.value())
                        .build());
    }
}
