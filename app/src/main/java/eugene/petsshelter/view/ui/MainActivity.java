package eugene.petsshelter.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import eugene.petsshelter.R;
import eugene.petsshelter.viewmodel.PetsViewModel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    public static final int TYPE_FRAGMENT_LIST = 1000;
    public static final int TYPE_FRAGMENT_DETAILS = 1001;

    //pets list fragment types
    public static final String FRAGMENT_LIST_TYPE_CATS = "cats";
    public static final String FRAGMENT_LIST_TYPE_DOGS = "dogs";
    public static final String FRAGMENT_LIST_TYPE_FAV = "favorites";

    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;

    private PetsListFragment dogsListFragment;
    private PetsListFragment catsListFragment;
    private PetDetailsFragment petDetailsFragment;
    private SheltersListFragment sheltersListFragment;
    private ShelterDetailsFragment shelterDetailsFragment;

    private PetsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        viewModel = ViewModelProviders.of(this).get(PetsViewModel.class);

        if(savedInstanceState==null){

            //initial fragment

            dogsListFragment = new PetsListFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, dogsListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        }
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


        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home){
            getSupportFragmentManager().popBackStack();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_dogs) {

            viewModel.setPetsListFragmentType(FRAGMENT_LIST_TYPE_DOGS);

            if (dogsListFragment == null)
                dogsListFragment = new PetsListFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, dogsListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();



        } else if (id == R.id.nav_cats) {

            viewModel.setPetsListFragmentType(FRAGMENT_LIST_TYPE_CATS);

            if (catsListFragment == null)
                catsListFragment = new PetsListFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, catsListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();

        } else if (id == R.id.nav_shelters) {

            if(sheltersListFragment==null)
                sheltersListFragment = new SheltersListFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, sheltersListFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();


        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void showPetDetails(){

        if(petDetailsFragment == null)
            petDetailsFragment = new PetDetailsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, petDetailsFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null).commit();

    }

    public void showShelterDetails(){

        if(shelterDetailsFragment == null)
            shelterDetailsFragment = new ShelterDetailsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, shelterDetailsFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(null).commit();

    }

    public void setToolbarTitle(String toolbarTitle, int fragmentType) {

        switch (fragmentType) {
            case TYPE_FRAGMENT_LIST:
                //Show toggle
                toggle.setDrawerIndicatorEnabled(true);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                break;
            case TYPE_FRAGMENT_DETAILS:
                //Hide caret and show toggle
                toggle.setDrawerIndicatorEnabled(false);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
        }

        getSupportActionBar().setTitle(toolbarTitle);
    }

}
