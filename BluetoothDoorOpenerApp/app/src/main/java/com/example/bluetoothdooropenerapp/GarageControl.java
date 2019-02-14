package com.example.bluetoothdooropenerapp;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.bluetoothdooropenerapp.speech.EVoiceCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

import static android.support.v4.content.ContextCompat.startActivity;

public class GarageControl implements IGarageControl {
    private IConsole console;
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice garageDevice;
    private Context context;
    BluetoothSocket socket;
    OutputStream outStream;
    InputStream inStream;

    public GarageControl(Context context)
    {
        console = DependencyInjector.GetDependencyInjector().GetConsole();
        this.context = context;
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private boolean InitializeGarageConnection() {
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(context, enableBluetooth, Bundle.EMPTY);
        }

        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("GARAGE-DOOR")) {
                    console.WriteLine("GARAGE-DOOR found");
                    garageDevice = device;
                    return true;
                }
            }

            console.WriteLine("Device not found");
            try {
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                console.WriteLine(ex.getMessage());
            }
        }
        return false;
    }

    private boolean InitializeStreams()
    {
        boolean isConnected = false;
        long startTime = System.currentTimeMillis();
        while (!isConnected && System.currentTimeMillis() - startTime < 60000)
        {
            console.WriteLine("Searching for device...");

            try {
                UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
                socket = garageDevice.createRfcommSocketToServiceRecord(uuid);
                socket.connect();
            } catch (IOException ex) {
                console.WriteLine(ex.getMessage());
            }

            isConnected = socket.isConnected();
        }

        if (!socket.isConnected())
        {
            console.WriteLine("Could not connect to device");
            return false;
        }

        try {
            outStream = socket.getOutputStream();
            inStream = socket.getInputStream();
        }
        catch (IOException ex)
        {
            console.WriteLine(ex.getMessage());
        }
        return true;
    }

    private boolean WriteCharacter(char c)
    {
        try {
            outStream.write(c);
        }
        catch (IOException ex)
        {
            return false;
        }

        return true;
    }

    private boolean CloseConnection()
    {
        try
        {
            socket.close();
        }
        catch (IOException ex)
        {
            return false;
        }

        return true;
    }

    public void OpenGarage()
    {
        if (!InitializeGarageConnection())
        {
            return;
        }

        if (!InitializeStreams())
        {
            return;
        }

        console.WriteLine("Sending command open garage");
        WriteCharacter('1');

        CloseConnection();
    }

    public void CloseGarage()
    {
        if (!InitializeGarageConnection())
        {
            return;
        }

        if (!InitializeStreams())
        {
            return;
        }

        console.WriteLine("Sending command close garage");
        WriteCharacter('0');

        CloseConnection();
    }

    @Override
    public void Listen(EVoiceCommand data) {
        switch (data)
        {
            case GARAGE_DOOR_OPEN:
                OpenGarage();
                break;
            case GARAGE_DOOR_CLOSE:
                CloseGarage();
                break;
        }
    }
}
