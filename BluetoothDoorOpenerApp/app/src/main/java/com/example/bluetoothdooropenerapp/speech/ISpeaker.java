package com.example.bluetoothdooropenerapp.speech;

public interface ISpeaker<T> {
    void AddListener(IListener<T> listener);

    void RemoveListener(IListener<T> listener);
}
