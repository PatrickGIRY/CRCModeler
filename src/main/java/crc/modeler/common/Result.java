package crc.modeler.common;


import java.util.function.Function;
import java.util.function.Supplier;

public abstract class Result<T> {
    private Result() {
    }

    public static <T> Result<T> success(T value) {
        return new Success<>(value);
    }

    public static <T> Result<T> failure(RuntimeException exception) {
        return new Failure<>(exception);
    }

    public abstract boolean isSuccess();

    public abstract boolean isFailure();

    public abstract T getOrElse(Supplier<T> defaultValue);

    public abstract T successValue();

    public abstract RuntimeException failureValue();

    public abstract <R> Result<R> map(Function<T, R> mapper);

    public abstract <R> Result<R> flatMap(Function<T, Result<R>> mapper);


    private static class Success<T> extends Result<T> {

        private final T value;

        private Success(T value) {
            this.value = value;
        }

        @Override
        public boolean isSuccess() {
            return true;
        }

        @Override
        public boolean isFailure() {
            return false;
        }

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return value;
        }

        @Override
        public T successValue() {
            return value;
        }

        @Override
        public RuntimeException failureValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public <R> Result<R> map(Function<T, R> mapper) {
            try {
                return Result.success(mapper.apply(value));
            } catch (RuntimeException e) {
                return Result.failure(e);
            }
        }

        @Override
        public <R> Result<R> flatMap(Function<T, Result<R>> mapper) {
            try {
                return mapper.apply(value);
            } catch (RuntimeException e) {
                return failure(e);
            }
        }

        @Override
        public String toString() {
            return "Success{" +
                    "value=" + value +
                    "} ";
        }
    }

    private static class Failure<T> extends Result<T> {

        private final RuntimeException exception;

        private Failure(RuntimeException exception) {
            this.exception = exception;
        }

        @Override
        public boolean isSuccess() {
            return false;
        }

        @Override
        public boolean isFailure() {
            return true;
        }

        @Override
        public T getOrElse(Supplier<T> defaultValue) {
            return defaultValue.get();
        }

        @Override
        public T successValue() {
            throw new UnsupportedOperationException();
        }

        @Override
        public RuntimeException failureValue() {
            return exception;
        }

        @Override
        public <R> Result<R> map(Function<T, R> mapper) {
            return Result.failure(exception);
        }

        @Override
        public <R> Result<R> flatMap(Function<T, Result<R>> mapper) {
            return Result.failure(exception);
        }

        @Override
        public String toString() {
            return "Failure{" +
                    "exception=" + exception +
                    "} ";
        }
    }
}
