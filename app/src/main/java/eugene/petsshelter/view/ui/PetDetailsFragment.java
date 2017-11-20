package eugene.petsshelter.view.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


    public PetDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pet_details, container, false);

        ImageView petImage = root.findViewById(R.id.pet_image_details);
        TextView petName = root.findViewById(R.id.pet_title_detail);

        //TODO description

        TextView ageView = root.findViewById(R.id.age_details);
//        TextView breedView = root.findViewById(R.id.breed_details);
        TextView shelterView = root.findViewById(R.id.shelter_title_details);

        PetsViewModel viewModel = ViewModelProviders.of(getActivity()).get(PetsViewModel.class);

        Pet selectedPet = viewModel.getSelectedPet().getValue();

        GlideApp.with(getContext()).load(selectedPet.getImageURL())
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(petImage);

        petName.setText(selectedPet.getName());
        ageView.setText(selectedPet.getAge());

        //TODO breed

        shelterView.setText(selectedPet.getShelter());

        return root;
    }

}
