package com.gemvietnam.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.gemvietnam.base.CoreDefault;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtils {
  public static long SIZE_VIDEO_MAX = 40 * 1024 * 1024;

  @SuppressLint("NewApi")
  public static String getPathFromUri(final Context context, final Uri uri) {
    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageDirectory() + "/" + split[1];
        }
      }
      // DownloadsProvider
      else if (isDownloadsDocument(uri)) {
        Cursor cursor = null;
        try {
          cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
          if (cursor != null && cursor.moveToFirst()) {
            String fileName = cursor.getString(0);
            String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
            if (!TextUtils.isEmpty(path)) {
              return path;
            }
          }
        } finally {
          if (cursor != null)
            cursor.close();
        }
        final String id = DocumentsContract.getDocumentId(uri);
        try {
          final Uri contentUri = ContentUris.withAppendedId(
              Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

          return getDataColumn(context, contentUri, null, null);
        } catch (NumberFormatException e) {
          //In Android 8 and Android P the id is not a number
          return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
        }
      }
      // MediaProvider
      else if (isMediaDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{
            split[1]
        };

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {

      // Return the remote address
      if (isGooglePhotosUri(uri))
        return uri.getLastPathSegment();

      return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }

    return null;
  }

  public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {
        column
    };

    try {
      cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
          null);
      if (cursor != null && cursor.moveToFirst()) {
        final int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } finally {
      if (cursor != null)
        cursor.close();
    }
    return null;
  }

  public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri.getAuthority());
  }

  public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri.getAuthority());
  }

  public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri.getAuthority());
  }

  public static boolean isGooglePhotosUri(Uri uri) {
    return "com.google.android.apps.photos.content".equals(uri.getAuthority());
  }

  public static MultipartBody.Part getMultipartBodyPart(String name, File file) {
    String mimeTypeString = getMimeType(file);
    if (mimeTypeString == null || mimeTypeString.trim().isEmpty()) {
      mimeTypeString = CoreDefault.MIME_TYPE_IMAGE_JPEG;
    }
    RequestBody requestBody = RequestBody.create(MediaType.parse(mimeTypeString), file);
    return MultipartBody.Part.createFormData(name, file.getName(), requestBody);
  }

  public static String getMimeType(File file) {
    String path = file.getPath();
    path = path.replace(" ", "");
    String extension = MimeTypeMap.getFileExtensionFromUrl(path).toLowerCase();
    return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
  }

  public static boolean isOverSize(File file) {
    long videoSize = file.length();
    return videoSize > SIZE_VIDEO_MAX;
  }

  public static long getSize(String path) {
    File file = new File(path);
    return file.length();
  }

  public static long getSizeMB(String path) {
    File file = new File(path);
    return file.length() / 1024 / 1024;
  }
}
