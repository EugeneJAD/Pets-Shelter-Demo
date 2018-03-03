package eugene.petsshelter.ui.adoption;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.databinding.FragmentAdoptionBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdoptionInfoFragment extends BaseFragment<FragmentAdoptionBinding,AdoptionInfoViewModel>
        implements Injectable {

    @Inject AppNavigator navigator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setAndBindContentView(inflater,container,savedInstanceState,R.layout.fragment_adoption);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO getArguments() pet_id

        binding.adoptionScrollView.setOnScrollChangeListener(
                (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY > oldScrollY) binding.adoptionFab.hide();
                    else binding.adoptionFab.show();
        });

        binding.adoptionFab.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser()==null)
                navigator.navigateToLogin();
            //TODO navigate to form
        });

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getAdoptionInfo().observe(this, adoptionInfo -> binding.setAdoptionInfo(adoptionInfo));
    }
}
