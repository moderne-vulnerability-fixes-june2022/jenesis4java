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
package net.sourceforge.jenesis4java.jaloppy;

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

import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.CompilationUnit;
import net.sourceforge.jenesis4java.CompilationUnitEncoder;
import net.sourceforge.jenesis4java.impl.MCodeWriter;
import de.hunsicker.jalopy.Jalopy;
import de.hunsicker.jalopy.Jalopy.State;
import de.hunsicker.jalopy.storage.Convention;
import de.hunsicker.jalopy.storage.ConventionDefaults;
import de.hunsicker.jalopy.storage.ConventionKeys;
import de.hunsicker.jalopy.storage.History;

/**
 * This encoder will use jalopy to format the java source file. To activate this
 * encoder set the System property "jenesis.encoder" to
 * <Code>net.sourceforge.jenesis4java.jaloppy.JenesisJalopyEncoder</Code>. This
 * will use the default jalopy settings from META-INF/jalopy.xml. To use an
 * other set the System property jenesis.encoder.jalopyconfig
 */
public class JenesisJalopyEncoder implements CompilationUnitEncoder {

    private static final Logger LOGGER = Logger.getLogger(JenesisJalopyEncoder.class.getName());

    final private String jalopyConfigAsResourceOrFile;

    private Jalopy jalopy;

    public JenesisJalopyEncoder() {
        this(System.getProperty("jenesis.encoder.jalopyconfig", "META-INF/jalopy.xml"));
    }

    public JenesisJalopyEncoder(String jalopyConfigAsResourceOrFile) {
        this.jalopyConfigAsResourceOrFile = jalopyConfigAsResourceOrFile;
    }

