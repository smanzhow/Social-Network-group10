package se.jensen.saman.socialnetworkmaven.service;

import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.saman.socialnetworkmaven.dto.HabitRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.HabitResponseDTO;
import se.jensen.saman.socialnetworkmaven.mapper.HabitMapper;
import se.jensen.saman.socialnetworkmaven.model.Habit;
import se.jensen.saman.socialnetworkmaven.model.User;
import se.jensen.saman.socialnetworkmaven.repository.HabitRepository;
import se.jensen.saman.socialnetworkmaven.repository.UserRepository;

import java.util.List;

@Service
@Transactional
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitMapper habitMapper;
    private final UserRepository userRepository;


    public HabitService(HabitRepository habitRepository, HabitMapper habitMapper, UserRepository userRepository){
        this.habitRepository = habitRepository;
        this. habitMapper = habitMapper;
        this.userRepository = userRepository;
    }


    public List<HabitResponseDTO> getAllHabits(){
       return habitRepository.findAll()
               .stream()
               .map(habitMapper::fromHabitToRespDTO)
                .toList();
    }

    public HabitResponseDTO getHabitById(Long id) {
       return habitRepository.findById(id)
               .map(habitMapper::fromHabitToRespDTO)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Habit not found."));

    }

    public HabitResponseDTO createHabit(String username, HabitRequestDTO reqDTO){
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(()-> new OpenApiResourceNotFoundException("User not found"));


        Habit habit = habitMapper.reqDTOToHabit(reqDTO);
        habit.setUser(user);

       Habit savedHabit = habitRepository.save(habit);
        return habitMapper.fromHabitToRespDTO(savedHabit);
        //tänk ut något åt enum inmatning, jag vill inte att den är så
        // svår att mata in,
        // kanske 1,2,3 som motsvarar de olika värdena.
    }


    public HabitResponseDTO updateHabit(Long id, HabitRequestDTO reqDTO){
        Habit habit = habitRepository.findById(id)
                .orElseThrow(()-> new OpenApiResourceNotFoundException("Habit not found"));

        habitMapper.updateFromReqDTOToHabit(reqDTO, habit);
        Habit savedHabit = habitRepository.save(habit);
        return habitMapper.fromHabitToRespDTO(savedHabit);

    }

    public void deleteHabit(Long id){
        Habit habit = habitRepository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Habit not found"));

        habitRepository.deleteById(habit.getId());

    }


    //gethabitswithUser.

}
