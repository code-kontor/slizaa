/**
 * slizaa-scanner-contentdefinition - Slizaa Static Software Analysis Tools
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
package io.codekontor.slizaa.scanner.contentdefinition;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;

import io.codekontor.slizaa.scanner.contentdefinition.internal.DefaultFileBasedContentInfoResolver;

/**
 * <p>
 * </p>
 * 
 * @author Gerd W&uuml;therich (gerd.wuetherich@codekontor.io)
 */
public final class NameAndVersionInfo {

	/** - */
	private String _name;

	/** - */
	private String _binaryName;

	/** - */
	private String _version;

	/** - */
	private boolean _isSource = false;

	/**
	 * <p>
	 * </p>
	 * 
	 * @param file
	 * @return
	 */
	public static NameAndVersionInfo resolveNameAndVersion(File file) {
		try {
			if (file.isFile()) {
				return extractInfoFromFile(file);
			}

			return extractInfoFromPath(file);

			//
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @return
	 */
	public String getName() {
		return _name;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @return
	 */
	public String getVersion() {
		return _version;
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @return
	 */
	public boolean isSource() {
		return _isSource;
	}

	/**
	 * @return the binaryName
	 */
	public String getBinaryName() {
		return _binaryName;
	}

	/**
	 * <p>
	 * Creates a new instance of type {@link NameAndVersionInfo}.
	 * </p>
	 * 
	 * @param name
	 * @param version
	 */
	private NameAndVersionInfo(String name, String binaryName, String version, boolean isSource) {
		checkNotNull(name);
		checkNotNull(version);

		_name = name;
		_binaryName = binaryName;
		_version = version;
		_isSource = isSource;
	}

	/**
	 * <p>
	 * </p>
	 *
	 * @param path
	 * @return
	 */
	private static NameAndVersionInfo extractInfoFromPath(File path) {

		String dirName = path.getName();
		int x = dirName.lastIndexOf('_');
		String version = "0.0.0";
		String name = dirName;

		if (x > 0) {
			name = dirName.substring(0, x);
			version = dirName.substring(x + 1);
		}

		return new NameAndVersionInfo(name, name, version, false);
	}

	/**
	 * <p>
	 * </p>
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private static NameAndVersionInfo extractInfoFromFile(File file) throws IOException {

		//
		DefaultFileBasedContentInfoResolver resolver = new DefaultFileBasedContentInfoResolver();

		//
		if (resolver.resolve(file)) {

			// return the result
			return new NameAndVersionInfo(resolver.getName(), resolver.getBinaryName(), resolver.getVersion(),
					resolver.isSource());
		}

		// return the default result
		return new NameAndVersionInfo(file.getName(), file.getName(), "0.0.0", false);
	}

}
