package ua.bolt;

/**
 * Created by kbelentsov on 22.12.2015.
 */
public class FileProcessingException extends RuntimeException {
    public FileProcessingException(Throwable cause) {
        super(cause);
    }
    public FileProcessingException(String message) {
        super(message);
    }
}
