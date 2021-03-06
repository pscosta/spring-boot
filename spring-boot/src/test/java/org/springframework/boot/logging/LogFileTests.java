/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.logging;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySourcesPropertyResolver;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link LogFile}.
 *
 * @author Phillip Webb
 */
public class LogFileTests {

	@Test
	public void noProperties() throws Exception {
		PropertyResolver resolver = getPropertyResolver(null, null);
		LogFile logFile = LogFile.get(resolver);
		assertThat(logFile, nullValue());
	}

	@Test
	public void loggingFile() throws Exception {
		PropertyResolver resolver = getPropertyResolver("log.file", null);
		LogFile logFile = LogFile.get(resolver);
		Properties properties = new Properties();
		logFile.applyTo(properties);
		assertThat(logFile.toString(), equalTo("log.file"));
		assertThat(properties.getProperty("LOG_FILE"), equalTo("log.file"));
		assertThat(properties.getProperty("LOG_PATH"), nullValue());
	}

	@Test
	public void loggingPath() throws Exception {
		PropertyResolver resolver = getPropertyResolver(null, "logpath");
		LogFile logFile = LogFile.get(resolver);
		Properties properties = new Properties();
		logFile.applyTo(properties);
		assertThat(logFile.toString(), equalTo("logpath/spring.log"));
		assertThat(properties.getProperty("LOG_FILE"), equalTo("logpath/spring.log"));
		assertThat(properties.getProperty("LOG_PATH"), equalTo("logpath"));
	}

	@Test
	public void loggingFileAndPath() throws Exception {
		PropertyResolver resolver = getPropertyResolver("log.file", "logpath");
		LogFile logFile = LogFile.get(resolver);
		Properties properties = new Properties();
		logFile.applyTo(properties);
		assertThat(logFile.toString(), equalTo("log.file"));
		assertThat(properties.getProperty("LOG_FILE"), equalTo("log.file"));
		assertThat(properties.getProperty("LOG_PATH"), equalTo("logpath"));
	}

	private PropertyResolver getPropertyResolver(String file, String path) {
		Map<String, Object> properties = new LinkedHashMap<String, Object>();
		properties.put("logging.file", file);
		properties.put("logging.path", path);
		PropertySource<?> propertySource = new MapPropertySource("properties",
				properties);
		MutablePropertySources propertySources = new MutablePropertySources();
		propertySources.addFirst(propertySource);
		return new PropertySourcesPropertyResolver(propertySources);
	}

}
