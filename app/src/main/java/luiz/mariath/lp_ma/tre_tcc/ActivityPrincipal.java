package luiz.mariath.lp_ma.tre_tcc;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.GpsStatus;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;
import luiz.mariath.lp_ma.tre_tcc.RealmHelper.PreencheBanco;
import luiz.mariath.lp_ma.tre_tcc.application.GeofenceRegistrationService;
import luiz.mariath.lp_ma.tre_tcc.application.GetDataFromUrl;
import luiz.mariath.lp_ma.tre_tcc.application.GetDirections;
import luiz.mariath.lp_ma.tre_tcc.domain.LocalRealm;


public class ActivityPrincipal extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, GpsStatus.Listener {

    private final GoogleMapOptions options = new GoogleMapOptions();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    public static final float raio = 200;

    //Inicio Geofence
    private ArrayList<Geofence> mGeofenceList = new ArrayList<>();
    private ArrayList<LatLng> markerPoints = new ArrayList<>();
    private LatLng destino, origem;
    //Fim Geofence

    //Inicio Rotas
    private PendingIntent pendingIntent;
    private CoordinatorLayout coordinatorLayout;
    private Snackbar snackbar;
    private int PLACE_PICKER_REQUEST = 1;

    //Realm
    private Realm realm;
    private RealmResults<LocalRealm> locais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initGMaps();

        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        Toolbar toolbar = findViewById(R.id.tool_bar);

