package com.android.systemui.statusbar.phone;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.widget.ImageView;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DarkIconDispatcherImpl implements SysuiDarkIconDispatcher, LightBarTransitionsController.DarkIntensityApplier {
    public float mDarkIntensity;
    public int mDarkModeIconColorSingleTone;
    public int mLightModeIconColorSingleTone;
    public final LightBarTransitionsController mTransitionsController;
    public final ArrayList<Rect> mTintAreas = new ArrayList<>();
    public final ArrayMap<Object, DarkIconDispatcher.DarkReceiver> mReceivers = new ArrayMap<>();
    public int mIconTint = -1;

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public final void addDarkReceiver(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.put(darkReceiver, darkReceiver);
        darkReceiver.onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    public final void applyIconTint() {
        for (int i = 0; i < this.mReceivers.size(); i++) {
            this.mReceivers.valueAt(i).onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
        }
    }

    @Override // com.android.systemui.statusbar.phone.LightBarTransitionsController.DarkIntensityApplier
    public final int getTintAnimationDuration() {
        return 120;
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public final void removeDarkReceiver(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.remove(darkReceiver);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public final void applyDark(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.get(darkReceiver).onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    @Override // com.android.systemui.statusbar.phone.LightBarTransitionsController.DarkIntensityApplier
    public final void applyDarkIntensity(float f) {
        this.mDarkIntensity = f;
        this.mIconTint = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightModeIconColorSingleTone), Integer.valueOf(this.mDarkModeIconColorSingleTone))).intValue();
        applyIconTint();
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "DarkIconDispatcher: ", "  mIconTint: 0x");
        m.append(Integer.toHexString(this.mIconTint));
        printWriter.println(m.toString());
        printWriter.println("  mDarkIntensity: " + this.mDarkIntensity + "f");
        StringBuilder sb = new StringBuilder();
        sb.append("  mTintAreas: ");
        sb.append(this.mTintAreas);
        printWriter.println(sb.toString());
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public final void removeDarkReceiver(ImageView imageView) {
        this.mReceivers.remove(imageView);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public final void setIconsDarkArea(ArrayList<Rect> arrayList) {
        if (arrayList != null || !this.mTintAreas.isEmpty()) {
            this.mTintAreas.clear();
            if (arrayList != null) {
                this.mTintAreas.addAll(arrayList);
            }
            applyIconTint();
        }
    }

    public DarkIconDispatcherImpl(Context context, CommandQueue commandQueue, DumpManager dumpManager) {
        this.mDarkModeIconColorSingleTone = context.getColor(2131099796);
        this.mLightModeIconColorSingleTone = context.getColor(2131099958);
        this.mTransitionsController = new LightBarTransitionsController(context, this, commandQueue);
        dumpManager.registerDumpable("DarkIconDispatcherImpl", this);
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher
    public final void addDarkReceiver(final ImageView imageView) {
        DarkIconDispatcher.DarkReceiver darkIconDispatcherImpl$$ExternalSyntheticLambda0 = new DarkIconDispatcher.DarkReceiver() { // from class: com.android.systemui.statusbar.phone.DarkIconDispatcherImpl$$ExternalSyntheticLambda0
            @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
            public final void onDarkChanged(ArrayList arrayList, float f, int i) {
                DarkIconDispatcherImpl darkIconDispatcherImpl = DarkIconDispatcherImpl.this;
                ImageView imageView2 = imageView;
                Objects.requireNonNull(darkIconDispatcherImpl);
                imageView2.setImageTintList(ColorStateList.valueOf(DarkIconDispatcher.getTint(darkIconDispatcherImpl.mTintAreas, imageView2, darkIconDispatcherImpl.mIconTint)));
            }
        };
        this.mReceivers.put(imageView, darkIconDispatcherImpl$$ExternalSyntheticLambda0);
        darkIconDispatcherImpl$$ExternalSyntheticLambda0.onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    @Override // com.android.systemui.statusbar.phone.SysuiDarkIconDispatcher
    public final LightBarTransitionsController getTransitionsController() {
        return this.mTransitionsController;
    }
}
