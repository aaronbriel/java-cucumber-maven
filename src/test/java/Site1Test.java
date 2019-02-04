/**
 * Created by aaronbriel on 5/18/17.
 */
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;
import cucumber.api.CucumberOptions;

@RunWith(Cucumber.class)

@CucumberOptions(
        format = { "pretty","html:target/site1/cucumber",
                "json:target/site1/cucumber.json",
                "junit:target/site1/cucumber.xml"},
        features = {"src/test/resources/site1/features"},
        glue = {"site1.steps"}
)

public class Site1Test {
}
