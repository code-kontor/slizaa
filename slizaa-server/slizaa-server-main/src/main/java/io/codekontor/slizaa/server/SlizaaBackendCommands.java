/**
 * slizaa-server-main - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server;

import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.extensions.IExtensionIdentifier;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Slizaa Backend Commands")
public class SlizaaBackendCommands extends AbstractGraphDatabaseCommandComponent{

    @ShellMethod(value = "List all available backend extensions.", key="listAvailableExtensions")
    @ShellMethodAvailability("availabilityCheck")
    public String listAvailableExtensions() {

        //
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Available Backend Extensions:\n");
        extensionService().getExtensions().forEach(extension -> {
            stringBuffer.append(formatExtension(extension));
        });
        return stringBuffer.toString();
    }

    @ShellMethod(value = "List installed extensions.", key="listInstalledExtensions")
    public String listInstalledExtensions() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Installed Backend Extensions:\n");
        backendService().getInstalledExtensions().forEach(extension -> {
            stringBuffer.append(formatExtension(extension));
        });


        return stringBuffer.toString();
    }

    @ShellMethod(value = "Install backend extensions.", key="installExtensions")
    @ShellMethodAvailability("availabilityCheck")
    public String installExtensions(String[] extensions) {

        // fail fast
        if (!hasModifiableBackendService()) {
            return cannotExecuteCommand("Backend is not modifiable.");
        }

        //
        List<IExtensionIdentifier> extensionIdList = new ArrayList<>();

        for (int i = 0; i < extensions.length; i++) {
            String extension = extensions[i];
            String[] split = extension.split("_");
            if (split.length != 2) {
                return cannotExecuteCommand(String.format("Invalid parameter value '%s'.", extension));
            }
            extensionIdList.add(new ExtensionIdentifier(split[0], split[1]));
        }

       //
       List<IExtension> extensionsToInstall = extensionService().getExtensions(extensionIdList);
       modifiableBackendService().installExtensions(extensionsToInstall);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Installed Backend Extensions:\n");
        modifiableBackendService().getInstalledExtensions().forEach(extension -> {
            stringBuffer.append(formatExtension(extension));
        });


        return stringBuffer.toString();
    }

    public Availability availabilityCheck() {
        return modifiableBackendService() != null
                ? Availability.available()
                : Availability.unavailable("an offline backend is not modifiable.");
    }

    private String formatExtension(IExtension extension) {
        return String.format(" - %1$s_%2$s (Symbolic name: %1$s, version: %2$s)\n", extension.getSymbolicName(), extension.getVersion());
    }
}