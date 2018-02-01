package eugene.petsshelter.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Pet;
import eugene.petsshelter.databinding.PetsListItemBinding;
import eugene.petsshelter.ui.base.DataBoundListAdapter;
import eugene.petsshelter.utils.Objects;


public class PetsRecyclerAdapter extends DataBoundListAdapter<Pet,PetsListItemBinding>{

    private OnItemClickListener<Pet> clickCallback;

    public PetsRecyclerAdapter(OnItemClickListener<Pet> clickCallback) {
        this.clickCallback = clickCallback;
    }

    @Override
    protected PetsListItemBinding createBinding(ViewGroup parent) {
        PetsListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.pets_list_item,parent,false);
        binding.getRoot().setOnClickListener(view -> clickCallback.onItemClick(binding.getPet()));
        return binding;
    }

    @Override
    protected void bind(PetsListItemBinding binding, Pet item) {
        binding.setPet(item);
    }

    @Override
    protected boolean areItemsTheSame(Pet oldItem, Pet newItem) {return Objects.equals(oldItem.getId(),newItem.getId());}

    @Override
    protected boolean areContentsTheSame(Pet oldItem, Pet newItem) {return Objects.equals(oldItem.getName(),newItem.getName());}

    private void viewRotateAnimation(final ImageView view){

        //Property Animation
        ValueAnimator fadeOutAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeOutAnim.setDuration(150);
        fadeOutAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
//                if(favorite)
//                    view.setImageResource(R.drawable.ic_favorite_24dp);
//                else
//                    view.setImageResource(R.drawable.ic_favorite_border_24dp);
//                super.onAnimationEnd(animation);
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
