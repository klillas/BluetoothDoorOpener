package com.example.bluetoothdooropenerapp;

public class DependencyInjector implements IDependencyInjector {
    private static IDependencyInjector dependencyInjector;
    private IConsole console;

    public static IDependencyInjector GetDependencyInjector() {
        if (dependencyInjector == null)
        {
            dependencyInjector = new DependencyInjector();
        }

        return dependencyInjector;
    }

    public void InitializeDependencyInjector(IConsole console)
    {
        this.console = console;
    }

    private DependencyInjector()
    {

    }


    @Override
    public IConsole GetConsole() {
        return console;
    }
}
