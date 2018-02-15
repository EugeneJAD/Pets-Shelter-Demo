package eugene.petsshelter.ui.donation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.content.res.Resources;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.ApiResponse;
import eugene.petsshelter.data.models.Donation;
import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.ui.base.BaseLoadingViewModel;
import eugene.petsshelter.utils.AbsentLiveData;
import okhttp3.ResponseBody;

/**
 * Created by Eugene on 07.02.2018.
 */

public class DonationActivityViewModel extends BaseLoadingViewModel {


    @Inject Repository repository;

    @Inject Resources resources;

    private LiveData<ApiResponse<ResponseBody>> chargeCardResponse;

    private MutableLiveData<Donation> donation = new MutableLiveData<>();

    @Inject
    public DonationActivityViewModel() {

        chargeCardResponse = Transformations.switchMap(donation, data ->{
           if(data.isConfirmed()) {
               startLoading(resources.getString(R.string.donation_processing));
               return repository.createCharge(getFieldsForCardCharge(data));
           }else {
               return AbsentLiveData.create();
           }
        });
    }

    public LiveData<ApiResponse<ResponseBody>> getChargeCardResponse() {return chargeCardResponse;}

    public LiveData<Donation> getDonation() {return donation;}

    public void setDonation(Donation donation) {this.donation.setValue(donation);}

    private Map<String, Object> getFieldsForCardCharge(Donation donation){

        Map<String, Object> fields = new HashMap<>();
        fields.put("amount", donation.getAmount());
        fields.put("currency", "cad");
        fields.put("description", donation.getDescription());
        fields.put("stripeToken", donation.getStripeTokenId());
        return fields;
    }
}
