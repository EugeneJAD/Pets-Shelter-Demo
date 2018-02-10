package eugene.petsshelter.ui.donation;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import eugene.petsshelter.R;
import eugene.petsshelter.databinding.ActivityDonationBinding;
import eugene.petsshelter.ui.base.BaseActivity;
import eugene.petsshelter.utils.AppConstants;
import eugene.petsshelter.utils.SnackbarUtils;

public class DonationActivity extends BaseActivity<ActivityDonationBinding,DonationActivityViewModel>
        implements HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> injector;

    private Stripe stripe;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindContentView(R.layout.activity_donation);

        if (getCurrentFocus()!= null) {
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        stripe = new Stripe(this, AppConstants.STRIPE_PUBLISHABLE_TEST_KEY);

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getChargeResponse().observe(this, responseMessage -> {
                if(responseMessage!=null) {
                    viewModel.onCompleteLoading();
                    SnackbarUtils.showSnackbar(binding.getRoot(), responseMessage, SnackbarUtils.TYPE_SUCCESS);
                }
        });

        viewModel.getLoadingState().observe(this, loadingState -> {
            if(loadingState!=null){
                binding.progressView.setIsLoading(loadingState.isRunning());
                if(!TextUtils.isEmpty(loadingState.getErrorMessage()))
                    SnackbarUtils.showSnackbar(binding.getRoot(),loadingState.getErrorMessage(),SnackbarUtils.TYPE_ERROR);
            }
        });

    }


    public void onAmountSelected(View view) {

        //set amount in cents
        if (binding.radioButton.isChecked()) {amount = 100;}
        else if (binding.radioButton2.isChecked()) {amount = 200;}
        else if (binding.radioButton3.isChecked()) {amount = 300;}
    }

    public void onApplyClick(View view) {

        if(amount==0){
            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.choose_amount),SnackbarUtils.TYPE_INFO);
            return;
        }

//        Card cardSelected = binding.cardInputWidget.getCard();
//        if (cardSelected == null) {
//            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.invalid_card),SnackbarUtils.TYPE_ERROR);
//            return;
//        }

        Card cardSelected = new Card(
                AppConstants.CANADA_TEST_CARD_NUMBER,
                12,19,
                "123");

        if(cardSelected.validateCard())
            createStripeToken(cardSelected);
        else
            SnackbarUtils.showSnackbar(binding.getRoot(),"card not valid",SnackbarUtils.TYPE_ERROR);
    }

    private void createStripeToken(Card card) {

        viewModel.startLoading(binding.donateButton);

        stripe.createToken(card,AppConstants.STRIPE_PUBLISHABLE_TEST_KEY, new TokenCallback() {
            @Override
            public void onError(Exception error) {viewModel.onFailLoading(error.getLocalizedMessage());}
            @Override
            public void onSuccess(Token token) {chargeToken(token);}
        });

    }

    private void chargeToken(Token token) {

        // Charge the user's card:
        Map<String, Object> fields = new HashMap<>();
        fields.put("amount", amount);
        fields.put("currency", "cad");
        fields.put("description", "Test charge 09/02/18");
        fields.put("stripeToken", token.getId());

        viewModel.chargeDonation(fields);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {return injector;}

}
