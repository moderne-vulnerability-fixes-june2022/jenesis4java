package net.sourceforge.jenesis4java.impl.test;

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
     * Returns the result of the call to {@code Codeable#toCode(CodeWriter out)} on the
     * specified {@code Codeable} as a {@code String}.
     * @param c the {@code Codeable} on which to call the {@code toCode} method.
     * @return the result of the call as a {@code String}.
     */
    public static String toCodeAsString(Codeable c)  {
        StringWriter stringWriter = new StringWriter();
        MCodeWriter codeWriter = new MCodeWriter(new PrintWriter(stringWriter));
        c.toCode(codeWriter);
        return stringWriter.toString();
    }
}
