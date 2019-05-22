/**
 * slizaa-scanner-cypherregistry - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.cypherregistry;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatement;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class DefaultCypherStatement implements ICypherStatement {

  /** the greoup id - never null */
  private String       _groupId;

  /** - */
  private String       _statementId;

  /** - */
  private String       _description;

  /** - */
  private String       _statement;

  /** - */
  private Object       _codeSource;

  /** - */
  private String       _relativePath;

  /** - */
  private List<String> _requiredStatements = new ArrayList<>();

  /**
   * <p>
   * Creates a new instance of type {@link DefaultCypherStatement}.
   * </p>
   */
  public DefaultCypherStatement() {
    super();
  }

  /**
   * <p>
   * Creates a new instance of type {@link DefaultCypherStatement}.
   * </p>
   *
   * @param groupId
   * @param statementId
   * @param statement
   */
  public DefaultCypherStatement(String groupId, String statementId, String statement) {
    this._groupId = checkNotNull(groupId);
    this._statementId = checkNotNull(statementId);
    this._statement = checkNotNull(statement);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getGroupId() {
    return this._groupId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatementId() {
    return this._statementId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getFullyQualifiedName() {
    return this._groupId + "." + this._statementId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription() {
    return this._description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatement() {
    return this._statement;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getRequiredStatements() {
    return this._requiredStatements;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRelativePath() {
    return this._relativePath;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getCodeSource() {
    return this._codeSource;
  }

  public void setGroupId(String groupId) {
    this._groupId = groupId;
  }

  public void setStatementId(String statementId) {
    this._statementId = statementId;
  }

  public void setDescription(String description) {
    this._description = description;
  }

  public void setStatement(String statement) {
    this._statement = statement;
  }

  public void setRequiredStatements(List<String> requiredStatements) {
    checkNotNull(requiredStatements).forEach(element -> checkNotNull(element));
    this._requiredStatements = requiredStatements;
  }

  public void setCodeSource(Object codeSource) {
    this._codeSource = codeSource;
  }

  public void setRelativePath(String relativePath) {
    this._relativePath = relativePath;
  }

  @Override
  public boolean isValid() {
    return this._groupId != null && this._statementId != null;
  }

  @Override
  public String toString() {
    return "DefaultCypherStatement [groupId=" + this._groupId + ", statementId=" + this._statementId + ", description="
        + this._description + ", statement=" + this._statement + ", requiredStatements=" + this._requiredStatements
        + ", codeSource=" + this._codeSource + ", relativePath=" + this._relativePath + "]";
  }
}
