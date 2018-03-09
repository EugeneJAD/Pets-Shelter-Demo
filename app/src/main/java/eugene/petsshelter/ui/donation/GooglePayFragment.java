package eugene.petsshelter.ui.donation;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import java.text.DecimalFormat;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Donation;
import eugene.petsshelter.data.models.Status;
import eugene.petsshelter.databinding.FragmentGooglePayBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.ButtonClickHandler;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;
import eugene.petsshelter.utils.SnackbarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class GooglePayFragment extends BaseFragment<FragmentGooglePayBinding,GooglePayViewModel>
        implements Injectable,ButtonClickHandler {

    @Inject AppNavigator navigator;

    public static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 321;

    private DonationActivityViewModel activityViewModel;
    private PaymentsClient paymentsClient;

    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setAndBindContentView(inflater,container,savedInstanceState,R.layout.fragment_google_pay);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setHandler(this);
        binding.setVm(viewModel);

        activityViewModel = ViewModelProviders.of(getActivity()).get(DonationActivityViewModel.class);

        paymentsClient = Wallet.getPaymentsClient(getContext(),
                new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .build());


        connectToGooglePay();

        observeActivityViewModel();
        observeViewModel();
    }

    private void connectToGooglePay() {

        if(viewModel.getIsGooglePayReady().getValue()==null ||
                viewModel.getIsGooglePayReady().getValue().status.equals(Status.ERROR)) {
            if(!isLoading) {
                activityViewModel.startLoading(getString(R.string.set_up_payment_methods));
                viewModel.isReadyToGooglePay(paymentsClient);
            }
        }
    }

    private void observeViewModel() {

        viewModel.getIsGooglePayReady().observe(this, booleanResource -> {
            if(booleanResource!=null) {
                if (booleanResource.status.equals(Status.ERROR)) {
                    SnackbarUtils.showSnackbar(binding.getRoot(), booleanResource.message, SnackbarUtils.TYPE_ERROR);
                } else {
                    if (booleanResource.status.equals(Status.SUCCESS)) {
                        if (booleanResource.data != null) {
                            if (!booleanResource.data) {
                                SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.google_pay_not_available), SnackbarUtils.TYPE_ERROR);
                            }
                        }
                    }
                }
                if (isLoading)
                    activityViewModel.onCompleteLoading();
            }
        });
    }

    private void observeActivityViewModel() {
        activityViewModel.getLoadingState().observe(this, loadingState -> {
            if(loadingState!=null)
                isLoading = loadingState.isRunning();
        });
    }

    private void onAmountSelected() {
        if (binding.gpRadioButton.isChecked()) {viewModel.setAmountInCents(1000);
        } else if (binding.gpRadioButton2.isChecked()) {viewModel.setAmountInCents(2500);
        } else if (binding.gpRadioButton3.isChecked()) {viewModel.setAmountInCents(5000);}
    }

    @Override
    public void onButtonClick(View view) {

        if(view.getId()==binding.gpRadioButton4.getId()) {
            binding.gpRadioGroup.clearCheck();
            if (binding.gpChooseAmountContainer.isCollapsed()) binding.gpChooseAmountContainer.expand();
        } else if(view.getId()==binding.gpDonateButton.getId()){
            onApplyClick();
        } else {
            binding.gpRadioButton4.setChecked(false);
            if(binding.gpChooseAmountContainer.isExpanded()) binding.gpChooseAmountContainer.collapse();
            onAmountSelected();
        }
    }

    private void onApplyClick() {

        if(binding.gpRadioButton4.isChecked()) {
            if (!TextUtils.isEmpty(viewModel.getInputAmount())) {
                double donationInCAD = formatInputToDouble(viewModel.getInputAmount().trim());
                double donationInCents = donationInCAD*100;
                viewModel.setAmountInCents((int)donationInCents);
            } else {viewModel.setAmountInCents(0);}
        }


        if(viewModel.getAmountInCents()==0) {
            SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.choose_amount), SnackbarUtils.TYPE_INFO);
            return;
        } else if(viewModel.getAmountInCents()<100){
            SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.minimum_donation), SnackbarUtils.TYPE_INFO);
            return;
        }

        PaymentDataRequest request = viewModel.createPaymentDataRequest();
        if (request != null) {
            activityViewModel.startLoading(getString(R.string.connecting_to_google_pay));
            activityViewModel.setDonation(getDonationData());
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    getActivity(),
                    LOAD_PAYMENT_DATA_REQUEST_CODE);
        } else {
            SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.unidentified_error), SnackbarUtils.TYPE_ERROR);
        }

    }

    private Donation getDonationData() {
        Donation donation = new Donation();
        donation.setAmount(viewModel.getAmountInCents());
        donation.setConfirmed(false);
        return donation;
    }

    private double formatInputToDouble(String s) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        String formattedStr = decimalFormat.format(Double.valueOf(s));
        return Double.parseDouble(formattedStr);
    }


}
