package crc.modeler.domain;


import crc.modeler.common.Result;

public class ClassName {

    private final String value;

    public static Result<ClassName> of(String value) {
        final Result<ClassName> className;
        if (value != null
                && Character.isJavaIdentifierStart(value.codePointAt(0))
                && value.codePoints().skip(1).allMatch(Character::isJavaIdentifierPart)) {
            className = Result.success(new ClassName(value));
        } else {
            className = Result.failure(new IllegalArgumentException("This is not a valid class name"));
        }
        return className;
    }

    private ClassName(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClassName className = (ClassName) o;

        return value != null ? value.equals(className.value) : className.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "ClassName{" +
                "value='" + value + '\'' +
                '}';
    }
}
