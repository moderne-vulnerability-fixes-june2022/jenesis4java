package net.sourceforge.jenesis4java.impl;

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
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.impl.util.BlockStyle;

/**
 * Base class for the block style classes.
 */
abstract class MStyle implements BlockStyle {

    static class MStyleMap {

        Properties p;

        MStyleMap(Properties p) {
            this.p = p;
            initStyles();
            initMap();
        }

        void addStyle(String key, String className) {
            try {
                // System.out.println("putting class for "+key);
                p.put(key, Class.forName(className).newInstance());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        BlockStyle get(String key) {
            return (BlockStyle) p.get(key);
        }

        void initMap() {
            // get all the keys
            Enumeration<?> e = p.propertyNames();

            String key;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (key.startsWith("style.")) {
                    continue;
                }
                // remap the object
                // System.out.println("getting key "+key+", property "+p.getProperty(key));
                Object remappedValue = p.get(p.getProperty(key));
                if (remappedValue != null) {
                    p.put(key, remappedValue);
                }
            }
        }

        void initStyles() {
            // get all the keys
            Enumeration<?> e = p.propertyNames();

            String key;
            while (e.hasMoreElements()) {
                key = (String) e.nextElement();
                if (key.startsWith("style.")) {
                    addStyle(key, p.getProperty(key));
                }
            }
        }
    }

    static class Lambda extends MStyle {

        @Override
        public void toCode(CodeWriter out, List<? extends Codeable> codeableList) {
            if (codeableList.size() > 0) {
                out.write('{').indentLine().write(codeableList).dedentLine().write('}');
            } else {
                out.write('{').newLine().write('}');
            }
        }
    }

    static class Optional extends MStyle {

        @Override
        public void toCode(CodeWriter out, List<? extends Codeable> codeableList) {
            if (codeableList.size() == 1) {
                out.indentLine().write(codeableList).dedentLine();
            } else {
                if (codeableList.size() != 0) {
                    out.space().write('{').indentLine().write(codeableList).dedentLine().write('}');
                } else {
                    out.space().write('{').newLine().write('}');
                }
            }
        }
    }

    static class SameLine extends MStyle {

        @Override
        public void toCode(CodeWriter out, List<? extends Codeable> codeableList) {
            if (codeableList.size() > 0) {
                out.space().write('{').indentLine().write(codeableList).dedentLine().write('}').space();
            } else {
                out.space().write('{').newLine().write('}');
            }
        }
    }

    static class Standard extends MStyle {

        @Override
        public void toCode(CodeWriter out, List<? extends Codeable> codeableList) {
            if (!out.isLineNew()) {
                out.newLine();
            }
            if (codeableList.size() > 0) {
                out.write('{').indentLine().write(codeableList).dedentLine().write('}');
            } else {
                out.write('{').newLine().write('}');
            }
        }
    }

    MStyle() {
    }
}
