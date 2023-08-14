package com.rupesh.test;

import com.rupesh.enums.ApplicableType;
import com.rupesh.enums.DiscountType;
import com.rupesh.payload.CalculateDiscountRequest;

import java.util.Arrays;

public class DiscountCalculator {

  /**
   * * method is applicable for all type of configuration, daily_limit, monthly_limit
   * * (per_user + per_user_per_day + per_user_per_month + per_user_per_merchant + per_merchant_per_day + per_merchant_per_month)
   *
   * @param request
   * @return
   */
  public static double calculate(final CalculateDiscountRequest request) {

    /**
     * first: calculating discount with previously accumulated taken discount
     * remaining_discount = allowed_discount - previous_taken_discount
     */
    Double[] discounts = {
      (request.getPerUserDiscount() > 0) ? request.getPerUserDiscount() - request.getPerUserDiscountTaken() : Double.POSITIVE_INFINITY,
      (request.getPerUserPerMerchantDiscount() > 0) ? request.getPerUserPerMerchantDiscount() - request.getPerUserPerMerchantDiscountTaken() : Double.POSITIVE_INFINITY,
      (request.getPerUserPerDayDiscount() > 0) ? request.getPerUserPerDayDiscount() - request.getPerUserPerDayDiscountTaken() : Double.POSITIVE_INFINITY,
      (request.getPerUserPerMonthDiscount() > 0) ? request.getPerUserPerMonthDiscount() - request.getPerUserPerMonthDiscountTaken() : Double.POSITIVE_INFINITY,
      (request.getPerMerchantPerDayDiscount() > 0) ? request.getPerMerchantPerDayDiscount() - request.getPerMerchantPerDayDiscountTaken() : Double.POSITIVE_INFINITY,
      (request.getPerMerchantPerMonthDiscount() > 0) ? request.getPerMerchantPerMonthDiscount() - request.getPerMerchantPerMonthDiscountTaken() : Double.POSITIVE_INFINITY
    };

    /**
     * after getting remaining discount compare them to get the lowest one
     */
    var applicableDiscount = Arrays.stream(discounts)
      .filter(value -> value != Double.POSITIVE_INFINITY)
      .min(Double::compareTo)
      .orElse(Double.POSITIVE_INFINITY);

    /**
     * calculating other parts and returning discount
     */
    var percentAmount = ((request.getDiscountPercentage() / 100) * request.getTransactionAmount());
    var perLower = Math.min(percentAmount, request.getFlatDiscountAmount());
    var perHigher = Math.max(percentAmount, request.getFlatDiscountAmount());
    var perBoth = (percentAmount + request.getFlatDiscountAmount());

    switch (DiscountType.byCode(request.getDiscountType())) {
      case FLAT:
        return applicableDiscount <= request.getFlatDiscountAmount() ? applicableDiscount : request.getFlatDiscountAmount();

      case PERCENTAGE:
        return applicableDiscount <= percentAmount ? applicableDiscount : percentAmount;

      case BOTH: {
        switch (ApplicableType.byCode(request.getApplicableType())) {
          case LOWER:
            return applicableDiscount <= perLower ? applicableDiscount : perLower;

          case HIGHER:
            return applicableDiscount <= perHigher ? applicableDiscount : perHigher;

          case COMBINATION:
            return perBoth <= applicableDiscount ? perBoth : applicableDiscount;

          default:
            return 0.0;
        }
      }
      default:
        return 0.0;
    }
  }

}
