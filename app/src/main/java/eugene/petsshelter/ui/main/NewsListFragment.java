package eugene.petsshelter.ui.main;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.NewsItem;
import eugene.petsshelter.databinding.ListFragmentBinding;
import eugene.petsshelter.di.Injectable;
import eugene.petsshelter.ui.adapter.NewsRecyclerAdapter;
import eugene.petsshelter.ui.adapter.OnItemClickListener;
import eugene.petsshelter.ui.base.BaseFragment;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends BaseFragment<ListFragmentBinding,NewsListViewModel>
        implements Injectable, OnItemClickListener<NewsItem>, FirebaseAuth.AuthStateListener {

    private NewsRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return setAndBindContentView(inflater,container,savedInstanceState,R.layout.list_fragment);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((MainActivity) getActivity()).setToolbar(getString(R.string.news_fragment_title), MainActivity.TYPE_FRAGMENT_NEWS_LIST);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,
                true);
        layoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(layoutManager);
        adapter = new NewsRecyclerAdapter(this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(NewsItem item, View view) {

        if(view.getId() == R.id.news_star_button){
            viewModel.doStarTransaction(item.key);
        }

    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {if(adapter!=null) adapter.refresh();}


    @Override
    public void onResume() {
        super.onResume();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter!=null) adapter.stopListening();
    }
}


