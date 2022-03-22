package com.google.android.systemui.keyguard;

import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.builders.ListBuilder;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.util.Assert;
import com.google.android.systemui.smartspace.SmartSpaceCard;
import com.google.android.systemui.smartspace.SmartSpaceController;
import com.google.android.systemui.smartspace.SmartSpaceData;
import com.google.android.systemui.smartspace.SmartSpaceUpdateListener;
import java.lang.ref.WeakReference;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardSliceProviderGoogle extends KeyguardSliceProvider implements SmartSpaceUpdateListener {
    public static final boolean DEBUG = Log.isLoggable("KeyguardSliceProvider", 3);
    public boolean mHideSensitiveContent;
    public SmartSpaceController mSmartSpaceController;
    public SmartSpaceData mSmartSpaceData;
    public boolean mHideWorkContent = true;
    public final Uri mWeatherUri = Uri.parse("content://com.android.systemui.keyguard/smartSpace/weather");
    public final Uri mCalendarUri = Uri.parse("content://com.android.systemui.keyguard/smartSpace/calendar");

    /* loaded from: classes.dex */
    public static class AddShadowTask extends AsyncTask<Bitmap, Void, Bitmap> {
        public final float mBlurRadius;
        public final WeakReference<KeyguardSliceProviderGoogle> mProviderReference;
        public final SmartSpaceCard mWeatherCard;

        @Override // android.os.AsyncTask
        public final Bitmap doInBackground(Bitmap[] bitmapArr) {
            Bitmap bitmap = bitmapArr[0];
            BlurMaskFilter blurMaskFilter = new BlurMaskFilter(this.mBlurRadius, BlurMaskFilter.Blur.NORMAL);
            Paint paint = new Paint();
            paint.setMaskFilter(blurMaskFilter);
            int[] iArr = new int[2];
            Bitmap extractAlpha = bitmap.extractAlpha(paint, iArr);
            Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Paint paint2 = new Paint();
            paint2.setAlpha(70);
            canvas.drawBitmap(extractAlpha, iArr[0], (this.mBlurRadius / 2.0f) + iArr[1], paint2);
            extractAlpha.recycle();
            paint2.setAlpha(255);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint2);
            return createBitmap;
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(Bitmap bitmap) {
            KeyguardSliceProviderGoogle keyguardSliceProviderGoogle;
            Bitmap bitmap2 = bitmap;
            synchronized (this) {
                SmartSpaceCard smartSpaceCard = this.mWeatherCard;
                Objects.requireNonNull(smartSpaceCard);
                smartSpaceCard.mIcon = bitmap2;
                keyguardSliceProviderGoogle = this.mProviderReference.get();
            }
            if (keyguardSliceProviderGoogle != null) {
                boolean z = KeyguardSliceProviderGoogle.DEBUG;
                keyguardSliceProviderGoogle.notifyChange();
            }
        }

        public AddShadowTask(KeyguardSliceProviderGoogle keyguardSliceProviderGoogle, SmartSpaceCard smartSpaceCard) {
            this.mProviderReference = new WeakReference<>(keyguardSliceProviderGoogle);
            this.mWeatherCard = smartSpaceCard;
            this.mBlurRadius = keyguardSliceProviderGoogle.getContext().getResources().getDimension(2131167033);
        }
    }

    @Override // com.google.android.systemui.smartspace.SmartSpaceUpdateListener
    public final void onSensitiveModeChanged(boolean z, boolean z2) {
        boolean z3;
        boolean z4;
        synchronized (this) {
            z3 = true;
            if (this.mHideSensitiveContent != z) {
                this.mHideSensitiveContent = z;
                if (DEBUG) {
                    Log.d("KeyguardSliceProvider", "Public mode changed, hide data: " + z);
                }
                z4 = true;
            } else {
                z4 = false;
            }
            if (this.mHideWorkContent != z2) {
                this.mHideWorkContent = z2;
                if (DEBUG) {
                    Log.d("KeyguardSliceProvider", "Public work mode changed, hide data: " + z2);
                }
            } else {
                z3 = z4;
            }
        }
        if (z3) {
            notifyChange();
        }
    }

    @Override // com.google.android.systemui.smartspace.SmartSpaceUpdateListener
    public final void onSmartSpaceUpdated(SmartSpaceData smartSpaceData) {
        synchronized (this) {
            this.mSmartSpaceData = smartSpaceData;
        }
        Objects.requireNonNull(smartSpaceData);
        SmartSpaceCard smartSpaceCard = smartSpaceData.mWeatherCard;
        if (smartSpaceCard == null || smartSpaceCard.mIcon == null || smartSpaceCard.mIconProcessed) {
            notifyChange();
            return;
        }
        smartSpaceCard.mIconProcessed = true;
        new AddShadowTask(this, smartSpaceCard).execute(smartSpaceCard.mIcon);
    }

    public final void addWeather(ListBuilder listBuilder) {
        SmartSpaceData smartSpaceData = this.mSmartSpaceData;
        Objects.requireNonNull(smartSpaceData);
        SmartSpaceCard smartSpaceCard = smartSpaceData.mWeatherCard;
        if (smartSpaceCard != null && !smartSpaceCard.isExpired()) {
            ListBuilder.RowBuilder rowBuilder = new ListBuilder.RowBuilder(this.mWeatherUri);
            rowBuilder.mTitle = smartSpaceCard.substitute(true);
            rowBuilder.mTitleLoading = false;
            Bitmap bitmap = smartSpaceCard.mIcon;
            if (bitmap != null) {
                IconCompat createWithBitmap = IconCompat.createWithBitmap(bitmap);
                createWithBitmap.mTintMode = PorterDuff.Mode.DST;
                rowBuilder.addEndItem(createWithBitmap, 1);
            }
            listBuilder.addRow(rowBuilder);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x0055 A[Catch: all -> 0x0077, TryCatch #0 {all -> 0x0077, blocks: (B:4:0x0011, B:6:0x001c, B:8:0x0022, B:10:0x002c, B:12:0x0032, B:14:0x0036, B:19:0x003f, B:21:0x0043, B:31:0x0055, B:34:0x005f, B:35:0x0063, B:39:0x006c, B:42:0x007a, B:44:0x008b, B:45:0x008d, B:47:0x0098, B:49:0x00a5, B:51:0x00aa, B:52:0x00ac, B:53:0x00af, B:54:0x00c0, B:56:0x00c2, B:58:0x00c8, B:59:0x00cc, B:60:0x00de, B:61:0x00ea), top: B:69:0x0011 }] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00c2 A[Catch: all -> 0x0077, TryCatch #0 {all -> 0x0077, blocks: (B:4:0x0011, B:6:0x001c, B:8:0x0022, B:10:0x002c, B:12:0x0032, B:14:0x0036, B:19:0x003f, B:21:0x0043, B:31:0x0055, B:34:0x005f, B:35:0x0063, B:39:0x006c, B:42:0x007a, B:44:0x008b, B:45:0x008d, B:47:0x0098, B:49:0x00a5, B:51:0x00aa, B:52:0x00ac, B:53:0x00af, B:54:0x00c0, B:56:0x00c2, B:58:0x00c8, B:59:0x00cc, B:60:0x00de, B:61:0x00ea), top: B:69:0x0011 }] */
    @Override // com.android.systemui.keyguard.KeyguardSliceProvider, androidx.slice.SliceProvider
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final androidx.slice.Slice onBindSlice() {
        /*
            Method dump skipped, instructions count: 275
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.keyguard.KeyguardSliceProviderGoogle.onBindSlice():androidx.slice.Slice");
    }

    @Override // com.android.systemui.keyguard.KeyguardSliceProvider, androidx.slice.SliceProvider
    public final void onCreateSliceProvider() {
        super.onCreateSliceProvider();
        this.mSmartSpaceData = new SmartSpaceData();
        SmartSpaceController smartSpaceController = this.mSmartSpaceController;
        Objects.requireNonNull(smartSpaceController);
        Assert.isMainThread();
        smartSpaceController.mListeners.add(this);
        SmartSpaceData smartSpaceData = smartSpaceController.mData;
        if (smartSpaceData != null) {
            onSmartSpaceUpdated(smartSpaceData);
        }
        onSensitiveModeChanged(smartSpaceController.mHidePrivateData, smartSpaceController.mHideWorkData);
    }

    @Override // com.android.systemui.keyguard.KeyguardSliceProvider
    public final void onDestroy() {
        super.onDestroy();
        SmartSpaceController smartSpaceController = this.mSmartSpaceController;
        Objects.requireNonNull(smartSpaceController);
        Assert.isMainThread();
        smartSpaceController.mListeners.remove(this);
    }

    @Override // com.android.systemui.keyguard.KeyguardSliceProvider
    public final void updateClockLocked() {
        notifyChange();
    }
}
