/*
 * Copyright 2015-2018 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.migrationsupport.conditions;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Tests for JUnit 4's {@link Ignore @Ignore} support.
 *
 * @since 5.4
 */
@ExtendWith(IgnoreCondition.class)
class IgnoreAnnotationTests {

	@Test
	@Ignore
	void ignored() {
		fail("This method should have been disabled via @Ignore");
	}

	@Test
	@Ignore("Ignore me!")
	void ignoredWithMessage() {
		fail("This method should have been disabled via @Ignore");
	}

	@Test
	// @Ignore
	void notIgnored() {
		/* no-op */
	}

}
