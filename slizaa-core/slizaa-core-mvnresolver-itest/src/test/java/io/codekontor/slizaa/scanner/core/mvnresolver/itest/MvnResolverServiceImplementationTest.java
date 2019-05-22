/**
 * slizaa-core-mvnresolver-itest - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.core.mvnresolver.itest;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;

import io.codekontor.slizaa.core.mvnresolver.MvnResolverServiceFactoryFactory;
import io.codekontor.slizaa.core.mvnresolver.api.IMvnResolverService;

public class MvnResolverServiceImplementationTest {

    @Test
    public void testIt() {

        //
        IMvnResolverService mvnResolverService = MvnResolverServiceFactoryFactory
                .createNewResolverServiceFactory().newMvnResolverService().create();

        //
        File[] files = mvnResolverService.resolve("org.neo4j.test:neo4j-harness:2.3.3");

        //
        assertThat(files).hasSize(74);
    }
}
