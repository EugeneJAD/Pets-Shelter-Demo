package eugene.petsshelter.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import eugene.petsshelter.R;
import eugene.petsshelter.databinding.ActivityMainBinding;
import eugene.petsshelter.databinding.NavHeaderMainBinding;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseActivity;
import eugene.petsshelter.utils.SnackbarUtils;
import timber.log.Timber;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel>
        implements NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector,
        FirebaseAuth.AuthStateListener{


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject AppNavigator navigator;

    public static final int TYPE_FRAGMENT_LIST_DOGS= 1000;
    public static final int TYPE_FRAGMENT_LIST_CATS= 1001;
    public static final int TYPE_FRAGMENT_DETAILS_PET = 1002;
    public static final int TYPE_FRAGMENT_SHELTER = 1003;
    public static final int TYPE_FRAGMENT_NEWS_LIST= 1004;

    private ActionBarDrawerToggle toggle;

    private FirebaseUser user;

    private NavHeaderMainBinding navHeaderBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindContentView(R.layout.activity_main);

        Timber.d("onCreate");

        if(getSupportActionBar()==null)
            setSupportActionBar(binding.toolbar);

        // Drawer
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
        //Bind Navigation Header
        navHeaderBinding = DataBindingUtil.inflate(this.getLayoutInflater(), R.layout.nav_header_main,binding.navView,false);
        binding.navView.addHeaderView(navHeaderBinding.getRoot());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        if(savedInstanceState==null){
            navigator.navigateToDogs();
            user = FirebaseAuth.getInstance().getCurrentUser();
            if(user==null) navigator.navigateToLogin();
            else viewModel.reloadUserData(user.getUid());
        }

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getProfile().observe(this, profile -> navHeaderBinding.setProfile(profile));

        viewModel.getFavorites().observe(this, favorites -> viewModel.repository.updateLocalFavoritePets(favorites));
    }

    private void onSignedInInitialize() {viewModel.reloadUserData(user.getEmail());}

    private void onSignedOutCleanup() {viewModel.reloadUserData(null);}

    private void signOut() {

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            return;

        viewModel.repository.updateRemoteFavoritePets();

        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(task ->{
                    if(task.isSuccessful())
                        SnackbarUtils.showSnackbar(binding.getRoot(), getString(R.string.signed_out), SnackbarUtils.TYPE_SUCCESS);});
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        FirebaseAuth.getInstance().removeAuthStateListener(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else if(getSupportFragmentManager().findFragmentById(R.id.fragment_container)
                instanceof ShelterDetailsFragment ) {
            navigator.navigateToDogs();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) return true;

        switch (item.getItemId()){
            case R.id.action_sign_out:
                signOut();
                break;
            case android.R.id.home:
                navigator.popUpBackStack();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_dogs) navigator.navigateToDogs();
        else if (id == R.id.nav_cats) navigator.navigateToCats();
        else if (id == R.id.nav_shelter) navigator.navigateToShelter();
        else if (id == R.id.nav_news) navigator.navigateToNews();
        else if (id == R.id.nav_tools) {}
        else if (id == R.id.nav_share) {}
        else if (id == R.id.nav_send) {}

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Call from Fragments
    public void setToolbar(String toolbarTitle, int fragmentType) {

        switch (fragmentType) {
            case TYPE_FRAGMENT_LIST_DOGS:
            case TYPE_FRAGMENT_LIST_CATS:
            case TYPE_FRAGMENT_SHELTER:
            case TYPE_FRAGMENT_NEWS_LIST:
                //Show toggle
                toggle.setDrawerIndicatorEnabled(true);
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                break;
            case TYPE_FRAGMENT_DETAILS_PET:
                //Hide toggle and show caret
                toggle.setDrawerIndicatorEnabled(false);
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
        }

        if(getSupportActionBar()!=null)
            getSupportActionBar().setTitle(toolbarTitle);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {return dispatchingAndroidInjector;}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppNavigator.RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.sign_in_successful), SnackbarUtils.TYPE_SUCCESS);
                viewModel.reloadUserData(FirebaseAuth.getInstance().getCurrentUser().getUid());
                return;
            } else {
                if (response == null) {
                    // User pressed back button
                    SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.sign_in_cancelled),SnackbarUtils.TYPE_INFO);
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.no_internet_connection),SnackbarUtils.TYPE_ERROR);
                    return;
                }
                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.sign_in_unknown_error),SnackbarUtils.TYPE_ERROR);
                    return;
                }
            }
            SnackbarUtils.showSnackbar(binding.getRoot(),getString(R.string.sign_in_failed),SnackbarUtils.TYPE_ERROR);
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        user = firebaseAuth.getCurrentUser();
        if (user == null) {onSignedOutCleanup();
        } else {onSignedInInitialize();}
    }
}
