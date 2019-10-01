package com.gemvietnam.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DecimalFormatUtils {
  public static DecimalFormat NUMBER_DECIMAL_FORMAT = new DecimalFormat(",##0.00", new DecimalFormatSymbols(Locale.ENGLISH));
  public static DecimalFormat NUMBER_DECIMAL_FORMAT_SIMPLE = new DecimalFormat("0.##", new DecimalFormatSymbols(Locale.ENGLISH));

  public static String formatPrice(double number) {
    if (number < 0) {
      number = 0;
    }
    return "$" + formatNumber(number);
  }

  public static double getPriceFromPriceFormatted(String priceFormatted) {
    double result = 0d;
    if (priceFormatted.contains("$")) {
      int indexDollar = priceFormatted.indexOf("$");
      priceFormatted = priceFormatted.substring(0, indexDollar) + priceFormatted.substring(indexDollar + 1);
    }
    priceFormatted = priceFormatted.replaceAll("-", "");
    priceFormatted = priceFormatted.replaceAll(" ", "");
    priceFormatted = priceFormatted.replaceAll(String.valueOf(NUMBER_DECIMAL_FORMAT.getDecimalFormatSymbols().getGroupingSeparator()), "");
    try {
      result = Double.parseDouble(priceFormatted);
    } catch (Exception e) {

    }
    return result;
  }

  public static String formatNumber(double number) {
    return formatNumber(NUMBER_DECIMAL_FORMAT, roundNumber(number));
  }

  public static String formatNumberSimple(double number) {
    return formatNumber(NUMBER_DECIMAL_FORMAT_SIMPLE, roundNumber(number));
  }

  public static String formatNumber(DecimalFormat decimalFormat, double number) {
    return decimalFormat.format(number);
  }

  public static double roundNumber(float number) {
    return (Math.round(number * 100) / 100d);
  }

  public static double roundNumber(double number) {
    return (Math.round(number * 100) / 100d);
  }
}
