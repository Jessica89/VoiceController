package it.controller.voice.voicecontroller;

/**
 * Created by Jessica on 30/01/2016.
 */
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetector extends Activity implements SensorEventListener {

    private static final float SHAKE_THRESHOLD = 7;
    private static final int SHAKE_TIME_MS = 200;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener myShakeListener;
    private long myShakeTimestamp ;
    private long myCurrentTimeMs = System.currentTimeMillis();
    private int myShakeCount;


    private float lastAcc=SensorManager.GRAVITY_EARTH;
    private  float acceleration=SensorManager.GRAVITY_EARTH;
 private float totAcc = 0.0f;

    public void setOnShakeListener(OnShakeListener listener) {
        this.myShakeListener = listener;
    }

    public interface OnShakeListener {
         void onShake();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (myShakeListener != null) {
            //The X axis refers to the screen's horizontal axis (the small edge in portrait mode, the long edge in landscape mode) and points to the right.
            //The Y axis refers to the screen's vertical axis and points towards the top of the screen (the origin is in the lower-left corner).
            //The Z axis points toward the sky when the device is lying on its back on a table.
            /*All values are angles in degrees.
            values[0]: Azimuth, rotation around the Z axis (0<=azimuth<360). 0 = North, 90 = East, 180 = South, 270 = West
            values[1]: Pitch, rotation around X axis (-180<=pitch<=180), with positive values when the z-axis moves toward the y-axis.
            values[2]: Roll, rotation around Y axis (-90<=roll<=90), with positive values when the z-axis moves toward the x-axis.
            All values are in SI units (m/s^2) and measure contact forces.
            values[0]: force applied by the device on the x-axis
            values[1]: force applied by the device on the y-axis
            values[2]: force applied by the device on the z-axis
            Examples:
            When the device is pushed on its left side toward the right, the x acceleration value is negative (the device applies a reaction force to the push toward the left)
            When the device lies flat on a table, the acceleration value is -STANDARD_GRAVITY, which correspond to the force the device applies on the table in reaction to gravity.
            SENSOR_MAGNETIC_FIELD:*/

            //get x,y,z values
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            totAcc = (x*x+y*y+z*z)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH);
           myShakeTimestamp = event.timestamp;
            if (totAcc>SHAKE_THRESHOLD){
                if(myShakeTimestamp -myCurrentTimeMs < SHAKE_TIME_MS ){
                    return;
                }

               myCurrentTimeMs = myShakeTimestamp ;
                myShakeListener.onShake();
            }





        }
    }
}
