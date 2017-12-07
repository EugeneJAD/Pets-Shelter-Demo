package eugene.petsshelter.view.adapter;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import eugene.petsshelter.R;
import eugene.petsshelter.databinding.ShelterListItemBinding;
import eugene.petsshelter.model.api.GlideApp;
import eugene.petsshelter.model.models.Shelter;


public class ShelterRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Shelter> shelters;
    private ItemClickCallback<Shelter> itemClickCallback;

    public ShelterRecyclerAdapter(ItemClickCallback<Shelter> itemClick){
        itemClickCallback = itemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ShelterListItemBinding shelterListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.shelter_list_item,parent,false);
        return new ShelterViewHolder(shelterListItemBinding);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ShelterViewHolder)holder).bind(shelters.get(position));
    }

    @BindingAdapter("bind:imageShelterUrl")
    public static void loadImageIntoShelterListItem(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext()).load(url)
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.RED))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }


    @Override
    public int getItemCount() { return shelters ==null? 0 : shelters.size();}

    public void setResults(List<Shelter> data){
        shelters = data;
    }

    public class ShelterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ShelterListItemBinding binding;

        public ShelterViewHolder(ShelterListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        public void bind(Shelter shelter){
            binding.setShelter(shelter);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View view) {
            itemClickCallback.onItemClick(shelters.get(getAdapterPosition()));
        }
    }
}
