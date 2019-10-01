package com.gemvietnam.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Collection Utils
 * Created by neo on 9/27/2016.
 */

public class CollectionUtils {
  public static boolean isNullOrEmpty(Collection collection) {
    return collection == null || collection.isEmpty();
  }

  public static boolean isHasContent(Collection collection) {
    return collection != null && !collection.isEmpty();
  }

  public static boolean isNullOrEmpty(HashMap map) {
    return map == null || map.isEmpty();
  }

  public static boolean isHasContent(HashMap map) {
    return map != null && !map.isEmpty();
  }

  public static <T> boolean isEndOfList(List<T> list, T t) {
    if (isNullOrEmpty(list)) {
      return false;
    }
    return list.indexOf(t) == list.size() - 1;
  }
}
