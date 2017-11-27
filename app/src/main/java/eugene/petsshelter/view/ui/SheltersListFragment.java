package eugene.petsshelter.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import eugene.petsshelter.R;
import eugene.petsshelter.model.models.Shelter;
import eugene.petsshelter.view.adapter.ItemClickCallback;
import eugene.petsshelter.view.adapter.ShelterRecyclerAdapter;
import eugene.petsshelter.viewmodel.SheltersViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SheltersListFragment extends Fragment implements ItemClickCallback<Shelter> {

    private RecyclerView recyclerView;
    private ShelterRecyclerAdapter adapter;
    private SheltersViewModel viewModel;

    public SheltersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_fragment, container, false);

        //RecyclerView
        recyclerView = rootView.findViewById(R.id.recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        adapter = new ShelterRecyclerAdapter(new ArrayList<Shelter>(), this);
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(SheltersViewModel.class);

        viewModel.getShelters().observe(this, new Observer<List<Shelter>>() {
            @Override
            public void onChanged(@Nullable List<Shelter> shelters) {
                updateAdapter(shelters);
            }
        });

        ((MainActivity)getActivity()).setToolbarTitle(getString(R.string.shelters_fragment_title),
                MainActivity.TYPE_FRAGMENT_LIST);

        return rootView;
    }

    private void updateAdapter(List<Shelter> data) {
        adapter.setResults(data);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(Shelter shelter) {

        viewModel.setSelectedShelter(shelter);
        ((MainActivity)getActivity()).showShelterDetails();


    }
}
