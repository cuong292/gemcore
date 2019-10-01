package com.gemvietnam.utils;

public class AvatarSocialUtils {
  public static String[] enpointImage = {
      "_normal.jpg", "_normal.jpeg", "_normal.png",
      "_bigger.jpg", "_bigger.jpeg", "_bigger.png",
      "_mini.jpg", "_mini.jpeg", "_mini.png",
  };

  public static String getFBImageLargeUrl(String userID) {
    return "https://graph.facebook.com/" + userID + "/picture?type=large";
  }

  public static String getTwitterImageLargeUrl(String urlNormal) {
    if (urlNormal == null || urlNormal.trim().isEmpty()) {
      return "";
    }
    return clearEndpointImage(urlNormal, enpointImage);
  }

  public static String clearEndpointImage(String urlNormal, String... endPoint) {
    StringBuilder url = new StringBuilder(urlNormal);
    for (int i = 0; i < endPoint.length; i++) {
      if (urlNormal.endsWith(endPoint[i])) {
        int positionStart = urlNormal.lastIndexOf(endPoint[i]);
        String type = endPoint[i].substring(endPoint[i].lastIndexOf('.'));
        return url.delete(positionStart, urlNormal.length() - type.length()).toString();
      }
    }
    return urlNormal;
  }
}
