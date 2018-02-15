package eugene.petsshelter.ui.donation;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DecimalFormat;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Donation;
import eugene.petsshelter.databinding.FragmentSummaryDonationBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.ButtonClickHandler;
import eugene.petsshelter.ui.base.BaseFragment;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummaryDonationFragment extends BaseFragment<FragmentSummaryDonationBinding, SummaryDonationViewModel>
        implements Injectable, ButtonClickHandler {

    private DonationActivityViewModel activityViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setAndBindContentView(inflater,container,savedInstanceState,R.layout.fragment_summary_donation);}


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.setHandler(this);

        activityViewModel = ViewModelProviders.of(getActivity()).get(DonationActivityViewModel.class);

        observeActvityViewModel();
    }

    private void observeActvityViewModel() {

        activityViewModel.getDonation().observe(this, donation -> {
                if(donation!=null) {
                    binding.setDonation(donation);
                    binding.setAmount(formattedAmount(donation.getAmount()/100));
                }
        });
    }

    @Override
    public void onButtonClick(View view) {
        if(view.getId()==binding.donateButton.getId()) {
            Timber.d("onButtonClick donateButton");
            Donation donation = activityViewModel.getDonation().getValue();
            donation.setConfirmed(true);
            activityViewModel.setDonation(donation);
        }
    }

    private String formattedAmount(int amount) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        return decimalFormat.format(amount);
    }

}
