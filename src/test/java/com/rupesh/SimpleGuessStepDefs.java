package com.rupesh;

import com.rupesh.test.SimpleGuess;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class SimpleGuessStepDefs {

  private SimpleGuess simpleGuess;
  private String result;

  @Given("Create a SimpleGuess game play")
  public void crateASimpleGuessGamePlay() throws Throwable {
    simpleGuess = new SimpleGuess();
  }

  @When("I play with number {int}")
  public void iPlayWithNumber(int arg) throws Throwable {
    result = simpleGuess.play(arg);
  }

//  @Then("The result is EVEN")
//  public void theResultIsEVEN() throws Throwable {
//    Assertions.assertEquals(result, "EVEN");
//  }

  @Then("The result is {string}")
  public void theResultIs(String resultString) throws Throwable {
    Assertions.assertEquals(result, resultString);
  }

}
