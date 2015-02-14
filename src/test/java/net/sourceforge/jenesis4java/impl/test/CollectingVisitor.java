package net.sourceforge.jenesis4java.impl.test;

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
