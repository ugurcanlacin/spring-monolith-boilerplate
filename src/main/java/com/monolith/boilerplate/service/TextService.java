package com.monolith.boilerplate.service;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

public interface TextService {
    String getText(Locale locale, String messageKey);
    Map<String, String> getTexts(Locale locale, Set<String> messageKeys);
    String getText(Locale locale, String messageKey, Map<String, Object> model);
}
