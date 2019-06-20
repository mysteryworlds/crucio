package com.mysteryworlds.crucio.spigot.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.mysteryworlds.crucio.api.service.LanguageService;
import com.mysteryworlds.crucio.spigot.service.language.LanguageServiceImpl;

public class CrucioSpigotModule extends AbstractModule {

    @Override
    protected void configure() {

        // Services
        bind(LanguageService.class).to(LanguageServiceImpl.class);

        // Constants
        bindConstant().annotatedWith(Names.named("crucioResourceBundleName")).to("crucio");
    }
}
