package eugene.petsshelter.ui.donation;

import android.arch.lifecycle.LiveData;

import java.util.Map;

import javax.inject.Inject;

import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.ui.base.BaseLoadingViewModel;

/**
 * Created by Eugene on 07.02.2018.
 */

public class DonationActivityViewModel extends BaseLoadingViewModel {

    @Inject Repository repository;

    @Inject
    public DonationActivityViewModel() {}

    public void chargeDonation(Map<String,Object> fields){repository.createCharge(fields);}

    public LiveData<String> getChargeResponse(){return repository.getStripeChargeResponse();}

}
