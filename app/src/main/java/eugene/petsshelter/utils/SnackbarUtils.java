package eugene.petsshelter.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import eugene.petsshelter.R;

public class SnackbarUtils {

    public static final int TYPE_INFO = 1;
    public static final int TYPE_SUCCESS = 2;
    public static final int TYPE_ERROR = 3;

    public static void showSnackbar(View root, String snackbarText, int type) {
        if (root == null || snackbarText == null) {
            return;
        }
        Snackbar snackbar = Snackbar.make(root, snackbarText, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();

        int color;
        if(type== TYPE_SUCCESS) color = root.getContext().getResources().getColor(R.color.material_green_600);
        else if(type== TYPE_ERROR) color = root.getContext().getResources().getColor(R.color.material_red_600);
        else color = root.getContext().getResources().getColor(R.color.material_blue_600);

        sbView.setBackgroundColor(color);
        snackbar.show();
    }

}
