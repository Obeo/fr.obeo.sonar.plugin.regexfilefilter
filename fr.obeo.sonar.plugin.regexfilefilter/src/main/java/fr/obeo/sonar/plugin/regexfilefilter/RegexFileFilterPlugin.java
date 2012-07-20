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

import java.util.Arrays;
import java.util.List;

import org.sonar.api.Plugin;
import org.sonar.api.Properties;
import org.sonar.api.Property;

/**
 * This sonar plugin adds the ability to remove from analysis any files which contain a given regular expression.
 * <p>
 * For example, this can be used to prevent the analysis of a file which contains the "@generated" String...
 * </p>
 *
 * @author Laurent Goubet
 */
@Properties({
    @Property(key=RegexFileFilterConstants.FILTER_PROPERTY, name="File Exclusion Filter",
        description = "Source files that contains this regex will be excluded from the analysis. A typical example would be \"@generated\" to exclude all files that contain \"@generated\".",
        global = true, project = true, module = true)
})
public final class RegexFileFilterPlugin implements Plugin {
    public String getKey() {
        return "obeo.filefilter";
    }

    public String getName() {
        return RegexFileFilterConstants.PLUGIN_NAME;
    }

    public String getDescription() {
        return "Allows users to exclude source files containing a given regular expression";
    }

    public List getExtensions() {
        return Arrays.asList(RegexFileFilter.class);
    }
}
