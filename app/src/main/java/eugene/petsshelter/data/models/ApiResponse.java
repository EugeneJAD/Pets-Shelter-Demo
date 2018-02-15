package eugene.petsshelter.data.models;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;

import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Eugene on 17.01.2018.
 */

public class ApiResponse<T> {

    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final String errorMessage;

    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        errorMessage = error.getMessage();
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if (response.isSuccessful()) {
            body = response.body();
            errorMessage = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    ApiError apiError = new Gson().fromJson(response.errorBody().string(),ApiError.class);
                    message = apiError.getMsg();
                } catch (IOException ignored) {
                    Timber.e(ignored, "error while parsing response");
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            errorMessage = message;
            body = null;
        }
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }
}
