package eugene.petsshelter.ui.donation;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.stripe.android.model.Token;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Donation;
import eugene.petsshelter.databinding.ActivityDonationBinding;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseActivity;
import eugene.petsshelter.utils.DateUtils;
import eugene.petsshelter.utils.NetworkUtils;
import eugene.petsshelter.utils.SnackbarUtils;
import timber.log.Timber;

public class DonationActivity extends BaseActivity<ActivityDonationBinding,DonationActivityViewModel>
        implements HasSupportFragmentInjector{

    @Inject AppNavigator navigator;
    @Inject FirebaseAuth firebaseAuth;
    @Inject DispatchingAndroidInjector<Fragment> injector;

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
                    SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.unidentified_error),SnackbarUtils.TYPE_ERROR);
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
                    donation.setDescription(donation.getDonator()+" donation " + DateUtils.getFormattedCurrentDate());
                }
            }
        });
    }

    private String getCurrentUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        return user!=null? user.getDisplayName() : getString(R.string.anonymous);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {return injector;}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(viewModel.isLoading())
            viewModel.onCompleteLoading();

        switch (requestCode) {
            case GooglePayFragment.LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        if(paymentData!=null) {
                            // This is the raw JSON string version of Stripe token.
                            String rawToken = paymentData.getPaymentMethodToken().getToken();
                            // Stripe token object
                            Token stripeToken = Token.fromString(rawToken);
                            if (stripeToken != null) {
                                Donation currentDonation = viewModel.getDonation().getValue();
                                //the user's card last 4 digits
                                currentDonation.setCardLastNumbers(paymentData.getCardInfo().getCardDetails());
                                currentDonation.setStripeTokenId(stripeToken.getId());
                                viewModel.setDonation(currentDonation);
                                navigator.navigateToSummary();
                            }
                        }
                        break;
                    case RESULT_CANCELED:
                        SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.cancelled), SnackbarUtils.TYPE_INFO);
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.donation_failed),SnackbarUtils.TYPE_ERROR);
                        break;
                    default:
                        // Do nothing.
                }
                break;
                // Handle any other startActivityForResult calls.
            default:
                // Do nothing.
        }

    }

    @Override
    public void onBackPressed() {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(binding.donationFragmentContainer.getId());
        if(binding.getLoadingState()!=null) {
            if (binding.getLoadingState().isRunning()){
                if(currentFragment instanceof CardDonationFragment){
                        viewModel.onFailLoading(getString(R.string.cancelled));
                        SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.cancelled), SnackbarUtils.TYPE_INFO);
                } else if(currentFragment instanceof SummaryDonationFragment){
                    return;
                }
            }
        }
        super.onBackPressed();
    }
}
