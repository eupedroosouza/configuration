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

import com.github.eupedroosouza.configuration.context.ConfigurationContext;
import com.github.eupedroosouza.configuration.context.Context;
import com.github.eupedroosouza.configuration.manager.LoadedConfiguration;
import com.github.eupedroosouza.configuration.test.util.TestConstants;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

public class ConfigurationContextTest {

    @Test
    void load() throws ConfigurateException {

        String value = "This is value!";
        String anotherValue = "This is another value!";

        final LoadedConfiguration<ConfigurationContextConfig, @NotNull CommentedConfigurationNode> configuration;
        configuration = TestConstants.CONFIGURATION_LOADER.loadWithDefaultAndContext(TestConstants.RUN_DIR.resolve("./context.yml"),
                new ConfigurationContextConfig(),
                ConfigurationContext.builder()
                        .add(value)
                        .add("another_value", anotherValue)
                        .build());
        TestConstants.CONFIGURATION_LOADER.save(configuration);

        ConfigurationContextConfig config = configuration.require();
        Assertions.assertEquals(value, config.getValue());
        Assertions.assertEquals(anotherValue, config.getAnotherValue());
    }

    @ConfigSerializable
    static class ConfigurationContextConfig {

        private String id = "This is a ID";

        private transient String value;
        private transient String anotherValue;

        @Context
        public void apply(ConfigurationContext context) {
            this.value = context.require(String.class);
            this.anotherValue = context.require("another_value", String.class);
        }

        public String getValue() {
            return value;
        }

        public String getAnotherValue() {
            return anotherValue;
        }
    }

}
