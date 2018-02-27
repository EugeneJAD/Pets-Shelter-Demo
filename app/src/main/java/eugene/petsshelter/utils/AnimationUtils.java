package eugene.petsshelter.utils;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Pet;

/**
 * Created by Eugene on 2/24/2018.
 */

public class AnimationUtils {

    public static void animateFavButton(ImageView view, Boolean favorite){

        ScaleAnimation scaleUpAnimation = new ScaleAnimation(1f,1.1f,1f,1.1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleUpAnimation.setDuration(150);
        scaleUpAnimation.setInterpolator(new DecelerateInterpolator());

        scaleUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                if(favorite) {
                    view.setImageResource(R.drawable.ic_favorite_border_24dp);
                }else {
                    view.setImageResource(R.drawable.ic_favorite_24dp);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        ScaleAnimation scaleDownAnimation = new ScaleAnimation(1.1f,1f,1.1f,1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleDownAnimation.setInterpolator(new AccelerateInterpolator());
        scaleDownAnimation.setDuration(150);
        scaleDownAnimation.setStartOffset(150);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(scaleUpAnimation);
        set.addAnimation(scaleDownAnimation);
        view.startAnimation(set);
    }
}