        buildGoogleApiClient();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            verificaPermissaoLocal();
        }
        setSupportActionBar(toolbar);

        realm = Realm.getDefaultInstance();
        locais = realm.where(LocalRealm.class).findAll();

        if (locais.size() == 0) {
            PreencheBanco pb = new PreencheBanco(realm);
            pb.preencheRealm();
        }
        locais.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<LocalRealm>>() {
            @Override
            public void onChange(@NonNull RealmResults<LocalRealm> localRealms, @NonNull OrderedCollectionChangeSet changeSet) {
                changeSet.getInsertions();
            }
        });

        fab(null, null, 0);
    }

    // Initialize GoogleMaps
    private void initGMaps() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, this)
                .build();
        mGoogleApiClient.connect();
    }

    private void fab(final LatLng origem, final LatLng destino, final int tamanho) {
        FloatingActionButton fab = findViewById(R.id.factionbutton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tamanho > 0) {
                    String url = GetDataFromUrl.getDirectionsUrl(origem, destino);
                    GetDirections getDirections = new GetDirections(ActivityPrincipal.this);
                    getDirections.startGettingDirections(url);
                } else {
                    snackbar = Snackbar.make(coordinatorLayout, "Selecione um local", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            mMap.setMyLocationEnabled(true);
        }

        mMap.getUiSettings().setMapToolbarEnabled(false);
        options.rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false);
        LatLng rildas = new LatLng(-22.527523, -41.945269);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(rildas));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

        adicionaCirculo(mMap);
        cliqueMapa(mMap);
    }

    private void Vibrar() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot((long) 1000, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate((long) 1000);
            }
        }
    }

    private void cliqueMapa(final GoogleMap mMap) {
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng point) {
                adicionaCirculo(mMap);
                markerPoints.add(point);
                mMap.addMarker(new MarkerOptions()
                        .title("Destino")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                        .position(point));
                if (ActivityCompat.checkSelfPermission(ActivityPrincipal.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ActivityPrincipal.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                Vibrar();

                origem = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                destino = markerPoints.get(0);
                fab(origem, destino, markerPoints.size());
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(final LatLng point) {
                if (markerPoints.size() > 0) {
                    adicionaCirculo(mMap);
                    fab(null, null, markerPoints.size());
                }
            }
        });
    }

    private void adicionaCirculo(GoogleMap map) {
        markerPoints.clear();
        map.clear();
        for (int i = 0; i < locais.size(); i++) {
            if (locais.get(i).getNivel_alerta().equals("1")) {
                Circle circle = map.addCircle(new CircleOptions()
                        .center(new LatLng(locais.get(i).getLatitude(), locais.get(i).getLongitude()))
                        .radius(raio)
                        .strokeWidth(4)
                        .strokeColor(Color.argb(55, 255, 0, 0))
                        .fillColor(Color.argb(50, 255, 0, 0))
                );
                map.addMarker(new MarkerOptions()
                        .title(locais.get(i).getNome())
                        .snippet(locais.get(i).getEndereco())
                        .icon(BitmapDescriptorFactory.defaultMarker())
                        .position(circle.getCenter()));
            } else if (locais.get(i).getNivel_alerta().equals("2")) {
                Circle circle = map.addCircle(new CircleOptions()
                        .center(new LatLng(locais.get(i).getLatitude(), locais.get(i).getLongitude()))
                        .radius(raio)
                        .strokeWidth(4)
                        .strokeColor(Color.argb(55, 255, 255, 0))
                        .fillColor(Color.argb(50, 255, 255, 0))
                );
                map.addMarker(new MarkerOptions()
                        .title(locais.get(i).getNome())
                        .snippet(locais.get(i).getEndereco())
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        .position(circle.getCenter()));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.reconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        int response = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (response != ConnectionResult.SUCCESS) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, response, 1).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            mGeofenceList.clear();
            stopGeoFencing();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGeofenceList.clear();
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.informacao: {
                Intent intent = new Intent(this, Informacao.class);
                startActivity(intent);
                break;
            }
            case R.id.adicionar: {
                try {
                    PlacePicker.IntentBuilder intentBuilder =
                            new PlacePicker.IntentBuilder();
                    Intent intent = intentBuilder.build(this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                final Place place = PlacePicker.getPlace(this, data);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Defina este local")
                        .setMessage("É sede dos Poderes Executivos ou Legislativos, sede dos Tribunais Judiciários, estabelecimento militar, casa de saúde ou hospital?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    realm.beginTransaction();
                                    LocalRealm local = realm.createObject(LocalRealm.class, locais.size() + 1);
                                    local.setNome(String.valueOf(place.getName()));
                                    local.setEndereco(String.valueOf(place.getAddress()));
                                    local.setLatitude(place.getLatLng().latitude);
                                    local.setLongitude(place.getLatLng().longitude);
                                    local.setNivel_alerta("1");
                                    realm.commitTransaction();

                                    adicionaCirculo(mMap);
                                    stopGeoFencing();
                                    startGeofencing();
                                } catch (Exception e) {
//                                    Log.i("", "" + e);
                                }
                            }
                        });

                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            realm.beginTransaction();
                            LocalRealm local = realm.createObject(LocalRealm.class, locais.size() + 1);
                            local.setNome(String.valueOf(place.getName()));
                            local.setEndereco(String.valueOf(place.getAddress()));
                            local.setLatitude(place.getLatLng().latitude);
                            local.setLongitude(place.getLatLng().longitude);
                            local.setNivel_alerta("2");
                            realm.commitTransaction();

                            adicionaCirculo(mMap);
                        } catch (Exception e) {
//                            Log.i("", "" + e);
                        }
                    }
                });
                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startGeofencing();
        startLocationMonitor();
    }

    @Override
    public void onConnectionSuspended(int i) {
        stopGeoFencing();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
    }

    private void verificaPermissaoLocal() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }

    //Início Geofence
    private void startLocationMonitor() {
//        Log.i("StartLocationMonitor", "");
        LocationRequest locationRequest = LocationRequest.create()
                .setInterval(2000)
                .setFastestInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
//                    Log.d(TAG, "Location Change Lat Lng " + location.getLatitude() + " " + location.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
                }
            });
        } catch (SecurityException e) {
//            Log.d(TAG, e.getMessage());
        }
    }

    private List<Geofence> getGeofence() {
        try {
//            Log.i("GetGeofence: ", ""+locais.size());

            for (int i = 0; i < locais.size(); i++) {
                Geofence fence = new Geofence.Builder()
                        .setRequestId(GeofenceRegistrationService.GEOFENCE_ID + "" + i)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setCircularRegion(locais.get(i).getLatitude(), locais.get(i).getLongitude(), raio)
                        .setNotificationResponsiveness(1000)
                        .setLoiteringDelay(0)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build();
                mGeofenceList.add(fence);
            }
            return mGeofenceList;
//            return null;
        } catch (Exception e) {
//            Log.i("ERROR parsing input ", "" + e);
            return null;
        }
    }

    private PendingIntent getGeofencePendingIntent() {
//        Log.i("PendingIntent", "");
        if (pendingIntent != null) {
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceRegistrationService.class);
        pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private void startGeofencing() {
//        Log.d(TAG, "Start geofencing monitoring call");
        pendingIntent = getGeofencePendingIntent();
        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER)
                .addGeofences(getGeofence())
                .build();

        if (mGoogleApiClient.isConnected()) {
            try {
                LocationServices.getGeofencingClient(this).addGeofences(geofencingRequest, pendingIntent);
                /*LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, geofencingRequest, pendingIntent).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            Log.d(TAG, "Successfully Geofencing Connected");
                        } else {
                            Log.d(TAG, "Failed to add Geofencing " + status.getStatus());
                        }
                    }
                });*/
            } catch (SecurityException e) {
//                Log.d(TAG, e.getMessage());
            }
        }
    }

    private void stopGeoFencing() {
        pendingIntent = getGeofencePendingIntent();
        LocationServices.GeofencingApi.removeGeofences
                (mGoogleApiClient, pendingIntent)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                    }
                });
    }

    //    Início Rotas
    public void onSuccessfullRouteFetch(final List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        String distance = "";
        String duration = "";

        if (result.size() < 1) {
            Toast.makeText(getBaseContext(), "No Points", Toast.LENGTH_SHORT).show();
            return;
        }

// Traversing through all the routes
        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

// Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

// Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                if (j == 0) { // Get distance from the list
                    distance = point.get("distance");
                    continue;
                } else if (j == 1) { // Get duration from the list
                    duration = point.get("duration");
                    continue;
                }

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

// Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(9);
            lineOptions.color(Color.BLUE);

        }

        snackbar = Snackbar.make(coordinatorLayout, "Distância: " + distance + "   Duração: " + duration, 5000);
        snackbar.show();

        mMap.addPolyline(lineOptions);
    }

    public void onFail() {
    }

    @Override
    public void onGpsStatusChanged(int event) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}