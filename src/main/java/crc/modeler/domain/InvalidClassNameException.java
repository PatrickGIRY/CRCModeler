package crc.modeler.domain;

import java.util.Objects;

public class InvalidClassNameException extends RuntimeException {
    private final String invalidClassName;

    public InvalidClassNameException(String invalidClassName) {
        this.invalidClassName = invalidClassName;
    }

    public String getInvalidClassName() {
        return invalidClassName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InvalidClassNameException that = (InvalidClassNameException) o;
        return Objects.equals(invalidClassName, that.invalidClassName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invalidClassName);
    }

    @Override
    public String toString() {
        return "InvalidClassNameException{" +
                "invalidClassName='" + invalidClassName + '\'' +
                '}';
    }
}
