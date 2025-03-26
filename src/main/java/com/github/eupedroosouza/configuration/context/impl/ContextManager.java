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
import com.github.eupedroosouza.configuration.context.Context;
import io.leangen.geantyref.GenericTypeReflector;
import org.spongepowered.configurate.objectmapping.meta.PostProcessor;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.util.Types;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContextManager {

    private static final ContextManager INSTANCE = new ContextManager();

    private final HashMap<Class<?>, List<Method>> discoveredMethods = new HashMap<>();

    private List<Method> discoverMethods(Type type) throws SerializationException {
        // Part of this code was copied from https://github.com/SpongePowered/Configurate/blob/trunk/core/src/main/java/org/spongepowered/configurate/objectmapping/meta/PostProcessor.java
        // Licensed by http://www.apache.org/licenses/LICENSE-2.0
        // Copyright (C) zml and Configurate contributors
        final Class<?> clazz = GenericTypeReflector.erase(type);
        if (discoveredMethods.containsKey(clazz)) {
            return discoveredMethods.get(clazz);
        } else {
            List<Method> methods = null;
            for (final Method method : Types.allDeclaredMethods(clazz)) {
                if (method.isAnnotationPresent(Context.class)) {
                    final int modifiers = method.getModifiers();
                    if (Modifier.isAbstract(modifiers)) {
                        continue;
                    }
                    if (Modifier.isStatic(modifiers)) {
                        throw new SerializationException(type,
                                "Context method " + method.getName() + "() must not be static.");
                    }

                    if (method.getParameterCount() != 1) {
                        throw new SerializationException(type,
                                "Context method " + method.getName() + "() must have exactly one parameter.");
                    }

                    Parameter parameter = method.getParameters()[0];
                    if (parameter.getType() != ConfigurationContext.class) {
                        throw new SerializationException(type,
                                "Context method " + method.getName() + "() must have ContextConfiguration as your parameter.");
                    }
                    method.setAccessible(true);
                    if (methods == null) {
                        methods = new ArrayList<>();
                    }
                    methods.add(method);
                }
            }
            discoveredMethods.put(clazz, methods);
            return methods;
        }
    }

    public PostProcessor.Factory createFactory(ConfigurationContext context) {
        // Part of this code was copied from https://github.com/SpongePowered/Configurate/blob/trunk/core/src/main/java/org/spongepowered/configurate/objectmapping/meta/PostProcessor.java
        // Licensed by http://www.apache.org/licenses/LICENSE-2.0
        // Copyright (C) zml and Configurate contributors
        return type -> {
            final List<Method> methods = discoverMethods(type);
            if (methods != null) {
                return instance -> {
                    SerializationException aggregateException = null;
                    for (final Method method : methods) {
                        SerializationException exc = null;
                        try {
                            method.invoke(instance, context);
                        } catch (final InvocationTargetException ex) {
                            if (ex.getCause() instanceof SerializationException) {
                                exc = (SerializationException) ex.getCause();
                                exc.initType(type);
                            } else if (ex.getCause() != null) {
                                exc = new SerializationException(type,
                                        "Failed occurred in context method " + method.getName() + "().", ex.getCause());
                            } else {
                                exc = new SerializationException(type,
                                        "Unknown error occurred attempting to invoke context method " + method.getName() + "()", ex);
                            }
                        } catch (final IllegalAccessException | IllegalArgumentException ex) {
                            exc = new SerializationException(type, "Failed to invoke context method " + method.getName() + "()", ex);
                        }

                        if (exc != null) {
                            if (aggregateException == null) {
                                aggregateException = exc;
                            } else {
                                aggregateException.addSuppressed(exc);
                            }
                        }
                    }

                    if (aggregateException != null) {
                        throw aggregateException;
                    }
                };
            }
            return null;
        };
    }

    public static ContextManager instance() {
        return INSTANCE;
    }


}
