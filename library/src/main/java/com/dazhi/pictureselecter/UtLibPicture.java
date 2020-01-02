package com.dazhi.pictureselecter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.View;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 功能：
 * 描述：
 * 作者：WangZezhi
 * 邮箱：wangzezhi528@163.com
 * 创建日期：2018/5/25 14:50
 * 修改日期：2018/5/25 14:50
 */
@SuppressWarnings("WeakerAccess")
public class UtLibPicture {

    /**
     * =======================================
     * 作者：WangZezhi  (2018/5/25  15:02)
     * 功能：显示图片选择器
     * 描述：
     * =======================================
     */
    public static void showDialog(final Activity activity, final List<LocalMedia> lsPicture, final int intMaxNum) {
        showDialog(activity, lsPicture, intMaxNum, 200);
    }

    public static void showDialog(final Activity activity, final List<LocalMedia> lsPicture, final int intMaxNum, final int intCompressSize) {
        new PictureSelecterDialog.Builder(activity)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int id = v.getId();
                        //选择拍照
                        if (id == R.id.photo_layout) {
                            PictureSelector.create(activity)
                                    .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                                    .loadImageEngine(GlideEngine.createGlideEngine())
                                    .isCamera(true)// 是否显示拍照按钮
                                    .maxSelectNum(intMaxNum)// 最大图片选择数量
                                    .selectionMedia(lsPicture)// 是否传入已选图片
                                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                                    .minSelectNum(1)// 最小选择数量
                                    .compress(true) // 打开压缩
                                    .cutOutQuality(100)
                                    .minimumCompressSize(intCompressSize)
                                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                                    .previewImage(true)// 是否可预览图片
                                    .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                                    .enableCrop(false)// 是否裁剪
                                    .forResult(PictureConfig.REQUEST_CAMERA);//结果回调onActivityResult code
                            return;
                        }
                        //选择图库
                        if (id == R.id.choose_layout) {
                            PictureSelector.create(activity)
                                    .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                                    .loadImageEngine(GlideEngine.createGlideEngine())
                                    .isCamera(false)// 是否显示拍照按钮
                                    .maxSelectNum(intMaxNum)// 最大图片选择数量
                                    .selectionMedia(lsPicture)// 是否传入已选图片
                                    .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                                    .minSelectNum(1)// 最小选择数量
                                    .compress(true) // 打开压缩
                                    .cutOutQuality(100)
                                    .minimumCompressSize(intCompressSize)
                                    .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                                    .previewImage(true)// 是否可预览图片
                                    .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                                    .enableCrop(false)// 是否裁剪
                                    .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                        }
                    }
                })
                .create()
                .show();
    }

    public static void showCamera(final Activity activity, final List<LocalMedia> lsPicture, final int intMaxNum, final int intCompressSize) {
        PictureSelector.create(activity)
                .openCamera(PictureMimeType.ofImage())// 单独拍照，也可录像或也可音频 看你传入的类型是图片or视频
                .loadImageEngine(GlideEngine.createGlideEngine())
                .isCamera(true)// 是否显示拍照按钮
                .maxSelectNum(intMaxNum)// 最大图片选择数量
                .selectionMedia(lsPicture)// 是否传入已选图片
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles
                .minSelectNum(1)// 最小选择数量
                .compress(true) // 打开压缩
                .cutOutQuality(100)
                .minimumCompressSize(intCompressSize)
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
                .previewEggs(false)//预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .enableCrop(false)// 是否裁剪
                .forResult(PictureConfig.REQUEST_CAMERA);//结果回调onActivityResult code
    }

    /**
     * =======================================
     * 作者：WangZezhi  (2018/5/25  16:04)
     * 功能：清除所有缓存 例如：压缩、裁剪、视频、音频所生成的临时文件
     * 描述：
     * 注意：需要系统sd卡权限
     * =======================================
     */
    public static void deleteCache(Activity activity) {
        PictureFileUtils.deleteAllCacheDirFile(activity);
    }

    /**
     * =======================================
     * 作者：WangZezhi  (2018/5/25  17:25)
     * 功能：图片压缩相关
     * 描述：
     * =======================================
     */
    public static Bitmap getBitmapByWH(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap == null) {
            return null;
        }
