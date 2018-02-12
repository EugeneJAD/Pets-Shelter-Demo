package eugene.petsshelter.ui.donation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.android.model.Token;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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
import timber.log.Timber;

public class DonationActivity extends BaseActivity<ActivityDonationBinding,DonationActivityViewModel>
        implements HasSupportFragmentInjector{

    @Inject
    DispatchingAndroidInjector<Fragment> injector;

    private Stripe stripe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindContentView(R.layout.activity_donation);

        stripe = new Stripe(this, AppConstants.STRIPE_PUBLISHABLE_TEST_KEY);

        if(binding.radioButton4.isChecked() &&  binding.chooseAmountContainer.isCollapsed())
            binding.chooseAmountContainer.expand();

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
        if(view.getId()==binding.radioButton4.getId()){
            binding.radioGroup.clearCheck();
            if(binding.chooseAmountContainer.isCollapsed()) binding.chooseAmountContainer.expand();
        } else {
            binding.radioButton4.setChecked(false);
            if(binding.chooseAmountContainer.isExpanded()) binding.chooseAmountContainer.collapse();
            if (binding.radioButton.isChecked()) {viewModel.setAmount(100);
            } else if (binding.radioButton2.isChecked()) {viewModel.setAmount(200);
            } else if (binding.radioButton3.isChecked()) {viewModel.setAmount(300);}
        }
    }

    public void onApplyClick(View view) {

        if(binding.radioButton4.isChecked()) {
            if (!TextUtils.isEmpty(binding.amountEditText.getText().toString())) {
                double donationInCAD = formatInputToDouble(binding.amountEditText.getText().toString().trim());
                int donationInCents = (int)donationInCAD*100;
                viewModel.setAmount(donationInCents);
            } else {viewModel.setAmount(0);}
        }

        Timber.d("amount = %s", viewModel.getAmount());

        if(viewModel.getAmount()==0) {
            SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.choose_amount), SnackbarUtils.TYPE_INFO);
            return;
        } else if(viewModel.getAmount()<100){
            SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.amount_donation), SnackbarUtils.TYPE_INFO);
            return;
        }

        //TODO use cardInputWidget
//        Card cardSelected = binding.cardInputWidget.getCard();
//        if (cardSelected == null) {
//            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.invalid_card),SnackbarUtils.TYPE_ERROR);
//            return;
//        }

        Card cardSelected = new Card(AppConstants.CANADA_TEST_CARD_NUMBER, 12,19, "123");

        if(cardSelected.validateCard()) {
            createStripeToken(cardSelected);
        } else
            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.card_not_valid),SnackbarUtils.TYPE_ERROR);
    }

    private double formatInputToDouble(String s) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedStr = decimalFormat.format(Double.valueOf(s));
        return Double.parseDouble(formattedStr);
    }

    private void createStripeToken(Card card) {

        viewModel.startLoading(binding.donateButton);

        stripe.createToken(card,AppConstants.STRIPE_PUBLISHABLE_TEST_KEY, new TokenCallback() {
            @Override
            public void onError(Exception error) {viewModel.onFailLoading(getString(R.string.error));}
            @Override
            public void onSuccess(Token token) {chargeToken(token);}
        });

    }

    private void chargeToken(Token token) {

        String description = "Test charge " + getFormattedCurrentDate();

        Map<String, Object> fields = new HashMap<>();
        fields.put("amount", viewModel.getAmount());
        fields.put("currency", "cad");
        fields.put("description", description);
        fields.put("stripeToken", token.getId());

        viewModel.chargeDonation(fields);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {return injector;}

    public String getFormattedCurrentDate() {

        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return dateFormat.format(currentDate);
    }

}
