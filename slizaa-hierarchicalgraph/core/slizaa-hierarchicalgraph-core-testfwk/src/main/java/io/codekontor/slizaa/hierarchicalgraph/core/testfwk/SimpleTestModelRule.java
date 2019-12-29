/**
 * slizaa-hierarchicalgraph-core-testfwk - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.hierarchicalgraph.core.testfwk;

import static io.codekontor.slizaa.hierarchicalgraph.core.model.HierarchicalgraphFactoryFunctions.*;

import java.util.function.Supplier;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import io.codekontor.slizaa.hierarchicalgraph.core.model.*;
import io.codekontor.slizaa.hierarchicalgraph.core.model.spi.IProxyDependencyResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * </p>
 *
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public class SimpleTestModelRule implements TestRule {

    private static Logger logger = LoggerFactory.getLogger(SimpleTestModelRule.class);

    //
    private HGNode _a1;

    //
    private HGNode _b1;

    //
    private HGNode _b2;

    private HGNode _a22;

    //
    private HGNode _a2;

    private HGNode _b22;

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
    private HGCoreDependency _dep_a2_b22_core1;

    //
    private HGCoreDependency _dep_a22_b22_core1;

    //
    private HGProxyDependency _dep_a3_b3_proxy1;

    //
    private HGCoreDependency _dep_a4_b4_core1;

    //
    private HGCoreDependency _dep_a4_b4_core2;

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

    public HGNode a4() {
        return _a4;
    }

    public HGNode b4() {
        return _b4;
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

    public HGNode a22() {
        return _a22;
    }

    public HGNode b22() {
        return _b22;
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

    public HGCoreDependency a22_b22_core1() {
        return _dep_a22_b22_core1;
    }

    public HGCoreDependency a2_b22_core1() {
        return _dep_a2_b22_core1;
    }

    /**
     * <p>
     * </p>
     *
     * @return the dep_a3_b3_proxy1
     */
    public HGProxyDependency a3_b3_proxy1() {
        return this._dep_a3_b3_proxy1;
    }

    public HGCoreDependency a4_b4_core1() {
        return _dep_a4_b4_core1;
    }

    public HGCoreDependency a4_b4_core2() {
        return _dep_a4_b4_core2;
    }

    /**
     * <p>
     * </p>
     */
    private void init() {

        //
        CustomFactoryStandaloneSupport.registerCustomHierarchicalgraphFactory();

        //
        Supplier<IDependencySource> dependencySourceSupplier = () -> HierarchicalgraphFactory.eINSTANCE
                .createDefaultDependencySource();

        this._rootNode = createNewRootNode(nodeSourceSupplier("root"));

        this._a1 = createNewNode(this._rootNode, this._rootNode, nodeSourceSupplier("a1"));
        this._b1 = createNewNode(this._rootNode, this._rootNode, nodeSourceSupplier("b1"));

        this._a2 = createNewNode(this._rootNode, this._a1, nodeSourceSupplier("a2"));
        this._b2 = createNewNode(this._rootNode, this._b1, nodeSourceSupplier("b2"));

        this._a22 = createNewNode(this._rootNode, this._a1, nodeSourceSupplier("a22"));
        this._b22 = createNewNode(this._rootNode, this._b1, nodeSourceSupplier("b22"));

        this._a3 = createNewNode(this._rootNode, this._a2, nodeSourceSupplier("a3"));
        this._b3 = createNewNode(this._rootNode, this._b2, nodeSourceSupplier("b3"));

        this._a4 = createNewNode(this._rootNode, this._a3, nodeSourceSupplier("a4"));
        this._b4 = createNewNode(this._rootNode, this._b3, nodeSourceSupplier("b4"));

        this._dep_a1_b1_core1 = createNewCoreDependency(this._a1, this._b1, "CORE_DEP", dependencySourceSupplier, false);
        this._dep_a1_b1_core2 = createNewCoreDependency(this._a1, this._b1, "CORE_DEP", dependencySourceSupplier, false);
        this._dep_a2_b2_core1 = createNewCoreDependency(this._a2, this._b2, "CORE_DEP", dependencySourceSupplier, false);
        this._dep_a2_b22_core1 = createNewCoreDependency(this._a2, this._b22, "CORE_DEP", dependencySourceSupplier, false);
        this._dep_a22_b22_core1 = createNewCoreDependency(this._a22, this._b22, "CORE_DEP", dependencySourceSupplier, false);

        this._dep_a3_b3_proxy1 = createNewProxyDependency(this._a3, this._b3, "PROXY_DEP", dependencySourceSupplier, false);

        this._rootNode.registerExtension(IProxyDependencyResolver.class, new TestProxyDependencyResolver());
    }

    private Supplier<INodeSource> nodeSourceSupplier(String id) {
        return () -> {
            DefaultNodeSource defaultNodeSource = HierarchicalgraphFactory.eINSTANCE.createDefaultNodeSource();
            defaultNodeSource.setIdentifier(id);
            return defaultNodeSource;
        };
    }

    private class TestProxyDependencyResolver implements IProxyDependencyResolver {

        @Override
        public IProxyDependencyResolverJob resolveProxyDependency(HGProxyDependency dependencyToResolve) {

            logger.info("Resolving HGProxyDependency: {}", dependencyToResolve);

            _dep_a4_b4_core1 = createNewCoreDependency(_a4, _b4,
                    "NEW_USAGE_1", () -> HierarchicalgraphFactory.eINSTANCE.createDefaultDependencySource(), false);

            _dep_a4_b4_core2 = createNewCoreDependency(_a4, _b4,
                    "NEW_USAGE_2", () -> HierarchicalgraphFactory.eINSTANCE.createDefaultDependencySource(), false);

            dependencyToResolve.getAccumulatedCoreDependencies().add(_dep_a4_b4_core1);
            dependencyToResolve.getAccumulatedCoreDependencies().add(_dep_a4_b4_core2);

            // return null as we resolved the dependencies immediately
            return null;
        }
    }
}
