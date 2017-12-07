package eugene.petsshelter.view.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import eugene.petsshelter.R;
import eugene.petsshelter.databinding.PetsListItemBinding;
import eugene.petsshelter.model.api.GlideApp;
import eugene.petsshelter.model.models.Pet;
import eugene.petsshelter.view.ui.ButtonClickHandler;


public class PetsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final String TAG = PetsRecyclerAdapter.class.getSimpleName();
    private List<Pet> pets;
    private ItemClickCallback<Pet> itemClickCallback;

    public PetsRecyclerAdapter(ItemClickCallback<Pet> itemClick){
        itemClickCallback = itemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        PetsListItemBinding petsListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.pets_list_item, parent, false);
        return new PetViewHolder(petsListItemBinding);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((PetViewHolder) holder).bind(pets.get(position));
    }

    @BindingAdapter("bind:imagePetUrl")
    public static void loadImageIntoPetListItem(ImageView imageView, String url) {
        GlideApp.with(imageView.getContext()).load(url)
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.RED))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    @Override
    public int getItemCount() { return pets ==null? 0 : pets.size();}

    public void setResults(List<Pet> data){
        pets = data;
    }

    public class PetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ButtonClickHandler{

        private PetsListItemBinding itemBinding;
        private boolean favorite = false;

        public PetViewHolder(PetsListItemBinding itemBinding) {
            super(itemBinding.getRoot());

            this.itemBinding = itemBinding;
            itemBinding.setHandler(this);
            itemBinding.getRoot().setOnClickListener(this);

        }

        public void bind(Pet pet) {

            itemBinding.setPet(pet);
            itemBinding.executePendingBindings();

            //check is favorite?
            itemBinding.addFavButton.setImageResource(R.drawable.ic_favorite_border_24dp);
    }

        @Override
        public void onClick(View view) {
            itemClickCallback.onItemClick(pets.get(getAdapterPosition()));
        }

        @Override
        public void onButtonClick(View view) {

            if(view.getId() == itemBinding.addFavButton.getId()){

                if(favorite) {
                    viewRotateAnimation((ImageView)view);
                    favorite=false;
                } else {
                    viewRotateAnimation((ImageView)view);
                    favorite=true;
                }
                Toast.makeText(view.getContext(), itemBinding.getPet().getName(), Toast.LENGTH_SHORT).show();

            }

        }

        private void viewRotateAnimation(final ImageView view){

            //Property Animation
            ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
            fadeOutAnim.setDuration(150);
            fadeOutAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if(favorite)
                        view.setImageResource(R.drawable.ic_favorite_24dp);
                    else
                        view.setImageResource(R.drawable.ic_favorite_border_24dp);
                    super.onAnimationEnd(animation);
                }
            });
            ValueAnimator fadeInAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeInAnim.setDuration(150);

            AnimatorSet animatorFadeSet = new AnimatorSet();
            animatorFadeSet.play(fadeOutAnim).before(fadeInAnim);

            ValueAnimator rotationAnim = ObjectAnimator.ofFloat(view,"rotation",0,360);
            rotationAnim.setDuration(300);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(rotationAnim,animatorFadeSet);
            animatorSet.start();

        }


    }

}
