package io.codekontor.slizaa.hierarchicalgraph.core.transform;

public interface IHierarchicalGraphModification {

    void execute();

    void undo();
}
