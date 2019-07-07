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

import com.google.common.base.Stopwatch;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.SourceOrTarget;
import io.codekontor.slizaa.hierarchicalgraph.core.selection.DependencySet;
import io.codekontor.slizaa.server.service.selection.ISelectionService;
import io.codekontor.slizaa.server.service.slizaa.IGraphDatabase;
import io.codekontor.slizaa.server.service.slizaa.IHierarchicalGraph;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@ShellComponent
@ShellCommandGroup("Slizaa Graph Databases Commands - TEST")
public class SlizaaTest extends AbstractGraphDatabaseCommandComponent {

    @Autowired
    private ISelectionService _selectionService;

    @ShellMethod(value = "Test.", key = "test", group = "Slizaa Graph Databases Commands - TEST")
    public String test(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier, @ShellOption({"-h", "--hierarchicalGraphId"}) String hierarchicalGraphIdentifier, @ShellOption({"-n", "--nodeId"}) long nodeID) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // check that the hierarchical graph does not exist
        IHierarchicalGraph hierarchicalGraph = graphDatabase.getHierarchicalGraph(hierarchicalGraphIdentifier);
        if (hierarchicalGraph == null) {
            return cannotExecuteCommand(String.format("The specified hierarchical graph '%s' does not exist.\n", hierarchicalGraphIdentifier));
        }

        //
        Runtime.getRuntime().gc();

        Stopwatch stopWatch = Stopwatch.createStarted();

        //
        HGRootNode rootNode = hierarchicalGraph.getRootNode();
        HGNode sourceNode = rootNode.lookupNode(410734l);
        HGNode targetNode = rootNode.lookupNode(3106332l);
        HGAggregatedDependency aggregatedDependency = sourceNode.getOutgoingDependenciesTo(targetNode);

        HGNode selectedNode = rootNode.lookupNode(nodeID);

        int size = 1;
        DependencySet[] dependencySets = new DependencySet[size];
        for (int i = 0; i < size; i++) {
            dependencySets[i] = new DependencySet(aggregatedDependency.getCoreDependencies());
        }

        System.out.println("Creation: " + stopWatch.elapsed(TimeUnit.MILLISECONDS));

        //
        DependencySet.ReferencedNodes referencedNodes = dependencySets[0].computeReferencedNodes(selectedNode, SourceOrTarget.SOURCE);
        System.out.println("referenced nodes 1: " + stopWatch.elapsed(TimeUnit.MILLISECONDS));

        //
        referencedNodes = dependencySets[0].computeReferencedNodes(selectedNode, SourceOrTarget.SOURCE);
        System.out.println("referenced nodes 2: " + stopWatch.elapsed(TimeUnit.MILLISECONDS));

        System.out.println(referencedNodes.getFilteredCoreDependencies().size());

        //
        Runtime.getRuntime().gc();

        String result = SlizaaAdmin.memoryUsage();

        // return the result
        return result;
    }

    @ShellMethod(value = "Test2.", key = "test2", group = "Slizaa Graph Databases Commands - TEST")
    public String test2(@ShellOption({"-d", "--databaseId"}) String databaseIdentifier, @ShellOption({"-h", "--hierarchicalGraphId"}) String hierarchicalGraphIdentifier, @ShellOption({"-n", "--nodeId"}) long nodeID) {

        // check the backend configuration
        String checkBackendConfigured = checkBackendConfigured();
        if (checkBackendConfigured != null) {
            return checkBackendConfigured;
        }

        // check that the requested database exists
        IGraphDatabase graphDatabase = slizaaService().getGraphDatabase(databaseIdentifier);
        if (graphDatabase == null) {
            return cannotExecuteCommand(String.format("The specified database '%s' does not exist.\n", databaseIdentifier));
        }

        // check that the hierarchical graph does not exist
        IHierarchicalGraph hierarchicalGraph = graphDatabase.getHierarchicalGraph(hierarchicalGraphIdentifier);
        if (hierarchicalGraph == null) {
            return cannotExecuteCommand(String.format("The specified hierarchical graph '%s' does not exist.\n", hierarchicalGraphIdentifier));
        }

        Stopwatch stopWatch = Stopwatch.createStarted();

        //
        HGRootNode rootNode = hierarchicalGraph.getRootNode();
        HGNode sourceNode = rootNode.lookupNode(410734l);
        HGNode targetNode = rootNode.lookupNode(3106332l);
        HGAggregatedDependency aggregatedDependency = sourceNode.getOutgoingDependenciesTo(targetNode);

        HGNode selectedNode = rootNode.lookupNode(nodeID);

        Set<HGNode> children = _selectionService.getChildrenFilteredByDependencySources(aggregatedDependency, selectedNode);
        Set<HGNode> referencedTargetNodes = _selectionService.getReferencedTargetNodes(aggregatedDependency, Collections.singleton(selectedNode), true);
        System.out.println("Computing time: " + stopWatch.elapsed(TimeUnit.MILLISECONDS));
        System.out.println("Children Size: " + children.size());
        System.out.println("Referenced Nodes Size: " + referencedTargetNodes.size());

        // return the result
        return "OK";
    }
}
