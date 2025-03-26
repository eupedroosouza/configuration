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

package com.github.eupedroosouza.configuration.test;

import com.github.eupedroosouza.configuration.manager.ConfigurationLoader;
import com.github.eupedroosouza.configuration.manager.ConfigurationManager;
import com.github.eupedroosouza.configuration.manager.LoadedConfiguration;
import com.github.eupedroosouza.configuration.test.util.TestConstants;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigurationDefaultTest {

    static Path defaultsPath = TestConstants.RUN_DIR.resolve("./defaults.yml");

    @BeforeAll
    static void beforeAll() throws IOException {
        if (Files.exists(defaultsPath)) {
            Files.delete(defaultsPath);
        }
    }

    @Test
    @Order(1)
    void loadWithDefault() throws ConfigurateException {
        LoadedConfiguration<DefaultConfig, @NotNull CommentedConfigurationNode> configuration =
                TestConstants.CONFIGURATION_LOADER.loadWithDefault(defaultsPath, new DefaultConfig());
        TestConstants.CONFIGURATION_LOADER.save(configuration); // save defaults

        DefaultConfig config = configuration.require();
        Assertions.assertEquals("This is default message!", config.getMessage());
        config.setMessage("This is another message!");
        TestConstants.CONFIGURATION_LOADER.save(configuration);
    }

    @Test
    @Order(2)
    void testPersistence() throws ConfigurateException {
        LoadedConfiguration<DefaultConfig, @NotNull CommentedConfigurationNode> configuration =
                TestConstants.CONFIGURATION_LOADER.loadWithDefault(defaultsPath, new DefaultConfig());
        Assertions.assertNotNull(configuration);

        DefaultConfig config = configuration.require();
        Assertions.assertNotNull(config);
        Assertions.assertEquals("This is another message!", config.getMessage());
    }

    @ConfigSerializable
    static class DefaultConfig {

        private String message = "This is default message!";

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}
