package net.sourceforge.jenesis4java.impl.test;

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

import net.sourceforge.jenesis4java.Codeable;
import net.sourceforge.jenesis4java.VirtualMachine;
import net.sourceforge.jenesis4java.impl.MCodeWriter;
import net.sourceforge.jenesis4java.impl.MVM;

import java.io.PrintWriter;
import java.io.StringWriter;

import static java.lang.System.lineSeparator;

/**
 * Provides convenient methods and constants for unit tests.
 */
public class TestHelper {

    public static String LS = lineSeparator();

    public static MVM vm() {
        return (MVM) MVM.getVirtualMachine();
    }

    /**
     * Returns the result of the call to {@code Codeable#toCode(CodeWriter out)}
     * on the specified {@code Codeable} as a {@code String}.
     * 
     * @param c
     *            the {@code Codeable} on which to call the {@code toCode}
     *            method.
     * @return the result of the call as a {@code String}.
     */
    public static String toCodeAsString(Codeable c) {
        StringWriter stringWriter = new StringWriter();
        MCodeWriter codeWriter = new MCodeWriter(new PrintWriter(stringWriter));
        c.toCode(codeWriter);
        return stringWriter.toString();
    }
}
