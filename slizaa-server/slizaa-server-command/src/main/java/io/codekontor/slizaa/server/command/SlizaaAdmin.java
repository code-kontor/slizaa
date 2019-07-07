/**
 * slizaa-server-command - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.command;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
@ShellCommandGroup("Slizaa Admin Commands")
public class SlizaaAdmin {

    @ShellMethod(value = "Memory usage.", key = "memUsage")
    public String memUsage() {

        return memoryUsage();
    }

    @ShellMethod(value = "Run garbage collector.", key = "gc")
    public String gc() {

        Runtime.getRuntime().gc();

        return memoryUsage();
    }

    static String memoryUsage() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Current Memory Usage:\n");
        Runtime rt = Runtime.getRuntime();
        long usedMB = (rt.totalMemory() - rt.freeMemory()) / 1024 / 1024;
        long totalMB = (rt.totalMemory()) / 1024 / 1024;
        stringBuffer.append("Used Memory: " + usedMB + " MB\n");
        stringBuffer.append("Total Memory: " + totalMB + " MB\n");

        return stringBuffer.toString();
    }
}
