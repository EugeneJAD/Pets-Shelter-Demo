package eugene.petsshelter.ui.adapter;

import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.data.repository.Repository;
import eugene.petsshelter.databinding.PetsListItemBinding;
import eugene.petsshelter.ui.base.DataBoundListAdapter;
import eugene.petsshelter.utils.Objects;


public class PetsRecyclerAdapter extends DataBoundListAdapter<Pet,PetsListItemBinding>{

    private Repository repository;

    private OnItemClickListener<Pet> clickCallback;

    public PetsRecyclerAdapter(OnItemClickListener<Pet> clickCallback, Repository repository) {
        this.clickCallback = clickCallback;
        this.repository = repository;
    }

    @Override
    protected PetsListItemBinding createBinding(ViewGroup parent) {
        PetsListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.pets_list_item,parent,false);
        binding.addFavButton.setOnClickListener(view -> {
            if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                animateFavButton((ImageView) view, binding.getPet());
                clickCallback.onItemClick(binding.getPet(), view);
            }
        });
        binding.getRoot().setOnClickListener(view -> clickCallback.onItemClick(binding.getPet(),view));
        return binding;
    }

    @Override
    protected void bind(PetsListItemBinding binding, Pet item) {
        item.setFavorite(repository.isFavorite(item.getId()));
        binding.setPet(item);
        binding.setIsUserLoggedIn(FirebaseAuth.getInstance().getCurrentUser()!=null);
    }

    @Override
    protected boolean areItemsTheSame(Pet oldItem, Pet newItem) {return Objects.equals(oldItem.getId(),newItem.getId());}

    @Override
    protected boolean areContentsTheSame(Pet oldItem, Pet newItem) {return Objects.equals(oldItem.getName(),newItem.getName());}


    private void animateFavButton(ImageView view, Pet pet){

        ScaleAnimation scaleUpAnimation = new ScaleAnimation(1f,1.2f,1f,1.2f,ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleUpAnimation.setDuration(150);
        scaleUpAnimation.setInterpolator(new DecelerateInterpolator());

        scaleUpAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                if(pet.isFavorite()) {
                    view.setImageResource(R.drawable.ic_favorite_border_24dp);
                    pet.setFavorite(false);
                }else {
                    view.setImageResource(R.drawable.ic_favorite_24dp);
                    pet.setFavorite(true);
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        ScaleAnimation scaleDownAnimation = new ScaleAnimation(1.2f,1f,1.2f,1f,ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleDownAnimation.setInterpolator(new AccelerateInterpolator());
        scaleDownAnimation.setDuration(150);
        scaleDownAnimation.setStartOffset(150);

        AnimationSet set = new AnimationSet(false);
        set.addAnimation(scaleUpAnimation);
        set.addAnimation(scaleDownAnimation);
        view.startAnimation(set);
    }
}
