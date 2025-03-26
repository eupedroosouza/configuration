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

package com.github.eupedroosouza.configuration.context.impl;

import com.github.eupedroosouza.configuration.context.ConfigurationContext;
import com.github.eupedroosouza.configuration.util.Builder;
import io.leangen.geantyref.GenericTypeReflector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Optional;

public class ConfigurationContextImpl implements ConfigurationContext {

    private final HashMap<String, Object> objects;

    public ConfigurationContextImpl(ConfigurationContextBuilder builder) {
        this.objects = new HashMap<>(builder.objects());
    }

    private <T> @Nullable T internalGet(String key, Class<T> clazz) {
        Object obj = objects.get(key);
        if (obj == null) {
            return null;
        }
        if (obj.getClass() != clazz) {
            throw new IllegalArgumentException("Object saved in " + key + " has a different class from " + obj.getClass().getName());
        }
        return clazz.cast(obj);
    }

    @Override
    public <T> @NotNull T require(Class<T> clazz) {
        return require(clazz.getName(), clazz);
    }

    @Override
    public <T> @NotNull T require(String key, Class<T> clazz) {
        T obj = internalGet(key, clazz);
        if (obj == null) {
            throw new NullPointerException("Required object not found: " + key);
        }
        return obj;
    }

    @Override
    public @NotNull Object require(Type type) {
        Class<?> clazz = GenericTypeReflector.erase(type);
        return require(clazz);
    }

    @Override
    public @NotNull Object require(String key, Type type) {
        Class<?> clazz = GenericTypeReflector.erase(type);
        return require(key, clazz);
    }

    @Override
    public <T> Optional<T> get(Class<T> clazz) {
        return get(clazz.getName(), clazz);
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> clazz) {
        return Optional.ofNullable(internalGet(key, clazz));
    }

    @Override
    public Optional<Object> get(Type type) {
        Class<?> clazz = GenericTypeReflector.erase(type);
        return Optional.ofNullable(internalGet(clazz.getName(), clazz));
    }

    @Override
    public Optional<Object> get(String key, Type type) {
        Class<?> clazz = GenericTypeReflector.erase(type);
        return Optional.ofNullable(internalGet(key, clazz));
    }

    public static class ConfigurationContextBuilder implements Builder<ConfigurationContextImpl> {

        private final HashMap<String, Object> objects = new HashMap<>();

        public <T> ConfigurationContextBuilder add(@NotNull String key, @NotNull T value) {
            this.objects.put(key, value);
            return this;
        }

        public <T> ConfigurationContextBuilder add(@NotNull T value) {
            this.add(value.getClass().getName(), value);
            return this;
        }

        public HashMap<String, Object> objects() {
            return objects;
        }

        @Override
        public ConfigurationContextImpl build() {
            return new ConfigurationContextImpl(this);
        }
    }

}
