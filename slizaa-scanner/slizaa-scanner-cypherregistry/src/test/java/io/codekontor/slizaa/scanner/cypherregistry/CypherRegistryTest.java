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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatement;
import io.codekontor.slizaa.scanner.api.cypherregistry.ICypherStatementRegistry;
import io.codekontor.slizaa.scanner.cypherregistry.CypherRegistryUtils;
import io.codekontor.slizaa.scanner.cypherregistry.CypherStatementRegistry;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class CypherRegistryTest {

  /** - */
  private ICypherStatementRegistry _statementRegistry;

  @Before
  public void init() {
    this._statementRegistry = new CypherStatementRegistry(
        () -> CypherRegistryUtils.getCypherStatementsFromClasspath(CypherRegistryTest.class));
    this._statementRegistry.rescan();
  }

  @Test
  public void testExistingStatements() {
    assertThat(this._statementRegistry.getAllStatements()).hasSize(3);
    assertThat(this._statementRegistry.getStatement("io.codekontor.slizaa.example.typeresolution.test-statement-1")).isNotEmpty();
    assertThat(this._statementRegistry.getStatement("io.codekontor.slizaa.example.typeresolution.test-statement-2")).isNotEmpty();
    assertThat(this._statementRegistry.getStatement("io.codekontor.slizaa.example.typeresolution.test-statement-3")).isNotEmpty();
  }

  @Test
  public void testNonExistingsStatements() {
    assertThat(this._statementRegistry.getStatement("io.codekontor.slizaa.example.typeresolution.NOT_EXISTING")).isEmpty();
  }

  @Test
  public void testRequiredStatements() {
    assertThat(this._statementRegistry.getStatement("io.codekontor.slizaa.example.typeresolution.test-statement-1").get()
        .getRequiredStatements()).containsExactly("test-statement-2");
    assertThat(this._statementRegistry.getStatement("io.codekontor.slizaa.example.typeresolution.test-statement-2").get()
        .getRequiredStatements()).isEmpty();
    assertThat(this._statementRegistry.getStatement("io.codekontor.slizaa.example.typeresolution.test-statement-3").get()
        .getRequiredStatements()).containsExactlyInAnyOrder("test-statement-1", "test-statement-2");
  }

  @Test
  public void testCypherStatements() {

    //
    ICypherStatement cypherStatement = this._statementRegistry
        .getStatement("io.codekontor.slizaa.example.typeresolution.test-statement-1").get();

    //
    assertThat(cypherStatement.getStatement()).isNotNull();
  }

  @Test
  public void testOrder() {
    List<ICypherStatement> cypherStatements = this._statementRegistry.getAllStatements();
    assertThat(cypherStatements).hasSize(3);
    assertThat(cypherStatements.stream().map(s -> s.getStatementId()).collect(Collectors.toList()))
        .containsExactly("test-statement-2", "test-statement-1", "test-statement-3");
  }
}
