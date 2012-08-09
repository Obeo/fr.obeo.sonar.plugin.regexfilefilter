/*******************************************************************************
 * Copyright (c) 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package fr.obeo.sonar.plugin.regexfilefilter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.configuration.Configuration;
import org.sonar.api.batch.FileFilter;
import org.sonar.api.utils.SonarException;

/**
 * This extension of sonar's FileFilter API removes from the analysis any file which contains a given regular expression.
 * 
 * @author Laurent Goubet
 */
public class RegexFileFilter extends FileFilter {
	private final Pattern filter;

	public RegexFileFilter(Configuration conf) {
		filter = parseFilter(conf);
	}

	private Pattern parseFilter(Configuration conf) {
		final String regex = conf.getString(RegexFileFilterConstants.FILTER_PROPERTY);
		try {
			if (regex == null || regex.length() == 0) {
				return Pattern.compile("");
			}
			return Pattern.compile(regex);
		} catch (PatternSyntaxException e) {
			throw new SonarException("Parameter " + regex + " is not a valid regular expression.", e);
		}
	}

	public boolean accept(File file) {
		if (filter.pattern().length() == 0) {
			return true;
		}

		boolean accept = false;
		BufferedReader reader = null;
		try {
			// For now, use default encoding of the system
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			String line = reader.readLine();
			while (line != null && !accept) {
				Matcher matcher = filter.matcher(line);
				accept = matcher.find();
				line = reader.readLine();
			}
		} catch (IOException e) {
			throw new SonarException("Could not check content of the file " + file.getPath(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					// Swallow this one
				}
			}
		}
		return !accept;
	}
}
