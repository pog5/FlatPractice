package me.pog5.flatpractice;

import me.pog5.flatpractice.objects.Variables;

public class ServiceManager {
    public ServiceManager(FlatPractice plugin) {
        variables = new Variables(plugin);
    }
    private final Variables variables;
}
