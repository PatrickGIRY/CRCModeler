package crc.modeler.domain;

import java.util.Objects;

public class ClassName {
    private String value;

    private ClassName(String value) {
        this.value = value;
    }

    public static ClassName of(String value) {
        return new ClassName(validate(value));
    }

    private static String validate(String value) {
        if (value == null
                || !Character.isJavaIdentifierStart(value.codePointAt(0))
                || value.codePoints().skip(1).anyMatch((codePoint) -> !Character.isJavaIdentifierPart(codePoint))) {
            throw new IllegalArgumentException("This is not a valid class name");
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassName className = (ClassName) o;
        return Objects.equals(value, className.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
