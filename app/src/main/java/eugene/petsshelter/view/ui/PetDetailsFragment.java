package eugene.petsshelter.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import eugene.petsshelter.R;
import eugene.petsshelter.databinding.FragmentPetDetailsBinding;
import eugene.petsshelter.model.api.GlideApp;
import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.viewmodel.MainViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetDetailsFragment extends Fragment implements ButtonClickHandler {

    public static final String TAG = PetDetailsFragment.class.getSimpleName();

    private MainViewModel viewModel;

    private FragmentPetDetailsBinding detailsBinding;

    public PetDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        detailsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_pet_details, container, false);

        viewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        if(viewModel.getSelectedPet().getValue()!=null)
            ((MainActivity)getActivity()).setToolbarTitle(viewModel.getSelectedPet().getValue().getName(),
                    MainActivity.TYPE_FRAGMENT_DETAILS);

        viewModel.getSelectedPet().observe(this, new Observer<Pet>() {
            @Override
            public void onChanged(@Nullable Pet pet) {
                updateUI(pet);
            }
        });

        detailsBinding.setHandler(this);

        return detailsBinding.getRoot();
    }

    private void updateUI(Pet selectedPet) {

        if(selectedPet==null) {
            return;
        }

        detailsBinding.setSelectedPet(selectedPet);

        GlideApp.with(getContext()).load(selectedPet.getImageURL())
        .centerCrop()
        .placeholder(new ColorDrawable(Color.LTGRAY))
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(detailsBinding.petImageDetails);

    }

    @Override
    public void onButtonClick(View view) {

        if(view.getId()==detailsBinding.buttonBuyFood.getId())
            viewModel.buyFood();
    }


}
