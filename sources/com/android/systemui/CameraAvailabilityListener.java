package com.android.systemui;

import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.camera2.CameraManager;
import androidx.leanback.R$drawable;
import com.android.systemui.CameraAvailabilityListener;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: CameraAvailabilityListener.kt */
/* loaded from: classes.dex */
public final class CameraAvailabilityListener {
    public final CameraManager cameraManager;
    public final Path cutoutProtectionPath;
    public final Set<String> excludedPackageIds;
    public final Executor executor;
    public final String targetCameraId;
    public Rect cutoutBounds = new Rect();
    public final ArrayList listeners = new ArrayList();
    public final CameraAvailabilityListener$availabilityCallback$1 availabilityCallback = new CameraManager.AvailabilityCallback() { // from class: com.android.systemui.CameraAvailabilityListener$availabilityCallback$1
        public final void onCameraClosed(String str) {
            if (Intrinsics.areEqual(CameraAvailabilityListener.this.targetCameraId, str)) {
                CameraAvailabilityListener cameraAvailabilityListener = CameraAvailabilityListener.this;
                Objects.requireNonNull(cameraAvailabilityListener);
                Iterator it = cameraAvailabilityListener.listeners.iterator();
                while (it.hasNext()) {
                    ((CameraAvailabilityListener.CameraTransitionCallback) it.next()).onHideCameraProtection();
                }
            }
        }

        public final void onCameraOpened(String str, String str2) {
            if (Intrinsics.areEqual(CameraAvailabilityListener.this.targetCameraId, str)) {
                CameraAvailabilityListener cameraAvailabilityListener = CameraAvailabilityListener.this;
                Objects.requireNonNull(cameraAvailabilityListener);
                if (!cameraAvailabilityListener.excludedPackageIds.contains(str2)) {
                    CameraAvailabilityListener cameraAvailabilityListener2 = CameraAvailabilityListener.this;
                    Objects.requireNonNull(cameraAvailabilityListener2);
                    Iterator it = cameraAvailabilityListener2.listeners.iterator();
                    while (it.hasNext()) {
                        ((CameraAvailabilityListener.CameraTransitionCallback) it.next()).onApplyCameraProtection(cameraAvailabilityListener2.cutoutProtectionPath, cameraAvailabilityListener2.cutoutBounds);
                    }
                }
            }
        }
    };

    /* compiled from: CameraAvailabilityListener.kt */
    /* loaded from: classes.dex */
    public interface CameraTransitionCallback {
        void onApplyCameraProtection(Path path, Rect rect);

        void onHideCameraProtection();
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.CameraAvailabilityListener$availabilityCallback$1] */
    public CameraAvailabilityListener(CameraManager cameraManager, Path path, String str, String str2, DelayableExecutor delayableExecutor) {
        this.cameraManager = cameraManager;
        this.cutoutProtectionPath = path;
        this.targetCameraId = str;
        this.executor = delayableExecutor;
        RectF rectF = new RectF();
        path.computeBounds(rectF, false);
        this.cutoutBounds.set(R$drawable.roundToInt(rectF.left), R$drawable.roundToInt(rectF.top), R$drawable.roundToInt(rectF.right), R$drawable.roundToInt(rectF.bottom));
        this.excludedPackageIds = CollectionsKt___CollectionsKt.toSet(StringsKt__StringsKt.split$default(str2, new String[]{","}));
    }
}
