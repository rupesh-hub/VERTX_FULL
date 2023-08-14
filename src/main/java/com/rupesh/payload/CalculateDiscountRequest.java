package com.rupesh.payload;

public class CalculateDiscountRequest {

  private double transactionAmount;
  private double discountPercentage;
  private double flatDiscountAmount;
  private String discountType;
  private String applicableType;

  private double perUserDiscount;
  private double perUserPerMerchantDiscount;

  private double perUserPerDayDiscount;
  private double perMerchantPerDayDiscount;

  private double perUserPerMonthDiscount;
  private double perMerchantPerMonthDiscount;

  private double perUserDiscountTaken;
  private double perUserPerMerchantDiscountTaken;

  private double perUserPerDayDiscountTaken;
  private double perMerchantPerDayDiscountTaken;

  private double perUserPerMonthDiscountTaken;
  private double perMerchantPerMonthDiscountTaken;

  public double getTransactionAmount() {
    return transactionAmount;
  }

  public void setTransactionAmount(double transactionAmount) {
    this.transactionAmount = transactionAmount;
  }

  public double getDiscountPercentage() {
    return discountPercentage;
  }

  public void setDiscountPercentage(double discountPercentage) {
    this.discountPercentage = discountPercentage;
  }

  public double getFlatDiscountAmount() {
    return flatDiscountAmount;
  }

  public void setFlatDiscountAmount(double flatDiscountAmount) {
    this.flatDiscountAmount = flatDiscountAmount;
  }

  public String getDiscountType() {
    return discountType;
  }

  public void setDiscountType(String discountType) {
    this.discountType = discountType;
  }

  public String getApplicableType() {
    return applicableType;
  }

  public void setApplicableType(String applicableType) {
    this.applicableType = applicableType;
  }

  public double getPerUserDiscount() {
    return perUserDiscount;
  }

  public void setPerUserDiscount(double perUserDiscount) {
    this.perUserDiscount = perUserDiscount;
  }

  public double getPerUserPerMerchantDiscount() {
    return perUserPerMerchantDiscount;
  }

  public void setPerUserPerMerchantDiscount(double perUserPerMerchantDiscount) {
    this.perUserPerMerchantDiscount = perUserPerMerchantDiscount;
  }

  public double getPerUserPerDayDiscount() {
    return perUserPerDayDiscount;
  }

  public void setPerUserPerDayDiscount(double perUserPerDayDiscount) {
    this.perUserPerDayDiscount = perUserPerDayDiscount;
  }

  public double getPerMerchantPerDayDiscount() {
    return perMerchantPerDayDiscount;
  }

  public void setPerMerchantPerDayDiscount(double erMerchantPerDayDiscount) {
    this.perMerchantPerDayDiscount = erMerchantPerDayDiscount;
  }

  public double getPerUserPerMonthDiscount() {
    return perUserPerMonthDiscount;
  }

  public void setPerUserPerMonthDiscount(double perUserPerMonthDiscount) {
    this.perUserPerMonthDiscount = perUserPerMonthDiscount;
  }

  public double getPerMerchantPerMonthDiscount() {
    return perMerchantPerMonthDiscount;
  }

  public void setPerMerchantPerMonthDiscount(double perMerchantPerMonthDiscount) {
    this.perMerchantPerMonthDiscount = perMerchantPerMonthDiscount;
  }

  public double getPerUserDiscountTaken() {
    return perUserDiscountTaken;
  }

  public void setPerUserDiscountTaken(double perUserDiscountTaken) {
    this.perUserDiscountTaken = perUserDiscountTaken;
  }

  public double getPerUserPerMerchantDiscountTaken() {
    return perUserPerMerchantDiscountTaken;
  }

  public void setPerUserPerMerchantDiscountTaken(double perUserPerMerchantDiscountTaken) {
    this.perUserPerMerchantDiscountTaken = perUserPerMerchantDiscountTaken;
  }

  public double getPerUserPerDayDiscountTaken() {
    return perUserPerDayDiscountTaken;
  }

  public void setPerUserPerDayDiscountTaken(double perUserPerDayDiscountTaken) {
    this.perUserPerDayDiscountTaken = perUserPerDayDiscountTaken;
  }

  public double getPerMerchantPerDayDiscountTaken() {
    return perMerchantPerDayDiscountTaken;
  }

  public void setPerMerchantPerDayDiscountTaken(double perMerchantPerDayDiscountTaken) {
    this.perMerchantPerDayDiscountTaken = perMerchantPerDayDiscountTaken;
  }

  public double getPerUserPerMonthDiscountTaken() {
    return perUserPerMonthDiscountTaken;
  }

  public void setPerUserPerMonthDiscountTaken(double perUserPerMonthDiscountTaken) {
    this.perUserPerMonthDiscountTaken = perUserPerMonthDiscountTaken;
  }

  public double getPerMerchantPerMonthDiscountTaken() {
    return perMerchantPerMonthDiscountTaken;
  }

  public void setPerMerchantPerMonthDiscountTaken(double perMerchantPerMonthDiscountTaken) {
    this.perMerchantPerMonthDiscountTaken = perMerchantPerMonthDiscountTaken;
  }
}
