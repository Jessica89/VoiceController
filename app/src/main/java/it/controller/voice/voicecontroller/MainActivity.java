package it.controller.voice.voicecontroller;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView resultTEXT;
    //Accessing Google APIs. When you want to make a connection to one of the Google APIs provided
    //in the Google Play services library (such as Google+, Games, or Drive), you need to create an
    // instance of GoogleApiClient ("Google API Client"). The Google API Client provides a common
    // entry point to all the Google Play services and manages the network connection between the
    // user's device and each Google service.
    private GoogleApiClient client;
    private TextToSpeech tts;
    // The following are used for the shake detection
    private SensorManager mySensorManager;
    private Sensor myAccelerometer;
    private ShakeDetector myShakeDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultTEXT = (TextView) findViewById(R.id.TVresult);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //create an instance of GoogleApiClient using the GoogleApiClient.Builder APIs in your
        // activity's onCreate() method.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    if (status != TextToSpeech.ERROR) {
                        Toast.makeText(MainActivity.this, "Initialisation Succesful!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Initialization Failed!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        mySensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        myAccelerometer = mySensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        myShakeDetector = new ShakeDetector();
        myShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake() {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                // when shake the speach recognition starts
                promtSpeachInput();
                // handleShake();

            }
        });

    }

    //shaker lancia l'app
    private void handleShake() {
        // start main activity
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    /////////////////////////shake methods
 @Override
 public void onResume() {
     super.onResume();
     // Add the following line to register the Session Manager Listener onResume
     mySensorManager.registerListener(myShakeDetector, myAccelerometer, SensorManager.SENSOR_DELAY_UI);
 }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mySensorManager.unregisterListener(myShakeDetector);
        super.onPause();
    }

    ///////////////////////////////////////////and shake

    /////////////////////////////////////////////////////////////////////////start speech recognition
    //The on click handler is responsible for firing off the voice intent.
    public void onButtonClick(View v) {
        if (v.getId() == R.id.imageButton) {

            promtSpeachInput();
        }
    }

  public void promtSpeachInput() {
      //ACTION_RECOGNIZE_SPEECH:  Starts an activity that will prompt the user for speech and send it through a speech recognizer.
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
      //EXTRA_LANGUAGE_MODEL: Informs the recognizer which speech model to prefer when performing ACTION_RECOGNIZE_SPEECH
      //LANGUAGE_MODEL_FREE_FORM:  Use a language model based on free-form speech recognition.
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
      //Locale: represents a language/country/variant combination. Locales are used to alter the presentation
      // of information such as numbers or dates to suit the conventions in the region they describe.
      //You can use getDefault() to get an appropriate locale for the user of the device you're
      // running on, or getAvailableLocales() to get a list of all the locales available on the device you're running on.
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
      //EXTRA_PROMPT: Optional text prompt to show to the user when asking them to speak.
      //  i.putExtra(RecognizerIntent.EXTRA_PROMPT,  getString(R.string.speech_prompt));
        try {
            //When we invoke android.speech.RecognizerIntent intent, we must use startActivityForResult() as we must listen back for result text.
            //a second integer parameter identifying the call.
            startActivityForResult(i, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(MainActivity.this, getString(R.string.speech_not_supported), Toast.LENGTH_LONG).show();
        }
    }


    //When the intent calls back, we display the transcribed text.
    public void onActivityResult(int request_code, int result_code, Intent i) {
        super.onActivityResult(request_code, result_code, i);
        /// check if the request code is same as what is passed
        if(request_code == 1) {
                if (result_code == RESULT_OK && i != null) {
                    //// fetch the message
                    // EXTRA_RESULTS: An ArrayList<String> of the recognition results when performing ACTION_RECOGNIZE_SPEECH.
                    ArrayList<String> result = i.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    resultTEXT.setText(result.get(0));
                }

        }
    }
/////////////////////////////////////////////////////////////////////////end speech recognition
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //menu toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_help:
                // User chose the "edit" action, mark the current item
                //   startActivity(Intent);
                //feedback
                Toast.makeText(this, getString(R.string.text_help), Toast.LENGTH_SHORT).show();
                Intent help = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(help);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    /*@Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://it.controller.voice.voicecontroller/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    } */

    /* @Override
   public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://it.controller.voice.voicecontroller/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }*/
}
