package se.jensen.saman.socialnetworkmaven.dto;

import se.jensen.saman.socialnetworkmaven.model.HabitLog;

import java.util.List;

public record HabitResponseDTO(Long id,
                               String habitName,
                               String description,
                               Long userId
                               /*List<HabitLogResp> habitLogList*/) {
}
