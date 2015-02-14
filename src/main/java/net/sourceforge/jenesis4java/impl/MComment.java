package net.sourceforge.jenesis4java.impl;

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
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jenesis4java.CodeWriter;
import net.sourceforge.jenesis4java.Comment;
import net.sourceforge.jenesis4java.DocumentationComment;
import net.sourceforge.jenesis4java.IVisitor;
import net.sourceforge.jenesis4java.impl.util.ListTypeSelector;

/**
 * Standard <code>Comment</code> implementations.
 */
public abstract class MComment extends MVM.MCodeable implements Comment {

    /* =============================================================== */
    /* DOCUMENTATION COMMENT */
    /* =============================================================== */
    static class MDocumentationComment extends MComment implements DocumentationComment {

        String tagAuthor;

        String tagDate;

        String tagDeprecated;

        String tagException;

        String tagReturn;

        String tagSerial;

        String tagSerialData;

        String tagSerialField;

        String tagSince;

        String tagThrows;

        String tagVersion;

        List<String> vtagParameter;

        List<String> vtagSee;

        public MDocumentationComment(MVM vm, String text) {
            super(vm, Comment.D, text);
            this.vtagParameter = new ArrayList<String>();
            this.vtagSee = new ArrayList<String>();
        }

        @Override
        public MDocumentationComment addParam(String s) {
            this.vtagParameter.add(s);
            return this;
        }

        @Override
        public MDocumentationComment addSee(String s) {
            this.vtagSee.add(s);
            return this;
        }

        @Override
        public String getAuthor() {
            return this.tagAuthor;
        }

        @Override
        public String getDate() {
            return this.tagDate;
        }

        @Override
        public String getDeprecated() {
            return this.tagDeprecated;
        }

        @Override
        public String getException() {
            return this.tagException;
        }

        @Override
        public List<String> getParams() {
            return ListTypeSelector.select(this.vtagParameter);
        }

        @Override
        public String getReturn() {
            return this.tagReturn;
        }

        @Override
        public List<String> getSees() {
            return ListTypeSelector.select(this.vtagSee);
        }

        @Override
        public String getSerial() {
            return this.tagSerial;
        }

        @Override
        public String getSerialData() {
            return this.tagSerialData;
        }

        @Override
        public String getSerialField() {
            return this.tagSerialField;
        }

        @Override
        public String getSince() {
            return this.tagSince;
        }

        @Override
        public String getThrows() {
            return this.tagThrows;
        }

        @Override
        public String getVersion() {
            return this.tagVersion;
        }

        @Override
        public MDocumentationComment setAuthor(String s) {
            this.tagAuthor = s;
            return this;
        }

        @Override
        public MDocumentationComment setDate(String s) {
            this.tagDate = s;
            return this;
        }

        @Override
        public MDocumentationComment setDeprecated(String s) {
            this.tagDeprecated = s;
            return this;
        }

        @Override
        public MDocumentationComment setException(String s) {
            this.tagException = s;
            return this;
        }

        @Override
        public MDocumentationComment setReturn(String s) {
            this.tagReturn = s;
            return this;
        }

        @Override
        public MDocumentationComment setSerial(String s) {
            this.tagSerial = s;
            return this;
        }

        @Override
        public MDocumentationComment setSerialData(String s) {
            this.tagSerialData = s;
            return this;
        }

        @Override
        public MDocumentationComment setSerialField(String s) {
            this.tagSerialField = s;
            return this;
        }

        @Override
        public MDocumentationComment setSince(String s) {
            this.tagSince = s;
            return this;
        }

        @Override
        public MDocumentationComment setThrows(String s) {
            this.tagThrows = s;
            return this;
        }

        @Override
        public MDocumentationComment setVersion(String s) {
            this.tagVersion = s;
            return this;
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            if (!out.isLineNew()) {
                out.newLine();
            }
            out.write("/**").newLine(); // */

            MComment.wordWrap(this.text, MComment.D_COMMENT_LENGTH, " *", out);

            writeTag(this.tagAuthor, "author", out);
            writeTag(this.tagDate, "date", out);
            writeTag(this.tagDeprecated, "deprecated", out);
            writeTag(this.tagException, "exception", out);
            writeTag(this.tagReturn, "return", out);
            writeTag(this.tagSerial, "serial", out);
            writeTag(this.tagSerialData, "serialData", out);
            writeTag(this.tagSerialField, "serialField", out);
            writeTag(this.tagSince, "since", out);
            writeTag(this.tagThrows, "throws", out);
            writeTag(this.tagVersion, "version", out);
            writeTags(this.vtagParameter, "param", out);
            writeTags(this.vtagSee, "see", out);

            if (!out.isLineNew()) {
                out.newLine();
            }
            out.space().write("*/");

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }

        private void writeTag(String tag, String tagLabel, CodeWriter out) {
            if (tag == null) {
                return;
            }

            String s = new StringBuffer().append('@').append(tagLabel).append(' ').append(tag).toString();

            MComment.wordWrap(s, 55, " *", out);
        }

        private void writeTags(List<String> v, String tagLabel, CodeWriter out) {
            for (String string : v) {
                writeTag(string, tagLabel, out);
            }
        }
    }

