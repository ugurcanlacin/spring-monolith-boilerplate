package com.monolith.boilerplate.config;

import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Locale;

@Configuration
public class LocaleConfiguration {

    private static final Locale SV_SE_LOCALE = new Locale("sv","SE");
    private static final Locale SV_LOCALE = new Locale("sv");
    private static final Locale EN_EN_LOCALE = new Locale("en","EN");
    private static final Locale EN_LOCALE = new Locale("en");

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver slr = new AcceptHeaderLocaleResolver();
        slr.setDefaultLocale(SV_SE_LOCALE);
        slr.setSupportedLocales(Lists.newArrayList(SV_LOCALE, SV_SE_LOCALE, EN_LOCALE, EN_EN_LOCALE));
        return slr;
    }

}
