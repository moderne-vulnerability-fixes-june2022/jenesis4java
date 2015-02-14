package net.sourceforge.jenesis4java;

public interface IVisitor {

    // if the current element should be replaced return the new Codeable
    Codeable visitReplace(Codeable current, Codeable parent);

}
