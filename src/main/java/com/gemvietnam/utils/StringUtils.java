package com.gemvietnam.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Patterns;
import android.webkit.URLUtil;

import com.gemvietnam.Constants;
import com.gemvietnam.base.log.Logger;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Utils
 * Created by neo on 2/16/2016.
 */
public class StringUtils {
  public static boolean isUrlLink(String url) {
    if (isNullOrEmpty(url))
      return false;
    return URLUtil.isValidUrl(url);
  }

  public static String formatUrl(String url) {
    if (url == null) return "";
    if (url.startsWith("http")) {
      return url;
    }
    return "http://" + url;
  }

  public static SpannableString formatQuestion(Context context, String question, String option, int optionColor) {
    return addOptionalString(context, question, option, optionColor);
  }

  public static SpannableString addOptionalString(Context context, String title, String option, int optionColor) {
    String fullTitle = title + option;
    SpannableString optional = new SpannableString(fullTitle);
    optional.setSpan(new RelativeSizeSpan(0.75f), title.length(), fullTitle.length(), 0);
    optional.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, optionColor)), title.length(), fullTitle.length(), 0);
    return optional;
  }

  public static SpannableStringBuilder formatNotification(Context context, String owner, String notification, int ownerColor) {
    SpannableStringBuilder result = new SpannableStringBuilder(notification);
    if (notification.contains(owner)) {
      int index = notification.indexOf(owner);
      result.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, ownerColor)), index, owner.length(), 0);
      return result;
    }
    return result;
  }

  public static String convertListToString(List<String> list) {
    if (list == null || list.isEmpty()) {
      return "";
    }
    if (list.size() == 1) {
      return list.get(0);
    }
    String result = "";
    for (String string : list) {
      result = result + string + ", ";
    }
    return result.substring(0, result.lastIndexOf(", "));
  }

  public static boolean isNullOrEmpty(String value) {
    return value == null || value.trim().isEmpty();
  }

  public static boolean isHasContent(String value) {
    return value != null && !value.trim().isEmpty();
  }

  public static boolean isEmpty(String s) {
    return s == null || s.trim().isEmpty();
  }

  public static boolean isEmpty(CharSequence s) {
    return s == null || s.length() == 0;
  }

  public static boolean isNumeric(String s) {
    return s.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
  }

  public static void openLink(Context context, String link) {
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(link));
    context.startActivity(i);
  }

  /**
   * method is used for checking valid email id format.
   *
   * @param email
   * @return boolean true for valid false for invalid
   */
  public static boolean isEmailValid(String email) {
    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(email);
    return matcher.matches();
  }

  /**
   * Strip accent character from string
   */
  public static String stripAccent(String s) {
    if (s == null) {
      return null;
    }

    String stripAccent = org.apache.commons.lang3.StringUtils.stripAccents(s);
    Logger.e("stripAccent " + stripAccent);

    stripAccent = replaceSpecialAccent(stripAccent);
    return stripAccent;
  }

  /**
   * Replace specials characters with EN characters
   */
  private static String replaceSpecialAccent(String s) {
    String result = s.replaceAll("đ", "d");
    result = result.replaceAll("Đ", "D");
    return result;
  }

  /**
   * Format number to thousands comma (,) separator
   */
  public static String getNumberFormatted(long number) {
    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
    DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();

    symbols.setGroupingSeparator('.');
    formatter.setDecimalFormatSymbols(symbols);

    return formatter.format(number);
  }

  public static String capitalizeFirstLetter(String original) {
    if (original == null || original.length() == 0) {
      return Constants.EMPTY_STRING;
    }
    return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
  }

  public static String validString(String original) {
    return original == null ? Constants.EMPTY_STRING : original;
  }

  public static int compareLowerCase(String string1, String string2) {
    if (StringUtils.isEmpty(string1)) {
      if (StringUtils.isEmpty(string2)) {
        return 0;
      }
      return 1;
    } else if (StringUtils.isEmpty(string2)) {
      return -1;
    } else {
      return string1.toLowerCase().compareTo(string2.toLowerCase());
    }
  }

  public static boolean equalsIgnoreCase(String s1, String s2) {
    return s1 != null && s1.equalsIgnoreCase(s2);
  }

  public static String getFullName(String firstName, String lastName) {
    if (isEmpty(firstName)) {
      return validString(lastName);
    } else if (isEmpty(lastName)) {
      return firstName;
    } else {
      return firstName.trim().concat(" ").concat(lastName.trim());
    }
  }

  public static String getNameDisplay(String... values) {
    if (values != null && values.length > 0) {
      for (String v : values) {
        if (v != null && !v.isEmpty()) {
          return v;
        }
      }
    }
    return "";
  }


  @NonNull
  public static String getPercent(String percent) {
    return " - ".concat(percent).concat("%");
  }

  @NonNull
  public static String getTotalPhotos(int total) {

    if (total <= 1) {
      return String.valueOf(total).concat(" photo");
    } else {
      return String.valueOf(total).concat(" photos");
    }
  }

  public static String setPercent(Double percent) {
    if (percent == null) {
      percent = 0d;
    }
    return String.valueOf(percent).concat("%");
  }

  public static boolean checkEmail(String email) {
    Pattern pattern = Patterns.EMAIL_ADDRESS;
    return !isEmpty(email) && pattern.matcher(email).matches();
  }

  public static boolean checkPhone(String phone) {
    Pattern pattern = Patterns.PHONE;
    return !isEmpty(phone) && pattern.matcher(phone).matches();
  }

  public static String removeAccent(String s) {
    return VNCharacterUtils.removeAccent(s).replaceAll("[^\\p{ASCII}]", "").replaceAll("[!@#$%^&*(),\\-+=]", "");
  }

  public static String getRangeFormat(String min, String max) {
    if (min == null || min.isEmpty()) {
      if (max == null || max.isEmpty()) {
        return "";
      } else {
        return max;
      }
    } else {
      if (max == null || max.isEmpty()) {
        return min + "+";
      } else {
        return min + "-" + max;
      }
    }
  }

  public static String getDashBoardLabel(String label, String type) {
    if ("week".equalsIgnoreCase(type)) {
      return label.substring(0, 3);
    } else if ("month".equalsIgnoreCase(type)) {
      return Integer.parseInt(label) + "";
    } else {
      return label;
    }
  }

  private static final NavigableMap<Long, String> suffixes = new TreeMap<>();

  static {
    suffixes.put(1_000L, "k");
    suffixes.put(1_000_000L, "M");
    suffixes.put(1_000_000_000L, "G");
    suffixes.put(1_000_000_000_000L, "T");
    suffixes.put(1_000_000_000_000_000L, "P");
    suffixes.put(1_000_000_000_000_000_000L, "E");
  }

  public static String format(long value) {
    //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
    if (value == Long.MIN_VALUE) return format(Long.MIN_VALUE + 1);
    if (value < 0) return "-" + format(-value);
    if (value < 1000) return Long.toString(value); //deal with easy case

    Map.Entry<Long, String> e = suffixes.floorEntry(value);
    Long divideBy = e.getKey();
    String suffix = e.getValue();

    long truncated = value / (divideBy / 10); //the number part of the output times 10
    boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
    return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
  }
}
