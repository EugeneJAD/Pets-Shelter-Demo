package eugene.petsshelter.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import eugene.petsshelter.R;
import eugene.petsshelter.model.api.GlideApp;
import eugene.petsshelter.model.models.Pet;


public class PetsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Pet> pets;

    private ItemClickCallback<Pet> itemClickCallback;

    public PetsRecyclerAdapter(ArrayList<Pet> pets, ItemClickCallback<Pet> itemClick){
        itemClickCallback = itemClick;
        this.pets = pets;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View petView = LayoutInflater.from(context).inflate(R.layout.pets_list_item,parent,false);
        return new PetViewHolder(petView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        PetViewHolder petViewHolder = (PetViewHolder) holder;

        Pet pet = pets.get(position);

        GlideApp.with(context).load(pet.getImageURL())
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.RED))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(petViewHolder.image);

        petViewHolder.title.setText(pet.getName());
        petViewHolder.shelter.setText(pet.getId());

    }

    @Override
    public int getItemCount() { return pets ==null? 0 : pets.size();}

    public void setResults(List<Pet> data){
        pets = data;
    }

    public class PetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView image;
        public TextView title;
        public TextView shelter;


        public PetViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.pet_image);
            title = itemView.findViewById(R.id.pet_title);
            shelter = itemView.findViewById(R.id.shelter_title);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            itemClickCallback.onItemClick(pets.get(getAdapterPosition()));
        }
    }

}
