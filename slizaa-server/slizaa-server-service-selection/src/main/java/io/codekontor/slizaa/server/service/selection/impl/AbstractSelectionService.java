package io.codekontor.slizaa.server.service.selection.impl;

import static com.google.common.base.Preconditions.*;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGAggregatedDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.selections.DependenciesSelection;
import io.codekontor.slizaa.server.service.selection.ISelectionService;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SelectionServiceImpl implements ISelectionService {

    private Map<HGAggregatedDependency, DependenciesSelection> selectionMap;

    @Override
    public HGNode getFilteredChildren(HGNode node, HGAggregatedDependency aggregatedDependency) {

        checkNotNull(node);
        checkNotNull(aggregatedDependency);



        return null;
    }
}
