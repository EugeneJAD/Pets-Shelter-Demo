package eugene.petsshelter.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarUtils {

    public static void showSnackbar(View root, String snackbarText) {
        if (root == null || snackbarText == null) {
            return;
        }
        Snackbar snackbar = Snackbar.make(root, snackbarText, Snackbar.LENGTH_LONG);
        View sbView = snackbar.getView();
//        sbView.setBackgroundColor(root.getContext().getResources().getColor(R.color.yellow_dark));
        snackbar.show();
    }

}
