/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.regex.Pattern;

import org.apiguardian.api.API;
import org.junit.platform.commons.util.ClassUtils;
import org.junit.platform.commons.util.Preconditions;

/**
 * {@code @DisplayNameGeneration} is used to declare...
 *
 * <p>Display names are typically used for test reporting in IDEs and build
 * tools and may contain spaces, special characters, and even emoji.
 *
 * @since 5.4
 * @see DisplayName
 * @see DisplayNameGenerator
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@API(status = EXPERIMENTAL, since = "5.4")
public @interface DisplayNameGeneration {

	Class<? extends DisplayNameGenerator> generator() default DisplayNameGenerator.class;

	Style value() default Style.DEFAULT;

	enum Style implements DisplayNameGenerator {
		/**
		 * Default display name generator.
		 */
		DEFAULT {
			/**
			 * TODO Javadoc
			 */
			@Override
			public String generateDisplayNameForClass(Class<?> testClass) {
				Preconditions.notNull(testClass, "Test class must not be null");
				String name = testClass.getName();
				int index = name.lastIndexOf('.');
				return name.substring(index + 1);
			}

			/**
			 * TODO Javadoc
			 */
			@Override
			public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
				Preconditions.notNull(nestedClass, "Nested test class must not be null");
				return nestedClass.getSimpleName();
			}

			/**
			 * TODO Javadoc
			 */
			@Override
			public String generateDisplayNameForMethod(Method testMethod) {
				Preconditions.notNull(testMethod, "Test method must not be null");
				return String.format("%s(%s)", testMethod.getName(),
					ClassUtils.nullSafeToString(Class::getSimpleName, testMethod.getParameterTypes()));
			}
		},

		/**
		 * TODO Javadoc
		 */
		UNDERSCORE {
			@Override
			public String generateDisplayNameForClass(Class<?> testClass) {
				return replaceUnderscore(DEFAULT.generateDisplayNameForClass(testClass));
			}

			@Override
			public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
				return replaceUnderscore(DEFAULT.generateDisplayNameForNestedClass(nestedClass));
			}

			@Override
			public String generateDisplayNameForMethod(Method testMethod) {
				return replaceUnderscore(DEFAULT.generateDisplayNameForMethod(testMethod));
			}

			private String replaceUnderscore(String name) {
				return name.replace('_', ' ');
			}
		},

		/**
		 * TODO Javadoc
		 */
		CAMEL_CASE {
			private final Pattern camelCasePattern = Pattern.compile(String.format("%s|%s|%s",
				"(?<=[A-Z])(?=[A-Z][a-z])", "(?<=[^A-Z])(?=[A-Z])", "(?<=[A-Za-z])(?=[^A-Za-z])"));

			@Override
			public String generateDisplayNameForClass(Class<?> testClass) {
				return splitCamelCase(DEFAULT.generateDisplayNameForClass(testClass));
			}

			@Override
			public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
				return splitCamelCase(DEFAULT.generateDisplayNameForNestedClass(nestedClass));
			}

			@Override
			public String generateDisplayNameForMethod(Method testMethod) {
				return splitCamelCase(DEFAULT.generateDisplayNameForMethod(testMethod));
			}

			private String splitCamelCase(String name) {
				return camelCasePattern.matcher(name).replaceAll(" ");
			}
		},

		/**
		 * TODO Javadoc
		 */
		SENTENCES {
			@Override
			public String generateDisplayNameForClass(Class<?> testClass) {
				return DEFAULT.generateDisplayNameForClass(testClass);
			}

			@Override
			public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
				return nestedClass.getSimpleName().replace('_', ' ') + "...";
			}

			@Override
			public String generateDisplayNameForMethod(Method testMethod) {
				StringBuilder builder = new StringBuilder();
				Class<?> current = testMethod.getDeclaringClass();
				while (current != null) {
					String currentSimpleName = current.getSimpleName();
					current = current.getEnclosingClass();
					if (current == null) {
						break;
					}
					builder.insert(0, ' ');
					builder.insert(0, currentSimpleName);
				}
				builder.append(testMethod.getName());
				builder.append('.');
				return builder.toString().replace('_', ' ');
			}
		}
	}

}
