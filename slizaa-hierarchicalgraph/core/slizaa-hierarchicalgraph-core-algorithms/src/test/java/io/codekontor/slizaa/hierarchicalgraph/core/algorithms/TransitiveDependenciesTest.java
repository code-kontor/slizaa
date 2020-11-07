/**
 * slizaa-hierarchicalgraph-core-algorithms - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.algorithms;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.junit.Test;

import com.google.common.base.Stopwatch;

import io.codekontor.slizaa.hierarchicalgraph.core.model.DefaultNodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;

/**
 * @author Gerd W&uuml;therich (gw@code-kontor.io)
 */
public class TransitiveDependenciesTest extends AbstractAlgorithmTest {

	@Test
	public void testTransitiveAccumulatedOutgoingCoreDependencies() {

		HGNode node = _graphProvider.node(3186l);
		
		assertThat(node.getAccumulatedOutgoingCoreDependencies()).hasSize(148);
		
    Stopwatch stopwatch = Stopwatch.createStarted();
		Set<HGCoreDependency> coreDependencies = TransitiveDependencyResolver.getTransitiveAccumulatedOutgoingCoreDependencies(node);
    stopwatch.stop();
    System.out.println("Elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
    
		assertThat(coreDependencies).hasSize(3845);
		
		assertThat(coreDependencies).containsAll(node.getAccumulatedOutgoingCoreDependencies());
		
		Set<String> nodeLabels = coreDependencies.stream().distinct()
        .flatMap(dep -> dep.getTo().getPredecessors().stream()).distinct()
        .map(n -> n.getNodeSource(DefaultNodeSource.class).get().getProperties().get("fqn"))
		    .filter(str -> str != null)
		    .collect(Collectors.toSet());
		
		assertThat(nodeLabels).containsExactlyInAnyOrder(
		    "org.mapstruct.ap.shaded.freemarker.core",
		    "org.mapstruct.ap.shaded.freemarker.cache",
		    "org.mapstruct.ap.shaded.freemarker.ext.beans",
		    "org.mapstruct.ap.shaded.freemarker.template",
		    "org.mapstruct.ap.shaded.freemarker.debug.impl",
		    "org.mapstruct.ap.shaded.freemarker.template.utility",
		    "org.mapstruct.ap.internal.prism",
		    "org.mapstruct.ap.internal.model.source",
		    "org.mapstruct.ap.shaded.freemarker.ext.dom",
		    "org.mapstruct.ap.internal.model.common",
		    "org.mapstruct.ap.internal.writer",
		    "org.mapstruct.ap.internal.util",
		    "org.mapstruct.ap.shaded.freemarker.ext.util",
		    "org.mapstruct.ap.spi",
		    "org.mapstruct.ap.shaded.freemarker.log",
		    "org.mapstruct.ap.shaded.freemarker.debug",
		    "org.mapstruct.ap.internal.util.workarounds",
		    "org.mapstruct.ap.internal.version"
		    );
	} 
	
	 @Test
	  public void testTransitiveAccumulatedIncomingCoreDependencies() {

	    HGNode node = _graphProvider.node(3186l);
	    
	    assertThat(node.getAccumulatedIncomingCoreDependencies()).hasSize(164);
	    
	    Stopwatch stopwatch = Stopwatch.createStarted();
	    Set<HGCoreDependency> coreDependencies = TransitiveDependencyResolver.getTransitiveAccumulatedIncomingCoreDependencies(node);
	    stopwatch.stop();
	    
	    System.out.println("Elapsed: " + stopwatch.elapsed(TimeUnit.MILLISECONDS));
	    
	    assertThat(coreDependencies).hasSize(483);
	    
	    assertThat(coreDependencies).containsAll(node.getAccumulatedIncomingCoreDependencies());
	    
	    Set<String> nodeLabels = coreDependencies.stream().distinct()
	        .flatMap(dep -> dep.getFrom().getPredecessors().stream())
	        .distinct().map(n -> n.getNodeSource(DefaultNodeSource.class).get().getProperties().get("fqn"))
	        .filter(str -> str != null)
	        .collect(Collectors.toSet());
	    
	    assertThat(nodeLabels).containsExactlyInAnyOrder(
	        "org.mapstruct.ap.internal.processor", 
	        "org.mapstruct.ap.internal.processor.creation", 
	        "org.mapstruct.ap.internal.model", 
	        "org.mapstruct.ap.internal.model.source.builtin", 
	        "org.mapstruct.ap.internal.model.source.selector", 
	        "org.mapstruct.ap.internal.conversion", 
	        "org.mapstruct.ap.internal.model.source"
	        );
	  } 
}
