package eugene.petsshelter.ui.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.databinding.FragmentPetDetailsBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.ButtonClickHandler;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;
import eugene.petsshelter.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetDetailsFragment extends BaseFragment<FragmentPetDetailsBinding,PetDetailsViewModel>
        implements ButtonClickHandler, Injectable, FirebaseAuth.AuthStateListener {

    @Inject AppNavigator navigator;
    @Inject FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setAndBindContentView(inflater, container, savedInstanceState, R.layout.fragment_pet_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null && getArguments().containsKey(AppConstants.KEY_PET_ID)
                && getArguments().containsKey(AppConstants.KEY_PET_TYPE))
            viewModel.setPetId(getArguments().getString(AppConstants.KEY_PET_ID), getArguments().getString(AppConstants.KEY_PET_TYPE));

        binding.setIsUserLoggedIn(firebaseAuth.getCurrentUser()!=null);
        binding.setHandler(this);

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getPet().observe(this, pet -> {
            if(pet!=null){
                ((MainActivity) getActivity()).setActivityView(pet.getName(), pet.getImageURL(), MainActivity.TYPE_FRAGMENT_DETAILS_PET);
                updateUI(pet);
            }
        });
    }

    private void updateUI(Pet pet) {binding.setPet(pet);}

    @Override
    public void onButtonClick(View view) {

        if(view.getId()==R.id.pet_d_fav_button) {
            Pet pet = binding.getPet();
            viewModel.addOrRemoveFavorite(pet.getId());
            pet.setFavorite(!pet.isFavorite());
            binding.setPet(pet);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.repository.updateRemoteFavoritePets();
        firebaseAuth.removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        binding.setIsUserLoggedIn(firebaseAuth.getCurrentUser()!=null);
    }
}
