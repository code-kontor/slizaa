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

import org.jline.reader.LineReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.commands.Quit;

import java.io.IOException;

@ShellComponent
@ShellCommandGroup("Built-In Commands")
public class SlizaaQuit implements Quit.Command {

    @Autowired
    private LineReader reader;

    @Autowired
    private ConfigurableApplicationContext applicationContext;

    @ShellMethod(
            value = "Exit the shell.",
            key = {"quit", "exit"}
    )

    public void quit() {

        //b
        String confirmation = ask("Do you really want to quit (y/n)? ");

        if ("y".equalsIgnoreCase(confirmation)) {
            try {
                reader.getTerminal().close();
            } catch (IOException e) {
                // simply ignore
            }
            SpringApplication.exit(applicationContext, () -> 0);
            System.exit(0);
        }
    }

    private String ask(String question) {
        String result = this.reader.readLine(question + ": ");
        return result != null ? result.trim() : null;
    }
}