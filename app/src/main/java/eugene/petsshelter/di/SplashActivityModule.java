package eugene.petsshelter.di;

import android.support.v4.app.FragmentActivity;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.ui.splash.SplashActivity;

/**
 * Created by Eugene on 3/3/2018.
 */

@Module
public class SplashActivityModule {

    @Provides
    FragmentActivity provideActivity(SplashActivity activity) {return activity;}

}
