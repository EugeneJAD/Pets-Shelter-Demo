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
import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.view.adapter.ItemClickCallback;
import eugene.petsshelter.view.adapter.RecyclerAdapter;
import eugene.petsshelter.viewmodel.PetsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetsListFragment extends Fragment implements ItemClickCallback {

    public static final String TAG = PetsListFragment.class.getSimpleName();

    private RecyclerView recyclerView;
    private RecyclerAdapter adapter;
    private PetsViewModel viewModel;

    public PetsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "PetsListFragment onCreateView " );

        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.list_fragment, container, false);

        //RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerAdapter(new ArrayList<Pet>(), this);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(PetsViewModel.class);


        viewModel.getDogs().observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(@Nullable List<Pet> pets) {
                Log.i("ListFragment getDogs", "observer");
                if(viewModel.getFragmentType().getValue().equals(MainActivity.FRAGMENT_TYPE_DOGS)) {
                    updateAdapter(pets);

                }
            }
        });


        viewModel.getCats().observe(this, new Observer<List<Pet>>() {
            @Override
            public void onChanged(@Nullable List<Pet> pets) {
                Log.i("ListFragment getCats", "observer");
                if(viewModel.getFragmentType().getValue().equals(MainActivity.FRAGMENT_TYPE_CATS)) {
                    updateAdapter(pets);
                }
            }
        });

        if(viewModel.getFragmentType().getValue().equals(MainActivity.FRAGMENT_TYPE_DOGS))
            ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.dogs_fragment_title), MainActivity.TYPE_FRAGMENT_LIST);
        else if (viewModel.getFragmentType().getValue().equals(MainActivity.FRAGMENT_TYPE_CATS))
            ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.cats_fragment_title), MainActivity.TYPE_FRAGMENT_LIST);


        return rootView;
    }

    public void updateAdapter(List<Pet> pets) {
        adapter.setResults(pets);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(Pet pet) {
        viewModel.setSelectedPet(pet);
        ((MainActivity)getActivity()).showDetails();
    }


}
