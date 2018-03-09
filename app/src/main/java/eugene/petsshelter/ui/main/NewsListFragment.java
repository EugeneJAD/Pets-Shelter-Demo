package eugene.petsshelter.ui.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.NewsItem;
import eugene.petsshelter.data.models.Status;
import eugene.petsshelter.databinding.ListFragmentBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.NewsRecyclerAdapter;
import eugene.petsshelter.ui.adapter.OnItemClickListener;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;
import eugene.petsshelter.utils.SnackbarUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends BaseFragment<ListFragmentBinding,NewsViewModel>
        implements Injectable, OnItemClickListener<NewsItem>, FirebaseAuth.AuthStateListener {

    @Inject AppNavigator navigator;

    @Inject FirebaseAuth firebaseAuth;

    private NewsRecyclerAdapter adapter;
    private LinearLayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setAndBindContentView(inflater,container,savedInstanceState,R.layout.list_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).setActivityView(getString(R.string.news_fragment_title), null, MainActivity.TYPE_FRAGMENT_NEWS_LIST);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsRecyclerAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getNews().observe(this, newsResource -> {
                if(newsResource!=null) {
                    if(newsResource.status.equals(Status.ERROR))
                        SnackbarUtils.showSnackbar(binding.getRoot(),newsResource.message,SnackbarUtils.TYPE_ERROR);
                    adapter.replace(newsResource.data);
                }
        });
    }

    @Override
    public void onItemClick(NewsItem item, View view) {

        if(view.getId() == R.id.news_star_button) {
            viewModel.doStarTransaction(item.key);
        } else {
            navigator.navigateToNewsDetails(item.key, item.imageUrl, item.title);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {if(adapter!=null) adapter.refresh();}

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(this);
        viewModel.startListeningNews();
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(this);
        viewModel.stopListeningNews();
    }

}


