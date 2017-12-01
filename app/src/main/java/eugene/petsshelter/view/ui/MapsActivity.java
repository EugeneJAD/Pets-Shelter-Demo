package eugene.petsshelter.view.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import eugene.petsshelter.R;
import eugene.petsshelter.model.models.Shelter;
import eugene.petsshelter.viewmodel.MapViewModel;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String KEY_SHELTER_ID = "shelter_id";

    private GoogleMap mMap;
    private Shelter shelter;

    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        MapViewModel viewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        shelter = viewModel.getSelectedShelter(getIntent().getExtras().getString(KEY_SHELTER_ID));

        if(shelter!=null)
            getSupportActionBar().setTitle(shelter.getTitle());
        else
            getSupportActionBar().setTitle(getString(R.string.title_activity_maps));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        LatLng latLng;
        if(shelter==null)
            latLng = mDefaultLocation;
        else
            latLng = new LatLng(shelter.getGeoLocation().getLat(), shelter.getGeoLocation().getLng());

        mMap.addMarker(new MarkerOptions().position(latLng).title(shelter.getTitle()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,DEFAULT_ZOOM));

    }
}
