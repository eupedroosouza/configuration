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

package com.github.eupedroosouza.configuration.context;

import com.github.eupedroosouza.configuration.context.impl.ConfigurationContextImpl;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.Optional;

public interface ConfigurationContext {

    static ConfigurationContextImpl.ConfigurationContextBuilder builder() {
        return new ConfigurationContextImpl.ConfigurationContextBuilder();
    }

    <T> @NotNull T require(Class<T> clazz);

    <T> @NotNull T require(String key, Class<T> clazz);

    @NotNull Object require(Type type);

    @NotNull Object require(String key, Type type);

    <T> Optional<T> get(Class<T> clazz);

    <T> Optional<T> get(String key, Class<T> clazz);

    Optional<Object> get(Type type);

    Optional<Object> get(String key, Type type);

}
