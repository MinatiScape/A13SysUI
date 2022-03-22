package com.android.keyguard;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.Uri;
import android.os.Trace;
import android.view.Display;
import android.view.View;
import androidx.lifecycle.Observer;
import androidx.slice.Slice;
import androidx.slice.widget.ListContent;
import androidx.slice.widget.RowContent;
import androidx.slice.widget.SliceLiveData;
import com.android.keyguard.KeyguardSliceView;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.ViewController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class KeyguardSliceViewController extends ViewController<KeyguardSliceView> implements Dumpable {
    public final ActivityStarter mActivityStarter;
    public HashMap mClickActions;
    public final ConfigurationController mConfigurationController;
    public int mDisplayId;
    public final DumpManager mDumpManager;
    public Uri mKeyguardSliceUri;
    public SliceLiveData.SliceLiveDataImpl mLiveData;
    public Slice mSlice;
    public final TunerService mTunerService;
    public KeyguardSliceViewController$$ExternalSyntheticLambda0 mTunable = new TunerService.Tunable() { // from class: com.android.keyguard.KeyguardSliceViewController$$ExternalSyntheticLambda0
        /* JADX WARN: Removed duplicated region for block: B:16:0x003e  */
        /* JADX WARN: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
        @Override // com.android.systemui.tuner.TunerService.Tunable
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onTuningChanged(java.lang.String r4, java.lang.String r5) {
            /*
                r3 = this;
                com.android.keyguard.KeyguardSliceViewController r3 = com.android.keyguard.KeyguardSliceViewController.this
                java.util.Objects.requireNonNull(r3)
                if (r5 != 0) goto L_0x0009
                java.lang.String r5 = "content://com.android.systemui.keyguard/main"
            L_0x0009:
                androidx.slice.widget.SliceLiveData$SliceLiveDataImpl r4 = r3.mLiveData
                r0 = 1
                r1 = 0
                if (r4 == 0) goto L_0x001e
                int r2 = r4.mActiveCount
                if (r2 <= 0) goto L_0x0015
                r2 = r0
                goto L_0x0016
            L_0x0015:
                r2 = r1
            L_0x0016:
                if (r2 == 0) goto L_0x001e
                com.android.keyguard.KeyguardSliceViewController$2 r1 = r3.mObserver
                r4.removeObserver(r1)
                goto L_0x001f
            L_0x001e:
                r0 = r1
            L_0x001f:
                android.net.Uri r4 = android.net.Uri.parse(r5)
                r3.mKeyguardSliceUri = r4
                T extends android.view.View r4 = r3.mView
                com.android.keyguard.KeyguardSliceView r4 = (com.android.keyguard.KeyguardSliceView) r4
                android.content.Context r4 = r4.getContext()
                android.net.Uri r5 = r3.mKeyguardSliceUri
                androidx.collection.ArraySet r1 = androidx.slice.widget.SliceLiveData.SUPPORTED_SPECS
                androidx.slice.widget.SliceLiveData$SliceLiveDataImpl r1 = new androidx.slice.widget.SliceLiveData$SliceLiveDataImpl
                android.content.Context r4 = r4.getApplicationContext()
                r1.<init>(r4, r5)
                r3.mLiveData = r1
                if (r0 == 0) goto L_0x0043
                com.android.keyguard.KeyguardSliceViewController$2 r3 = r3.mObserver
                r1.observeForever(r3)
            L_0x0043:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.KeyguardSliceViewController$$ExternalSyntheticLambda0.onTuningChanged(java.lang.String, java.lang.String):void");
        }
    };
    public AnonymousClass1 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.KeyguardSliceViewController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onDensityOrFontScaleChanged() {
            ((KeyguardSliceView) KeyguardSliceViewController.this.mView).onDensityOrFontScaleChanged();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            KeyguardSliceView keyguardSliceView = (KeyguardSliceView) KeyguardSliceViewController.this.mView;
            Objects.requireNonNull(keyguardSliceView);
            for (int i = 0; i < keyguardSliceView.mRow.getChildCount(); i++) {
                View childAt = keyguardSliceView.mRow.getChildAt(i);
                if (childAt instanceof KeyguardSliceView.KeyguardSliceTextView) {
                    KeyguardSliceView.KeyguardSliceTextView keyguardSliceTextView = (KeyguardSliceView.KeyguardSliceTextView) childAt;
                    Objects.requireNonNull(keyguardSliceTextView);
                    keyguardSliceTextView.setTextAppearance(2132017919);
                }
            }
        }
    };
    public AnonymousClass2 mObserver = new AnonymousClass2();

    /* renamed from: com.android.keyguard.KeyguardSliceViewController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements Observer<Slice> {
        public AnonymousClass2() {
        }

        @Override // androidx.lifecycle.Observer
        public final void onChanged(Slice slice) {
            Slice slice2 = slice;
            KeyguardSliceViewController keyguardSliceViewController = KeyguardSliceViewController.this;
            keyguardSliceViewController.mSlice = slice2;
            Trace.beginSection("KeyguardSliceViewController#showSlice");
            boolean z = false;
            if (slice2 == null) {
                KeyguardSliceView keyguardSliceView = (KeyguardSliceView) keyguardSliceViewController.mView;
                Objects.requireNonNull(keyguardSliceView);
                keyguardSliceView.mTitle.setVisibility(8);
                keyguardSliceView.mRow.setVisibility(8);
                keyguardSliceView.mHasHeader = false;
                Runnable runnable = keyguardSliceView.mContentChangeListener;
                if (runnable != null) {
                    runnable.run();
                }
                Trace.endSection();
                return;
            }
            ListContent listContent = new ListContent(slice2);
            RowContent rowContent = listContent.mHeaderContent;
            if (rowContent != null && !rowContent.mSliceItem.hasHint("list_item")) {
                z = true;
            }
            List list = (List) listContent.mRowItems.stream().filter(KeyguardSliceViewController$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList());
            KeyguardSliceView keyguardSliceView2 = (KeyguardSliceView) keyguardSliceViewController.mView;
            if (!z) {
                rowContent = null;
            }
            keyguardSliceViewController.mClickActions = keyguardSliceView2.showSlice(rowContent, list);
            Trace.endSection();
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  mSlice: ");
        m.append(this.mSlice);
        printWriter.println(m.toString());
        printWriter.println("  mClickActions: " + this.mClickActions);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        SliceLiveData.SliceLiveDataImpl sliceLiveDataImpl;
        Display display = ((KeyguardSliceView) this.mView).getDisplay();
        if (display != null) {
            this.mDisplayId = display.getDisplayId();
        }
        this.mTunerService.addTunable(this.mTunable, "keyguard_slice_uri");
        if (this.mDisplayId == 0 && (sliceLiveDataImpl = this.mLiveData) != null) {
            sliceLiveDataImpl.observeForever(this.mObserver);
        }
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        DumpManager dumpManager = this.mDumpManager;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("KeyguardSliceViewCtrl@");
        m.append(Integer.toHexString(hashCode()));
        dumpManager.registerDumpable(m.toString(), this);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        if (this.mDisplayId == 0) {
            this.mLiveData.removeObserver(this.mObserver);
        }
        this.mTunerService.removeTunable(this.mTunable);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        DumpManager dumpManager = this.mDumpManager;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("KeyguardSliceViewCtrl@");
        m.append(Integer.toHexString(hashCode()));
        dumpManager.unregisterDumpable(m.toString());
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.keyguard.KeyguardSliceViewController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.keyguard.KeyguardSliceViewController$1] */
    public KeyguardSliceViewController(KeyguardSliceView keyguardSliceView, ActivityStarter activityStarter, ConfigurationController configurationController, TunerService tunerService, DumpManager dumpManager) {
        super(keyguardSliceView);
        this.mActivityStarter = activityStarter;
        this.mConfigurationController = configurationController;
        this.mTunerService = tunerService;
        this.mDumpManager = dumpManager;
    }
}
