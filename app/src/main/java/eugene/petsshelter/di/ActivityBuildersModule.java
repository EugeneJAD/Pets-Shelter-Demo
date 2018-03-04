package eugene.petsshelter.di;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import eugene.petsshelter.ui.adoption.AdoptionActivity;
import eugene.petsshelter.ui.donation.DonationActivity;
import eugene.petsshelter.ui.main.MainActivity;
import eugene.petsshelter.ui.map.MapsActivity;
import eugene.petsshelter.ui.splash.SplashActivity;

/**
 * Created by Eugene on 17.01.2018.
 */

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(modules = {MainActivityModule.class, FragmentBuildersModule.class})
    abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = {MapActivityModule.class, FragmentBuildersModule.class})
    abstract MapsActivity contributeMapsActivity();

    @ContributesAndroidInjector(modules = {DonationActivityModule.class, FragmentBuildersModule.class})
    abstract DonationActivity contributeDonationActivity();

    @ContributesAndroidInjector(modules = {AdoptionActivityModule.class, FragmentBuildersModule.class})
    abstract AdoptionActivity contributeAdoptionActivity();

    @ContributesAndroidInjector(modules = {SplashActivityModule.class, FragmentBuildersModule.class})
    abstract SplashActivity contributeSplashActivity();

}

