package eugene.petsshelter.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.databinding.FragmentPetDetailsBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.ButtonClickHandler;
import eugene.petsshelter.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetDetailsFragment extends BaseFragment<FragmentPetDetailsBinding,PetDetailsViewModel>
        implements ButtonClickHandler, Injectable {

    public static final String KEY_PET_ID = "pet_id";
    public static final String KEY_PET_TYPE = "pet_type";
    public static final String PET_TYPE_CAT = "pet_cat";
    public static final String PET_TYPE_DOG = "pet_dog";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setAndBindContentView(inflater, container, savedInstanceState, R.layout.fragment_pet_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null && getArguments().containsKey(KEY_PET_ID)
                && getArguments().containsKey(KEY_PET_TYPE))
            viewModel.setPetId(getArguments().getString(KEY_PET_ID), getArguments().getString(KEY_PET_TYPE));

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getPet().observe(this, pet -> {
            if(pet!=null){
                ((MainActivity) getActivity()).setToolbar(pet.getName(), pet.getImageURL(), MainActivity.TYPE_FRAGMENT_DETAILS_PET);
                updateUI(pet);
            }
        });
    }

    private void updateUI(Pet pet) {binding.setPet(pet);}

    @Override
    public void onButtonClick(View view) {


    }


}
