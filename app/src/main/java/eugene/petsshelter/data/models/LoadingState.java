package eugene.petsshelter.data.models;

/**
 * Created by Eugene on 10.02.2018.
 */

public class LoadingState {

    private final boolean running;
    private final String errorMessage;

    public LoadingState(boolean running, String errorMessage) {
        this.running = running;
        this.errorMessage = errorMessage;
    }

    public boolean isRunning() {return running;}

    public String getErrorMessage() {
        return errorMessage;
    }

}
