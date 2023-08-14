package com.rupesh;

import com.rupesh.payload.CalculateDiscountRequest;
import com.rupesh.test.DiscountCalculator;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

public class DiscountCalculatorStepDefs {

  private CalculateDiscountRequest request = new CalculateDiscountRequest();
  private DiscountCalculator discountCalculator;
  private double afterDiscountAmount = 0.0;

  @Given("I have an amount of {double}")
  public void iHaveAnAmountOf(double txtAmount) {
    request.setTransactionAmount(txtAmount);
  }

  @Given("I have selected discount type {string}")
  public void iHaveSelectedDiscountType(String type) {
    request.setDiscountType(type.toUpperCase());
  }

  @Given("The percentage is {double}")
  public void thePercentageIs(double percentage) {
    request.setDiscountPercentage(percentage);
  }

  @Given("The flat discount is {double}")
  public void theFlatDiscountIs(double flatDiscount) {
    request.setFlatDiscountAmount(flatDiscount);
  }

  @Given("I have selected {string} as applicable type")
  public void iHaveSelectedApplicableTypeAs(String applicable) {
    request.setApplicableType(applicable.toUpperCase());
  }

  @And("I have given per user discount {double}")
  public void iHaveGivenPerUserDiscount(double perUserDiscount) {
    request.setPerUserDiscount(perUserDiscount);
  }

  @And("I have given per user per merchant discount {double}")
  public void iHaveGivenPerUserPerMerchantDiscount(double perUserPerMerchantDiscount) {
    request.setPerUserPerMerchantDiscount(perUserPerMerchantDiscount);
  }

  @And("I have given per user per day discount {double}")
  public void iHaveGivenPerUserPerDayDiscount(double perUserPerDayDiscount) {
    request.setPerUserPerDayDiscount(perUserPerDayDiscount);
  }

  @And("I have given per user per month discount {double}")
  public void iHaveGivenPerUserPerMonthDiscount(double perUserPerMonthDiscount) {
    request.setPerUserPerMonthDiscount(perUserPerMonthDiscount);
  }

  @And("I have given per merchant per day discount {double}")
  public void iHaveGivenPerMerchantPerDayDiscount(double perMerchantPerDayDiscount) {
    request.setPerMerchantPerDayDiscount(perMerchantPerDayDiscount);
  }

  @And("I have given per merchant per month discount {double}")
  public void iHaveGivenPerMerchantPerMonthDiscount(double perMerchantPerMonthDiscount) {
    request.setPerMerchantPerMonthDiscount(perMerchantPerMonthDiscount);
  }

  @And("And per user discount taken is {double}")
  public void andPerUserDiscountTakenIsPer_user_discount_taken(double discountTaken) {
    request.setPerUserDiscountTaken(discountTaken);
  }

  @And("And per user per merchant discount taken is {double}")
  public void andPerUserPerMerchantDiscountTakenIsPer_user_per_merchant_discount_taken(double discountTaken) {
    request.setPerUserPerMerchantDiscountTaken(discountTaken);
  }

  @And("And per user per day discount taken is {double}")
  public void andPerUserPerDayDiscountTakenIsPer_user_per_day_discount_taken(double discountTaken) {
    request.setPerUserPerDayDiscountTaken(discountTaken);
  }

  @And("And per user per month discount taken is {double}")
  public void andPerUserPerMonthDiscountTakenIsPer_user_per_month_discount_taken(double discountTaken) {
    request.setPerUserPerMonthDiscountTaken(discountTaken);
  }

  @And("And per merchant per day discount taken is {double}")
  public void andPerMerchantPerDayDiscountTakenIsPer_merchant_per_day_discount_taken(double discountTaken) {
    request.setPerMerchantPerDayDiscountTaken(discountTaken);
  }

  @And("And per merchant per month discount taken is {double}")
  public void andPerMerchantPerMonthDiscountTakenIsPer_merchant_per_month_discount_taken(double discountTaken) {
    request.setPerMerchantPerMonthDiscountTaken(discountTaken);
  }

  @When("I calculate the discount")
  public void iCalculateTheDiscount() {
    this.discountCalculator = new DiscountCalculator();
    this.afterDiscountAmount = discountCalculator.calculate(request);
  }

  @Then("The calculated discount and amount should be {double} and {double} respectively")
  public void theCalculatedDiscountAndAmountShouldBeDiscount_amountAndCalculated_amountRespectively(double discount, double calculatedAmount) {
    System.out.println("FINAL AMOUNT: " + calculatedAmount + " DISCOUNT: " + discount);
    Assertions.assertEquals(this.afterDiscountAmount, discount);
  }

}
