package com.rupesh;

import com.rupesh.test.DiscountCalculator;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class DiscountCalculatorStepDefs {

  private DiscountCalculator calculator;
  private double amount = 0.0;
  private String discountType;
  private double discount = 0.0;
  private double discountFlatAmount = 0.0;
  private double afterDiscountAmount = 0.0;
  private String applicableType;

  @Given("I have an amount of {double}")
  public void iHaveAnAmountOf(double txtAmount) throws Throwable {
    amount = txtAmount;
  }

  @Given("I have selected discount type {string}")
  public void iHaveSelectedDiscountType(String type) {
    this.discountType = type;
  }

  @Given("The percentage is {double}")
  public void thePercentageIs(double percentage) {
    this.discount = percentage;
  }

  @Given("The flat discount is {double}")
  public void theFlatDiscountIs(double flatDiscount) {
    this.discountFlatAmount = flatDiscount;
  }

  @Given("I have selected {string} as applicable type")
  public void iHaveSelectedApplicableTypeAs(String applicable) {
    this.applicableType = applicable;
  }

  @When("I calculate the discount")
  public void iCalculateTheDiscount() {
    this.calculator = new DiscountCalculator();
    this.afterDiscountAmount = this.calculator.calculate(this.amount, this.discount, this.discountFlatAmount , discountType, this.applicableType);
  }

  @Then("The calculated amount should be {double}")
  public void theCalculatedAmountShouldBe(double result) {
    System.out.println("FINAL DISCOUNT AMOUNT: "+this.afterDiscountAmount);
    Assertions.assertEquals(this.afterDiscountAmount, result);
  }

}
