package com.gemvietnam.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.widget.ImageView;

import com.gemvietnam.base.log.Logger;
import com.gemvietnam.common.R;
import com.gemvietnam.utils.ContextUtils;
import com.gemvietnam.utils.StringUtils;
import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.greenrobot.eventbus.EventBus.TAG;

/**
 * Image Utils
 * Created by neo on 3/25/2016.
 */
public class ImageUtils {
  public static final int IMAGE_QUALITY = 100;
  public static final Bitmap.CompressFormat BITMAP_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
  private static final String FILE_EXTENSION = ".JPG";
  private static final int DEFAULT_PLACE_HOLDER = R.drawable.image_thumbnail;
  private static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/OvvyCoporate/";
  private static String CACHE_FOLDER = ".cache/";

  /**
   * Load image with default place holder
   */
  public static void loadImage(Context context, String imageUrl, ImageView imageView) {
    loadImage(context, imageUrl, imageView, DEFAULT_PLACE_HOLDER, DEFAULT_PLACE_HOLDER);
  }

  public static void loadImage(Context context, String imageUrl, ImageView imageView, boolean fit) {
    loadImage(context, imageUrl, imageView, DEFAULT_PLACE_HOLDER, DEFAULT_PLACE_HOLDER, fit);
  }

  /**
   * Using Glide to load image
   */
  public static void loadImage(Context context, String imageUrl, ImageView imageView, int placeHolderId, int errorId) {
    loadImage(context, imageUrl, imageView, placeHolderId, errorId, false);
  }

  /**
   * Using Glide to load image
   */
  public static void loadImage(Context context, String imageUrl, ImageView imageView, int placeHolderId, int errorId, boolean fit) {
    if (!ContextUtils.isValidContext(context)) {
      return;
    }

    ImageLoaderFactory.getInstance().loadImage(context, imageUrl, imageView, placeHolderId, errorId, fit);
  }

  /**
   * Using Glide to load image
   */
  public static void loadImage(Context context, int imageResId, ImageView imageView, int placeHolderId, int errorId, boolean fit) {
    if (!ContextUtils.isValidContext(context)) {
      return;
    }

    ImageLoaderFactory.getInstance().loadImage(context, imageResId, imageView, placeHolderId, errorId, fit);
  }

  public static void loadImageFromUri(Context context, Uri uri, ImageView imageView) {
    if (!ContextUtils.isValidContext(context)) {
      return;
    }

    if (imageView == null) {
      return;
    }

    ImageLoaderFactory.getInstance().loadImageFromUri(context, uri, imageView);
  }

  public static Bitmap getBitmapFromUri(Context context, Uri uri) {
    return ImageLoaderFactory.getInstance().getBitmapFromUri(context, uri);
  }

  //Glide
  public static void loadImageWithoutPlaceHolder(Context context, String imageUrl, ImageView imageView, boolean fit) {
    if (!ContextUtils.isValidContext(context)) {
      return;
    }

    ImageLoaderFactory.getInstance().loadImageWithoutPlaceHolder(context, imageUrl, imageView, fit);
  }

  public static void pauseLoad(Context context) {
    ImageLoaderFactory.getInstance().pauseLoad(context);
  }

  public static void resumeLoad(Context context) {
    ImageLoaderFactory.getInstance().resumeLoad(context);
  }

  public static Drawable getDrawable(Context context, int drawableId) {
    Drawable myDrawable;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      myDrawable = context.getDrawable(drawableId);
    } else {
      myDrawable = context.getResources().getDrawable(drawableId);
    }

    return myDrawable;
  }

  public static String getRootPath() {
    File root = new File(ROOT_PATH);
    if (!root.exists()) {
      root.mkdirs();
    }

    return ROOT_PATH;
  }

  public static String getCachePath() {
    String cachePath = getRootPath() + CACHE_FOLDER;

    File cache = new File(cachePath);
    if (!cache.exists()) {
      cache.mkdirs();
    }

    return cachePath;
  }

  public static Bitmap getBitmapFromLocal(String path) {
    final BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(path, options);
  }

  public static Bitmap getBitmapFromLocal(String path, BitmapFactory.Options options) {
    // final BitmapFactory.Options options = new BitmapFactory.Options();
    // options.inJustDecodeBounds = false;
    return BitmapFactory.decodeFile(path, options);
  }

  public static String saveCache(Bitmap data) {
    String imageName = String.valueOf(System.currentTimeMillis()) + FILE_EXTENSION;
    String pathImage = getCachePath() + imageName;
    saveBitmap(data, pathImage);

    return pathImage;
  }

  public static boolean saveBitmap(Bitmap bitmap, String file) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

    File f = new File(file);

    try {
      if (!f.exists())
        f.createNewFile();
      // write the bytes in file
      FileOutputStream fo = new FileOutputStream(f);
      fo.write(bytes.toByteArray());

      // remember close de FileOutput
      fo.close();
      Logger.i("Save file success == " + file);
      bitmap.recycle();
      bitmap = null;
      return true;
    } catch (IOException e) {
      Logger.e("Save file Error: \n" + e.getMessage());
      bitmap.recycle();
      return false;
    }
  }

  public static void loadImageRounded(Context context, String imageUrl, ImageView imageView) {
    if (StringUtils.isNullOrEmpty(imageUrl)) {
      imageUrl = null;
    }

    Transformation transformation = new RoundedTransformationBuilder()
        .cornerRadiusDp(5)
        .oval(false)
        .build();

    Picasso.with(context)
        .load(imageUrl)
        .fit()
        .centerCrop()
        .placeholder(R.drawable.image_thumbnail)
        .error(R.drawable.image_thumbnail)
        .transform(transformation)
        .tag(TAG)
        .into(imageView);
  }

  private static Bitmap rotateImage(Bitmap bitmap, int rotate) {
    Bitmap rotateBitmap;
    if (rotate > 0) {
      Matrix matrix = new Matrix();
      matrix.postRotate(rotate);
      rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    } else {
      rotateBitmap = bitmap;
    }
    return rotateBitmap;
  }
}
