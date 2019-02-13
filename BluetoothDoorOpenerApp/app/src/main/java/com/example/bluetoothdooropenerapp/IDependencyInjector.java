package com.example.bluetoothdooropenerapp;

public interface IDependencyInjector {
    public void InitializeDependencyInjector(IConsole console);

    IConsole GetConsole();
}
