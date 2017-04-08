package crc.modeler.domain;


import crc.modeler.common.Result;

public class ClassName {

    private final String value;

    private ClassName(String value) {
        this.value = value;
    }

    public static Result<ClassName> of(String value) {
        final Result<ClassName> result;
        if (value != null) {
            int startCodePoint = value.codePointAt(0);
            if (Character.isJavaIdentifierStart(startCodePoint) && !Character.isLowerCase(startCodePoint)
                    && value.codePoints().skip(1).allMatch(Character::isJavaIdentifierPart)) {
                result = Result.success(new ClassName(value));
            } else {
                result = Result.failure(new InvalidClassNameException(value));
            }
        } else {
            result = Result.failure(new InvalidClassNameException(null));
        }
        return result;
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
