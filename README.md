Live template - used to generate new files easier:

Settings -> Search for "file and code templates" -> (Enable them if they are not already) Press "+" -> Give it the name you want -> paste the code bellow  

Page Object:
import org.openqa.selenium.WebDriver;

public class ${NAME} extends BaseClass{
    public ${NAME}(WebDriver driver){
        super(driver);
    }

}

Step definition:
import org.openqa.selenium.WebDriver;
import cucumber.api.java.en.Given;

import java.util.HashMap;


public class ${NAME} extends StepDefHelper{
    
    public ${NAME}() throws Throwable {
    }
    
    @Given("^dummy for ${NAME}$")
    public void ${NAME}def(){
           //this is here for auto complete
    }
}

