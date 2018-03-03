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
import eugene.petsshelter.databinding.ListFragmentBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.NewsRecyclerAdapter;
import eugene.petsshelter.ui.adapter.OnItemClickListener;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseFragment;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends BaseFragment<ListFragmentBinding,NewsViewModel>
        implements Injectable, OnItemClickListener<NewsItem>, FirebaseAuth.AuthStateListener {

    @Inject AppNavigator navigator;
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

        ((MainActivity) getActivity()).setToolbar(getString(R.string.news_fragment_title), null, MainActivity.TYPE_FRAGMENT_NEWS_LIST);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsRecyclerAdapter(this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setNestedScrollingEnabled(false);

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getNews().observe(this, newsItems -> {
                if(newsItems!=null) adapter.replace(newsItems);
        });
    }

    @Override
    public void onItemClick(NewsItem item, View view) {

        if(view.getId() == R.id.news_star_button) {
            viewModel.doStarTransaction(item.key);
        } else {
            navigator.navigateToNewsDetails(item.key, item.imageUrl);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {if(adapter!=null) adapter.refresh();}

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
        viewModel.startListeningNews();
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
        viewModel.stopListeningNews();
    }

}


