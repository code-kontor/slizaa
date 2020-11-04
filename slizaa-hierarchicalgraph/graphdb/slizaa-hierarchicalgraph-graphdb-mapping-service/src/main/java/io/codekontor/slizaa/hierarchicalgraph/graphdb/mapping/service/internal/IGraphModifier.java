package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.service.internal;

import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;

public interface IGraphModifier {

  /**
   * <p>
   * </p>
   *
   * @param rootNode
   * @return
   */
  HGRootNode modify(HGRootNode rootNode);
}
