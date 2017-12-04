package eugene.petsshelter.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eugene.petsshelter.R;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.view.adapter.ItemClickCallback;
import eugene.petsshelter.view.adapter.PetsRecyclerAdapter;
import eugene.petsshelter.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetsListFragment extends Fragment implements ItemClickCallback<Pet>, Injectable {

    public static final String TAG = PetsListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private PetsRecyclerAdapter adapter;
    private MainViewModel viewModel;

    public PetsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.list_fragment, container, false);

        //RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        adapter = new PetsRecyclerAdapter(new ArrayList<Pet>(), this);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);


        viewModel.getDogs().observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(@Nullable List<Pet> pets) {
                if(viewModel.getPetsListFragmentType().equals(MainActivity.FRAGMENT_LIST_TYPE_DOGS)) {
                    updateAdapter(pets);
                }
            }
        });


        viewModel.getCats().observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(@Nullable List<Pet> pets) {
                if(viewModel.getPetsListFragmentType().equals(MainActivity.FRAGMENT_LIST_TYPE_CATS)) {
                    updateAdapter(pets);
                }
            }
        });


        if(viewModel.getPetsListFragmentType().equals(MainActivity.FRAGMENT_LIST_TYPE_DOGS))
            ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.dogs_fragment_title), MainActivity.TYPE_FRAGMENT_LIST);
        else if (viewModel.getPetsListFragmentType().equals(MainActivity.FRAGMENT_LIST_TYPE_CATS))
            ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.cats_fragment_title), MainActivity.TYPE_FRAGMENT_LIST);


        return rootView;
    }

    private void updateAdapter(List<Pet> data) {
        Log.i(TAG, "updateAdapter");
        adapter.setResults(data);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(Pet pet) {
        viewModel.setSelectedPet(pet);
        ((MainActivity)getActivity()).showPetDetails();
    }


}
