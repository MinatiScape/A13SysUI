package com.android.systemui.screenshot;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.os.Trace;
import android.provider.MediaStore;
import androidx.exifinterface.media.ExifInterface;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.UUID;
/* loaded from: classes.dex */
public final class ImageExporter {
    public static final Duration PENDING_ENTRY_TTL = Duration.ofHours(24);
    public static final String SCREENSHOTS_PATH = Environment.DIRECTORY_PICTURES + File.separator + Environment.DIRECTORY_SCREENSHOTS;
    public Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.PNG;
    public int mQuality = 100;
    public final ContentResolver mResolver;

    /* loaded from: classes.dex */
    public static final class ImageExportException extends IOException {
        public ImageExportException(String str) {
            super(str);
        }

        public ImageExportException(String str, IOException iOException) {
            super(str, iOException);
        }
    }

    /* loaded from: classes.dex */
    public static class Result {
        public String fileName;
        public Bitmap.CompressFormat format;
        public boolean published;
        public UUID requestId;
        public long timestamp;
        public Uri uri;

        public final String toString() {
            return "Result{uri=" + this.uri + ", requestId=" + this.requestId + ", fileName='" + this.fileName + "', timestamp=" + this.timestamp + ", format=" + this.format + ", published=" + this.published + ", deleted=false}";
        }
    }

    /* loaded from: classes.dex */
    public static class Task {
        public final Bitmap mBitmap;
        public final ZonedDateTime mCaptureTime;
        public final String mFileName;
        public final Bitmap.CompressFormat mFormat;
        public final boolean mPublish = true;
        public final int mQuality;
        public final UUID mRequestId;
        public final ContentResolver mResolver;

        public final Result execute() throws ImageExportException, InterruptedException {
            Uri uri;
            ImageExportException e;
            Trace.beginSection("ImageExporter_execute");
            Result result = new Result();
            try {
                try {
                    uri = ImageExporter.m80$$Nest$smcreateEntry(this.mResolver, this.mFormat, this.mCaptureTime, this.mFileName);
                } catch (ImageExportException e2) {
                    e = e2;
                    uri = null;
                }
                try {
                    ImageExporter.m82$$Nest$smthrowIfInterrupted();
                    ImageExporter.m84$$Nest$smwriteImage(this.mResolver, this.mBitmap, this.mFormat, this.mQuality, uri);
                    ImageExporter.m82$$Nest$smthrowIfInterrupted();
                    ImageExporter.m83$$Nest$smwriteExif(this.mResolver, uri, this.mRequestId, this.mBitmap.getWidth(), this.mBitmap.getHeight(), this.mCaptureTime);
                    ImageExporter.m82$$Nest$smthrowIfInterrupted();
                    if (this.mPublish) {
                        ImageExporter.m81$$Nest$smpublishEntry(this.mResolver, uri);
                        result.published = true;
                    }
                    result.timestamp = this.mCaptureTime.toInstant().toEpochMilli();
                    result.requestId = this.mRequestId;
                    result.uri = uri;
                    result.fileName = this.mFileName;
                    result.format = this.mFormat;
                    return result;
                } catch (ImageExportException e3) {
                    e = e3;
                    if (uri != null) {
                        this.mResolver.delete(uri, null);
                    }
                    throw e;
                }
            } finally {
                Trace.endSection();
            }
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("export [");
            m.append(this.mBitmap);
            m.append("] to [");
            m.append(this.mFormat);
            m.append("] at quality ");
            m.append(this.mQuality);
            return m.toString();
        }

        public Task(ContentResolver contentResolver, UUID uuid, Bitmap bitmap, ZonedDateTime zonedDateTime, Bitmap.CompressFormat compressFormat, int i) {
            this.mResolver = contentResolver;
            this.mRequestId = uuid;
            this.mBitmap = bitmap;
            this.mCaptureTime = zonedDateTime;
            this.mFormat = compressFormat;
            this.mQuality = i;
            this.mFileName = ImageExporter.createFilename(zonedDateTime, compressFormat);
        }
    }

    @VisibleForTesting
    public static String createFilename(ZonedDateTime zonedDateTime, Bitmap.CompressFormat compressFormat) {
        String str;
        Object[] objArr = new Object[2];
        objArr[0] = zonedDateTime;
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
        if (i == 1) {
            str = "jpg";
        } else if (i == 2) {
            str = "png";
        } else if (i == 3 || i == 4 || i == 5) {
            str = "webp";
        } else {
            throw new IllegalArgumentException("Unknown CompressFormat!");
        }
        objArr[1] = str;
        return String.format("Screenshot_%1$tY%<tm%<td-%<tH%<tM%<tS.%2$s", objArr);
    }

