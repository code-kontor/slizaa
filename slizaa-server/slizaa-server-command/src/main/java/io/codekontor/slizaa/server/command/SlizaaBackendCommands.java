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

import io.codekontor.slizaa.server.service.backend.extensions.IExtension;
import io.codekontor.slizaa.server.service.backend.extensions.IExtensionIdentifier;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Slizaa Backend Commands")
public class SlizaaBackendCommands extends AbstractGraphDatabaseCommandComponent{

     @ShellMethod(value = "List installed extensions.", key="listInstalledExtensions")
    public String listInstalledExtensions() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Installed Backend Extensions:\n");
        backendService().getInstalledExtensions().forEach(extension -> {
            stringBuffer.append(formatExtension(extension));
        });


        return stringBuffer.toString();
    }
}