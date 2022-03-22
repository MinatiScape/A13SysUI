package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import androidx.core.graphics.drawable.RoundedBitmapDrawable21;
import com.android.launcher3.icons.RoundDrawableWrapper;
import com.android.systemui.privacy.television.TvOngoingPrivacyChip$$ExternalSyntheticLambda0;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda3;
import com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda7;
import com.android.wm.shell.dagger.WMShellBaseModule$$ExternalSyntheticLambda1;
import com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public class BcSmartspaceCardDoorbell extends BcSmartspaceCardGenericImage {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final HashMap mUriToDrawable = new HashMap();
    public int mGifFrameDurationInMs = 200;

    /* loaded from: classes.dex */
    public static class DrawableWithUri extends RoundDrawableWrapper {
        public ContentResolver mContentResolver;
        public Drawable mDrawable;
        public int mHeightInPx;
        public Uri mUri;

        public DrawableWithUri(Uri uri, ContentResolver contentResolver, int i, float f) {
            super(new ColorDrawable(0), f);
            this.mUri = uri;
            this.mHeightInPx = i;
            this.mContentResolver = contentResolver;
        }
    }

    /* loaded from: classes.dex */
    public static class LoadUriTask extends AsyncTask<DrawableWithUri, Void, DrawableWithUri> {
        @Override // android.os.AsyncTask
        public final DrawableWithUri doInBackground(DrawableWithUri[] drawableWithUriArr) {
            DrawableWithUri[] drawableWithUriArr2 = drawableWithUriArr;
            Drawable drawable = null;
            if (drawableWithUriArr2.length <= 0) {
                return null;
            }
            DrawableWithUri drawableWithUri = drawableWithUriArr2[0];
            try {
                InputStream openInputStream = drawableWithUri.mContentResolver.openInputStream(drawableWithUri.mUri);
                final int i = drawableWithUri.mHeightInPx;
                int i2 = BcSmartspaceCardDoorbell.$r8$clinit;
                try {
                    drawable = ImageDecoder.decodeDrawable(ImageDecoder.createSource((Resources) null, openInputStream), new ImageDecoder.OnHeaderDecodedListener() { // from class: com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell$$ExternalSyntheticLambda0
                        @Override // android.graphics.ImageDecoder.OnHeaderDecodedListener
                        public final void onHeaderDecoded(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
                            float f;
                            int i3 = i;
                            int i4 = BcSmartspaceCardDoorbell.$r8$clinit;
                            imageDecoder.setAllocator(3);
                            Size size = imageInfo.getSize();
                            if (size.getHeight() != 0) {
                                f = size.getWidth() / size.getHeight();
                            } else {
                                f = 0.0f;
                            }
                            imageDecoder.setTargetSize((int) (i3 * f), i3);
                        }
                    });
                } catch (IOException e) {
                    Log.e("BcSmartspaceCardBell", "Unable to decode stream: " + e);
                }
                drawableWithUri.mDrawable = drawable;
            } catch (Exception e2) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("open uri:");
                m.append(drawableWithUri.mUri);
                m.append(" got exception:");
                m.append(e2);
                Log.w("BcSmartspaceCardBell", m.toString());
            }
            return drawableWithUri;
        }

        @Override // android.os.AsyncTask
        public final void onPostExecute(DrawableWithUri drawableWithUri) {
            Drawable drawable;
            DrawableWithUri drawableWithUri2 = drawableWithUri;
            if (drawableWithUri2 != null && (drawable = drawableWithUri2.mDrawable) != null) {
                drawableWithUri2.setDrawable(drawable);
            }
        }
    }

    public BcSmartspaceCardDoorbell(Context context) {
        super(context);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public final boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, CardPagerAdapter$$ExternalSyntheticLambda0 cardPagerAdapter$$ExternalSyntheticLambda0, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        Bundle bundle;
        if (!getContext().getPackageName().equals(ThemeOverlayApplier.SYSUI_PACKAGE)) {
            return false;
        }
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        if (baseAction == null) {
            bundle = null;
        } else {
            bundle = baseAction.getExtras();
        }
        List list = (List) smartspaceTarget.getIconGrid().stream().filter(ThemeOverlayApplier$$ExternalSyntheticLambda7.INSTANCE$2).map(ThemeOverlayApplier$$ExternalSyntheticLambda3.INSTANCE$1).map(WMShellBaseModule$$ExternalSyntheticLambda1.INSTANCE$1).collect(Collectors.toList());
        if (!list.isEmpty()) {
            if (bundle != null && bundle.containsKey("frameDurationMs")) {
                this.mGifFrameDurationInMs = bundle.getInt("frameDurationMs");
            }
            final ContentResolver contentResolver = getContext().getApplicationContext().getContentResolver();
            final int dimensionPixelOffset = getResources().getDimensionPixelOffset(2131165709);
            final float dimension = getResources().getDimension(2131165714);
            AnimationDrawable animationDrawable = new AnimationDrawable();
            for (Drawable drawable : (List) list.stream().map(new Function() { // from class: com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell$$ExternalSyntheticLambda2
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    BcSmartspaceCardDoorbell bcSmartspaceCardDoorbell = BcSmartspaceCardDoorbell.this;
                    final ContentResolver contentResolver2 = contentResolver;
                    final int i = dimensionPixelOffset;
                    final float f = dimension;
                    int i2 = BcSmartspaceCardDoorbell.$r8$clinit;
                    Objects.requireNonNull(bcSmartspaceCardDoorbell);
                    return (BcSmartspaceCardDoorbell.DrawableWithUri) bcSmartspaceCardDoorbell.mUriToDrawable.computeIfAbsent((Uri) obj, new Function() { // from class: com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell$$ExternalSyntheticLambda1
                        @Override // java.util.function.Function
                        public final Object apply(Object obj2) {
                            ContentResolver contentResolver3 = contentResolver2;
                            int i3 = i;
                            float f2 = f;
                            int i4 = BcSmartspaceCardDoorbell.$r8$clinit;
                            BcSmartspaceCardDoorbell.DrawableWithUri drawableWithUri = new BcSmartspaceCardDoorbell.DrawableWithUri((Uri) obj2, contentResolver3, i3, f2);
                            new BcSmartspaceCardDoorbell.LoadUriTask().execute(drawableWithUri);
                            return drawableWithUri;
                        }
                    });
                }
            }).filter(TvOngoingPrivacyChip$$ExternalSyntheticLambda0.INSTANCE$1).collect(Collectors.toList())) {
                animationDrawable.addFrame(drawable, this.mGifFrameDurationInMs);
            }
            this.mImageView.setImageDrawable(animationDrawable);
            animationDrawable.start();
            Log.d("BcSmartspaceCardBell", "imageUri is set");
            return true;
        } else if (bundle == null || !bundle.containsKey("imageBitmap")) {
            return false;
        } else {
            Bitmap bitmap = (Bitmap) bundle.get("imageBitmap");
            if (bitmap.getHeight() != 0) {
                int dimension2 = (int) getResources().getDimension(2131165709);
                bitmap = Bitmap.createScaledBitmap(bitmap, (int) (dimension2 * (bitmap.getWidth() / bitmap.getHeight())), dimension2, true);
            }
            RoundedBitmapDrawable21 roundedBitmapDrawable21 = new RoundedBitmapDrawable21(getResources(), bitmap);
            roundedBitmapDrawable21.setCornerRadius(getResources().getDimension(2131165714));
            this.mImageView.setImageDrawable(roundedBitmapDrawable21);
            Log.d("BcSmartspaceCardBell", "imageBitmap is set");
            return true;
        }
    }

    public BcSmartspaceCardDoorbell(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
    }
}
