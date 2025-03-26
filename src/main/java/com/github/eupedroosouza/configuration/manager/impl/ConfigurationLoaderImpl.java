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

import com.github.eupedroosouza.configuration.context.ConfigurationContext;
import com.github.eupedroosouza.configuration.context.impl.ContextManager;
import com.github.eupedroosouza.configuration.manager.ConfigurationLoader;
import com.github.eupedroosouza.configuration.manager.LoadedConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.loader.AbstractConfigurationLoader;
import org.spongepowered.configurate.objectmapping.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public class ConfigurationLoaderImpl<
        T extends AbstractConfigurationLoader.Builder<@NotNull T, L>,
        L extends AbstractConfigurationLoader<N>,
        N extends ScopedConfigurationNode<N>> implements ConfigurationLoader<N> {

    private final T loaderBuilder;

    public ConfigurationLoaderImpl(T loaderBuilder) {
        this.loaderBuilder = loaderBuilder;
    }

    private void copyStreams(Path path, String resourceName) throws ConfigurateException {
        try (InputStream stream = ConfigurationLoaderImpl.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (stream == null) {
                throw new ConfigurateException("Resource not found: " + resourceName);
            }
            if (Files.notExists(path)) {
                if (Files.notExists(path.getParent())) {
                    Files.createDirectories(path.getParent());
                } else if (!Files.isDirectory(path.getParent())) {
                    throw new ConfigurateException("Parent directory is not a directory: " + path.getParent());
                }
                Files.copy(stream, path);
            }
        } catch (IOException ex) {
            throw new ConfigurateException(ex);
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <S> LoadedConfiguration<S, N> loadWithDefaultAndContext(@NotNull Path path, @NotNull S defaultValue, @NotNull ConfigurationContext context) throws ConfigurateException {
        ObjectMapper.Factory mapperFactory = ObjectMapper.factoryBuilder()
                .addPostProcessor(ContextManager.instance().createFactory(context))
                .build();
        T builder = loaderBuilder.defaultOptions(options ->
                        options.serializers(serializerBuilder ->
                                serializerBuilder.registerAnnotatedObjects(mapperFactory)))
                .path(path);
        L loader = builder.build();
        N loadedNode = loader.load();
        S object = (S) loadedNode.get(defaultValue.getClass(), defaultValue);
        return new LoadedConfiguration<>(path, loader, loadedNode, object);
    }

    @SuppressWarnings("unchecked")
    @NotNull
    @Override
    public <S> LoadedConfiguration<S, N> loadWithDefault(@NotNull Path path, @NotNull S defaultValue) throws ConfigurateException {
        L loader = loaderBuilder.path(path).build();
        N loadedNode = loader.load();
        S object = (S) loadedNode.get(defaultValue.getClass(), defaultValue);
        return new LoadedConfiguration<>(path, loader, loadedNode, object);
    }

    @NotNull
    @Override
    public <S> LoadedConfiguration<S, N> loadWithDefault(@NotNull Path path, @NotNull Supplier<S> instanceCreator) throws ConfigurateException {
        return loadWithDefault(path, instanceCreator.get());
    }

    @Override
    public @NotNull <S> LoadedConfiguration<S, N> loadWithResourcesAndContext(@NotNull Path path, @NotNull Class<S> clazz, @NotNull String resourceName, @NotNull ConfigurationContext context) throws ConfigurateException {
        this.copyStreams(path, resourceName);
        ObjectMapper.Factory mapperFactory = ObjectMapper.factoryBuilder()
                .addPostProcessor(ContextManager.instance().createFactory(context))
                .build();
        T builder = loaderBuilder.defaultOptions(options ->
                        options.serializers(serializerBuilder ->
                                serializerBuilder.registerAnnotatedObjects(mapperFactory)))
                .path(path);
        L loader = builder.build();
        N loadedNode = loader.load();
        S object = loadedNode.get(clazz);
        return new LoadedConfiguration<>(path, loader, loadedNode, object);
    }

    @Override
    public <S> @NotNull LoadedConfiguration<S, N> loadWithResources(@NotNull Path path, @NotNull Class<S> clazz, @NotNull String resourceName) throws ConfigurateException {
        this.copyStreams(path, resourceName);
        return load(path, clazz);
    }


    @Override
    public @NotNull <S> LoadedConfiguration<S, N> load(@NotNull Path path, @NotNull Class<S> clazz) throws ConfigurateException {
        L loader = loaderBuilder.path(path).build();
        N loadedNode = loader.load();
        S object = loadedNode.get(clazz);
        return new LoadedConfiguration<>(path, loader, loadedNode, object);
    }

    @Override
    public <S> void save(@NotNull LoadedConfiguration<S, N> configuration) throws ConfigurateException {
        configuration.node().set(configuration.value());
        configuration.loader().save(configuration.node());
    }

    @NotNull
    @Override
    public N load(@NotNull Path path) throws ConfigurateException {
        L loader = loaderBuilder.path(path).build();
        return loader.load();
    }

    @Nullable
    @Override
    public N loadWithResources(@NotNull Path path, @NotNull String resourceName) throws ConfigurateException {
        this.copyStreams(path, resourceName);
        return load(path);
    }

    @Override
    public void save(@NotNull Path path, @NotNull N value) throws ConfigurateException {
        L loader = loaderBuilder.path(path).build();
        loader.save(value);
    }
}
