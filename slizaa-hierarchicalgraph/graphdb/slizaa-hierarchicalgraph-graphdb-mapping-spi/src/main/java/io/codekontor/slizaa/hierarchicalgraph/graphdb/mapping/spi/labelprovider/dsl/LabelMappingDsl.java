/**
 * slizaa-hierarchicalgraph-graphdb-mapping-spi - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.labelprovider.dsl;

import io.codekontor.slizaa.hierarchicalgraph.core.model.DefaultNodeSource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.INodeSource;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.mapping.spi.ILabelDefinitionProvider.OverlayPosition;
import io.codekontor.slizaa.hierarchicalgraph.graphdb.model.GraphDbNodeSource;

import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <p>
 *
 * </p>
 */
public class LabelMappingDsl {

  /**
   * -
   */
  private Function<HGNode, ILabelAndPropertyProvider> _labelAndPropertyProviderAdapterFunction;

  /**
   * @param labelAndPropertyProviderAdapterFunction
   */
  public LabelMappingDsl(Function<HGNode, ILabelAndPropertyProvider> labelAndPropertyProviderAdapterFunction) {
    _labelAndPropertyProviderAdapterFunction = checkNotNull(labelAndPropertyProviderAdapterFunction);
  }

  /**
   *
   */
  public LabelMappingDsl() {
    _labelAndPropertyProviderAdapterFunction = hgNode ->

        new ILabelAndPropertyProvider() {

          @Override
          public Map<String, String> getProperties() {
            INodeSource nodeSource = hgNode.getNodeSource();
            if (nodeSource instanceof GraphDbNodeSource) {
              return ((GraphDbNodeSource)nodeSource).getProperties().map();
            }
            else if (nodeSource instanceof DefaultNodeSource) {
              return ((DefaultNodeSource)nodeSource).getProperties();
            }
            else {
              return Collections.emptyMap();
            }
          }

          @Override
          public Collection<String> getLabels() {
            INodeSource nodeSource = hgNode.getNodeSource();
            if (nodeSource instanceof GraphDbNodeSource) {
              return ((GraphDbNodeSource)nodeSource).getLabels();
            }
            else if (nodeSource instanceof DefaultNodeSource) {
              return  ((DefaultNodeSource)nodeSource).getLabels();
            }
            return Collections.emptyList();
          }
        };
  }

  /**
   * <p>
   * </p>
   *
   * @param path
   * @return
   */
  public ILabelDefinitionProcessor setBaseImage(String path) {
    return (node, labelDefinition) -> labelDefinition.setBaseImage(checkNotNull(path));
  }

  /**
   * <p>
   * </p>
   *
   * @param path
   * @param overlayPosition
   * @return
   */
  public ILabelDefinitionProcessor setOverlayImage(String path, OverlayPosition overlayPosition) {
    return (node, labelDefinition) -> labelDefinition.setOverlayImage(checkNotNull(path),
        checkNotNull(overlayPosition));
  }

  public ILabelDefinitionProcessor setLabelText(String textLabel) {
    return (node, labelDefinition) -> labelDefinition.setText(textLabel);
  }
  
  public ILabelDefinitionProcessor setIsOverlayImage(boolean isOverlayImage) {
    return (node, labelDefinition) -> labelDefinition.setIsOverlayImage(isOverlayImage);
  }

  /**
   * <p>
   * </p>
   *
   * @param string
   * @return
   */
  public Function<HGNode, Boolean> nodeHasLabel(String string) {
    return (node) -> nodeSource(node).getLabels().contains(string);
  }

  /**
   * <p>
   * </p>
   *
   * @param
   * @return
   */
  public Function<HGNode, Boolean> nodeHasProperty(String propertyName) {
    checkNotNull(propertyName);
    return (node) -> nodeSource(node).getProperties().containsKey(propertyName);
  }

  /**
   * <p>
   * </p>
   *
   * @param propertyName
   * @param value
   * @return
   */
  public Function<HGNode, Boolean> nodeHasPropertyWithValue(String propertyName, String value) {
    checkNotNull(propertyName);
    return (node) -> nodeSource(node).getProperties().containsKey(propertyName)
        && nodeSource(node).getProperties().get(propertyName).equals(value);
  }

  /**
   * <p>
   * </p>
   *
   * @param containsLabel
   * @return
   */
  public LabelMappingConditionBuilder when(Function<HGNode, Boolean> containsLabel) {
    return new LabelMappingConditionBuilder(checkNotNull(containsLabel));
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public ILabelDefinitionProcessor doNothing() {
    return (n, l) -> {
    };
  }

  /**
   * <p>
   * </p>
   *
   * @return
   */
  public ExclusiveChoiceBuilder exclusiveChoice() {
    return new ExclusiveChoiceBuilder();
  }

  /**
   * <p>
   * </p>
   *
   * @param processors
   * @return
   */
  public ILabelDefinitionProcessor executeAll(ILabelDefinitionProcessor... processors) {
    checkNotNull(processors);

    //
    return (n, l) -> {
      for (ILabelDefinitionProcessor labelDefinitionProcessor : processors) {
        labelDefinitionProcessor.processLabelDefinition(n, l);
      }
    };
  }

  /**
   * <p>
   * </p>
   *
   * @param textMapper
   * @return
   */
  protected ILabelDefinitionProcessor setLabelText(Function<HGNode, String> textMapper) {
    return (hgNode, labelDefinition) -> {
      if (textMapper != null) {
        String text = textMapper.apply(hgNode);
        if (text != null) {
          labelDefinition.setText(text);
        }
        return;
      }
      labelDefinition.setText(String.format("<<LABEL_MAPPING_IS_NULL: (%s) >>", hgNode.getIdentifier()));
    };
  }

  /**
   * <p>
   * </p>
   *
   * @param key
   * @return
   */
  protected Function<HGNode, String> propertyValue(String key) {
    checkNotNull(key);
    return hgNode -> {

      // try to read the requested value
      Map<String, String> properties = nodeSource(hgNode).getProperties();
      if (properties != null) {
        String value = nodeSource(hgNode).getProperties().get(key);
        if (value != null) {
          return value;
        }
      }

      // returns the default value for unspecified labels
      return String.format("<<UNKNOWN_PROPERTY_KEY: %s (%s) >>", key, hgNode.getIdentifier());
    };
  }

  /**
   * <p>
   * </p>
   *
   * @param key
   * @param formatter
   * @return
   */
  protected Function<HGNode, String> propertyValue(String key, Function<String, String> formatter) {
    checkNotNull(key);
    checkNotNull(formatter);
    return propertyValue(key)
        .andThen(
            value -> value != null && !value.startsWith("<<UNKNOWN_PROPERTY_KEY:") ? formatter.apply(value) : value);
  }

  /**
   * <p>
   * </p>
   *
   * @param node
   * @return
   */
  private ILabelAndPropertyProvider nodeSource(HGNode node) {
    return _labelAndPropertyProviderAdapterFunction.apply(node);
  }
}
