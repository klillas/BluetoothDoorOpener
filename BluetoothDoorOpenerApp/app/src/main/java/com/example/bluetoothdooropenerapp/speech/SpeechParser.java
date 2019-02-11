package com.example.bluetoothdooropenerapp.speech;

import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class SpeechParser extends Speaker<String> implements RecognitionListener, ISpeechParser {
    private TextView debugTextView;

    public SpeechParser(TextView debugTextView)
    {
        this.debugTextView = debugTextView;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d("", "Ready for speech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d("", "Beginning of speech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        Log.d("", "End of speech");
    }

    @Override
    public void onError(int error) {
        if(error != 7) {
            Log.d("tag",  "error " +  error);
        }
    }

    @Override
    public void onResults(Bundle results) {
        String str = ""; // Create new empty string
        // Get the results from the speech recognizer
        ArrayList data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        // If there is data
        if(data.size() != 0) {
            // Add all the data to a string
            for (int i = 0; i < data.size(); i++) {
                Log.d("tag", "result " + data.get(i));
                str += data.get(i);
                str += " ";
            }
            // Create a lowercase string
            str = str.toLowerCase();
            // Send the GET request with message
            String message = "message=" + str;
            Log.d("tag", message);
            //new Background_get().execute(message);
            debugTextView.setText(message);

            Speak(str);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.d("tag", "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d("tag", "onEvent " + eventType);
    }
}
