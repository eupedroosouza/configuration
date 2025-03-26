/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2025 Pedro Souza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.github.eupedroosouza.configuration.manager.impl;

import com.github.eupedroosouza.configuration.manager.ConfigurationLoader;
import com.github.eupedroosouza.configuration.manager.ConfigurationManager;
import com.github.eupedroosouza.configuration.util.Builder;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurationOptions;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;

import java.util.Objects;
import java.util.function.UnaryOperator;


public class ConfigurationManagerImpl implements ConfigurationManager {

    @NotNull
    private final ConfigurationOptions defaultOptions;

    public ConfigurationManagerImpl(ConfigurationManagerBuilder builder) {
        this.defaultOptions = builder.defaultOptions();
    }

    @Override
    public @NotNull ConfigurationOptions defaultOptions() {
        return defaultOptions;
    }

    @Override
    public @NotNull <
            T extends AbstractConfigurationLoader.Builder<T, L>, L
            extends AbstractConfigurationLoader<N>,
            N extends ScopedConfigurationNode<N>> ConfigurationLoader<N> provide(@NotNull T loaderBuilder) {
        loaderBuilder.defaultOptions(opt -> opt
                .shouldCopyDefaults(defaultOptions.shouldCopyDefaults() && !opt.shouldCopyDefaults())
                .serializers(defaultOptions.serializers().childBuilder().registerAll(opt.serializers()).build()));
        // todo: wrap loader options with default options
        return new ConfigurationLoaderImpl<>(loaderBuilder);
    }


    public static class ConfigurationManagerBuilder implements Builder<ConfigurationManagerImpl> {

        private ConfigurationOptions defaultOptions = ConfigurationOptions.defaults();

        public ConfigurationManagerBuilder defaultOptions(ConfigurationOptions options) {
            this.defaultOptions = Objects.requireNonNull(options, "defaultOptions (updated)");
            return this;
        }

        public ConfigurationManagerBuilder defaultOptions(final UnaryOperator<ConfigurationOptions> defaultOptions) {
            this.defaultOptions = Objects.requireNonNull(defaultOptions.apply(this.defaultOptions), "defaultOptions (updated)");
            return this;
        }

        @NotNull
        public ConfigurationOptions defaultOptions() {
            return defaultOptions;
        }

        @Override
        public ConfigurationManagerImpl build() {
            return new ConfigurationManagerImpl(this);
        }

    }

}
