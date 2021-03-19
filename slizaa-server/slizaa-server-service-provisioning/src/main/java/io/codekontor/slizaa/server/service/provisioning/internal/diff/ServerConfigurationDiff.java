package io.codekontor.slizaa.server.service.provisioning.internal.diff;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.MapDifference.ValueDifference;
import io.codekontor.slizaa.server.service.provisioning.model.diff.IServerConfigurationDiff;

public class ServerConfigurationDiff<T> implements IServerConfigurationDiff<T> {

  /** - */
  private List<T>                  _componentsToRemove;

  /** - */
  private List<T>                  _componentsToCreate;

  /** - */
  private List<ValueDifference<T>> _componentsToModify;


  /**
   * <p>
   * Creates a new instance of type {@link ServerConfigurationDiff}.
   * </p>
   */
  public ServerConfigurationDiff(Collection<T> componentsToRemove,
                                 Collection<T> componentsToCreate,
                                 Collection<ValueDifference<T>> componentsToModify) {

    checkNotNull(componentsToRemove);
    checkNotNull(componentsToCreate);
    checkNotNull(componentsToModify);

    _componentsToRemove = Collections.unmodifiableList(new LinkedList<>(componentsToRemove));
    _componentsToCreate = Collections.unmodifiableList(new LinkedList<>(componentsToCreate));
    _componentsToModify = Collections.unmodifiableList(new LinkedList<>(componentsToModify));
  }

  @Override
  public List<T> getComponentsToRemove() {
    return _componentsToRemove;
  }

  @Override
  public List<T> getComponentsToCreate() {
    return _componentsToCreate;
  }

  @Override
  public List<ValueDifference<T>> getComponentsToModify() {
    return _componentsToModify;
  }
}
