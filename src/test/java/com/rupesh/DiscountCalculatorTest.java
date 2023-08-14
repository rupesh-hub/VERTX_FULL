package com.rupesh;

import com.rupesh.payload.CalculateDiscountRequest;
import com.rupesh.test.DiscountCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountCalculatorTest {

  private double transactionAmount;
  private double discountPercentage;
  private double flatDiscountAmount;
  private String discountType;
  private String applicableType;
  private double perUserPerDayDiscount = 0.0;
  private double perUserPerDayDiscountTaken = 0.0;
  private DiscountCalculator discountCalculator;
  private CalculateDiscountRequest request;

  @BeforeEach
  void assignValues() {
    transactionAmount = 1000.0;
    discountPercentage = 10.0;
    flatDiscountAmount = 40.0;
    discountCalculator = new DiscountCalculator();
    request = new CalculateDiscountRequest();
    request.setTransactionAmount(transactionAmount);
    request.setDiscountPercentage(discountPercentage);
    request.setFlatDiscountAmount(flatDiscountAmount);
  }

  @Test
  void testFlatDiscountCalculation() {
    discountType = "flat";
    request.setTransactionAmount(transactionAmount);
    request.setFlatDiscountAmount(flatDiscountAmount);
    request.setDiscountType(discountType);
    double discountAmount = discountCalculator.calculate(request);
    assertEquals(discountAmount, 40.0);
  }

  @Test
  void testPercentageDiscount() {
    discountType = "percentage";
    request.setTransactionAmount(transactionAmount);
    request.setDiscountPercentage(discountPercentage);
    request.setDiscountType(discountType);
    double discountAmount = discountCalculator.calculate(request);
    assertEquals(discountAmount, 100.0);
  }

  @Test
  void testBothDiscountWithPerUserPerDayDiscount() {
    discountType = "both";
    applicableType = "lower";
    request.setDiscountType(discountType);
    request.setApplicableType(applicableType);
    request.setPerUserDiscount(perUserPerDayDiscount);
    request.setPerUserPerDayDiscountTaken(perUserPerDayDiscountTaken);
    double discountAmount = discountCalculator.calculate(request);
    assertEquals(discountAmount, 40.0);
  }

  @ParameterizedTest
  @CsvSource({
    /* discount_type applicable_type per_user_per_day per_user_per_day_taken discount_amount*/
    "percentage, null, 0.0, 0.0, 100.0",
    "flat, null, 0.0, 0.0, 40.0",
    "both, lower, 0.0, 0.0, 40.0",
    "both, higher, 0.0, 0.0, 100.0",
    "both, combination, 0.0, 0.0, 140.0",
    "both, combination, 40.0, 30.0, 10.0",
  })
  @DisplayName("test discount calculation with discount type and applicable type")
  void testCalculateWithDifferentDiscountTypes(String discountType,
                                               String applicableType,
                                               double perUserPerDayDiscount,
                                               double perUserPerDayDiscountTaken,
                                               double expectedDiscount) {
    request.setDiscountType(discountType);
    request.setApplicableType(applicableType);
    request.setPerUserPerDayDiscount(perUserPerDayDiscount);
    request.setPerUserPerDayDiscountTaken(perUserPerDayDiscountTaken);
    double discountAmount = discountCalculator.calculate(request);
    assertEquals(expectedDiscount, discountAmount);
  }

  @Test
  void testDefaultInCaseOfNoDiscountScheme() {
    CalculateDiscountRequest request = new CalculateDiscountRequest();
    request.setTransactionAmount(1000.0);
    double discountAmount = discountCalculator.calculate(request);
    assertEquals(0.0, discountAmount);
  }

  @Test
  void testInCaseOfSelectedBothButProvidedWrongOrNoApplicableType() {
    CalculateDiscountRequest request = new CalculateDiscountRequest();
    request.setTransactionAmount(1000.0);
    request.setDiscountType("both");
    double discountAmount = discountCalculator.calculate(request);
    assertEquals(0.0, discountAmount);
  }


  @ParameterizedTest
  @MethodSource("arguments")
  void testCalculateWithParameters(double amount,
                                   String discountType,
                                   double percentage,
                                   double flatDiscount,
                                   String applicableType,
                                   double perUserDiscount,
                                   double perUserDiscountTaken,
                                   double perUserPerMerchantDiscount,
                                   double perUserPerMerchantDiscountTaken,
                                   double perUserPerDayDiscount,
                                   double perUserPerDayDiscountTaken,
                                   double perUserPerMonthDiscount,
                                   double perUserPerMonthDiscountTaken,
                                   double perMerchantPerDayDiscount,
                                   double perMerchantPerDayDiscountTaken,
                                   double perMerchantPerMonthDiscount,
                                   double perMerchantPerMonthDiscountTaken,
                                   double expectedDiscountAmount,
                                   double expectedCalculatedAmount
  ) {

    CalculateDiscountRequest request = new CalculateDiscountRequest();
    request.setTransactionAmount(amount);
    request.setDiscountType(discountType);
    request.setDiscountPercentage(percentage);
    request.setFlatDiscountAmount(flatDiscount);
    request.setApplicableType(applicableType);
    request.setPerUserDiscount(perUserDiscount);
    request.setPerUserDiscountTaken(perUserDiscountTaken);
    request.setPerUserPerMerchantDiscount(perUserPerMerchantDiscount);
    request.setPerUserPerMerchantDiscountTaken(perUserPerMerchantDiscountTaken);
    request.setPerUserPerDayDiscount(perUserPerDayDiscount);
    request.setPerUserPerDayDiscountTaken(perUserPerDayDiscountTaken);
    request.setPerUserPerMonthDiscount(perUserPerMonthDiscount);
    request.setPerUserPerMonthDiscountTaken(perUserPerMonthDiscountTaken);
    request.setPerMerchantPerDayDiscount(perMerchantPerDayDiscount);
    request.setPerMerchantPerDayDiscountTaken(perMerchantPerDayDiscountTaken);
    request.setPerMerchantPerMonthDiscount(perMerchantPerMonthDiscount);
    request.setPerMerchantPerMonthDiscountTaken(perMerchantPerMonthDiscountTaken);

    double discountAmount = discountCalculator.calculate(request);
    assertEquals(expectedDiscountAmount, discountAmount);

    double calculatedAmount = amount - discountAmount;
    assertEquals(expectedCalculatedAmount, calculatedAmount);
  }

  static Stream<Arguments> arguments() {
    return Stream.of(
      Arguments.of(1000, "both", 10, 50, "combination", 200, 10, 190, 10, 180, 10, 170, 40, 140, 0, 150, 10, 130, 870.0, 870.0),
      Arguments.of(1000, "both", 10, 50, "combination", 200, 200, 190, 190, 180, 180, 170, 170, 140, 140, 150, 150, 0.0, 1000.0, 1000.0),
      Arguments.of(1000, "percentage", 10, 0, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 900),
      Arguments.of(1000, "flat", 0, 20, null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 980),
      Arguments.of(1000, "both", 10, 20, "lower", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 20, 980),
      Arguments.of(1000, "both", 10, 20, "higher", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 100, 900),
      Arguments.of(1000, "both", 10, 20, "combination", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 120, 880)

    );
  }

}
