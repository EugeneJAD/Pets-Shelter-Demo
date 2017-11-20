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


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private Context context;
    private List<Pet> pets;

    private ItemClickCallback itemClickCallback;

    public RecyclerAdapter(ArrayList<Pet> pets, ItemClickCallback itemClick){
        itemClickCallback = itemClick;
        this.pets = pets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.pets_list_item,parent,false);

        return new RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Pet pet = pets.get(position);

        GlideApp.with(context).load(pet.getImageURL())
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.RED))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);

        holder.title.setText(pet.getName());
        holder.shelter.setText(pet.getId());

    }

    @Override
    public int getItemCount() { return pets==null? 0 : pets.size();}

    public void setResults(List<Pet> data){
        pets = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView image;
        public TextView title;
        public TextView shelter;

        public ViewHolder(View itemView) {
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
