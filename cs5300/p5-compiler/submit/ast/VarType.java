/*
 * Code formatter project
 * CS 4481
 */
package submit.ast;

/**
 *
 * @author edwajohn
 */
public enum VarType {

  INT("int", 4), BOOL("bool", 4), CHAR("char", 4);

  private final String value;
  private final int size;

  private VarType(String value, int size) {
    this.value = value;
    this.size = size;
  }

  public static VarType fromString(String s) {
    for (VarType vt : VarType.values()) {
      if (vt.value.equals(s)) {
        return vt;
      }
    }
    throw new RuntimeException("Illegal string in VarType.fromString()");
  }

  @Override
  public String toString() {
    return value;
  }

  public int getSize() { return size; }
  public String getValue() { return value; }

}
