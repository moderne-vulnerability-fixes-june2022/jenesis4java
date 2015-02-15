package net.sourceforge.jenesis4java;

/*
 * #%L
 * Jenesis 4 Java Code Generator
 * %%
 * Copyright (C) 2000 - 2015 jenesis4java
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * #L%
 */

import java.util.List;

/**
 * Copyright (C) 2008, 2010 Richard van Nieuwenhoven - ritchie [at] gmx [dot] at
 * Copyright (C) 2000, 2001 Paul Cody Johnston - pcj@inxar.org <br>
 * This file is part of Jenesis4java. Jenesis4java is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.<br>
 * Jenesis4java is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.<br>
 * You should have received a copy of the GNU Lesser General Public License
 * along with Jenesis4java. If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * <code>Comment</code> subinterface for documentation comments (javadoc).
 */
public interface DocumentationComment extends Comment {

    /**
     * Adds a <code>param</code> tag to the list of <code>param</code> tags.
     */
    DocumentationComment addParam(String s);

    /**
     * Adds a <code>see</code> tag to the list of <code>see</code> tags.
     */
    DocumentationComment addSee(String s);

    /**
     * Returns the <code>author</code> tag.
     */
    String getAuthor();

    /**
     * Returns the <code>date</code> tag.
     */
    String getDate();

    /**
     * Returns the <code>deprecated</code> tag.
     */
    String getDeprecated();

    /**
     * Returns the <code>exception</code> tag.
     */
    String getException();

    /**
     * Returns an <code>List</code> of <code>String</code> over the list of
     * <code>param</code> tags.
     */
    List<String> getParams();

    /**
     * Returns the <code>return</code> tag.
     */
    String getReturn();

    /**
     * Returns an <code>List</code> of <code>String</code> over the list of
     * <code>see</code> tags.
     */
    List<String> getSees();

    /**
     * Returns the <code>serial</code> tag.
     */
    String getSerial();

    /**
     * Returns the <code>serialData</code> tag.
     */
    String getSerialData();

    /**
     * Returns the <code>serialField</code> tag.
     */
    String getSerialField();

    /**
     * Returns the <code>since</code> tag.
     */
    String getSince();

    /**
     * Returns the <code>throws</code> tag.
     */
    String getThrows();

    /**
     * Returns the <code>version</code> tag.
     */
    String getVersion();

    /**
     * Sets the <code>author</code> tag.
     */
    DocumentationComment setAuthor(String s);

    /**
     * Sets the <code>datew</code> tag.
     */
    DocumentationComment setDate(String s);

    /**
     * Sets the <code>deprecated</code> tag.
     */
    DocumentationComment setDeprecated(String s);

    /**
     * Sets the <code>exception</code> tag.
     */
    DocumentationComment setException(String s);

    /**
     * Sets the <code>return</code> tag.
     */
    DocumentationComment setReturn(String s);

    /**
     * Sets the <code>serial</code> tag.
     */
    DocumentationComment setSerial(String s);

    /**
     * Sets the <code>serialData</code> tag.
     */
    DocumentationComment setSerialData(String s);

    /**
     * Sets the <code>serialField</code> tag.
     */
    DocumentationComment setSerialField(String s);

    /**
     * Sets the <code>since</code> tag.
     */
    DocumentationComment setSince(String s);

    /**
     * Sets the <code>throws</code> tag.
     */
    DocumentationComment setThrows(String s);

    /**
     * Sets the <code>version</code> tag.
     */
    DocumentationComment setVersion(String s);
}
