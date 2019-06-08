/**
 * slizaa-server-spec - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.spec

import org.assertj.core.api.Assertions
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.OutputStream

class TestSource() {

	 @Test
     fun testWriteYaml() {

         val contentDefinitionSpec : ContentDefinitionSpec = ContentDefinitionSpec("Type", "Definition")
         val example = GraphDatabaseSpec("hello", contentDefinitionSpec)
         val outputStream : OutputStream = ByteArrayOutputStream();

         YamlMapper.writeYml(outputStream, example)

         Assertions.assertThat(outputStream.toString()).isEqualTo(
                 """---
                    |identifier: "hello"
                    |contentDefinition:
                    |  type: "Type"
                    |  definition: "Definition"
                    |running: true
                    |forceRebuild: true
                    |
                """.trimMargin())
    }

    @Test
    fun testReadYaml() {

        val contentDefinitionSpec : ContentDefinitionSpec = ContentDefinitionSpec("Type", "Definition")
        val example : GraphDatabaseSpec = GraphDatabaseSpec("hello", contentDefinitionSpec)
        val outputStream : ByteArrayOutputStream = ByteArrayOutputStream();

        YamlMapper.writeYml(outputStream, example)

        Assertions.assertThat(outputStream.toString()).isEqualTo(
                """---
                    |identifier: "hello"
                    |contentDefinition:
                    |  type: "Type"
                    |  definition: "Definition"
                    |running: true
                    |forceRebuild: true
                    |
                """.trimMargin())
    }
}