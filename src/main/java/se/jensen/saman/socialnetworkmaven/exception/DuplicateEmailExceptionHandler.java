package se.jensen.saman.socialnetworkmaven.exception;

public class DuplicateEmailExceptionHandler extends RuntimeException {

    public DuplicateEmailExceptionHandler(String email) {
        super("Email: "+ email + " is already in use.");
    }
}
