package eugene.petsshelter.data.models;

/**
 * Created by Eugene on 10.02.2018.
 */

public class LoadingState {

    private boolean running;
    private String message;
    private String errorMessage;

    public LoadingState(boolean running, String message, String errorMessage) {
        this.running = running;
        this.errorMessage = errorMessage;
        this.message = message;
    }

    public boolean isRunning() {return running;}

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getMessage() {return message;}

}
