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
import net.sourceforge.jenesis4java.IVisitor;

import java.util.List;

/**
 * Simple IVisitor implementation that collects all elements it visits.<br>
 * Used to test that {@code visit} methods work as expected.
 */
public class CollectingVisitor implements IVisitor {

    private final List<Codeable> visited;

    public CollectingVisitor(List<Codeable> visited) {
        this.visited = visited;
    }

    @Override
    public Codeable visitReplace(Codeable current, Codeable parent) {
        visited.add(current);
        return current;
    }
}
