package se.jensen.saman.socialnetworkmaven.dto;

public record UserRequestFollowDTO(Long theOneThatWantsToFollowId, Long theOneThatIsBeingFollowedId) {
}
