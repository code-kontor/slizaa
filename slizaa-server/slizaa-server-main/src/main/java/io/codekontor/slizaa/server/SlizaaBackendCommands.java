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

import io.codekontor.slizaa.server.service.backend.IBackendService;
import io.codekontor.slizaa.server.service.backend.IModifiableBackendService;
import io.codekontor.slizaa.server.service.extensions.IExtension;
import io.codekontor.slizaa.server.service.extensions.IExtensionIdentifier;
import io.codekontor.slizaa.server.service.extensions.IExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.*;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Slizaa Backend Commands")
public class SlizaaBackendCommands {

    @Autowired(required = false)
    private IModifiableBackendService _modifiableBackendService;

    @Autowired
    private IBackendService _backendService;

    @Autowired
    private IExtensionService _extensionService;

    @ShellMethod(value = "List all available backend extensions.", key="listAvailableExtensions")
    @ShellMethodAvailability("availabilityCheck")
    public String listAvailableExtensions() {

        //
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Available Backend Extensions:\n");
        _extensionService.getExtensions().forEach(extension -> {
            stringBuffer.append(format(extension));
        });
        return stringBuffer.toString();
    }

    @ShellMethod(value = "List installed extensions.", key="listInstalledExtensions")
    public String listInstalledExtensions() {

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Installed Backend Extensions:\n");
        _backendService.getInstalledExtensions().forEach(extension -> {
            stringBuffer.append(format(extension));
        });


        return stringBuffer.toString();
    }

    @ShellMethod(value = "Install backend extensions.", key="installExtensions")
    @ShellMethodAvailability("availabilityCheck")
    public String installExtensions(@ShellOption({"-e", "--extensions"}) String[] extensions) {

        // fail fast
        if (_modifiableBackendService == null) {
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
       List<IExtension> extensionsToInstall = _extensionService.getExtensions(extensionIdList);
       _modifiableBackendService.installExtensions(extensionsToInstall);

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Installed Backend Extensions:\n");
        _modifiableBackendService.getInstalledExtensions().forEach(extension -> {
            stringBuffer.append(format(extension));
        });


        return stringBuffer.toString();
    }

    public Availability availabilityCheck() {
        return _modifiableBackendService != null
                ? Availability.available()
                : Availability.unavailable("an offline backend is not modifiable.");
    }

    /**
     * Checks if the backend is configured properly.
     *
     * @return <code>true</code> if the backend is configured properly.
     */
    private String checkBackendConfigured() {
        if (!_backendService.hasInstalledExtensions()) {
            return cannotExecuteCommand("The Slizaa Server has not been configured properly: There are not installed backend extensions.\n");
        }
        return null;
    }

    private String cannotExecuteCommand(String msg) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Can not execute command.\n");
        stringBuffer.append(msg + "\n");
        return stringBuffer.toString();
    }

    private String format(IExtension extension) {
        return String.format(" - %1$s_%2$s (Symbolic name: %1$s, version: %2$s)\n", extension.getSymbolicName(), extension.getVersion());
    }
}