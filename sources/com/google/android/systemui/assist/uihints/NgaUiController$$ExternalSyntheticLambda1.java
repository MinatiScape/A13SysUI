package com.google.android.systemui.assist.uihints;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ImageView;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import androidx.lifecycle.ViewModel;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NgaUiController$$ExternalSyntheticLambda1 implements DaggerViewModelProviderFactory.ViewModelCreator {
    public final /* synthetic */ Object f$0;

    @Override // com.android.systemui.dreams.complication.dagger.DaggerViewModelProviderFactory.ViewModelCreator
    public final ViewModel create() {
        return (ComplicationViewModel) this.f$0;
    }

    public /* synthetic */ NgaUiController$$ExternalSyntheticLambda1(Object obj) {
        this.f$0 = obj;
    }

    public final void onLightnessUpdate(float f) {
        PorterDuff.Mode mode;
        NgaUiController ngaUiController = (NgaUiController) this.f$0;
        Objects.requireNonNull(ngaUiController);
        boolean z = NgaUiController.VERBOSE;
        if (ngaUiController.mColorMonitoringStart > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime() - ngaUiController.mColorMonitoringStart;
            if (z) {
                StringBuilder sb = new StringBuilder();
                sb.append("Got lightness update (");
                sb.append(f);
                sb.append(") after ");
                sb.append(elapsedRealtime);
                ExifInterface$$ExternalSyntheticOutline2.m(sb, " ms", "NgaUiController");
            }
            ngaUiController.mColorMonitoringStart = 0L;
        }
        IconController iconController = ngaUiController.mIconController;
        boolean z2 = true;
        Objects.requireNonNull(iconController);
        iconController.mHasAccurateLuma = true;
        iconController.maybeUpdateIconVisibility(iconController.mKeyboardIcon, iconController.mKeyboardIconRequested);
        iconController.maybeUpdateIconVisibility(iconController.mZeroStateIcon, iconController.mZerostateIconRequested);
        GlowController glowController = ngaUiController.mGlowController;
        Objects.requireNonNull(glowController);
        GlowView glowView = glowController.mGlowView;
        int i = (f > 0.4f ? 1 : (f == 0.4f ? 0 : -1));
        if (i <= 0) {
            mode = PorterDuff.Mode.LIGHTEN;
        } else {
            mode = PorterDuff.Mode.SRC_OVER;
        }
        Objects.requireNonNull(glowView);
        glowView.mPaint.setXfermode(new PorterDuffXfermode(mode));
        Iterator<ImageView> it = glowView.mGlowImageViews.iterator();
        while (it.hasNext()) {
            it.next().setLayerPaint(glowView.mPaint);
        }
        glowController.mMedianLightness = f;
        ScrimController scrimController = ngaUiController.mScrimController;
        Objects.requireNonNull(scrimController);
        scrimController.mHaveAccurateLightness = true;
        scrimController.mMedianLightness = f;
        scrimController.refresh();
        TranscriptionController transcriptionController = ngaUiController.mTranscriptionController;
        Objects.requireNonNull(transcriptionController);
        if (!transcriptionController.mHasAccurateBackground) {
            transcriptionController.mHasAccurateBackground = true;
            transcriptionController.maybeSetState();
        }
        if (i > 0) {
            z2 = false;
        }
        String str = "dark";
        if (ngaUiController.mHasDarkBackground != z2) {
            ngaUiController.mHasDarkBackground = z2;
            ColorChangeHandler colorChangeHandler = ngaUiController.mColorChangeHandler;
            Objects.requireNonNull(colorChangeHandler);
            colorChangeHandler.mIsDark = z2;
            colorChangeHandler.sendColor();
            if (NgaUiController.VERBOSE) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("switching to ");
                if (!ngaUiController.mHasDarkBackground) {
                    str = "light";
                }
                m.append(str);
                Log.v("NgaUiController", m.toString());
            }
            ngaUiController.dispatchHasDarkBackground();
        } else if (NgaUiController.VERBOSE) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("not switching; already ");
            if (!z2) {
                str = "light";
            }
            m2.append(str);
            Log.v("NgaUiController", m2.toString());
        }
        ngaUiController.refresh();
    }
}
