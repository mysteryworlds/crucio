package com.mysteryworlds.crucio.spigot;

import com.google.inject.Binder;
import com.mysteryworlds.crucio.spigot.module.CrucioSpigotModule;
import de.d3adspace.isabelle.spgiot.theresa.IsabelleSpigotExtension;
import de.d3adspace.theresa.lifecycle.annotation.WarmUp;

import javax.annotation.PreDestroy;

public class CrucioSpigotPlugin extends IsabelleSpigotExtension {

    @WarmUp
    public void onStart() {


    }

    @PreDestroy
    public void onStop() {


    }

    @Override
    public void configure(Binder binder) {

        binder.install(new CrucioSpigotModule());
    }
}
