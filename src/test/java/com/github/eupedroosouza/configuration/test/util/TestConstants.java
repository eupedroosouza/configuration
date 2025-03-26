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

package com.github.eupedroosouza.configuration.test.util;

import com.github.eupedroosouza.configuration.manager.ConfigurationLoader;
import com.github.eupedroosouza.configuration.manager.ConfigurationManager;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;
import java.nio.file.Paths;

public class TestConstants {

    public static final Path RUN_DIR = Paths.get("./run/");
    public static final ConfigurationManager CONFIGURATION_MANAGER;
    public static final ConfigurationLoader<@NotNull CommentedConfigurationNode> CONFIGURATION_LOADER;

    static {
        CONFIGURATION_MANAGER =  ConfigurationManager.builder().build();
        CONFIGURATION_LOADER = CONFIGURATION_MANAGER.provide(YamlConfigurationLoader.builder().nodeStyle(NodeStyle.BLOCK).indent(2));
    }

}
