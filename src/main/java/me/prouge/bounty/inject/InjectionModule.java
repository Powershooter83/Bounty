package me.prouge.bounty.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import me.prouge.bounty.Bounty;

public class InjectionModule extends AbstractModule {
    private final Bounty plugin;

    public InjectionModule(Bounty plugin) {
        this.plugin = plugin;
        Injector injector = this.createInjector();
        injector.injectMembers(this);
    }

    private Injector createInjector() {
        return Guice.createInjector(this);
    }

    @Override
    protected void configure() {
        this.bind(Bounty.class).toInstance(this.plugin);
    }

}
