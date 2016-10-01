/*
 * MIT License
 *
 * Copyright (c) 2016 Timofey Klyubin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.klyubin.plurals;

import ru.klyubin.plurals.util.UTF8Control;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Created by Timofey Klyubin on 01.10.2016.
 * t.klyubin@gmail.com
 */
public class Pluralize {

    private static final String DEFAULT_PROPERTIES = "plurals";
    private static final String DEFAULT_PREFIX = "java.plurals";

    private final PluralizationRuleProvider pluralizationRuleProvider;
    private final ResourceBundle bundle;
    private final String prefix;

    public Pluralize(PluralizationRuleProvider pluralizationRuleProvider,
                     ResourceBundle bundle,
                     final String prefix) {
        this.pluralizationRuleProvider = pluralizationRuleProvider;
        this.bundle = bundle;
        this.prefix = prefix;
    }

    public Pluralize(PluralizationRuleProvider pluralizationRuleProvider,
                     String bundleName,
                     final String prefix) {
        this(pluralizationRuleProvider, ResourceBundle.getBundle(bundleName, new UTF8Control()), prefix);
    }

    public Pluralize(PluralizationRuleProvider pluralizationRuleProvider,
                     String bundleName) {
        this(pluralizationRuleProvider, ResourceBundle.getBundle(bundleName, new UTF8Control()), DEFAULT_PREFIX);
    }

    public Pluralize(PluralizationRuleProvider pluralizationRuleProvider) {
        this(pluralizationRuleProvider, DEFAULT_PROPERTIES);
    }

    public Pluralize(final String prefix) {
        this(new DefaultPluralizationRuleProvider(), DEFAULT_PROPERTIES, prefix);
    }

    public Pluralize() {
        this(new DefaultPluralizationRuleProvider());
    }

    public String pluralize(int quantity) {
        return pluralize(quantity, Locale.getDefault());
    }

    public String pluralize(int quantity, Locale locale) {
        PluralizationRule rule = pluralizationRuleProvider.getRuleForLocale(locale);
        Pluralization pluralization = rule.select(quantity);
        Locale.setDefault(locale);
        final String rawString = bundle.getString(prefix + pluralization.toString());
        return MessageFormat.format(rawString, quantity);
    }

    protected PluralizationRule getRuleForLocale(Locale locale) {
        return pluralizationRuleProvider.getRuleForLocale(locale);
    }
}