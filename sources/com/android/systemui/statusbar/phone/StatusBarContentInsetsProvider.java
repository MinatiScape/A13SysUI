package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.LruCache;
import android.util.Pair;
import android.view.DisplayCutout;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import androidx.preference.R$id;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatusBarContentInsetsProvider.kt */
/* loaded from: classes.dex */
public final class StatusBarContentInsetsProvider implements CallbackController<StatusBarContentInsetsChangedListener>, ConfigurationController.ConfigurationListener, Dumpable {
    public final ConfigurationController configurationController;
    public final Context context;
    public final LruCache<CacheKey, Rect> insetsCache = new LruCache<>(16);
    public final LinkedHashSet listeners = new LinkedHashSet();
    public final Lazy isPrivacyDotEnabled$delegate = LazyKt__LazyJVMKt.lazy$1(new StatusBarContentInsetsProvider$isPrivacyDotEnabled$2(this));

    /* compiled from: StatusBarContentInsetsProvider.kt */
    /* loaded from: classes.dex */
    public static final class CacheKey {
        public final int rotation;
        public final String uniqueDisplayId;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CacheKey)) {
                return false;
            }
            CacheKey cacheKey = (CacheKey) obj;
            return Intrinsics.areEqual(this.uniqueDisplayId, cacheKey.uniqueDisplayId) && this.rotation == cacheKey.rotation;
        }

        public final int hashCode() {
            return Integer.hashCode(this.rotation) + (this.uniqueDisplayId.hashCode() * 31);
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CacheKey(uniqueDisplayId=");
            m.append(this.uniqueDisplayId);
            m.append(", rotation=");
            return Insets$$ExternalSyntheticOutline0.m(m, this.rotation, ')');
        }

        public CacheKey(String str, int i) {
            this.uniqueDisplayId = str;
            this.rotation = i;
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener) {
        this.listeners.add(statusBarContentInsetsChangedListener);
    }

    public final boolean currentRotationHasCornerCutout() {
        DisplayCutout cutout = this.context.getDisplay().getCutout();
        if (cutout == null) {
            return false;
        }
        Rect boundingRectTop = cutout.getBoundingRectTop();
        Point point = new Point();
        this.context.getDisplay().getRealSize(point);
        if (boundingRectTop.left <= 0 || boundingRectTop.right >= point.y) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        for (Map.Entry<CacheKey, Rect> entry : this.insetsCache.snapshot().entrySet()) {
            printWriter.println(entry.getKey() + " -> " + entry.getValue());
        }
        printWriter.println(this.insetsCache);
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x015f, code lost:
        r15 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:91:0x0197, code lost:
        if (r3.right >= r14) goto L_0x0199;
     */
    /* JADX WARN: Code restructure failed: missing block: B:92:0x0199, code lost:
        r7 = r15;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final android.graphics.Rect getAndSetCalculatedAreaForRotation(int r20, android.content.res.Resources r21, com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider.CacheKey r22) {
        /*
            Method dump skipped, instructions count: 464
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider.getAndSetCalculatedAreaForRotation(int, android.content.res.Resources, com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider$CacheKey):android.graphics.Rect");
    }

    public final Rect getBoundingRectForPrivacyChipForRotation(int i) {
        Rect rect = this.insetsCache.get(new CacheKey(this.context.getDisplay().getUniqueId(), i));
        if (rect == null) {
            rect = getStatusBarContentAreaForRotation(i);
        }
        Resources resourcesForRotation = R$id.getResourcesForRotation(i, this.context);
        return StatusBarContentInsetsProviderKt.getPrivacyChipBoundingRectForInsets(rect, resourcesForRotation.getDimensionPixelSize(2131166707), resourcesForRotation.getDimensionPixelSize(2131166700), this.configurationController.isLayoutRtl());
    }

    public final Rect getStatusBarContentAreaForRotation(int i) {
        CacheKey cacheKey = new CacheKey(this.context.getDisplay().getUniqueId(), i);
        Rect rect = this.insetsCache.get(cacheKey);
        if (rect == null) {
            return getAndSetCalculatedAreaForRotation(i, R$id.getResourcesForRotation(i, this.context), cacheKey);
        }
        return rect;
    }

    public final Pair<Integer, Integer> getStatusBarContentInsetsForCurrentRotation() {
        int i;
        int exactRotation = R$id.getExactRotation(this.context);
        CacheKey cacheKey = new CacheKey(this.context.getDisplay().getUniqueId(), exactRotation);
        Point point = new Point();
        this.context.getDisplay().getRealSize(point);
        int exactRotation2 = R$id.getExactRotation(this.context);
        if (!(exactRotation2 == 0 || exactRotation2 == 2)) {
            int i2 = point.y;
            point.y = point.x;
            point.x = i2;
        }
        if (exactRotation == 0 || exactRotation == 2) {
            i = point.x;
        } else {
            i = point.y;
        }
        Rect rect = this.insetsCache.get(cacheKey);
        if (rect == null) {
            rect = getAndSetCalculatedAreaForRotation(exactRotation, R$id.getResourcesForRotation(exactRotation, this.context), cacheKey);
        }
        return new Pair<>(Integer.valueOf(rect.left), Integer.valueOf(i - rect.right));
    }

    public final int getStatusBarPaddingTop(Integer num) {
        Resources resources;
        if (num == null) {
            resources = null;
        } else {
            resources = R$id.getResourcesForRotation(num.intValue(), this.context);
        }
        if (resources == null) {
            resources = this.context.getResources();
        }
        return resources.getDimensionPixelSize(2131167072);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onDensityOrFontScaleChanged() {
        this.insetsCache.evictAll();
        for (StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener : this.listeners) {
            statusBarContentInsetsChangedListener.onStatusBarContentInsetsChanged();
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onMaxBoundsChanged() {
        for (StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener : this.listeners) {
            statusBarContentInsetsChangedListener.onStatusBarContentInsetsChanged();
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onThemeChanged() {
        this.insetsCache.evictAll();
        for (StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener : this.listeners) {
            statusBarContentInsetsChangedListener.onStatusBarContentInsetsChanged();
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener) {
        this.listeners.remove(statusBarContentInsetsChangedListener);
    }

    public StatusBarContentInsetsProvider(Context context, ConfigurationController configurationController, DumpManager dumpManager) {
        this.context = context;
        this.configurationController = configurationController;
        configurationController.addCallback(this);
        dumpManager.registerDumpable("StatusBarInsetsProvider", this);
    }
}
