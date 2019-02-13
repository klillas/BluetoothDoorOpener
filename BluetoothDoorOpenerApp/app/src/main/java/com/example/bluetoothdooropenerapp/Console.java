package com.example.bluetoothdooropenerapp;

import android.widget.TextView;

public class Console implements IConsole {

    private TextView textView;

    public Console(TextView textView)
    {

        this.textView = textView;
    }

    @Override
    public void WriteLine(String message) {
        CharSequence consoleText = textView.getText();
        if (consoleText.length() > 1000)
        {
            consoleText = consoleText.subSequence(0, 1000);
        }
        textView.setText(String.format("%s\r\n%s", message, consoleText));
    }

    @Override
    public void ClearConsole() {
        textView.setText("");
    }
}
