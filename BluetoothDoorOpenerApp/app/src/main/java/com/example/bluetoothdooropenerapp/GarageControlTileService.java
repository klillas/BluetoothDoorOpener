package com.example.bluetoothdooropenerapp;

import android.os.Build;
import android.provider.Settings;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.bluetoothdooropenerapp.speech.CommandParser;
import com.example.bluetoothdooropenerapp.speech.EControlCommand;
import com.example.bluetoothdooropenerapp.speech.ICommandParser;

@RequiresApi(api = Build.VERSION_CODES.N)
public class GarageControlTileService extends TileService {
    ICommandParser commandParser;
    Tile tile;

    public GarageControlTileService()
    {
        GarageControl garageControl = new GarageControl(this);

        commandParser = new CommandParser();
        commandParser.AddListener(garageControl);
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
    }

    @Override
    public void onStartListening() {
        tile = getQsTile();
        if (tile != null)
        {
            tile.setState(Tile.STATE_ACTIVE);
            tile.updateTile();
        }
        super.onStartListening();
    }

    @Override
    public void onClick()
    {
        super.onClick();
        tile.setState(Tile.STATE_UNAVAILABLE);
        tile.updateTile();
        try {
            commandParser.Speak(EControlCommand.GARAGE_DOOR_PUSH);
        }
        catch (Exception e) {
            Toast toast = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            toast.show();
        }
        tile.setState(Tile.STATE_ACTIVE);
        tile.updateTile();
    }
}
