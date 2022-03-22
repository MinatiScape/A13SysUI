package com.android.keyguard.clock;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ClockOptionsProvider extends ContentProvider {
    public Provider<List<ClockInfo>> mClockInfosProvider;

    /* loaded from: classes.dex */
    public static class MyWriter implements ContentProvider.PipeDataWriter<Bitmap> {
        @Override // android.content.ContentProvider.PipeDataWriter
        public final void writeDataToPipe(ParcelFileDescriptor parcelFileDescriptor, Uri uri, String str, Bundle bundle, Bitmap bitmap) {
            Bitmap bitmap2 = bitmap;
            try {
                ParcelFileDescriptor.AutoCloseOutputStream autoCloseOutputStream = new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor);
                bitmap2.compress(Bitmap.CompressFormat.PNG, 100, autoCloseOutputStream);
                autoCloseOutputStream.close();
            } catch (Exception e) {
                Log.w("ClockOptionsProvider", "fail to write to pipe", e);
            }
        }
    }

    @Override // android.content.ContentProvider
    public final int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public final Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public final int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    @VisibleForTesting
    public ClockOptionsProvider(Provider<List<ClockInfo>> provider) {
        this.mClockInfosProvider = provider;
    }

    @Override // android.content.ContentProvider
    public final String getType(Uri uri) {
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() <= 0) {
            return "vnd.android.cursor.dir/clock_faces";
        }
        if ("preview".equals(pathSegments.get(0)) || "thumbnail".equals(pathSegments.get(0))) {
            return "image/png";
        }
        return "vnd.android.cursor.dir/clock_faces";
    }

    @Override // android.content.ContentProvider
    public final ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        ClockInfo clockInfo;
        Bitmap bitmap;
        List<String> pathSegments = uri.getPathSegments();
        if (pathSegments.size() != 2 || (!"preview".equals(pathSegments.get(0)) && !"thumbnail".equals(pathSegments.get(0)))) {
            throw new FileNotFoundException("Invalid preview url");
        }
        String str2 = pathSegments.get(1);
        if (!TextUtils.isEmpty(str2)) {
            List<ClockInfo> list = this.mClockInfosProvider.mo144get();
            int i = 0;
            while (true) {
                clockInfo = null;
                if (i >= list.size()) {
                    break;
                }
                ClockInfo clockInfo2 = list.get(i);
                Objects.requireNonNull(clockInfo2);
                if (str2.equals(clockInfo2.mId)) {
                    clockInfo = list.get(i);
                    break;
                }
                i++;
            }
            if (clockInfo != null) {
                if ("preview".equals(pathSegments.get(0))) {
                    bitmap = clockInfo.mPreview.get();
                } else {
                    bitmap = clockInfo.mThumbnail.get();
                }
                return openPipeHelper(uri, "image/png", null, bitmap, new MyWriter());
            }
            throw new FileNotFoundException("Invalid preview url, id not found");
        }
        throw new FileNotFoundException("Invalid preview url, missing id");
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        if (!"/list_options".equals(uri.getPath())) {
            return null;
        }
        MatrixCursor matrixCursor = new MatrixCursor(new String[]{"name", "title", "id", "thumbnail", "preview"});
        List<ClockInfo> list = this.mClockInfosProvider.mo144get();
        for (int i = 0; i < list.size(); i++) {
            ClockInfo clockInfo = list.get(i);
            MatrixCursor.RowBuilder newRow = matrixCursor.newRow();
            Objects.requireNonNull(clockInfo);
            newRow.add("name", clockInfo.mName).add("title", clockInfo.mTitle.get()).add("id", clockInfo.mId).add("thumbnail", new Uri.Builder().scheme("content").authority("com.android.keyguard.clock").appendPath("thumbnail").appendPath(clockInfo.mId).build()).add("preview", new Uri.Builder().scheme("content").authority("com.android.keyguard.clock").appendPath("preview").appendPath(clockInfo.mId).build());
        }
        return matrixCursor;
    }
}
