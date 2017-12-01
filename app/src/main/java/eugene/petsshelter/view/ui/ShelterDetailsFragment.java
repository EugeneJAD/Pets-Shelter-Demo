package eugene.petsshelter.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import eugene.petsshelter.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShelterDetailsFragment extends Fragment implements ButtonClickHandler{

    public static final String TAG = ShelterDetailsFragment.class.getSimpleName();

    private FragmentShelterDetailsBinding detailsBinding;
    private Shelter selectedShelter;

    public ShelterDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        detailsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_shelter_details, container, false);

        MainViewModel viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        viewModel.getSelectedShelter().observe(this, new Observer<Shelter>() {
            @Override
            public void onChanged(@Nullable Shelter shelter) {
                selectedShelter = shelter;
                detailsBinding.setShelter(shelter);
            }
        });

        if(viewModel.getSelectedShelter().getValue()!=null)
            ((MainActivity)getActivity()).setToolbarTitle(viewModel.getSelectedShelter().getValue().getGeoLocation().getCity(),
                    MainActivity.TYPE_FRAGMENT_DETAILS);

        detailsBinding.setHandler(this);

        return detailsBinding.getRoot();
    }

    @Override
    public void onButtonClick(View view) {


        if(view.getId()==detailsBinding.mapButton.getId()){

            if(selectedShelter!=null) {
                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra(MapsActivity.KEY_SHELTER_ID, selectedShelter.getId());
                startActivity(intent);
            }
        }
    }
}
