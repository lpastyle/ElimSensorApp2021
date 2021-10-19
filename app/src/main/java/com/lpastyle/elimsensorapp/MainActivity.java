package com.lpastyle.elimsensorapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lpastyle.elimsensorapp.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, RVOnClick {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    // define sensor sampling period
    private static final int SAMPLING_INTERVAL_US = 1000000; // one second

    private SensorManager sensorManager;
    private Sensor currentSensor;

    private List<Sensor> sensors;

    // Views
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // From Android Studio 3.6 we can use init view binding
        // (the old way of proceeding was setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager != null) {

            // get sensor list content
            sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

            // init recycler view
            binding.sensorRv.setHasFixedSize(true);
            binding.sensorRv.setLayoutManager(new LinearLayoutManager(this));
            binding.sensorRv.setAdapter(new SensorListAdapter(sensors,this));

        }

    }

    @Override
    public void onItemClick(int position) {
        Log.i(LOG_TAG, "Sensor item clicked=" + position);
        unRegisterSensorListener();
        currentSensor = sensors.get(position);
        switch (currentSensor.getType()) {
            // only sensor handled by Android emulator:
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
            case Sensor.TYPE_MAGNETIC_FIELD:
            case Sensor.TYPE_PROXIMITY:
            case Sensor.TYPE_LIGHT:
            case Sensor.TYPE_PRESSURE:
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                Log.i(LOG_TAG, "Handle " + currentSensor.getStringType());
                registerSensorListener();
                break;
            default:
                Toast.makeText(this, R.string.unsupported_sensor_type, Toast.LENGTH_SHORT).show();
                currentSensor = null;
                binding.customSensorView.setAngle(0);
                binding.arrayValuesTv.setText(R.string.no_values);

        }
    }

    private void registerSensorListener() {
        if (sensorManager != null && currentSensor != null)
            sensorManager.registerListener(this, currentSensor, SAMPLING_INTERVAL_US);
    }

    private void unRegisterSensorListener() {
        if (sensorManager != null && currentSensor != null)
            sensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i(LOG_TAG, "onAccuracyChanged(" + accuracy + ")");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Log.i(LOG_TAG, "onSensorChanged()");
        if (event != null) {
            StringBuilder values = new StringBuilder();
            int n = event.values.length;
            for (int i = 0; i < n; i++) {
                values.append("v[").append(i).append("]=").append(event.values[i]).append(((n - i) == 1) ? '.' : ", ");
            }

            binding.customSensorView.setAngle((event.values[0] * 360) / event.sensor.getMaximumRange());
            binding.arrayValuesTv.setText(values.toString());
        }

    }


    // activity life cycle management:
    // unregister sensors listener to save power

    @Override
    protected void onResume() {
        super.onResume();
        registerSensorListener();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterSensorListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterSensorListener();
    }


}
