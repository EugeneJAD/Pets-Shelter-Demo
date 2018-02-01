package eugene.petsshelter.ui.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import eugene.petsshelter.R;
import eugene.petsshelter.data.models.GeoLocation;
import eugene.petsshelter.data.models.Shelter;
import eugene.petsshelter.databinding.ActivityMapsBinding;
import eugene.petsshelter.ui.base.BaseActivity;

public class MapsActivity extends BaseActivity<ActivityMapsBinding,MapViewModel>
        implements OnMapReadyCallback, HasSupportFragmentInjector {

    @Inject DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    private GoogleMap mMap;

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAndBindContentView(R.layout.activity_maps);

        setSupportActionBar(binding.toolbarMap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        observeViewModel();
    }

    private void observeViewModel() {

        viewModel.getShelter().observe(this, shelter -> {
            if(shelter!=null){
                getSupportActionBar().setTitle(shelter.getTitle());
                setMapFragment();
            }
        });
    }

    private void setMapFragment(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        binding.progressBar.setVisibility(View.GONE);

        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        Shelter shelter = viewModel.getShelter().getValue();
        GeoLocation geoLocation = shelter.getGeoLocation();

        LatLng latLng = new LatLng(geoLocation.getLat(), geoLocation.getLng());

        mMap.addMarker(new MarkerOptions().position(latLng).title(shelter.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {return dispatchingAndroidInjector;}
}
