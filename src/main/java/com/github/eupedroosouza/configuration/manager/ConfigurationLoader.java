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

package com.github.eupedroosouza.configuration.manager;

import com.github.eupedroosouza.configuration.context.ConfigurationContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ScopedConfigurationNode;

import java.nio.file.Path;
import java.util.function.Supplier;

public interface ConfigurationLoader<N extends ScopedConfigurationNode<N>> {

    @NotNull
    <S> LoadedConfiguration<S, N> loadWithDefaultAndContext(@NotNull Path path, @NotNull S defaultValue, @NotNull ConfigurationContext context) throws ConfigurateException;

    @NotNull
    <S> LoadedConfiguration<S, N> loadWithDefault(@NotNull Path path, @NotNull S defaultValue) throws ConfigurateException;

    @NotNull
    <S> LoadedConfiguration<S, N> loadWithDefault(@NotNull Path path, @NotNull Supplier<S> instanceCreator) throws ConfigurateException;

    @NotNull
    <S> LoadedConfiguration<S, N> loadWithResourcesAndContext(@NotNull Path path, @NotNull Class<S> clazz, @NotNull String resourceName, @NotNull ConfigurationContext context) throws ConfigurateException;

    @NotNull
    <S> LoadedConfiguration<S, N> loadWithResources(@NotNull Path path, @NotNull Class<S> clazz, @NotNull String resourceName) throws ConfigurateException;

    @NotNull
    <S> LoadedConfiguration<S, N> load(@NotNull Path path, @NotNull Class<S> clazz) throws ConfigurateException;

    <S> void save(@NotNull LoadedConfiguration<S, N> configuration) throws ConfigurateException;

    @NotNull
    N load(@NotNull Path path) throws ConfigurateException;

    @Nullable
    N loadWithResources(@NotNull Path path, @NotNull String resourceName) throws ConfigurateException;

    void save(@NotNull Path path, @NotNull N value) throws ConfigurateException;

}
