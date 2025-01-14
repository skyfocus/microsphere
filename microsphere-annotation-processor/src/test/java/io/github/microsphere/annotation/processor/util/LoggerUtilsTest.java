/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.microsphere.annotation.processor.util;

import org.junit.jupiter.api.Test;

import static io.github.microsphere.annotation.processor.util.LoggerUtils.info;
import static io.github.microsphere.annotation.processor.util.LoggerUtils.warn;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * {@link LoggerUtils} Test
 *
 * @since 1.0.0
 */
public class LoggerUtilsTest {

    @Test
    public void testLogger() {
        assertNotNull(LoggerUtils.LOGGER);
    }

    @Test
    public void testInfo() {
        info("Hello,World");
        info("Hello,%s", "World");
        info("%s,%s", "Hello", "World");
    }

    @Test
    public void testWarn() {
        warn("Hello,World");
        warn("Hello,%s", "World");
        warn("%s,%s", "Hello", "World");
    }
}
