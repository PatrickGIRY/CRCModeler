package crc.modeler.domain;

import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.stream.Stream;

public class ClassName {
    private String value;

    private ClassName(String value) {
        this.value = value;
    }

    public static ClassName of(String value) {
        return new ClassName(validate(value));
    }

    private static String validate(String value) {
        int first = value.codePointAt(0);
        boolean anyMatch = Stream.<IntPredicate>of(
                start -> start == '$',
                start -> start == '_',
                Character::isLetter).anyMatch(p -> p.test(first));
        if (!anyMatch) {
            throw new IllegalArgumentException("This is not a valid class name");
        }
        return value;
    }

    public boolean contains(String value) {
        return Objects.equals(this.value, value);
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
