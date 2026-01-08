package se.jensen.saman.socialnetworkmaven.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import se.jensen.saman.socialnetworkmaven.dto.HabitRequestDTO;
import se.jensen.saman.socialnetworkmaven.dto.HabitResponseDTO;
import se.jensen.saman.socialnetworkmaven.model.Habit;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime .class)
public interface HabitMapper {

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "habitLogList", ignore = true)
    @Mapping(target = "id", ignore = true)
    Habit reqDTOToHabit(HabitRequestDTO reqDTO);



    @Mapping(source = "user.id", target = "userId")
    HabitResponseDTO fromHabitToRespDTO(Habit habit);


    @Mapping(target = "user", ignore = true)
    @Mapping(target = "habitLogList", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromReqDTOToHabit(HabitRequestDTO reqDTO, @MappingTarget Habit habit);

}
