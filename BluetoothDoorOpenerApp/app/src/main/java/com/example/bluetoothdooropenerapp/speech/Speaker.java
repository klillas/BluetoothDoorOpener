package com.example.bluetoothdooropenerapp.speech;

import java.util.ArrayList;
import java.util.List;

public class Speaker<T> implements ISpeaker<T> {
    private List<IListener<T>> listeners = new ArrayList<IListener<T>>();

    @Override
    public void AddListener(IListener<T> listener) {
        listeners.add(listener);
    }

    @Override
    public void RemoveListener(IListener<T> listener) {
        listeners.remove(listener);
    }

    protected void Speak(T data)
    {
        for(IListener<T> listener : listeners)
        {
            listener.Listen(data);
        }
    }
}
