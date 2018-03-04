package eugene.petsshelter.di;

import android.support.v4.app.FragmentActivity;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.ui.adoption.AdoptionActivity;
import eugene.petsshelter.ui.donation.DonationActivity;

/**
 * Created by Eugene on 3/2/2018.
 */

@Module
public class AdoptionActivityModule {

    @Provides
    FragmentActivity provideActivity(AdoptionActivity activity) {return activity;}

}
