package eugene.petsshelter.ui.base;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

/**
 * Created by Eugene on 17.01.2018.
 */

public class BaseFragment<B extends ViewDataBinding, V extends ViewModel> extends Fragment {

    @Inject Class<V> viewModelClass;

    protected B binding;
    protected V viewModel;

    @Inject ViewModelProvider.Factory viewModelFactory;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(viewModelClass);
    }


    protected final View setAndBindContentView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, @LayoutRes int layoutResID) {
        binding = DataBindingUtil.inflate(inflater, layoutResID, container, false);
        return binding.getRoot();
    }

}
