/*
 * Copyright The Original Author or Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package io.jenkins.plugins.opentelemetry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class OpenTelemetryAttributesActionTest {

    @Test
    void spanIsRememberedOnce() {
        OpenTelemetryAttributesAction action = new OpenTelemetryAttributesAction();
        assertTrue(action.isNotYetAppliedToSpan("span-1"));
        assertFalse(action.isNotYetAppliedToSpan("span-1"));
    }

    @Test
    void appliedToSpansIsBounded() {
        OpenTelemetryAttributesAction action = new OpenTelemetryAttributesAction();
        assertTrue(action.isNotYetAppliedToSpan("span-0"));
        for (int i = 1; i <= OpenTelemetryAttributesAction.MAX_APPLIED_TO_SPANS; i++) {
            assertTrue(action.isNotYetAppliedToSpan("span-" + i));
        }
        // the oldest span id was evicted, so it is seen as new again
        assertTrue(action.isNotYetAppliedToSpan("span-0"));
    }

    @Test
    void recentlyUsedSpanIsNotEvicted() {
        OpenTelemetryAttributesAction action = new OpenTelemetryAttributesAction();
        assertTrue(action.isNotYetAppliedToSpan("long-running-span"));
        for (int i = 1; i <= OpenTelemetryAttributesAction.MAX_APPLIED_TO_SPANS; i++) {
            assertTrue(action.isNotYetAppliedToSpan("span-" + i));
            // a span that is still open keeps being checked and stays in the set
            assertFalse(action.isNotYetAppliedToSpan("long-running-span"));
        }
    }
}