    /* renamed from: com.android.systemui.screenshot.ImageExporter$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$android$graphics$Bitmap$CompressFormat;

        static {
            int[] iArr = new int[Bitmap.CompressFormat.values().length];
            $SwitchMap$android$graphics$Bitmap$CompressFormat = iArr;
            try {
                iArr[Bitmap.CompressFormat.JPEG.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.PNG.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSLESS.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$android$graphics$Bitmap$CompressFormat[Bitmap.CompressFormat.WEBP_LOSSY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* renamed from: -$$Nest$smcreateEntry  reason: not valid java name */
    public static Uri m80$$Nest$smcreateEntry(ContentResolver contentResolver, Bitmap.CompressFormat compressFormat, ZonedDateTime zonedDateTime, String str) {
        Trace.beginSection("ImageExporter_createEntry");
        try {
            Uri insert = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, createMetadata(zonedDateTime, compressFormat, str));
            if (insert != null) {
                return insert;
            }
            throw new ImageExportException("ContentResolver#insert returned null.");
        } finally {
            Trace.endSection();
        }
    }

    /* renamed from: -$$Nest$smpublishEntry  reason: not valid java name */
    public static void m81$$Nest$smpublishEntry(ContentResolver contentResolver, Uri uri) {
        Trace.beginSection("ImageExporter_publishEntry");
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("is_pending", (Integer) 0);
            contentValues.putNull("date_expires");
            if (contentResolver.update(uri, contentValues, null) < 1) {
                throw new ImageExportException("Failed to publish entry. ContentResolver#update reported no rows updated.");
            }
        } finally {
            Trace.endSection();
        }
    }

    /* renamed from: -$$Nest$smwriteExif  reason: not valid java name */
    public static void m83$$Nest$smwriteExif(ContentResolver contentResolver, Uri uri, UUID uuid, int i, int i2, ZonedDateTime zonedDateTime) {
        Trace.beginSection("ImageExporter_writeExif");
        try {
            try {
                ParcelFileDescriptor openFile = contentResolver.openFile(uri, "rw", null);
                if (openFile != null) {
                    try {
                        ExifInterface exifInterface = new ExifInterface(openFile.getFileDescriptor());
                        updateExifAttributes(exifInterface, uuid, i, i2, zonedDateTime);
                        try {
                            exifInterface.saveAttributes();
                            FileUtils.closeQuietly(openFile);
                            Trace.endSection();
                        } catch (IOException e) {
                            throw new ImageExportException("ExifInterface threw an exception writing to the file descriptor.", e);
                        }
                    } catch (IOException e2) {
                        throw new ImageExportException("ExifInterface threw an exception reading from the file descriptor.", e2);
                    }
                } else {
                    throw new ImageExportException("ContentResolver#openFile returned null.");
                }
            } catch (FileNotFoundException e3) {
                throw new ImageExportException("ContentResolver#openFile threw an exception.", e3);
            }
        } catch (Throwable th) {
            FileUtils.closeQuietly((AutoCloseable) null);
            Trace.endSection();
            throw th;
        }
    }

    /* renamed from: -$$Nest$smwriteImage  reason: not valid java name */
    public static void m84$$Nest$smwriteImage(ContentResolver contentResolver, Bitmap bitmap, Bitmap.CompressFormat compressFormat, int i, Uri uri) {
        Trace.beginSection("ImageExporter_writeImage");
        try {
            try {
                OutputStream openOutputStream = contentResolver.openOutputStream(uri);
                try {
                    SystemClock.elapsedRealtime();
                    if (bitmap.compress(compressFormat, i, openOutputStream)) {
                        if (openOutputStream != null) {
                            openOutputStream.close();
                        }
                        return;
                    }
                    throw new ImageExportException("Bitmap.compress returned false. (Failure unknown)");
                } catch (Throwable th) {
                    if (openOutputStream != null) {
                        try {
                            openOutputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            } finally {
                Trace.endSection();
            }
        } catch (IOException e) {
            throw new ImageExportException("ContentResolver#openOutputStream threw an exception.", e);
        }
    }

    public static ContentValues createMetadata(ZonedDateTime zonedDateTime, Bitmap.CompressFormat compressFormat, String str) {
        String str2;
        ContentValues contentValues = new ContentValues();
        contentValues.put("relative_path", SCREENSHOTS_PATH);
        contentValues.put("_display_name", str);
        int i = AnonymousClass1.$SwitchMap$android$graphics$Bitmap$CompressFormat[compressFormat.ordinal()];
        if (i == 1) {
            str2 = "image/jpeg";
        } else if (i == 2) {
            str2 = "image/png";
        } else if (i == 3 || i == 4 || i == 5) {
            str2 = "image/webp";
        } else {
            throw new IllegalArgumentException("Unknown CompressFormat!");
        }
        contentValues.put("mime_type", str2);
        contentValues.put("date_added", Long.valueOf(zonedDateTime.toEpochSecond()));
        contentValues.put("date_modified", Long.valueOf(zonedDateTime.toEpochSecond()));
        contentValues.put("date_expires", Long.valueOf(zonedDateTime.plus((TemporalAmount) PENDING_ENTRY_TTL).toEpochSecond()));
        contentValues.put("is_pending", (Integer) 1);
        return contentValues;
    }

    /* renamed from: -$$Nest$smthrowIfInterrupted  reason: not valid java name */
    public static void m82$$Nest$smthrowIfInterrupted() {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    public ImageExporter(ContentResolver contentResolver) {
        this.mResolver = contentResolver;
    }

    public static void updateExifAttributes(ExifInterface exifInterface, UUID uuid, int i, int i2, ZonedDateTime zonedDateTime) {
        exifInterface.setAttribute("ImageUniqueID", uuid.toString());
        exifInterface.setAttribute("Software", "Android " + Build.DISPLAY);
        exifInterface.setAttribute("ImageWidth", Integer.toString(i));
        exifInterface.setAttribute("ImageLength", Integer.toString(i2));
        String format = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss").format(zonedDateTime);
        String format2 = DateTimeFormatter.ofPattern("SSS").format(zonedDateTime);
        String format3 = DateTimeFormatter.ofPattern("xxx").format(zonedDateTime);
        exifInterface.setAttribute("DateTimeOriginal", format);
        exifInterface.setAttribute("SubSecTimeOriginal", format2);
        exifInterface.setAttribute("OffsetTimeOriginal", format3);
    }
}
