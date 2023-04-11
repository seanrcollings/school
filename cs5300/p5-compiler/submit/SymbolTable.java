package submit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/*
 * Code formatter project
 * CS 4481
 */
/**
 *
 */
public class SymbolTable {

  private final HashMap<String, SymbolInfo> table;
  private SymbolTable parent;
  private final List<SymbolTable> children;
  private int uniqueLabelCount = 0;
  private final HashMap<String, Integer> offsets;
  private int currOffset = 0;

  public SymbolTable() {
    table = new HashMap<>();
    parent = null;
    children = new ArrayList<>();
    offsets = new HashMap<>();

    table.put("println", new SymbolInfo("println", null, true));
  }

  public void addSymbol(String id, SymbolInfo symbol) {
    table.put(id, symbol);
    if (!symbol.getFunction()) {
      currOffset += symbol.getType().getSize();
      offsets.put(id, currOffset);
    }
  }

  public Integer getOffset(String id) {
    return offsets.get(id);
  }

  /**
   * Returns null if no symbol with that id is in this symbol table or an
   * ancestor table.
   *
   * @param id
   * @return
   */
  public SymbolInfo find(String id) {
    if (table.containsKey(id)) {
      return table.get(id);
    }
    if (parent != null) {
      return parent.find(id);
    }
    return null;
  }

  /**
   * Returns the new child.
   *
   * @return
   */
  public SymbolTable createChild() {
    SymbolTable child = new SymbolTable();
    children.add(child);
    child.parent = this;
    return child;
  }

  public SymbolTable getChild(int i) {
    return children.get(i);
  }

  public SymbolTable getParent() {
    return parent;
  }

  public SymbolTable getRoot() {
    SymbolTable curr = this;

    while (curr.getParent() != null)
      curr = curr.getParent();

    return curr;
  }

  public int size() {
    return table
            .values()
            .stream()
            .filter((v) -> !v.getFunction())
            .mapToInt((v) -> v.getType().getSize())
            .sum();
  }

  public String getUniqueLabel() {
    if (parent == null) {
      return String.format("datalabel%d", uniqueLabelCount++);
    }
    return getRoot().getUniqueLabel();
  }
}
