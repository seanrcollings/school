package submit;

public enum Syscall {

    PRINT_INTEGER(1), PRINT_FLOAT(2), PRINT_DOUBLE(3), PRINT_STRING(4),
    READ_INTEGER(5), READ_FLOAT(6), READ_DOUBLE(7), READ_STRING(8),
    MEM_ALLOCATE(9), EXIT(10);

    public final int value;

    Syscall(int value) {
        this.value = value;
    }
}
