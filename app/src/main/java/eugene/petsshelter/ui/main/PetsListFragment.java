package eugene.petsshelter.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.databinding.ListFragmentBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.OnItemClickListener;
import eugene.petsshelter.ui.adapter.PetsRecyclerAdapter;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;
import eugene.petsshelter.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetsListFragment extends BaseFragment<ListFragmentBinding,PetsViewModel>
        implements OnItemClickListener<Pet>, Injectable, FirebaseAuth.AuthStateListener {

    @Inject AppNavigator navigator;

    private PetsRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setAndBindContentView(inflater, container, savedInstanceState, R.layout.list_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null && getArguments().containsKey(AppConstants.KEY_LIST_TYPE))
            viewModel.setListType(getArguments().getString(AppConstants.KEY_LIST_TYPE,AppConstants.FRAGMENT_LIST_TYPE_DOGS));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);
        adapter = new PetsRecyclerAdapter(this, viewModel.repository);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false);

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getListType().observe(this, type -> {
            if(type!=null) {
                if (type.equals(AppConstants.FRAGMENT_LIST_TYPE_DOGS)) {
                    ((MainActivity) getActivity()).setActivityView(getString(R.string.dogs_fragment_title), null, MainActivity.TYPE_FRAGMENT_LIST_DOGS);
                } else if (type.equals(AppConstants.FRAGMENT_LIST_TYPE_CATS)) {
                    ((MainActivity) getActivity()).setActivityView(getString(R.string.cats_fragment_title), null, MainActivity.TYPE_FRAGMENT_LIST_CATS);
                }
            }
        });

        viewModel.getPets().observe(this,pets -> adapter.replace(pets));

        viewModel.getFavorites().observe(this, favorites -> adapter.replace(viewModel.getPets().getValue()));
    }

    @Override
    public void onItemClick(Pet pet, View view) {

        if(view.getId()==R.id.add_fav_button){
            viewModel.addOrRemoveFavorite(pet.getId());
            pet.setFavorite(!pet.isFavorite());
        } else if(view.getId()==R.id.pet_item_donate_button) {
            navigator.navigateToDonation();
        } else if(view.getId()==R.id.pet_item_adopt_button){
            Bundle args = new Bundle();
            args.putString(AppConstants.KEY_PET_ID, pet.getId());
            navigator.navigateToAdoption(args);
        } else {
            if (pet.getPetType().equals(AppConstants.PET_TYPE_DOG))
                navigator.navigateToDogDetails(pet.getId());
            else
                navigator.navigateToCatDetails(pet.getId());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        viewModel.repository.updateRemoteFavoritePets();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {if(adapter!=null) adapter.refresh();}
}
