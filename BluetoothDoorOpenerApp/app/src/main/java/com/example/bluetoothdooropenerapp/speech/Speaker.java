package com.example.bluetoothdooropenerapp.speech;

import java.util.ArrayList;
import java.util.List;

public class Speaker<T> implements ISpeaker<T> {
    private List<IListener<T>> listeners = new ArrayList<IListener<T>>();

    public void AddListener(IListener<T> listener) {
        listeners.add(listener);
    }

    public void RemoveListener(IListener<T> listener) {
        listeners.remove(listener);
    }

    public void Speak(T data)
    {
        for(IListener<T> listener : listeners)
        {
            listener.Listen(data);
        }
    }
}
