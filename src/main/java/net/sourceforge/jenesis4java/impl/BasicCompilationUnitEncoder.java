package net.sourceforge.jenesis4java.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.CompilationUnitEncoder;
import net.sourceforge.jenesis4java.impl.MDeclaration.MCompilationUnit;

public class BasicCompilationUnitEncoder implements CompilationUnitEncoder {

    @Override
    public void encode(CompilationUnit compilationUnit) {

        // wrap this section for io exceptions
        PrintWriter fout = null;

        try {

            // Make a file object which corresponds to the
            // filename which we will write to.
            File file = new File(((MCompilationUnit) compilationUnit).getFileName());

            // Get the parent file such that we can create the
            // directory if necessary. NOTE: JDK1.2 dependency
            // here.
            File dir = file.getParentFile();

            // Make sure it exists.
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new RuntimeException("Could not create directory(s) " + dir.getCanonicalPath());
                }
            }

            // now we want to make the stream...
            fout = new PrintWriter(new BufferedWriter(new FileWriter(file)));

            // ...wrap it with a code writer...
            CodeWriter out = new MCodeWriter(fout);

            // ...generate the code...
            compilationUnit.toCode(out);

            // ... and finally close and drop the stream
            fout.close();
            fout = null;

        } catch (IOException ioex) {
            ioex.printStackTrace();
        } finally {
            if (fout != null) {
                try {
                    fout.close();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return;
    }

}