<h2>Live template - used to generate new files easier:</h2>

<b>Settings -> Search for "file and code templates" -> (Enable them if they are not already) Press "+" -> Give it the name you want -> paste the code bellow</b>  

<h3>Page Object:</h3>
<pre>
import org.openqa.selenium.WebDriver;

public class ${NAME} extends BaseClass{
    public ${NAME}(WebDriver driver){
        super(driver);
    }

}
</pre>

<h3>Step definition:</h3>
<pre>
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
</pre>


<h2>What is the logical structure:</h2>

<p>Uses the Page Objects design pattern which states that each physical page of our website is a class.
Each of the page's elements is a method that retrieves the element for us.</p>
 
<p>The actions performed by the elements are grouped into the Actions classes which reside in the "actions" package.
The action classes are static and so are their methods, due to this property they are accessible all over our project 
but their logic must be independent from the page's elements.</p> 
  
<p>The steps are implemented in the step definition classes. They reside in the classes of the step_definition package.
They call for any action from any feature. This is why it is important that the actions are static and decoupled from the elements.
The steps retrieve the data from the actions and are passing it along into others as parameters.</p>
  
<p>The human like sentences exist in the .feature files. Any sentence can be called in any feature. They are groupped 
by feature to be tested or by group of tests. For instance a smoke test it is not a feature per say but it is a faster
chaining of the most common desired steps.</p> 
 
<p>All the data that need to be sent between steps is located in the sendData.properties file.</p>
    
<p>All the translations are located in the localisation.properties file.</p>
    
<p>All the selectors/locators are located in the locators.properties file.</p>
    
<p>All the information related to the environment configuration, accounts etc resides in the staticInformation.properties file.</p>