package eugene.petsshelter.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eugene.petsshelter.R;
import eugene.petsshelter.databinding.FragmentShelterDetailsBinding;
import eugene.petsshelter.model.models.Shelter;
import eugene.petsshelter.viewmodel.SheltersViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShelterDetailsFragment extends Fragment {


    private FragmentShelterDetailsBinding detailsBinding;

    public ShelterDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        detailsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_shelter_details, container, false);

        SheltersViewModel viewModel = ViewModelProviders.of(getActivity()).get(SheltersViewModel.class);

        viewModel.getSelectedShelter().observe(this, new Observer<Shelter>() {
            @Override
            public void onChanged(@Nullable Shelter shelter) {
                detailsBinding.setShelter(shelter);
            }
        });

        if(viewModel.getSelectedShelter().getValue()!=null)
            ((MainActivity)getActivity()).setToolbarTitle(viewModel.getSelectedShelter().getValue().getCity(),
                    MainActivity.TYPE_FRAGMENT_DETAILS);

        return detailsBinding.getRoot();
    }

}
