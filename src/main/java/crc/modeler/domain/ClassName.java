package crc.modeler.domain;


import java.util.Objects;
import java.util.function.Consumer;

public abstract class ClassName {

    private ClassName() {}

    public static ClassName of(String value) {
        final ClassName className;
        if (value != null
                && Character.isJavaIdentifierStart(value.codePointAt(0))
                && value.codePoints().skip(1).allMatch(Character::isJavaIdentifierPart)) {
            className = new Validated(value);
        } else {
            className = new Invalid(new IllegalArgumentException("This is not a valid class name"));
        }
        return className;
    }

    public abstract boolean isValid();

    public abstract void ifInvalid(Consumer<RuntimeException> consumer);

    private static class Invalid extends ClassName {
        private final RuntimeException error;

        private Invalid(RuntimeException error) {
            super();
            this.error = error;
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public void ifInvalid(Consumer<RuntimeException> consumer) {
            consumer.accept(error);
        }

        @Override
        public String toString() {
            return "ClassNameInvalid{" +
                    error +
                    '}';
        }
    }

    private static class Validated extends ClassName {
        private String value;

        private Validated(String value) {
            super();
            this.value = value;
        }

        public boolean isValid() {
            return true;
        }

        public void ifInvalid(Consumer<RuntimeException> consumer) {

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Validated validatedClassName = (Validated) o;
            return Objects.equals(value, validatedClassName.value);
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
}
