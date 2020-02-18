package com.example.bluetoothdooropenerapp.speech;

public class CommandParser extends Speaker<EControlCommand> implements ICommandParser {

    @Override
    public void Listen(EControlCommand command) {
        Speak(command);
    }
}
