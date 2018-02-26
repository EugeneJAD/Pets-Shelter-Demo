package eugene.petsshelter.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.databinding.FragmentShelterDetailsBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.ButtonClickHandler;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;
import timber.log.Timber;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShelterDetailsFragment extends BaseFragment<FragmentShelterDetailsBinding,ShelterViewModel>
        implements ButtonClickHandler,Injectable {

    @Inject AppNavigator navigator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setAndBindContentView(inflater, container, savedInstanceState, R.layout.fragment_shelter_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).setToolbar(getString(R.string.shelter_info_title), MainActivity.TYPE_FRAGMENT_SHELTER);

        binding.setHandler(this);

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getShelter().observe(this, shelter -> {
            if(shelter!=null) binding.setShelter(shelter);
        });
    }

    @Override
    public void onButtonClick(View view) {

        int id = view.getId();
        if(id==binding.mapButton.getId()) navigator.navigateToMap();
        else if(id==binding.donateButton.getId()) navigator.navigateToDonation();
    }
}
