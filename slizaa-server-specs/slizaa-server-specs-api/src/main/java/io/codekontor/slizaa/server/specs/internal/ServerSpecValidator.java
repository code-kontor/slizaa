/**
 * slizaa-server-specs-api - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.server.specs.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.codekontor.slizaa.server.specs.InvalidServerSpecException;
import io.codekontor.slizaa.server.specs.SlizaaAdminClient;
import io.codekontor.slizaa.server.specs.internal.ValidationItem.Severity;
import io.codekontor.slizaa.server.specs.internal.api.ServerExtensionSpec;
import io.codekontor.slizaa.server.specs.internal.api.SlizaaServerSpec;
import io.codekontor.slizaa.server.specs.internal.model.serverconfig.AvailableServerExtensions;

/**
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class ServerSpecValidator {

  private static final Logger  logger = LoggerFactory.getLogger(SlizaaAdminClient.class);

  /** the QueryExecutor */
  private QueryExecutor        _queryExecutor;

  /** the SlizaaServerSpec */
  private SlizaaServerSpec     _slizaaServerSpec;

  /** the validation errors */
  private List<ValidationItem> _validationErrors;

  /** the validation warnings */
  private List<ValidationItem> _validationWarnings;

  /**
   * Validates the specified {@link SlizaaServerSpec}.
   * 
   * @param queryExecutor
   * @param slizaaServerSpec
   * @return
   */
  public static void validate(QueryExecutor queryExecutor, SlizaaServerSpec slizaaServerSpec) {

    ServerSpecValidator serverSpecValidator = new ServerSpecValidator(queryExecutor, slizaaServerSpec);

    serverSpecValidator.validateServerExtensions();
    serverSpecValidator.failOnError();
    
    serverSpecValidator.dumpWarnings();
  }

  private void validateServerExtensions() {

    // at least one server extensions?
    if (_slizaaServerSpec.getServerExtensionSpecs() == null || _slizaaServerSpec.getServerExtensionSpecs().isEmpty()) {
      addErrorItem(String.format("You must specify at least one server extension. Current specification: %s",
          _slizaaServerSpec.getServerExtensionSpecs()));
      return;
    }

    // do the server extensions exist?
    AvailableServerExtensions availableServerExtensions = _queryExecutor.fetchAvailableServerExtensions();

    List<ServerExtensionSpec> unknownExtensions = _slizaaServerSpec.getServerExtensionSpecs().stream()
        .filter(extensionSpec -> !availableServerExtensions.getAvailableServerExtensions().contains(extensionSpec))
        .collect(Collectors.toList());

    if (!unknownExtensions.isEmpty()) {
      unknownExtensions.forEach(
          unknownExtension -> addErrorItem(String.format("Unknown server extensions: %s.", unknownExtensions)));
    }
  }

  private void failOnError() {
    if (!_validationErrors.isEmpty()) {
      StringBuilder errorMessage = new StringBuilder("Invalid Slizaa server specification:\n - ");
      errorMessage.append(
          String.join("\n - ", _validationErrors.stream().map(item -> item.toString()).collect(Collectors.toList())));
      throw new InvalidServerSpecException(errorMessage.toString());
    }
  }

  private void dumpWarnings() {
    _validationErrors.forEach(msg -> logger.warn(msg.toString()));
  }

  private ServerSpecValidator(QueryExecutor queryExecutor, SlizaaServerSpec slizaaServerSpec) {
    _queryExecutor = checkNotNull(queryExecutor);
    _slizaaServerSpec = checkNotNull(slizaaServerSpec);
    _validationErrors = new ArrayList<>();
    _validationWarnings = new ArrayList<>();
  }

  private void addErrorItem(String message) {
    _validationErrors.add(new ValidationItem(Severity.ERROR, message));
  }

  private void addWarnItem(String message) {
    _validationWarnings.add(new ValidationItem(Severity.WARN, message));
  }
}