    /* =============================================================== */
    /* MULTIPLE LINE COMMENT */
    /* =============================================================== */
    static final class MMultipleLineComment extends MComment {

        boolean onlyAtBeginningAndEnd = false;

        MMultipleLineComment(MVM vm, String text) {
            super(vm, Comment.M, text);
        }

        MMultipleLineComment(MVM vm, String text, boolean onlyAtBeginningAndEnd) {
            super(vm, Comment.M, text);
            this.onlyAtBeginningAndEnd = onlyAtBeginningAndEnd;

        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            if (!out.isLineNew()) {
                out.newLine();
            }
            out.write("/* ").newLine(); // */
            if (this.onlyAtBeginningAndEnd) {
                MComment.wordWrap(this.text, MComment.COMMENT_LENGTH_UNLIMITED, "\t", out);
            } else {
                MComment.wordWrap(this.text, MComment.M_COMMENT_LENGTH, " *", out);
            }

            if (!out.isLineNew()) {
                out.newLine();
            }
            out.write(" */");

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    /* =============================================================== */
    /* SINGLE LINE COMMENT */
    /* =============================================================== */
    static final class MSingleLineComment extends MComment {

        MSingleLineComment(MVM vm, String text) {
            super(vm, Comment.S, text);
        }

        @Override
        public CodeWriter toCode(CodeWriter out) {
            super.toCode(out);

            MComment.wordWrap(this.text, MComment.S_COMMENT_LENGTH, "//", out);

            return out;
        }

        @Override
        public void visit(IVisitor visitor) {
            super.visit(visitor);
        }
    }

    public static final int S_COMMENT_LENGTH = 30;

    public static final int M_COMMENT_LENGTH = 180;

    public static final int D_COMMENT_LENGTH = 180;

    public static final int COMMENT_LENGTH_UNLIMITED = 500;

    static void wordWrap(char[] s, int maxlen, String head, CodeWriter out) {
        int strlen = s.length, trailer = 0, leader = 0, anchor = 0;

        // loop while we look for newline characters.
        while (leader < strlen) {

            // are we at the max line length? If so then we need
            // to write the line thus far (but do so at a good place)
            if (leader - trailer == maxlen) {
                // set the anchor in case we need to retrieve our position
                anchor = leader;
                // we don't want words to be broken up.
                // check to make sure the leader hits a space OR a comma (added
                // comma 9/5/00)
                word_search: while (s[leader] == ',' || s[leader] != ' ') {
                    // word_search: while (s[leader] != ' ') {
                    // move back one character
                    --leader;
                    // if it goes all the way back, then too bad, we have to cut
                    // it...
                    if (leader == trailer) {
                        leader = anchor;
                        break word_search;
                    }
                }
                // ok, we've discovered a place to break the line. Now we write
                // this much of the output and reset the tracking variables.
                out.write(head).space().write(s, trailer, leader - trailer).newLine();

                // move the trailer up to the leader
                trailer = leader;
            }

            // if we've gone this far we need to just make sure that the
            // character is
            // not a newline
            switch (s[leader]) {
                case 13: /* CR */{
                    // in this case we check to see if there is an LF after
                    // this character. If so this is a Caldera newline
                    if (s[leader + 1] == 10) {
                        // flush the line thus far
                        out.write(head).space().write(s, trailer, leader - trailer).newLine();
                        // move the leader up two characters such that we skip
                        // over the newline
                        trailer = leader + 2;
                        leader = leader + 2;
                        // looks like a mac newline. Whadda know?
                    } else {
                        // flush the line thus far
                        out.write(head).space().write(s, trailer, leader - trailer).newLine();
                        // move the leader up one character such that we skip
                        // over the newline
                        trailer = ++leader;
                    }
                    break;
                }
                case 10: /* LF */{
                    // this is a nix machine
                    // flush the line thus far
                    out.write(head).space().write(s, trailer, leader - trailer).newLine();
                    // move the leader up one character such that we skip over
                    // the newline
                    trailer = ++leader;
                    // bust out
                    break;
                }
                default: {
                    // just advance the leader
                    leader++;
                }
            }
        }

        // flush the rest of the line to the writer
        out.write(head).space().write(s, trailer, leader - trailer).newLine();
    }

    /**
     * <P>
     * Breaks the given String into lines with given length int tabbed to a
     * distance given int tab and prefixed with an asterisk and a space.
     * Therefore,
     * <P>
     * <code>wordWrap("How now brown cow", 5, 0);</code>
     * <P>
     * returns:
     * <P>
     * How<BR>
     * now<BR>
     * brown<BR>
     * cow<BR>
     */
    static void wordWrap(String s, int maxlen, String head, CodeWriter out) {
        wordWrap(s.toCharArray(), maxlen, head, out);
    }

    int type;

    String text;

    MComment(MVM vm, int type, String text) {
        super(vm);
        this.type = type;
        this.text = text;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public MComment setText(String text) {
        this.text = text;
        return this;
    }

    /* =============================================================== */
    /* CLASS METHODS */
    /* =============================================================== */

    @Override
    public int type() {
        return this.type;
    }

    @Override
    public void visit(IVisitor visitor) {
        super.visit(visitor);
    }
}
