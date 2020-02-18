package com.example.bluetoothdooropenerapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bluetoothdooropenerapp.speech.CommandParser;
import com.example.bluetoothdooropenerapp.speech.EControlCommand;
import com.example.bluetoothdooropenerapp.speech.ICommandParser;
import com.example.bluetoothdooropenerapp.speech.ISpeechParser;
import com.example.bluetoothdooropenerapp.speech.SpeechParser;

public class MainActivity extends AppCompatActivity {
    private SpeechRecognizer sr;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.garage_control);
        // Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);

        TextView textView = (TextView) findViewById(R.id.console);

        IConsole console = new Console(textView);
        IDependencyInjector dependencyInjector = DependencyInjector.GetDependencyInjector();
        dependencyInjector.InitializeDependencyInjector(console);

        requestRecordAudioPermission();
        requestBluetoothPermission();

        GarageControl garageControl = new GarageControl(this);

        ICommandParser commandParser = new CommandParser();
        commandParser.AddListener(garageControl);

        //ISpeechParser speechParser = new SpeechParser();
        //speechParser.AddListener(commandParser);

        //sr = SpeechRecognizer.createSpeechRecognizer(this);
        //sr.setRecognitionListener((RecognitionListener)speechParser);


        //garageControl.OpenGarage();

        Button buttonUp = findViewById(R.id.buttonUp);
        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commandParser.Listen(EControlCommand.GARAGE_DOOR_OPEN);
            }
        });

        Button buttonDown = findViewById(R.id.buttonDown);
        buttonDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commandParser.Listen(EControlCommand.GARAGE_DOOR_CLOSE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestRecordAudioPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.RECORD_AUDIO;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }

    private void requestBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String requiredPermission = Manifest.permission.BLUETOOTH;

            // If the user previously denied this permission then show a message explaining why
            // this permission is needed
            if (checkCallingOrSelfPermission(requiredPermission) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(new String[]{requiredPermission}, 101);
            }
        }
    }
}
