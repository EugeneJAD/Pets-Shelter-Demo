package eugene.petsshelter.ui.splash;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import eugene.petsshelter.R;
import eugene.petsshelter.databinding.ActivitySplashBinding;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseActivity;
import eugene.petsshelter.utils.NetworkUtils;

public class SplashActivity extends BaseActivity<ActivitySplashBinding,SplashActivityViewModel> implements HasSupportFragmentInjector {

    @Inject DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject AppNavigator navigator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindContentView(R.layout.activity_splash);

        if(savedInstanceState==null) {
            animateLogo();
        } else {
            showProgress();
        }
    }

    private void observeViewModel() {

        if(!NetworkUtils.isConnected(this)) {
            navigateToMain();
        } else {
            viewModel.getNews().observe(this, newsResource -> {
                if (newsResource != null) {
                    navigateToMain();
                }
            });
        }
    }

    private void animateLogo() {

        Animation logoAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_logo_anim);
        logoAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                showProgress();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        binding.splashLogo.startAnimation(logoAnimation);
    }

    private void showProgress() {

        Animation rotationAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_progress_rotation);
        binding.splashProgress.setVisibility(View.VISIBLE);
        binding.splashProgress.startAnimation(rotationAnimation);

        Animation updateMessageAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_message_slide_up_anim);
        updateMessageAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {observeViewModel();}
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        binding.splashTextUpdate.setVisibility(View.VISIBLE);
        binding.splashTextUpdate.startAnimation(updateMessageAnimation);
    }

    private void navigateToMain() {

        Animation updateMessageAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_message_slide_down_anim);
        updateMessageAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                navigator.navigateToMain();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        binding.splashTextUpdate.startAnimation(updateMessageAnimation);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {return dispatchingAndroidInjector;}
}