//        int degree = readPictureDegree(filePath);
        int degree = 0;
        try {
            degree = getOrientation(toByteArray(new FileInputStream(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        bitmap = rotateBitmap(bitmap, degree);
        return bitmap;
    }

    public static byte[] bitmapToByteArr(String strPath, int size) {
        Bitmap bitmap=BitmapFactory.decodeFile(strPath);
        return bitmapToByteArr(bitmap, size);
    }
    public static byte[] bitmapToByteArr(Bitmap bitmap, int size) {
        byte[] bytes = null;
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
        bytes = outputStream.toByteArray();
        while (bytes.length / 1024 > size) {
            outputStream.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
            bytes = outputStream.toByteArray();
        }
        return bytes;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

//    private static int readPictureDegree(String filePath) {
//        int degree = 0;
//        try {
//            ExifInterface exifInterface = new ExifInterface(filePath);
//            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    degree = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    degree = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    degree = 270;
//                    break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return degree;
//    }

    private static byte[] toByteArray(InputStream is) {
        if (is == null) {
            return new byte[0];
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int read;
        byte[] data = new byte[4096];

        try {
            while ((read = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, read);
            }
        } catch (Exception ignored) {
            return new byte[0];
        } finally {
            try {
                buffer.close();
            } catch (IOException ignored) {
            }
        }

        return buffer.toByteArray();
    }

    private static int getOrientation(byte[] jpeg) {
        if (jpeg == null) {
            return 0;
        }

        int offset = 0;
        int length = 0;

        // ISO/IEC 10918-1:1993(E)
        while (offset + 3 < jpeg.length && (jpeg[offset++] & 0xFF) == 0xFF) {
            int marker = jpeg[offset] & 0xFF;

            // Check if the marker is a padding.
            if (marker == 0xFF) {
                continue;
            }
            offset++;

            // Check if the marker is SOI or TEM.
            if (marker == 0xD8 || marker == 0x01) {
                continue;
            }
            // Check if the marker is EOI or SOS.
            if (marker == 0xD9 || marker == 0xDA) {
                break;
            }

            // Get the length and check if it is reasonable.
            length = pack(jpeg, offset, 2, false);
            if (length < 2 || offset + length > jpeg.length) {
//                Log.e(TAG, "Invalid length");
                return 0;
            }

            // Break if the marker is EXIF in APP1.
            if (marker == 0xE1 && length >= 8
                    && pack(jpeg, offset + 2, 4, false) == 0x45786966
                    && pack(jpeg, offset + 6, 2, false) == 0) {
                offset += 8;
                length -= 8;
                break;
            }

            // Skip other markers.
            offset += length;
            length = 0;
        }

        // JEITA CP-3451 Exif Version 2.2
        if (length > 8) {
            // Identify the byte order.
            int tag = pack(jpeg, offset, 4, false);
            if (tag != 0x49492A00 && tag != 0x4D4D002A) {
//                Log.e(TAG, "Invalid byte order");
                return 0;
            }
            boolean littleEndian = (tag == 0x49492A00);

            // Get the offset and check if it is reasonable.
            int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
            if (count < 10 || count > length) {
//                Log.e(TAG, "Invalid offset");
                return 0;
            }
            offset += count;
            length -= count;

            // Get the count and go through all the elements.
            count = pack(jpeg, offset - 2, 2, littleEndian);
            while (count-- > 0 && length >= 12) {
                // Get the tag and check if it is orientation.
                tag = pack(jpeg, offset, 2, littleEndian);
                if (tag == 0x0112) {
                    int orientation = pack(jpeg, offset + 8, 2, littleEndian);
                    switch (orientation) {
                        case 1:
                            return 0;
                        case 3:
                            return 180;
                        case 6:
                            return 90;
                        case 8:
                            return 270;
                    }
//                    Log.e(TAG, "Unsupported orientation");
                    return 0;
                }
                offset += 12;
                length -= 12;
            }
        }

//        Log.e(TAG, "Orientation not found");
        return 0;
    }

    private static int pack(byte[] bytes, int offset, int length, boolean littleEndian) {
        int step = 1;
        if (littleEndian) {
            offset += length - 1;
            step = -1;
        }

        int value = 0;
        while (length-- > 0) {
            value = (value << 8) | (bytes[offset] & 0xFF);
            offset += step;
        }
        return value;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
        if (bitmap == null)
            return null;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix mtx = new Matrix();
        mtx.postRotate(rotate);
        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }


}
