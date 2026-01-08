package se.jensen.saman.socialnetworkmaven.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.jensen.saman.socialnetworkmaven.dto.HabitRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.HabitResponseDTO;
import se.jensen.saman.socialnetworkmaven.service.HabitService;


import java.util.List;

@RestController
@RequestMapping("/habits")
@CrossOrigin(origins = "http://localhost:5173")
public class HabitController {
    private final static Logger logger = LoggerFactory.getLogger(HabitController.class);
    private final HabitService habitService;


    public HabitController(HabitService habitService){
        this.habitService = habitService;

    }

    @GetMapping
    public ResponseEntity<List<HabitResponseDTO>> getAllHabits(){
        List<HabitResponseDTO> respDTOList = habitService.getAllHabits();
        return ResponseEntity.ok(respDTOList);
    }

    @GetMapping("/{id}")
    ResponseEntity<HabitResponseDTO> getHabitById(@PathVariable Long id){
        HabitResponseDTO respDTO = habitService.getHabitById(id);
        return ResponseEntity.ok(respDTO);

    }

    @PostMapping("/{id}/createhabit")
    public ResponseEntity<HabitResponseDTO> createHabit(@PathVariable Long id, @AuthenticationPrincipal String username, @Valid @RequestBody HabitRequestDTO reqDTO){
        HabitResponseDTO respDTO = habitService.createHabit(username, reqDTO);
        return ResponseEntity.ok(respDTO);
    }

    @PutMapping("/{id}/updatehabit")
    public ResponseEntity<HabitResponseDTO> updateHabit(@PathVariable Long id,@AuthenticationPrincipal String username, @Valid @RequestBody HabitRequestDTO reqDTO){
        HabitResponseDTO respDTO = habitService.updateHabit(id , reqDTO);

       return ResponseEntity.ok(respDTO);
    }

    @DeleteMapping("/{id}/deletehabit")
    public ResponseEntity<Void> deleteHabit(@PathVariable Long id){
        logger.warn("Trying to delete habit with id: {}.", id);
        habitService.deleteHabit(id);
        logger.info("Successfully deleted habit with id: {}", id);
        return ResponseEntity.noContent().build();
    }



}
