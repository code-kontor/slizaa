package io.codekontor.slizaa.hierarchicalgraph.graphdb.model.impl;

import io.codekontor.slizaa.core.boltclient.IBoltClient;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.types.Node;

import java.util.*;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkNotNull;

public class InternalQueryUtils {

    private static final String BATCH_UPDATE_QUERY = "MATCH (p) where id(p) in { ids } RETURN p";

    private static final String SORT_NODES_QUERY = "MATCH (p) where id(p) in { ids } RETURN id(p) ORDER BY p.name";


    public static List<HGCoreDependency> sortCoreDependencies(Collection<HGCoreDependency> dependenciesToSort) {

        checkNotNull(dependenciesToSort);

        if (dependenciesToSort.size() > 0) {

            IBoltClient boltClient = dependenciesToSort.iterator().next().getRootNode().getExtension(IBoltClient.class);

            Map<Long, List<HGCoreDependency>> nodeIdsToDependencies = new HashMap<>();
            dependenciesToSort.forEach(dep -> nodeIdsToDependencies.computeIfAbsent((Long) dep.getFrom().getIdentifier(), id -> new ArrayList<>()).add(dep));

            // query
            Map<String, Object> params = new HashMap<>();
            params.put("ids", nodeIdsToDependencies.keySet());
            StatementResult result = boltClient.syncExecCypherQuery(SORT_NODES_QUERY, params);

            //
            List<HGCoreDependency> orderedList = new ArrayList<>(dependenciesToSort.size());

            //
            result.forEachRemaining(record -> {
                List<HGCoreDependency> dependencies = nodeIdsToDependencies.get(new Long(record.get(0).asLong()));
                orderedList.addAll(dependencies);
            });

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
            StatementResult result = boltClient.syncExecCypherQuery(SORT_NODES_QUERY, params);

            //
            List<HGNode> orderedList = new ArrayList<>(nodesToSort.size());

            //
            result.forEachRemaining(record -> {
                HGNode hgNode = nodes.get(new Long(record.get(0).asLong()));
                orderedList.add(hgNode);
            });

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
            StatementResult result = boltClient.syncExecCypherQuery(BATCH_UPDATE_QUERY, params);

            result.forEachRemaining(record -> {
                Node node = record.get(0).asNode();
                HGNode hgNode = nodes.get(new Long(node.id()));

                // set the labels and properties
                ((ExtendedGraphDbNodeSourceImpl) hgNode.getNodeSource()).getTrait().setLabels(node);
                ((ExtendedGraphDbNodeSourceImpl) hgNode.getNodeSource()).getTrait().setProperties(node);

            });
        }
    }
}