    @Override
    public void encode(CompilationUnit unit) {

        // wrap this section for io exceptions
        PrintWriter fout = null;
        try {

            // Make a file object which corresponds to the
            // filename which we will write to.
            File file = new File(getFileName(unit));

            // Get the parent file such that we can create the
            // directory if necessary. NOTE: JDK1.2 dependency
            // here.
            File dir = file.getParentFile();

            // Make sure it exists.
            if (!dir.exists()) {
                if (!dir.mkdirs()) {
                    throw new RuntimeException("could not create directory: " + dir.getCanonicalPath());
                }
            }
            StringWriter stringWriter;
            BufferedWriter bufferedWriter;
            // if (changed || !file.exists()) {
            // bufferedWriter = new BufferedWriter(new FileWriter(file));
            // } else {
            stringWriter = new StringWriter();
            bufferedWriter = new BufferedWriter(stringWriter);
            // }
            // now we want to make the stream...

            fout = new PrintWriter(bufferedWriter);

            // ...wrap it with a code writer...
            CodeWriter out = new MCodeWriter(fout);

            // ...generate the code...
            unit.toCode(out);

            // ... and finally close and drop the stream
            fout.close();
            fout = null;

            String javaSource = stringWriter.toString();
            String formatedSource = formatFile(file, javaSource);
            if (formatedSource != null) {
                javaSource = formatedSource;
            }
            writeIfChanged(javaSource, file);
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
    }

    private Jalopy createJalopy(Jalopy jalopy) {
        InputStream in;
        try {
            in = this.getClass().getClassLoader().getResourceAsStream(jalopyConfigAsResourceOrFile);
            Convention.importSettings(in, Convention.EXTENSION_XML);
        } catch (IOException ex) {
            in = null;
        }
        if (in == null) {
            try {
                File configFile = new File(jalopyConfigAsResourceOrFile);
                in = new FileInputStream(configFile);
                Convention.importSettings(in, Convention.EXTENSION_XML);
            } catch (IOException ex) {
                throw new RuntimeException("could NOT find a Jalopy config (as resource and file) " + jalopyConfigAsResourceOrFile, ex);
            }
        }

        Convention settings = Convention.getInstance();

        jalopy.setFileFormat("auto");

        jalopy.setInspect(settings.getBoolean(ConventionKeys.INSPECTOR, ConventionDefaults.INSPECTOR));

        jalopy.setHistoryPolicy(History.Policy.DISABLED);

        History.Method historyMethod = History.Method.valueOf(settings.get(ConventionKeys.HISTORY_METHOD, ConventionDefaults.HISTORY_METHOD));

        jalopy.setHistoryMethod(historyMethod);

        jalopy.setBackup(settings.getInt(ConventionKeys.BACKUP_LEVEL, ConventionDefaults.BACKUP_LEVEL) > 0);

        jalopy.setForce(settings.getBoolean(ConventionKeys.FORCE_FORMATTING, ConventionDefaults.FORCE_FORMATTING));

        return jalopy;
    }

    private String formatFile(File file, String source) {
        if (getJalopy() != null) {
            try {
                Reader inputReader = new StringReader(source);
                Writer formattedWriter = new StringWriter();
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        throw new RuntimeException("Could not access File");
                    }
                }
                getJalopy().setInput(inputReader, file.getCanonicalPath());
                getJalopy().setOutput(formattedWriter);
                getJalopy().format();
                logMessage(jalopy, file.getCanonicalPath());
                if (jalopy.getState() == Jalopy.State.OK || jalopy.getState() == Jalopy.State.PARSED) {
                    return formattedWriter.toString();
                } else {
                    resetJalopy();
                    throw new RuntimeException("Could not format java file due to Jalopy State " + jalopy.getState());

                }
            } catch (Exception e) {
                resetJalopy();
                throw new RuntimeException("Could not format java file due to Exception", e);

            }
        }
        return null;
    }

    /**
     * Method to read a file and return it as String
     */
    private String getFileContents(File file) throws IOException {

        InputStreamReader fr = new InputStreamReader(new FileInputStream(file), "UTF-8");
        StringWriter sr = new StringWriter();

        try {
            char[] buf = new char[4096];
            int len = 0;
            while (len != -1) {
                try {
                    len = fr.read(buf, 0, buf.length);
                } catch (EOFException eof) {
                    break;
                }
                if (len != -1) {
                    sr.write(buf, 0, len);
                }
            }

            fr.close();
            sr.close();

            return sr.toString();

        } finally {
            try {
                fr.close();
            } catch (IOException ioe) {
                LOGGER.log(Level.WARNING, "Exception during InputStreamReader.close(). ", ioe);
            }
        }
    }

    private String getFileName(CompilationUnit unit) {
        // make the fileName of the target file as the concatenation
        // of the sourcepath, the package name, and the className,
        // and the ending dot java
        StringBuffer file;

        // was a path specified?
        if (unit.getCodebase() != null && unit.getCodebase().length() > 0) {
            // make the buffer
            file = new StringBuffer(unit.getCodebase());
            // does the codebase end with a slash?
            if (!unit.getCodebase().endsWith(Character.toString(File.separatorChar))) {
                // make it so...
                file.append(File.separatorChar);
            }
        } else {
            file = new StringBuffer();
        }

        // append to the filepath if exists
        if (unit.getNamespace() != null) {
            // fetch the package name
            String pkg = unit.getNamespace().getName();
            if (pkg != null) {
                // append the package name
                file.append(pkg.replace('.', java.io.File.separatorChar));
            }
        }

        // add a separator, the top level name, and the extension
        String topLevelTypeName = unit.getTopLevelType().getName();
        int indexOfGeneric = topLevelTypeName.indexOf('<');
        if (indexOfGeneric > 0) {
            topLevelTypeName = topLevelTypeName.substring(0, indexOfGeneric).trim();
        }

        file.append(java.io.File.separatorChar).append(topLevelTypeName).append(".java");

        // done
        return file.toString();
    }

    private Jalopy getJalopy() {
        if (jalopy == null && jalopyConfigAsResourceOrFile != null) {
            jalopy = createJalopy(new Jalopy());
        }
        return jalopy;
    }

    /**
     * Just to be sure if the jalopy formater is in a inconsistent state, delete
     * it the lazy getter will create a new one.
     */
    private void resetJalopy() {
        jalopy = null;
    }

    private void writeIfChanged(String newFileContents, File file) throws IOException {
        String oldFileContents = getFileContents(file);
        if (!oldFileContents.equals(newFileContents)) {
            FileOutputStream outStream = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(outStream, "UTF-8");
            writer.write(newFileContents);
            writer.close();
            outStream.close();
            logMessage("INFO", "File " + file.getCanonicalPath() + " was written to disk");
        }
    }

    private void logMessage(Jalopy jalopy, String message) {
        State state = jalopy.getState();
        if (state == Jalopy.State.OK) {
            logMessage("DEBUG", message + " formatted correctly.");
        } else if (state == Jalopy.State.WARN) {
            logMessage("WARN", message + " formatted with warnings.");
        } else if (state == Jalopy.State.ERROR) {
            logMessage("ERROR", message + " could not be formatted.");
        } else {
            logMessage("INFO", message + " formatted with unknown state (" + state + ")");
        }
    }

    private void logMessage(String level, String message) {
        System.out.println(level + " " + message);
    }
}
