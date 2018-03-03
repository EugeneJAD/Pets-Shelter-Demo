package eugene.petsshelter.ui.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import eugene.petsshelter.R;
import eugene.petsshelter.databinding.FragmentNewsDetailsBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.base.BaseFragment;
import eugene.petsshelter.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailsFragment extends BaseFragment<FragmentNewsDetailsBinding, NewsViewModel>
        implements Injectable, FirebaseAuth.AuthStateListener {

    private String selectedNewsItemId;
    private String toolbarImageUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setAndBindContentView(inflater,container,savedInstanceState,R.layout.fragment_news_details);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments()!=null) {
            if (getArguments().containsKey(AppConstants.NEWS_ITEM_ID_KEY))
                selectedNewsItemId = getArguments().getString(AppConstants.NEWS_ITEM_ID_KEY);
            if(getArguments().containsKey(AppConstants.TOOLBAR_IMAGE_URL_KEY))
                toolbarImageUrl = getArguments().getString(AppConstants.TOOLBAR_IMAGE_URL_KEY);
        }

        ((MainActivity) getActivity()).setToolbar(null, toolbarImageUrl, MainActivity.TYPE_FRAGMENT_NEWS_DETAILS);

        binding.newsDStarButton.setOnClickListener(v -> {
            if(binding.getNewsItem()!=null) {viewModel.doStarTransaction(binding.getNewsItem().key);}
        });

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getSelectedNews().observe(this, newsItem -> {
            if(newsItem!=null){
                if(getUser()!=null){
                    if(newsItem.stars.containsKey(getUser().getUid())) newsItem.isFavorite = true;
                    else newsItem.isFavorite = false;
                } else {newsItem.isFavorite = false;}

                binding.setNewsItem(newsItem);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
        viewModel.startListeningNewsItem(selectedNewsItemId);
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        viewModel.stopListeningNewsItem(selectedNewsItemId);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        binding.setIsUserLoggedIn(firebaseAuth.getCurrentUser()!=null);
    }

    private FirebaseUser getUser(){return FirebaseAuth.getInstance().getCurrentUser(); }
}
