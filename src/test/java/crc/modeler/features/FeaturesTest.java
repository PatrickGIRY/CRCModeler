package crc.modeler.features;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(strict = true,
        tags = {"~@wip"},
        glue = {"crc.modeler.features"},
        plugin = {"pretty", "html:target/cucumber"})
public class FeaturesTest {
}
