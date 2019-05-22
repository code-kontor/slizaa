/**
 * slizaa-core-mvnresolver-uber - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.core.mvnresolver.uber;

/**
 *
 */
public class FilteringClassLoader extends ClassLoader {

    //
    private static final ClassLoader EXTENSION_CLASS_LOADER;

    //
    private String[] _regexps;

    static {
        EXTENSION_CLASS_LOADER = ClassLoader.getSystemClassLoader().getParent();

        try {
            ClassLoader.registerAsParallelCapable();
        } catch (NoSuchMethodError ignore) {
            // Not supported on Java 6
        }
    }

    /**
     *
     */
    private static class RetrieveSystemPackagesClassLoader extends ClassLoader {

        RetrieveSystemPackagesClassLoader(ClassLoader parent) {
            super(parent);
        }

        protected Package[] getPackages() {
            return super.getPackages();
        }
    }

    public FilteringClassLoader(ClassLoader parent, String... regexps) {
        super(parent);

        _regexps = regexps;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {

        //
        try {
            return EXTENSION_CLASS_LOADER.loadClass(name);
        } catch (ClassNotFoundException ignore) {
            // ignore
        }

        //
        if (!isClassAllowed(name)) {
            throw new ClassNotFoundException(name + " not found.");
        }

        //
        Class<?> cl = super.loadClass(name, false);
        if (resolve) {
            resolveClass(cl);
        }

        //
        return cl;
    }

    @Override
    public String toString() {
        return FilteringClassLoader.class.getSimpleName() + "(" + getParent() + ")";
    }

    /**
     * @param className
     * @return
     */
    private boolean isClassAllowed(String className) {

        //
        for (String regexp : _regexps) {
            if (className.matches(regexp)) {
                return true;
            }
        }

        //
        return false;
    }
}
