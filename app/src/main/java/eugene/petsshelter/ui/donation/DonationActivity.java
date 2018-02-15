package eugene.petsshelter.ui.donation;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import eugene.petsshelter.R;
import eugene.petsshelter.databinding.ActivityDonationBinding;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseActivity;
import eugene.petsshelter.utils.NetworkUtils;
import eugene.petsshelter.utils.SnackbarUtils;

public class DonationActivity extends BaseActivity<ActivityDonationBinding,DonationActivityViewModel>
        implements HasSupportFragmentInjector{

    @Inject AppNavigator navigator;

    @Inject
    DispatchingAndroidInjector<Fragment> injector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindContentView(R.layout.activity_donation);

        if(savedInstanceState==null)
            navigator.navigateToPaymentMethods();

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getChargeCardResponse().observe(this, apiResponse ->{
            if(apiResponse!=null){
                viewModel.onCompleteLoading();
                if(apiResponse.isSuccessful()) {
                    SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.donation_successful), SnackbarUtils.TYPE_SUCCESS);
                } else if(!NetworkUtils.isConnected(this)){
                    SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.no_internet_connection),SnackbarUtils.TYPE_ERROR);
                } else if(apiResponse.code==500){
                    SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.service_unavailable),SnackbarUtils.TYPE_ERROR);
                } else {
                    SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.donation_failed),SnackbarUtils.TYPE_ERROR);
                }
                viewModel.getDonation().getValue().setConfirmed(false);
            }
        });

        viewModel.getLoadingState().observe(this, loadingState -> {
            if(loadingState!=null) {binding.setLoadingState(loadingState);}
        });

        viewModel.getDonation().observe(this,donation -> {
            if(donation!=null) {
                if (!donation.isConfirmed()) {
                    donation.setDonator(getCurrentUser());
                    donation.setDonatory(viewModel.repository.getShelter().getValue().getTitle());
                    donation.setDescription(donation.getDonator()+" donation " + getFormattedCurrentDate());
                }
            }
        });
    }

    private String getFormattedCurrentDate() {
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(currentDate);
    }

    private String getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user!=null? user.getDisplayName() : getString(R.string.anonimus);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {return injector;}

    @Override
    public void onBackPressed() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(binding.donationFragmentContainer.getId());
        if(binding.getLoadingState()!=null) {
            if (binding.getLoadingState().isRunning()){
                if(currentFragment instanceof CardDonationFragment){
                    if(viewModel.getDonation().getValue()!=null && !viewModel.getDonation().getValue().isConfirmed()) {
                        viewModel.onFailLoading(getString(R.string.cancelled));
                        SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.cancelled), SnackbarUtils.TYPE_INFO);
                    }
                } else if(currentFragment instanceof SummaryDonationFragment){
                    return;
                }
            }
        }
        super.onBackPressed();
    }
}
