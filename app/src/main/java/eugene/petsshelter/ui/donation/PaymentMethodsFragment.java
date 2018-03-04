package eugene.petsshelter.ui.donation;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.databinding.FragmentPaymentMethodsBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.ButtonClickHandler;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;
import eugene.petsshelter.utils.NetworkUtils;
import eugene.petsshelter.utils.SnackbarUtils;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentMethodsFragment extends BaseFragment<FragmentPaymentMethodsBinding, PaymentMethodsViewModel>
        implements Injectable, ButtonClickHandler{

    @Inject AppNavigator navigator;

    private boolean isLoading = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      return setAndBindContentView(inflater,container,savedInstanceState,R.layout.fragment_payment_methods);}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setHandler(this);

        if(!NetworkUtils.isConnected(getContext())){
            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.no_internet_connection), SnackbarUtils.TYPE_ERROR);
        }

        observeActivityViewModel();
    }

    private void observeActivityViewModel() {

        DonationActivityViewModel activityViewModel = ViewModelProviders.of(getActivity()).get(DonationActivityViewModel.class);

        activityViewModel.getLoadingState().observe(this, loadingState -> {
            Timber.d("activityViewModel.getLoadingState().observe");
            if(loadingState!=null)
                isLoading = loadingState.isRunning();
        });
    }

    @Override
    public void onButtonClick(View view) {

        Timber.d("onButtonClick");

        if(isLoading) return;

        if(view.getId()==binding.buttonPayCard.getId()){navigator.navigateToCardDonation();}

    }
}
