package eugene.petsshelter.ui.main;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        implements Injectable, OnItemClickListener<NewsItem> {

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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(adapter!=null) adapter.stopListening();
    }
}
