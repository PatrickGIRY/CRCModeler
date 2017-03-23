package crc.modeler.common;

import org.junit.Test;

import java.io.StringWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ResultTest {

    @Test
    public void should_create_result_success() throws Exception {
        Result<String> result = Result.success("foo");
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.isFailure()).isFalse();
        assertThat(result.successValue()).isEqualTo("foo");
        assertThatThrownBy(result::failureValue).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    public void should_create_result_failure() throws Exception {
        RuntimeException exception = new RuntimeException("foo");
        Result<String> result = Result.failure(exception);
        assertThat(result.isSuccess()).isFalse();
        assertThat(result.isFailure()).isTrue();
        assertThatThrownBy(result::successValue).isInstanceOf(UnsupportedOperationException.class);
        assertThat(result.failureValue()).isEqualTo(exception);
    }

    @Test
    public void should_return_value_when_success() throws Exception {
        Result<String> result = Result.success("foo");
        assertThat(result.getOrElse(() -> "bar")).isEqualTo("foo");
    }

    @Test
    public void should_return_default_value_when_failure() throws Exception {
        Result<String> result = Result.failure(new RuntimeException("foo"));
        assertThat(result.getOrElse(() -> "bar")).isEqualTo("bar");
    }

    @Test
    public void should_return_mapped_result_when_success() throws Exception {
        Result<String> result = Result.success("foo");
        assertThat(result.map(s -> "bar")).extracting(Result::successValue).containsExactly("bar");
    }

    @Test
    public void should_return_failure_result_when_success_mapping_thrown_exception() throws Exception {
        Result<String> result = Result.success("foo");
        RuntimeException runtimeException = new RuntimeException();
        assertThat(result.map(s -> {
            throw runtimeException;
        })).extracting(Result::failureValue).containsExactly(runtimeException);
    }

    @Test
    public void should_return_same_result_when_failure() throws Exception {
        RuntimeException exception = new RuntimeException("foo");
        Result<String> result = Result.failure(exception);
        assertThat(result.map(s -> "bar")).extracting(Result::failureValue)
                .containsExactly(exception);
    }

    @Test
    public void should_return_flat_mapped_result_when_success() throws Exception {
        Result<String> result = Result.success("foo");
        assertThat(result.flatMap(s -> Result.success("bar")))
                .extracting(Result::successValue).containsExactly("bar");
    }

    @Test
    public void should_return_failure_result_when_success_flat_mapping_thrown_exception() throws Exception {
        Result<String> result = Result.success("foo");
        RuntimeException runtimeException = new RuntimeException();
        assertThat(result.flatMap(s -> {
            throw runtimeException;
        })).extracting(Result::failureValue).containsExactly(runtimeException);
    }

    @Test
    public void should_return_same_result_too_when_failure() throws Exception {
        RuntimeException exception = new RuntimeException("foo");
        Result<String> result = Result.failure(exception);
        assertThat(result.flatMap(s -> Result.success("bar"))).extracting(Result::failureValue)
                .containsExactly(exception);
    }

    @Test
    public void should_call_consumer_on_success() throws Exception {
        Result<String> result = Result.success("foo");
        StringWriter stringWriter = new StringWriter();
        result.onSuccess(stringWriter::write);
        assertThat(stringWriter.toString()).isEqualTo("foo");
    }

    @Test
    public void should_not_call_consumer_on_failure() throws Exception {
        RuntimeException exception = new RuntimeException("foo");
        Result<String> result = Result.failure(exception);
        StringWriter stringWriter = new StringWriter();
        result.onSuccess(stringWriter::write);
        assertThat(stringWriter.toString()).isEmpty();
    }
}
