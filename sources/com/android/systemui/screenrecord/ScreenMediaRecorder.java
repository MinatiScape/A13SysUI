package com.android.systemui.screenrecord;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.hardware.display.VirtualDisplay;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.media.projection.IMediaProjection;
import android.media.projection.IMediaProjectionManager;
import android.media.projection.MediaProjection;
import android.net.Uri;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.WindowManager;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ScreenMediaRecorder {
    public ScreenInternalAudioRecorder mAudio;
    public ScreenRecordingAudioSource mAudioSource;
    public Context mContext;
    public Surface mInputSurface;
    public MediaRecorder.OnInfoListener mListener;
    public MediaProjection mMediaProjection;
    public MediaRecorder mMediaRecorder;
    public File mTempAudioFile;
    public File mTempVideoFile;
    public int mUser;
    public VirtualDisplay mVirtualDisplay;

    /* loaded from: classes.dex */
    public class SavedRecording {
        public Bitmap mThumbnailBitmap;
        public Uri mUri;

        public SavedRecording(Uri uri, File file, Size size) {
            this.mUri = uri;
            try {
                this.mThumbnailBitmap = ThumbnailUtils.createVideoThumbnail(file, size, null);
            } catch (IOException e) {
                Log.e("ScreenMediaRecorder", "Error creating thumbnail", e);
            }
        }
    }

    public final SavedRecording save() throws IOException {
        String format = new SimpleDateFormat("'screen-'yyyyMMdd-HHmmss'.mp4'").format(new Date());
        ContentValues contentValues = new ContentValues();
        contentValues.put("_display_name", format);
        contentValues.put("mime_type", "video/mp4");
        contentValues.put("date_added", Long.valueOf(System.currentTimeMillis()));
        contentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        ContentResolver contentResolver = this.mContext.getContentResolver();
        Uri insert = contentResolver.insert(MediaStore.Video.Media.getContentUri("external_primary"), contentValues);
        Log.d("ScreenMediaRecorder", insert.toString());
        ScreenRecordingAudioSource screenRecordingAudioSource = this.mAudioSource;
        if (screenRecordingAudioSource == ScreenRecordingAudioSource.MIC_AND_INTERNAL || screenRecordingAudioSource == ScreenRecordingAudioSource.INTERNAL) {
            try {
                Log.d("ScreenMediaRecorder", "muxing recording");
                File createTempFile = File.createTempFile("temp", ".mp4", this.mContext.getCacheDir());
                new ScreenRecordingMuxer(createTempFile.getAbsolutePath(), this.mTempVideoFile.getAbsolutePath(), this.mTempAudioFile.getAbsolutePath()).mux();
                this.mTempVideoFile.delete();
                this.mTempVideoFile = createTempFile;
            } catch (IOException e) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("muxing recording ");
                m.append(e.getMessage());
                Log.e("ScreenMediaRecorder", m.toString());
                e.printStackTrace();
            }
        }
        OutputStream openOutputStream = contentResolver.openOutputStream(insert, "w");
        Files.copy(this.mTempVideoFile.toPath(), openOutputStream);
        openOutputStream.close();
        File file = this.mTempAudioFile;
        if (file != null) {
            file.delete();
        }
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        SavedRecording savedRecording = new SavedRecording(insert, this.mTempVideoFile, new Size(displayMetrics.widthPixels, displayMetrics.heightPixels));
        this.mTempVideoFile.delete();
        return savedRecording;
    }

    public final void start() throws IOException, RemoteException, RuntimeException {
        int i;
        int i2;
        DisplayMetrics displayMetrics;
        int i3;
        ScreenRecordingAudioSource screenRecordingAudioSource;
        boolean z;
        int[] iArr;
        Log.d("ScreenMediaRecorder", "start recording");
        ScreenRecordingAudioSource screenRecordingAudioSource2 = ScreenRecordingAudioSource.MIC_AND_INTERNAL;
        this.mMediaProjection = new MediaProjection(this.mContext, IMediaProjection.Stub.asInterface(IMediaProjectionManager.Stub.asInterface(ServiceManager.getService("media_projection")).createProjection(this.mUser, this.mContext.getPackageName(), 0, false).asBinder()));
        File cacheDir = this.mContext.getCacheDir();
        cacheDir.mkdirs();
        this.mTempVideoFile = File.createTempFile("temp", ".mp4", cacheDir);
        MediaRecorder mediaRecorder = new MediaRecorder();
        this.mMediaRecorder = mediaRecorder;
        ScreenRecordingAudioSource screenRecordingAudioSource3 = this.mAudioSource;
        ScreenRecordingAudioSource screenRecordingAudioSource4 = ScreenRecordingAudioSource.MIC;
        if (screenRecordingAudioSource3 == screenRecordingAudioSource4) {
            mediaRecorder.setAudioSource(0);
        }
        this.mMediaRecorder.setVideoSource(2);
        this.mMediaRecorder.setOutputFormat(2);
        DisplayMetrics displayMetrics2 = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService("window");
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics2);
        int refreshRate = (int) windowManager.getDefaultDisplay().getRefreshRate();
        int i4 = displayMetrics2.widthPixels;
        int i5 = displayMetrics2.heightPixels;
        MediaCodec createDecoderByType = MediaCodec.createDecoderByType("video/avc");
        MediaCodecInfo.VideoCapabilities videoCapabilities = createDecoderByType.getCodecInfo().getCapabilitiesForType("video/avc").getVideoCapabilities();
        createDecoderByType.release();
        int intValue = videoCapabilities.getSupportedWidths().getUpper().intValue();
        int intValue2 = videoCapabilities.getSupportedHeights().getUpper().intValue();
        if (i4 % videoCapabilities.getWidthAlignment() != 0) {
            i = i4 - (i4 % videoCapabilities.getWidthAlignment());
        } else {
            i = i4;
        }
        if (i5 % videoCapabilities.getHeightAlignment() != 0) {
            i2 = i5 - (i5 % videoCapabilities.getHeightAlignment());
        } else {
            i2 = i5;
        }
        char c = 1;
        if (intValue < i || intValue2 < i2 || !videoCapabilities.isSizeSupported(i, i2)) {
            double d = i4;
            screenRecordingAudioSource = screenRecordingAudioSource2;
            displayMetrics = displayMetrics2;
            double d2 = i5;
            double min = Math.min(intValue / d, intValue2 / d2);
            int i6 = (int) (d * min);
            int i7 = (int) (d2 * min);
            if (i6 % videoCapabilities.getWidthAlignment() != 0) {
                i6 -= i6 % videoCapabilities.getWidthAlignment();
            }
            if (i7 % videoCapabilities.getHeightAlignment() != 0) {
                i7 -= i7 % videoCapabilities.getHeightAlignment();
            }
            int intValue3 = videoCapabilities.getSupportedFrameRatesFor(i6, i7).getUpper().intValue();
            if (intValue3 < refreshRate) {
                refreshRate = intValue3;
            }
            Log.d("ScreenMediaRecorder", "Resized by " + min + ": " + i6 + ", " + i7 + ", " + refreshRate);
            c = 1;
            i3 = 2;
            iArr = new int[]{i6, i7, refreshRate};
            z = false;
        } else {
            int intValue4 = videoCapabilities.getSupportedFrameRatesFor(i, i2).getUpper().intValue();
            if (intValue4 < refreshRate) {
                refreshRate = intValue4;
            }
            ExifInterface$$ExternalSyntheticOutline1.m("Screen size supported at rate ", refreshRate, "ScreenMediaRecorder");
            i3 = 2;
            int[] iArr2 = {i, i2, refreshRate};
            z = false;
            displayMetrics = displayMetrics2;
            iArr = iArr2;
            screenRecordingAudioSource = screenRecordingAudioSource2;
        }
        char c2 = z ? 1 : 0;
        char c3 = z ? 1 : 0;
        char c4 = z ? 1 : 0;
        char c5 = z ? 1 : 0;
        int i8 = iArr[c2];
        int i9 = iArr[c];
        int i10 = iArr[i3];
        this.mMediaRecorder.setVideoEncoder(i3);
        this.mMediaRecorder.setVideoEncodingProfileLevel(8, 256);
        this.mMediaRecorder.setVideoSize(i8, i9);
        this.mMediaRecorder.setVideoFrameRate(i10);
        this.mMediaRecorder.setVideoEncodingBitRate((((i8 * i9) * i10) / 30) * 6);
        this.mMediaRecorder.setMaxDuration(3600000);
        this.mMediaRecorder.setMaxFileSize(5000000000L);
        if (this.mAudioSource == screenRecordingAudioSource4) {
            this.mMediaRecorder.setAudioEncoder(4);
            this.mMediaRecorder.setAudioChannels(1);
            this.mMediaRecorder.setAudioEncodingBitRate(196000);
            this.mMediaRecorder.setAudioSamplingRate(44100);
        }
        this.mMediaRecorder.setOutputFile(this.mTempVideoFile);
        this.mMediaRecorder.prepare();
        Surface surface = this.mMediaRecorder.getSurface();
        this.mInputSurface = surface;
        this.mVirtualDisplay = this.mMediaProjection.createVirtualDisplay("Recording Display", i8, i9, displayMetrics.densityDpi, 16, surface, null, null);
        this.mMediaRecorder.setOnInfoListener(this.mListener);
        ScreenRecordingAudioSource screenRecordingAudioSource5 = this.mAudioSource;
        ScreenRecordingAudioSource screenRecordingAudioSource6 = ScreenRecordingAudioSource.INTERNAL;
        if (screenRecordingAudioSource5 == screenRecordingAudioSource6 || screenRecordingAudioSource5 == screenRecordingAudioSource) {
            File createTempFile = File.createTempFile("temp", ".aac", this.mContext.getCacheDir());
            this.mTempAudioFile = createTempFile;
            String absolutePath = createTempFile.getAbsolutePath();
            MediaProjection mediaProjection = this.mMediaProjection;
            if (this.mAudioSource == screenRecordingAudioSource) {
                z = true;
            }
            this.mAudio = new ScreenInternalAudioRecorder(absolutePath, mediaProjection, z);
        }
        this.mMediaRecorder.start();
        ScreenRecordingAudioSource screenRecordingAudioSource7 = this.mAudioSource;
        if (screenRecordingAudioSource7 == screenRecordingAudioSource6 || screenRecordingAudioSource7 == screenRecordingAudioSource) {
            ScreenInternalAudioRecorder screenInternalAudioRecorder = this.mAudio;
            Objects.requireNonNull(screenInternalAudioRecorder);
            synchronized (screenInternalAudioRecorder) {
                if (!screenInternalAudioRecorder.mStarted) {
                    screenInternalAudioRecorder.mStarted = true;
                    screenInternalAudioRecorder.mAudioRecord.startRecording();
                    if (screenInternalAudioRecorder.mMic) {
                        screenInternalAudioRecorder.mAudioRecordMic.startRecording();
                    }
                    Log.d("ScreenAudioRecorder", "channel count " + screenInternalAudioRecorder.mAudioRecord.getChannelCount());
                    screenInternalAudioRecorder.mCodec.start();
                    if (screenInternalAudioRecorder.mAudioRecord.getRecordingState() == 3) {
                        screenInternalAudioRecorder.mThread.start();
                    } else {
                        throw new IllegalStateException("Audio recording failed to start");
                    }
                } else if (screenInternalAudioRecorder.mThread == null) {
                    throw new IllegalStateException("Recording stopped and can't restart (single use)");
                } else {
                    throw new IllegalStateException("Recording already started");
                }
            }
        }
    }

    public ScreenMediaRecorder(Context context, int i, ScreenRecordingAudioSource screenRecordingAudioSource, MediaRecorder.OnInfoListener onInfoListener) {
        this.mContext = context;
        this.mUser = i;
        this.mListener = onInfoListener;
        this.mAudioSource = screenRecordingAudioSource;
    }
}
