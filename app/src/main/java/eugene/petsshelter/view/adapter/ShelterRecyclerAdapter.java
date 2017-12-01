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
import eugene.petsshelter.model.models.Shelter;


public class ShelterRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<Shelter> shelters;

    private ItemClickCallback<Shelter> itemClickCallback;

    public ShelterRecyclerAdapter(ArrayList<Shelter> shelters, ItemClickCallback<Shelter> itemClick){
        itemClickCallback = itemClick;
        this.shelters = shelters;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();

        View shelterView = LayoutInflater.from(context).inflate(R.layout.shelter_list_item,parent,false);
        return new ShelterViewHolder(shelterView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ShelterViewHolder shelterViewHolder = (ShelterViewHolder)holder;
        Shelter shelter = shelters.get(position);

        GlideApp.with(context).load(shelter.getImageURL())
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.RED))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(shelterViewHolder.image);

        shelterViewHolder.title.setText(shelter.getTitle());
        shelterViewHolder.city.setText(shelter.getGeoLocation().getCity());
        shelterViewHolder.country.setText(shelter.getGeoLocation().getCountry());

    }


    @Override
    public int getItemCount() { return shelters ==null? 0 : shelters.size();}

    public void setResults(List<Shelter> data){
        shelters = data;
    }

    public class ShelterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView image;
        public TextView title;
        public TextView city;
        public TextView country;

        public ShelterViewHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.shelter_image);
            title = itemView.findViewById(R.id.shelter_title);
            city = itemView.findViewById(R.id.shelter_city);
            country = itemView.findViewById(R.id.shelter_country);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            itemClickCallback.onItemClick(shelters.get(getAdapterPosition()));
        }
    }
}
