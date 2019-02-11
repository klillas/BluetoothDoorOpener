package com.example.bluetoothdooropenerapp.speech;

public class CommandParser extends Speaker<EVoiceCommand> implements ICommandParser {

    @Override
    public void Listen(String data) {
        if (IsGarageDoorOpen(data))
        {
            Speak(EVoiceCommand.GARAGE_DOOR_OPEN);
        }

        if (IsGarageDoorClosed(data))
        {
            Speak(EVoiceCommand.GARAGE_DOOR_CLOSE);
        }
    }

    private boolean IsGarageDoorOpen(String words)
    {
        return words.contains("garage") && words.contains("open");
    }

    private boolean IsGarageDoorClosed(String words)
    {
        return words.contains("garage") && words.contains("close");
    }
}
