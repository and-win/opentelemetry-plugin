/*
 * Copyright The Original Author or Authors
 * SPDX-License-Identifier: Apache-2.0
 */
package io.jenkins.plugins.opentelemetry.computer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import hudson.model.Computer;
import hudson.slaves.DumbSlave;
import io.jenkins.plugins.opentelemetry.BaseIntegrationTest;
import io.jenkins.plugins.opentelemetry.OpenTelemetryAttributesAction;
import org.junit.jupiter.api.Test;

class MonitoringComputerListenerTest extends BaseIntegrationTest {

    @Test
    void reconnectKeepsOneAttributesAction() throws Exception {
        DumbSlave agent = jenkinsRule.createOnlineSlave();
        Computer computer = agent.toComputer();
        assertEquals(1, computer.getActions(OpenTelemetryAttributesAction.class).size());

        for (int i = 0; i < 3; i++) {
            computer.disconnect(null).get();
            computer.connect(true).get();
            jenkinsRule.waitOnline(agent);
        }

        assertEquals(1, computer.getActions(OpenTelemetryAttributesAction.class).size());
    }
}
