package eugene.petsshelter.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Eugene on 07.02.2018.
 */

public class NetworkUtils {

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

}
