/**
 * slizaa-hierarchicalgraph-core-model - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.model.simple;

import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewCoreDependency;
import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewNode;
import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewProxyDependency;
import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.createNewRootNode;

import java.util.function.Supplier;

import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import io.codekontor.slizaa.hierarchicalgraph.core.model.CustomFactoryStandaloneSupport;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGCoreDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGProxyDependency;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HGRootNode;
import io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactory;
import io.codekontor.slizaa.hierarchicalgraph.core.model.IDependencySource;
import io.codekontor.slizaa.hierarchicalgraph.core.model.INodeSource;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class SimpleTestModelRule implements TestRule {

    //
    private HGNode _a1;

    //
    private HGNode _b1;

    //
    private HGNode _b2;

    //
    private HGNode _a2;

    //
    private HGNode _a3;

    //
    private HGNode _b3;

    //
    private HGNode _a4;

    //
    private HGNode _b4;

    //
    private HGCoreDependency _dep_a1_b1_core1;

    //
    private HGCoreDependency _dep_a1_b1_core2;

    //
    private HGCoreDependency _dep_a2_b2_core1;

    //
    private HGProxyDependency _dep_a3_b3_proxy1;

    //
    private HGRootNode _rootNode;


    /**
     * {@inheritDoc}
     */
    @Override
    public Statement apply(Statement base, Description description) {

        return new Statement() {

            @Override
            public void evaluate() throws Throwable {

                //
                init();

                //
                base.evaluate();
            }
        };
    }

    /**
     * <p>
     * </p>
     *
     * @return
     */
    public HGRootNode root() {
        return this._rootNode;
    }

    /**
     * <p>
     * </p>
     *
     * @return the a1
     */
    public HGNode a1() {
        return this._a1;
    }

    /**
     * <p>
     * </p>
     *
     * @return the b1
     */
    public HGNode b1() {
        return this._b1;
    }

    /**
     * <p>
     * </p>
     *
     * @return the b2
     */
    public HGNode b2() {
        return this._b2;
    }

    /**
     * <p>
     * </p>
     *
     * @return the a2
     */
    public HGNode a2() {
        return this._a2;
    }

    /**
     * <p>
     * </p>
     *
     * @return the a3
     */
    public HGNode a3() {
        return this._a3;
    }

    /**
     * <p>
     * </p>
     *
     * @return the b3
     */
    public HGNode b3() {
        return this._b3;
    }

    /**
     * <p>
     * </p>
     *
     * @return the dep_a1_b1_core1
     */
    public HGCoreDependency a1_b1_core1() {
        return this._dep_a1_b1_core1;
    }

    /**
     * <p>
     * </p>
     *
     * @return the dep_a1_b1_core2
     */
    public HGCoreDependency a1_b1_core2() {
        return this._dep_a1_b1_core2;
    }

    /**
     * <p>
     * </p>
     *
     * @return the dep_a2_b2_core1
     */
    public HGCoreDependency a2_b2_core1() {
        return this._dep_a2_b2_core1;
    }

    /**
     * <p>
     * </p>
     *
     * @return the dep_a3_b3_proxy1
     */
    public HGProxyDependency dep_a3_b3_proxy1() {
        return this._dep_a3_b3_proxy1;
    }

    /**
     * <p>
     * </p>
     */
    private void init() {

        //
        CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();

        //
        Supplier<INodeSource> nodeSourceSupplier = () -> HierarchicalgraphFactory.eINSTANCE.createDefaultNodeSource();
        Supplier<IDependencySource> dependencySourceSupplier = () -> HierarchicalgraphFactory.eINSTANCE
                .createDefaultDependencySource();

        this._rootNode = createNewRootNode(nodeSourceSupplier);

        this._a1 = createNewNode(this._rootNode, this._rootNode, nodeSourceSupplier);
        this._b1 = createNewNode(this._rootNode, this._rootNode, nodeSourceSupplier);

        this._a2 = createNewNode(this._rootNode, this._a1, nodeSourceSupplier);
        this._b2 = createNewNode(this._rootNode, this._b1, nodeSourceSupplier);

        this._a3 = createNewNode(this._rootNode, this._a2, nodeSourceSupplier);
        this._b3 = createNewNode(this._rootNode, this._b2, nodeSourceSupplier);

        this._a4 = createNewNode(this._rootNode, this._a3, nodeSourceSupplier);
        this._b4 = createNewNode(this._rootNode, this._b3, nodeSourceSupplier);

        this._dep_a1_b1_core1 = createNewCoreDependency(this._a1, this._b1, "CORE_DEP", dependencySourceSupplier, false);
        this._dep_a1_b1_core2 = createNewCoreDependency(this._a1, this._b1, "CORE_DEP", dependencySourceSupplier, false);
        this._dep_a2_b2_core1 = createNewCoreDependency(this._a2, this._b2, "CORE_DEP", dependencySourceSupplier, false);

        this._dep_a3_b3_proxy1 = createNewProxyDependency(this._a3, this._b3, "PROXY_DEP", dependencySourceSupplier, false);

        this._rootNode.registerExtension(IProxyDependencyResolver.class, new TestProxyDependencyResolver());
    }

    private class TestProxyDependencyResolver implements IProxyDependencyResolver {

        @Override
        public IProxyDependencyResolverJob resolveProxyDependency(HGProxyDependency dependencyToResolve) {

            HGCoreDependency newDependency_1 = createNewCoreDependency(dependencyToResolve.getFrom(), dependencyToResolve.getTo(),
                    "NEW_USAGE_1", () -> HierarchicalgraphFactory.eINSTANCE.createDefaultDependencySource(), false);

            HGCoreDependency newDependency_2 = createNewCoreDependency(dependencyToResolve.getFrom(), dependencyToResolve.getTo(),
                    "NEW_USAGE_2", () -> HierarchicalgraphFactory.eINSTANCE.createDefaultDependencySource(), false);

            dependencyToResolve.getAccumulatedCoreDependencies().add(newDependency_1);
            dependencyToResolve.getAccumulatedCoreDependencies().add(newDependency_2);

            // return null as we resolved the dependencies immediately
            return null;
        }
    }
}
