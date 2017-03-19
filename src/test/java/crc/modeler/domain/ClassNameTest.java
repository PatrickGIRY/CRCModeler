package crc.modeler.domain;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;


@RunWith(JUnitQuickcheck.class)
public class ClassNameTest {

    @Property
    public void class_name_should_created_when_value_start_by_valid_code_point_follow_by_valid_string(@CodePoint int start, String part) throws Exception {
        assumeTrue(Character.isJavaIdentifierStart(start));
        assumeTrue(part.codePoints().allMatch(Character::isJavaIdentifierPart));

        String s = String.valueOf((char) start).concat(part);
        assertThat(ClassName.of(s)).isNotNull();
    }

    @Property
    public void class_name_should_not_created_when_value_start_by_invalid_code_point(@CodePoint int codePoint) throws Exception {
        assumeFalse(Character.isJavaIdentifierStart(codePoint));

        String s = String.valueOf((char) codePoint);
        assertThatThrownBy(() -> ClassName.of(s)).isInstanceOf(IllegalArgumentException.class).hasMessage("This is not a valid class name");
    }

    @Property
    public void class_name_should_not_created_when_value_start_by_valid_code_point_follow_by_invalid_string(@CodePoint int start, String part) throws Exception {
        assumeTrue(Character.isJavaIdentifierStart(start));
        assumeTrue(part.codePoints().anyMatch((codePoint) -> !Character.isJavaIdentifierPart(codePoint)));

        String s = String.valueOf((char) start).concat(part);
        assertThatThrownBy(() -> ClassName.of(s)).isInstanceOf(IllegalArgumentException.class).hasMessage("This is not a valid class name");
    }

    @Test
    public void class_name_should_not_craated_when_value_is_null() throws Exception {
        assertThatThrownBy(() -> ClassName.of(null)).isInstanceOf(IllegalArgumentException.class).hasMessage("This is not a valid class name");
    }
}