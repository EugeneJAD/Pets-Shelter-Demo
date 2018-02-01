package eugene.petsshelter.ui.base;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

/**
 * Created by Eugene on 17.01.2018.
 */

public abstract class BaseActivity<B extends ViewDataBinding, V extends ViewModel> extends AppCompatActivity {

    @Inject
    Class<V> viewModelClass;

    protected B binding;
    protected V viewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    protected final void setAndBindContentView(@LayoutRes int layoutResID) {
        binding = DataBindingUtil.setContentView(this, layoutResID);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(viewModelClass);
    }
}
