/**
 * slizaa-core-progressmonitor - Slizaa Static Software Analysis Tools
 * Copyright © 2019 Code-Kontor GmbH and others (slizaa@codekontor.io)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package io.codekontor.slizaa.core.progressmonitor;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 */
public class DefaultProgressMonitorTest {

    /**
     *
     */
    @Test
    public void testDefaultProgressMonitor() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 250,
                DefaultProgressMonitor.nullLogger())) {

            //
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(0);

            //
            for (int i = 1; i <= 250; i++) {
                progressMonitor.advance(1);
                assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(i);
            }
        }
    }

    /**
     *
     */
    @Test
    public void testTotalWorkIsNull() {
        IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 0);
        assertThat(progressMonitor.isComplete());
    }

    /**
     *
     */
    @Test
    public void testSubMonitor() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 250,
                DefaultProgressMonitor.nullLogger())) {

            IProgressMonitor submonitor = progressMonitor.subTask("Check 1")
                    .withParentConsumptionInPercentage(20)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(50);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(20);

            submonitor = progressMonitor.subTask("Check 2")
                    .withParentConsumptionInPercentage(50)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(175);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(70);

            submonitor = progressMonitor.subTask("Check 3")
                    .withParentConsumptionInPercentage(30)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(250);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(100);

        }
    }

    @Test
    public void testSubMonitor2() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 250,
                DefaultProgressMonitor.nullLogger())) {

            IProgressMonitor submonitor = progressMonitor.subTask("Check 1")
                    .withParentConsumptionInWorkTicks(50)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(50);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(20);

            submonitor = progressMonitor.subTask("Check 2")
                    .withParentConsumptionInWorkTicks(125)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(175);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(70);

            submonitor = progressMonitor.subTask("Check 3")
                    .withParentConsumptionInWorkTicks(75)
                    .withTotalWorkTicks(100)
                    .create();

            consumeSubmonitor(submonitor);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(250);
            assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(100);

        }
    }

    @Test
    public void testSubMonitor_3() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 250,
                DefaultProgressMonitor.nullLogger())) {

            // sm 1
            try (IProgressMonitor submonitor = progressMonitor.subTask("Check 1")
                    .withParentConsumptionInWorkTicks(50)
                    .withTotalWorkTicks(100)
                    .create()) {

                consumeSubmonitor(submonitor);
                assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(50);
                assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(20);
            }

            // sm 2
            try (IProgressMonitor submonitor = progressMonitor.subTask("Check 2")
                    .withParentConsumptionInWorkTicks(125)
                    .withTotalWorkTicks(100)
                    .create()) {

                try (IProgressMonitor submonitor_2 = submonitor.subTask("Check 2.1")
                        .withParentConsumptionInPercentage(100)
                        .withTotalWorkTicks(100)
                        .create()) {

                    consumeSubmonitor(submonitor_2);
                }

                assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(175);
                assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(70);
            }

            // sm 3
            try (IProgressMonitor submonitor = progressMonitor.subTask("Check 3")
                    .withParentConsumptionInWorkTicks(75)
                    .withTotalWorkTicks(100)
                    .create()) {

                consumeSubmonitor(submonitor);
                assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(250);
                assertThat(progressMonitor.getWorkDoneInPercentage()).isEqualTo(100);

            }
        }
    }

    @Test
    public void testSubMonitor_4() {

        try (IProgressMonitor progressMonitor = new DefaultProgressMonitor("Master check", 100,
                DefaultProgressMonitor.nullLogger())) {

            // sm 1
            try (IProgressMonitor submonitor = progressMonitor.subTask("Check 1")
                    .withParentConsumptionInPercentage(100)
                    .withTotalWorkTicks(2)
                    .create()) {

                try (IProgressMonitor subsubmonitor = submonitor.subTask("Check 1.1")
                        .withParentConsumptionInPercentage(10)
                        .withTotalWorkTicks(100)
                        .create()) {

                    consumeSubmonitor(subsubmonitor);
                }

                try (IProgressMonitor subsubmonitor = submonitor.subTask("Check 1.2")
                        .withParentConsumptionInPercentage(90)
                        .withTotalWorkTicks(100)
                        .create()) {

                    consumeSubmonitor(subsubmonitor);
                }
            }
        }
    }

    /**
     * @param progressMonitor
     */
    private void consumeSubmonitor(IProgressMonitor progressMonitor) {

        //
        assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(0);

        //
        for (int i = 1; i <= 100; i++) {
            progressMonitor.advance(1);
            assertThat(progressMonitor.getWorkDoneInTicks()).isEqualTo(i);
        }

        //
        progressMonitor.close();
    }
}
