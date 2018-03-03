package eugene.petsshelter.ui.adoption;

import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import eugene.petsshelter.R;
import eugene.petsshelter.databinding.ActivityAdoptionBinding;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.utils.AppConstants;

public class AdoptionActivity extends AppCompatActivity implements HasSupportFragmentInjector {

    private ActivityAdoptionBinding binding;
    @Inject AppNavigator navigator;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_adoption);

        if(savedInstanceState==null) {
            Bundle args = getIntent().getBundleExtra(AppConstants.EXTRA_BUNDLE_KEY);
            navigator.navigateToAdoptionInfo(args);
        }
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {return dispatchingAndroidInjector;}
}
