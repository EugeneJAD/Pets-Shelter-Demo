package eugene.petsshelter.di;

import android.support.v4.app.FragmentActivity;

import dagger.Module;
import dagger.Provides;
import eugene.petsshelter.ui.donation.DonationActivity;

/**
 * Created by Eugene on 07.02.2018.
 */

@Module
public class DonationActivityModule {

    @Provides
    FragmentActivity provideActivity(DonationActivity activity) {return activity;}

}
