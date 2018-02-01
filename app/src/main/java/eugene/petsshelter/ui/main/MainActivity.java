package eugene.petsshelter.ui.main;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import eugene.petsshelter.R;
import eugene.petsshelter.databinding.ActivityMainBinding;
import eugene.petsshelter.ui.base.AppNavigator;
import eugene.petsshelter.ui.base.BaseActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding,MainViewModel>
        implements NavigationView.OnNavigationItemSelectedListener, HasSupportFragmentInjector {


    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Inject AppNavigator navigator;

    public static final int TYPE_FRAGMENT_LIST_DOGS= 1000;
    public static final int TYPE_FRAGMENT_LIST_CATS= 1001;
    public static final int TYPE_FRAGMENT_DETAILS_PET = 1002;
    public static final int TYPE_FRAGMENT_SHELTER = 1003;

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindContentView(R.layout.activity_main);

        if(getSupportActionBar()==null)
            setSupportActionBar(binding.toolbar);

        // Drawer
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState==null){navigator.navigateToDogs();}

        observeViewModel();
    }

    private void observeViewModel() {


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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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

        int id = item.getItemId();

        if (id == R.id.action_settings) return true;
        else if (id == android.R.id.home)navigator.popUpBackStack();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_dogs) navigator.navigateToDogs();
        else if (id == R.id.nav_cats) navigator.navigateToCats();
        else if (id == R.id.nav_shelter) navigator.navigateToShelter();
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
}
