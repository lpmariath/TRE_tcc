package luiz.mariath.lp_ma.tre_tcc.application;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import luiz.mariath.lp_ma.tre_tcc.domain.GlobalTime;

public class GeofenceRegistrationService extends IntentService {

    private static final String TAG = "GeoIntentService";
    public static final String GEOFENCE_ID = "GEO_ID";
    final GlobalTime gt = GlobalTime.getInstance();

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.i("Geofence", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    public GeofenceRegistrationService() {
        super(TAG);
        Log.i("Geofence", "RegistrationServiceSuper");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.i("onHandleIntent: ", "onHandleIntent");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "GeofencingEvent error " + geofencingEvent.getErrorCode());
        } else {
            int transaction = geofencingEvent.getGeofenceTransition();
            if (transaction == Geofence.GEOFENCE_TRANSITION_ENTER) { //&& geofence.getRequestId().equals(GEOFENCE_ID)) {
                Log.d(TAG, "ZONA PROIBIDA");
                Vibrar(1000);
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Zona proibida, desligue o som!!", Toast.LENGTH_LONG).show();

                        gt.setTime(System.currentTimeMillis());
                    }
                });

            } else if (transaction == Geofence.GEOFENCE_TRANSITION_EXIT) {
                Log.d(TAG, "Zona Permitida");
                Vibrar(500);
                Handler mHandler = new Handler(getMainLooper());
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Zona permitida, ligue o som!!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "Saida: " + (System.currentTimeMillis() - gt.getTime()));
                    }
                });

            }
        }
    }

    public void Vibrar(long time) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                v.vibrate(time);
            }
        }
    }
}