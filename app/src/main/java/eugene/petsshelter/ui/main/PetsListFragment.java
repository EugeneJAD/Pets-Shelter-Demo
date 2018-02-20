package eugene.petsshelter.ui.main;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.databinding.ListFragmentBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.OnItemClickListener;
import eugene.petsshelter.ui.adapter.PetsRecyclerAdapter;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetsListFragment extends BaseFragment<ListFragmentBinding,PetsViewModel> implements OnItemClickListener<Pet>, Injectable {

    @Inject AppNavigator navigator;

    public static final String KEY_LIST_TYPE = "list_type";
    public static final String FRAGMENT_LIST_TYPE_CATS = "cats";
    public static final String FRAGMENT_LIST_TYPE_DOGS = "dogs";

    private PetsRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return setAndBindContentView(inflater, container, savedInstanceState, R.layout.list_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null && getArguments().containsKey(KEY_LIST_TYPE))
            viewModel.setListType(getArguments().getString(KEY_LIST_TYPE,FRAGMENT_LIST_TYPE_DOGS));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setHasFixedSize(true);
        adapter = new PetsRecyclerAdapter(this, viewModel.repository);
        binding.recyclerView.setAdapter(adapter);

        observeViewModel();

        observeActivityViewModel();

    }

    private void observeViewModel() {

        viewModel.getListType().observe(this, type -> {
            if(type!=null) {
                if (type.equals(FRAGMENT_LIST_TYPE_DOGS))
                    ((MainActivity) getActivity()).setToolbar(getString(R.string.dogs_fragment_title), MainActivity.TYPE_FRAGMENT_LIST_DOGS);
                else if (type.equals(FRAGMENT_LIST_TYPE_CATS))
                    ((MainActivity) getActivity()).setToolbar(getString(R.string.cats_fragment_title), MainActivity.TYPE_FRAGMENT_LIST_CATS);
            }
        });

        viewModel.getPets().observe(this,pets -> adapter.replace(pets));
    }

    private void observeActivityViewModel() {

        MainViewModel activityViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        activityViewModel.getFavorites().observe(this, favorites -> adapter.replace(viewModel.getPets().getValue()));
    }

    @Override
    public void onItemClick(Pet pet, View view) {

        if(view.getId()==R.id.add_fav_button){
            viewModel.addOrRemoveFavorite(pet.getId());
        } else {
            if (viewModel.getListType().getValue().equals(FRAGMENT_LIST_TYPE_DOGS))
                navigator.navigateToDogDetails(pet.getId());
            else
                navigator.navigateToCatDetails(pet.getId());
        }
    }

    @Override
    public void onPause() {
        viewModel.repository.updateRemoteFavoritePets();
        super.onPause();
    }
}
