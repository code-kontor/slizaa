/**
 * slizaa-hierarchicalgraph-graphdb-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.model.impl;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import org.neo4j.driver.Result;
import org.neo4j.driver.types.Node;

import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class InternalQueryUtils {

    private static final String BATCH_UPDATE_QUERY = "MATCH (p) where id(p) in { ids } RETURN p";

    private static final String SORT_NODES_QUERY = "MATCH (p) where id(p) in { ids } RETURN id(p) as id ORDER BY p.name";


    public static List<HGCoreDependency> sortCoreDependencies(Collection<HGCoreDependency> dependenciesToSort) {

        checkNotNull(dependenciesToSort);

        if (dependenciesToSort.size() > 0) {

            IBoltClient boltClient = dependenciesToSort.iterator().next().getRootNode().getExtension(IBoltClient.class);

            Map<Long, List<HGCoreDependency>> nodeIdsToDependencies = new HashMap<>();
            dependenciesToSort.forEach(dep -> nodeIdsToDependencies.computeIfAbsent((Long) dep.getFrom().getIdentifier(), id -> new ArrayList<>()).add(dep));

            // query
            Map<String, Object> params = new HashMap<>();
            params.put("ids", nodeIdsToDependencies.keySet());

            List<HGCoreDependency> orderedList = new ArrayList<>();
            boltClient.syncExecAndConsume(SORT_NODES_QUERY, params, result ->
                    result.forEachRemaining(record -> {
                        Long id = Long.valueOf(record.get("id").asLong());
                        orderedList.addAll(nodeIdsToDependencies.get(id));
                    })
            );

            return orderedList;
        }

        return Collections.emptyList();
    }

    public static List<HGNode> sortNodes(List<HGNode> nodesToSort) {

        checkNotNull(nodesToSort);

        if (nodesToSort.size() > 0) {

            IBoltClient boltClient = nodesToSort.get(0).getRootNode().getExtension(IBoltClient.class);

            Map<Long, HGNode> nodes = new HashMap<>();
            nodesToSort.forEach((n) -> nodes.put((Long) n.getIdentifier(), n));

            // query
            Map<String, Object> params = new HashMap<>();
            params.put("ids", nodes.keySet());
            List<Long> sortedIds = boltClient.syncExecCypherQuery(SORT_NODES_QUERY, params, result -> result.stream().map(record -> Long.valueOf(record.get("id").asLong())).collect(Collectors.toList()));

            //
            List<HGNode> orderedList = new ArrayList<>(nodesToSort.size());
            boltClient.syncExecAndConsume(SORT_NODES_QUERY, params, result ->
                    result.forEachRemaining(record -> {
                        Long id = Long.valueOf(record.get("id").asLong());
                        orderedList.add(nodes.get(id));
                    })
            );

            return orderedList;
        }

        return Collections.emptyList();
    }

    /**
     * <p>
     * </p>
     *
     * @param hgNodes
     */
    public static void loadLabelsAndProperties(List<HGNode> hgNodes) {

        checkNotNull(hgNodes);

        if (hgNodes.size() > 0) {

            IBoltClient boltClient = hgNodes.get(0).getRootNode().getExtension(IBoltClient.class);

            Map<Long, HGNode> nodes = new HashMap<>();
            hgNodes.forEach((n) -> nodes.put((Long) n.getIdentifier(), n));

            // query
            Map<String, Object> params = new HashMap<>();
            params.put("ids", nodes.keySet());
            boltClient.syncExecAndConsume(BATCH_UPDATE_QUERY, params, result -> {
                result.forEachRemaining(record -> {
                    Node node = record.get(0).asNode();
                    HGNode hgNode = nodes.get(Long.valueOf(node.id()));

                    // set the labels and properties
                    ((ExtendedGraphDbNodeSourceImpl) hgNode.getNodeSource()).getTrait().setLabels(node);
                    ((ExtendedGraphDbNodeSourceImpl) hgNode.getNodeSource()).getTrait().setProperties(node);
                });
            });
        }
    }
}
