package io.codekontor.slizaa.server.service.provisioning.model.diff;

import java.util.List;

import com.google.common.collect.MapDifference.ValueDifference;
import io.codekontor.slizaa.server.service.provisioning.model.descr.GraphDatabaseDescr;
import io.codekontor.slizaa.server.service.provisioning.model.request.GraphDatabaseRequest;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd@gerd-wuetherich.de)
 */
public interface IServerConfigurationDiff<T> {

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<T> getComponentsToRemove();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<T> getComponentsToCreate();

  /**
   * <p>
   * </p>
   *
   * @return
   */
  List<ValueDifference<T>> getComponentsToModify();
}
