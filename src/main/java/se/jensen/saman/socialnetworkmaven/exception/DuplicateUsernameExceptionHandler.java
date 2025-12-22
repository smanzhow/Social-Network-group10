package se.jensen.saman.socialnetworkmaven.exception;

public class DuplicateUsernameExceptionHandler extends RuntimeException{

    public DuplicateUsernameExceptionHandler(String username) {
        super("Username '" + username + "' is already taken.");
    }

}

