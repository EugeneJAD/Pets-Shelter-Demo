package eugene.petsshelter.ui.donation;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.stripe.android.Stripe;
import com.stripe.android.model.Card;

import java.text.DecimalFormat;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Donation;
import eugene.petsshelter.databinding.FragmentCardDonationBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.ButtonClickHandler;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;
import eugene.petsshelter.utils.AppConstants;
import eugene.petsshelter.utils.NetworkUtils;
import eugene.petsshelter.utils.SnackbarUtils;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class CardDonationFragment extends BaseFragment<FragmentCardDonationBinding, CardDonationViewModel>
        implements Injectable, ButtonClickHandler{

    @Inject AppNavigator navigator;

    private Stripe stripe;
    private DonationActivityViewModel activityViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setAndBindContentView(inflater,container,savedInstanceState,R.layout.fragment_card_donation);}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activityViewModel = ViewModelProviders.of(getActivity()).get(DonationActivityViewModel.class);

        binding.setVm(viewModel);
        binding.setHandler(this);

        stripe = new Stripe(getContext(), AppConstants.STRIPE_PUBLISHABLE_TEST_KEY);

        if(binding.radioButton4.isChecked())
            if (binding.chooseAmountContainer.isCollapsed()) binding.chooseAmountContainer.expand();

        if(!NetworkUtils.isConnected(getContext())){
            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.no_internet_connection), SnackbarUtils.TYPE_ERROR);
        }

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getStripeToken().observe(this, tokenId->{
            if(!TextUtils.isEmpty(tokenId)){
                activityViewModel.onCompleteLoading();
                activityViewModel.setDonation(getDonationData());
                navigateToSummary();
            }
        });
    }

    private void navigateToSummary() {
        viewModel.resetStripeToken();
        navigator.navigateToSummury();
    }

    private Donation getDonationData() {
        Donation donation = new Donation();
        donation.setAmount(viewModel.getAmount());
        donation.setCardLastNumbers(viewModel.getSelectedCard().getLast4());
        donation.setStripeTokenId(viewModel.getStripeToken().getValue());
        donation.setConfirmed(false);
        return donation;
    }

    public void onAmountSelected() {

        //set amount in cents
        if (binding.radioButton.isChecked()) {viewModel.setAmount(1000);
        } else if (binding.radioButton2.isChecked()) {viewModel.setAmount(2500);
        } else if (binding.radioButton3.isChecked()) {viewModel.setAmount(5000);}

    }

    public void onApplyClick() {

        if(binding.radioButton4.isChecked()) {
            if (!TextUtils.isEmpty(viewModel.getInputAmount())) {
                double donationInCAD = formatInputToDouble(viewModel.getInputAmount().trim());
                double donationInCents = donationInCAD*100;
                viewModel.setAmount((int)donationInCents);
            } else {viewModel.setAmount(0);}
        }

        if(viewModel.getAmount()==0) {
            SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.choose_amount), SnackbarUtils.TYPE_INFO);
            return;
        } else if(viewModel.getAmount()<100){
            SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.minimum_donation), SnackbarUtils.TYPE_INFO);
            return;
        }

        //TODO use cardInputWidget
//        Card cardSelected = binding.cardInputWidget.getCard();
//        if (cardSelected == null) {
//            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.invalid_card),SnackbarUtils.TYPE_ERROR);
//            return;
//        }

        viewModel.setSelectedCard(new Card(AppConstants.CANADA_TEST_CARD_NUMBER, 12,19, "123"));

        if(viewModel.getSelectedCard().validateCard()) {
            activityViewModel.startLoading(getString(R.string.card_validation));
            viewModel.createStripeToken(stripe);
        } else
            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.card_not_valid),SnackbarUtils.TYPE_ERROR);
    }

    private double formatInputToDouble(String s) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedStr = decimalFormat.format(Double.valueOf(s));
        return Double.parseDouble(formattedStr);
    }

    @Override
    public void onButtonClick(View view) {

        if(view.getId()==binding.radioButton4.getId()) {
            binding.radioGroup.clearCheck();
            if (binding.chooseAmountContainer.isCollapsed()) binding.chooseAmountContainer.expand();
        } else if(view.getId()==binding.donateButton.getId()){
            onApplyClick();
        } else {
            binding.radioButton4.setChecked(false);
            if(binding.chooseAmountContainer.isExpanded()) binding.chooseAmountContainer.collapse();
            onAmountSelected();
        }
    }


}
