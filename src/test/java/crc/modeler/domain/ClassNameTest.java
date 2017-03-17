package crc.modeler.domain;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@RunWith(JUnitQuickcheck.class)
public class ClassNameTest {

    @Test
    public void class_name_should_created_when_value_start_by_$() throws Exception {
        assertThat(ClassName.of("$").contains("$")).isTrue();
    }

    @Test
    public void class_name_should_created_when_value_start_by__() throws Exception {
        assertThat(ClassName.of("_").contains("_")).isTrue();
    }

    @Property
    public void class_name_should_created_when_value_start_by_letter(@CodePoint int codePoint) throws Exception {
        Assume.assumeTrue(Character.isLetter(codePoint));
        String s = String.valueOf((char) codePoint);
        assertThat(ClassName.of(s).contains(s)).isTrue();
    }

    @Property
    public void class_name_should_not_created_when_value_start_by_digit(int value) throws Exception {
        String s = String.valueOf(value);
        assertThatThrownBy(() -> ClassName.of(s)).isInstanceOf(IllegalArgumentException.class).hasMessage("This is not a valid class name");
    }
}