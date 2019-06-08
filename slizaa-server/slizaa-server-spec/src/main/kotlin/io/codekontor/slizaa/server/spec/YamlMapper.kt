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

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

object YamlMapper {

    @Throws(IOException::class)
    fun writeYml(out: OutputStream, obj: Any) {

        val yamlFactory = YAMLFactory()
        val mapper = ObjectMapper(yamlFactory)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY)
        val sp = DefaultSerializerProvider.Impl()
        mapper.setSerializerProvider(sp)

        mapper.writeValue(out, obj)
    }

    @Throws(IOException::class)
    fun <T> readYml(inputStream: InputStream, type: Class<T>): T {

        val yamlFactory = YAMLFactory()
        val mapper = ObjectMapper(yamlFactory)

        return mapper.readValue(inputStream, type)
    }
}
