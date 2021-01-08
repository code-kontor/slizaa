/**
 * slizaa-hierarchicalgraph-graphdb-mapping-service - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.internal;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.core.progressmonitor.IProgressMonitor;
import io.codekontor.slizaa.core.progressmonitor.NullProgressMonitor;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import io.codekontor.slizaa.hierarchicalgraph.core.model.INodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.impl.ExtendedHGRootNodeImpl;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IAutoExpandInterceptor;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeComparator;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.INodeLabelProvider;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.cypher.IBoltClientAware;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.IMappingService;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.MappingException;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IDependencyDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IHierarchyDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.IMappingProvider;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbHierarchicalgraphFactory;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbRootNodeSource;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.internal.GraphFactoryFunctions.*;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class DefaultMappingService implements IMappingService {
  
    /**
     * create the node source creator function
     */
    static Function<Long, INodeSource> createNodeSourceFunction = (id) -> {

        // create the node source
        INodeSource nodeSource = GraphDbHierarchicalgraphFactory.eINSTANCE
                .createGraphDbNodeSource();
        nodeSource.setIdentifier(id);

        // return the result
        return nodeSource;
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public HGRootNode convert(IMappingProvider mappingDescriptor, final IBoltClient boltClient,
                              IProgressMonitor progressMonitor) throws MappingException {

        checkNotNull(mappingDescriptor);
        checkNotNull(boltClient);

        //
        if (progressMonitor == null) {
            progressMonitor = new NullProgressMonitor();
        }

        try {
            // create the root element
            final HGRootNode rootNode = HierarchicalgraphFactory.eINSTANCE.createHGRootNode();
            rootNode.registerExtension(IBoltClient.class, boltClient);
            GraphDbRootNodeSource rootNodeSource = GraphDbHierarchicalgraphFactory.eINSTANCE.createGraphDbRootNodeSource();
            rootNodeSource.setIdentifier(-1l);
            rootNodeSource.setBoldClient(boltClient);
            rootNode.setNodeSource(rootNodeSource);

            // process root, hierarchy and dependency queries
            IHierarchyDefinitionProvider hierarchyProvider = initializeBoltClientAwareMappingProviderComponent(
                    mappingDescriptor.getHierarchyDefinitionProvider(), boltClient, progressMonitor);

            if (hierarchyProvider != null) {

                //
                progressMonitor.step("Requesting root nodes...");
                List<Long> rootNodes = hierarchyProvider.getToplevelNodeIds();

                createFirstLevelElements(rootNodes.toArray(new Long[0]), rootNode, createNodeSourceFunction, progressMonitor.subTask("Creating root nodes...")
                    .withParentConsumptionInPercentage(15)
                    .withTotalWorkTicks(100)
                    .create());

                //
                progressMonitor.step("Requesting nodes...");
                List<Long[]> parentChildNodeIds = hierarchyProvider.getParentChildNodeIds();

                createHierarchy(parentChildNodeIds, rootNode, createNodeSourceFunction, progressMonitor.subTask("Creating nodes...")
                    .withParentConsumptionInPercentage(40)
                    .withTotalWorkTicks(100)
                    .create());

                // filter 'dangling' nodes
                removeDanglingNodes(rootNode);

                //
                IDependencyDefinitionProvider dependencyProvider = initializeBoltClientAwareMappingProviderComponent(
                        mappingDescriptor.getDependencyDefinitionProvider(), boltClient, progressMonitor);

                if (dependencyProvider != null) {

                    //
                    createDependencies(dependencyProvider.getDependencies(), rootNode,
                            (id, type) -> GraphFactoryFunctions.createDependencySource(id, type, null), false, progressMonitor.subTask("Creating dependencies...")
                            .withParentConsumptionInPercentage(45)
                            .withTotalWorkTicks(100)
                            .create());
                }
            }

            // register default extensions
            rootNode.registerExtension(IProxyDependencyResolver.class, new CustomProxyDependencyResolver());
            rootNode.registerExtension(IMappingProvider.class, mappingDescriptor);
            rootNode.registerExtension(INodeComparator.class, mappingDescriptor.getNodeComparator());
            rootNode.registerExtension(ILabelDefinitionProvider.class, mappingDescriptor.getLabelDefinitionProvider());
            rootNode.registerExtension(INodeLabelProvider.class, new NodeLabelProviderAdapter(mappingDescriptor.getLabelDefinitionProvider()));

            //
            rootNode.registerExtension(IAutoExpandInterceptor.class, node -> {

                Optional<GraphDbNodeSource> nodeSource = node.getNodeSource(GraphDbNodeSource.class);
                if (nodeSource.isPresent()) {
                    // TODO
                    return nodeSource.get().getLabels().contains("Resource");
                }
                return false;
            });

            /**************************************************/
            IGraphModifier graphModifier = new GraphModifier();
            graphModifier.modify(rootNode);
            /****************************************************/

            //
            return rootNode;
        }
        //
        catch (Exception e) {
            throw new MappingException(e.getMessage(), e);
        }
    }

    /**
     * <p>
     * </p>
     *
     * @param boltClient
     * @param progressMonitor
     * @throws Exception
     */
    private <T> T initializeBoltClientAwareMappingProviderComponent(T component, final IBoltClient boltClient,
                                                                    IProgressMonitor progressMonitor) throws Exception {

        if (component instanceof IBoltClientAware) {
            ((IBoltClientAware) component).initialize(boltClient, progressMonitor);
        }

        return component;
    }

    /**
     * <p>
     * </p>
     *
     * @param rootNode
     */
    private void removeDanglingNodes(final HGRootNode rootNode) {

        //
        List<Object> nodeKeys2Remove = ((ExtendedHGRootNodeImpl) rootNode).getIdToNodeMap().entrySet().stream()
                .filter((n) -> {
                    try {
                        return !new Long(0).equals(n.getValue().getIdentifier()) && n.getValue().getRootNode() == null;
                    } catch (Exception e) {
                        return true;
                    }
                }).map(n -> n.getKey()).collect(Collectors.toList());

        //
        nodeKeys2Remove.forEach(k -> ((ExtendedHGRootNodeImpl) rootNode).getIdToNodeMap().remove(k));
    }
}
