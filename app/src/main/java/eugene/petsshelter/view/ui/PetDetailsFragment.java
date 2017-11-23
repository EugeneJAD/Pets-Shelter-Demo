package eugene.petsshelter.view.ui;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import eugene.petsshelter.R;
import eugene.petsshelter.model.api.GlideApp;
import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.viewmodel.PetsViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class PetDetailsFragment extends Fragment {

    public static final String TAG = PetDetailsFragment.class.getSimpleName();

    ImageView petImage;
    TextView petName;
    TextView ageView;
    TextView shelterView;

    public PetDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i(TAG, "PetDetailsFragment onCreateView");

        View root = inflater.inflate(R.layout.fragment_pet_details, container, false);

        petImage = root.findViewById(R.id.pet_image_details);
        petName = root.findViewById(R.id.pet_title_detail);

        //TODO description

        ageView = root.findViewById(R.id.age_details);
//        TextView breedView = root.findViewById(R.id.breed_details);
        shelterView = root.findViewById(R.id.shelter_title_details);

        PetsViewModel viewModel = ViewModelProviders.of(getActivity()).get(PetsViewModel.class);

        if(viewModel.getSelectedPet().getValue()!=null)
            ((MainActivity)getActivity()).setToolbarTitle(viewModel.getSelectedPet().getValue().getName(),
                    MainActivity.TYPE_FRAGMENT_DETAILS);

        viewModel.getSelectedPet().observe(this, new Observer<Pet>() {
            @Override
            public void onChanged(@Nullable Pet pet) {
                updateUI(pet);
            }
        });

        return root;
    }

    private void updateUI(Pet selectedPet) {

        if(selectedPet==null)
            return;

        GlideApp.with(getContext()).load(selectedPet.getImageURL())
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(petImage);

        petName.setText(selectedPet.getName());
        ageView.setText(selectedPet.getAge());

        //TODO breed

        shelterView.setText(selectedPet.getShelter());

    }

}
