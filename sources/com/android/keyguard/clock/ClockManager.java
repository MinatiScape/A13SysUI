package com.android.keyguard.clock;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import androidx.lifecycle.Observer;
import com.android.keyguard.clock.ClockManager;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.dock.DockManager;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.settings.CurrentUserObservable;
import com.android.systemui.shared.plugins.PluginManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class ClockManager {
    public final AnonymousClass1 mContentObserver;
    public final ContentResolver mContentResolver;
    public final CurrentUserObservable mCurrentUserObservable;
    public final DockManager mDockManager;
    public final int mHeight;
    public boolean mIsDocked;
    public final Handler mMainHandler;
    public final PluginManager mPluginManager;
    public final SettingsWrapper mSettingsWrapper;
    public final int mWidth;
    public final ArrayList mBuiltinClocks = new ArrayList();
    public final ClockManager$$ExternalSyntheticLambda0 mCurrentUserObserver = new Observer() { // from class: com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda0
        @Override // androidx.lifecycle.Observer
        public final void onChanged(Object obj) {
            ClockManager clockManager = ClockManager.this;
            Integer num = (Integer) obj;
            Objects.requireNonNull(clockManager);
            clockManager.reload();
        }
    };
    public final AnonymousClass2 mDockEventListener = new DockManager.DockEventListener() { // from class: com.android.keyguard.clock.ClockManager.2
        @Override // com.android.systemui.dock.DockManager.DockEventListener
        public final void onEvent(int i) {
            ClockManager clockManager = ClockManager.this;
            boolean z = true;
            if (!(i == 1 || i == 2)) {
                z = false;
            }
            clockManager.mIsDocked = z;
            clockManager.reload();
        }
    };
    public final ArrayMap mListeners = new ArrayMap();
    public final AvailableClocks mPreviewClocks = new AvailableClocks();

    /* loaded from: classes.dex */
    public final class AvailableClocks implements PluginListener<ClockPlugin> {
        public ClockPlugin mCurrentClock;
        public final ArrayMap mClocks = new ArrayMap();
        public final ArrayList mClockInfo = new ArrayList();

        public AvailableClocks() {
        }

        @Override // com.android.systemui.plugins.PluginListener
        public final void onPluginConnected(ClockPlugin clockPlugin, Context context) {
            boolean z;
            ClockPlugin clockPlugin2 = clockPlugin;
            addClockPlugin(clockPlugin2);
            boolean z2 = true;
            if (clockPlugin2 == this.mCurrentClock) {
                z = true;
            } else {
                z = false;
            }
            reloadCurrentClock();
            if (clockPlugin2 != this.mCurrentClock) {
                z2 = false;
            }
            if (z || z2) {
                ClockManager.this.reload();
            }
        }

        @Override // com.android.systemui.plugins.PluginListener
        public final void onPluginDisconnected(ClockPlugin clockPlugin) {
            boolean z;
            ClockPlugin clockPlugin2 = clockPlugin;
            String name = clockPlugin2.getClass().getName();
            this.mClocks.remove(name);
            boolean z2 = false;
            int i = 0;
            while (true) {
                if (i >= this.mClockInfo.size()) {
                    break;
                }
                ClockInfo clockInfo = (ClockInfo) this.mClockInfo.get(i);
                Objects.requireNonNull(clockInfo);
                if (name.equals(clockInfo.mId)) {
                    this.mClockInfo.remove(i);
                    break;
                }
                i++;
            }
            if (clockPlugin2 == this.mCurrentClock) {
                z = true;
            } else {
                z = false;
            }
            reloadCurrentClock();
            if (clockPlugin2 == this.mCurrentClock) {
                z2 = true;
            }
            if (z || z2) {
                ClockManager.this.reload();
            }
        }

        /* JADX WARN: Code restructure failed: missing block: B:7:0x0031, code lost:
            if (r0 != null) goto L_0x0062;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void reloadCurrentClock() {
            /*
                r5 = this;
                com.android.keyguard.clock.ClockManager r0 = com.android.keyguard.clock.ClockManager.this
                boolean r0 = r0.isDocked()
                if (r0 == 0) goto L_0x0034
                com.android.keyguard.clock.ClockManager r0 = com.android.keyguard.clock.ClockManager.this
                com.android.keyguard.clock.SettingsWrapper r1 = r0.mSettingsWrapper
                com.android.systemui.settings.CurrentUserObservable r0 = r0.mCurrentUserObservable
                com.android.systemui.settings.CurrentUserObservable$1 r0 = r0.getCurrentUser()
                java.lang.Object r0 = r0.getValue()
                java.lang.Integer r0 = (java.lang.Integer) r0
                int r0 = r0.intValue()
                java.util.Objects.requireNonNull(r1)
                android.content.ContentResolver r1 = r1.mContentResolver
                java.lang.String r2 = "docked_clock_face"
                java.lang.String r0 = android.provider.Settings.Secure.getStringForUser(r1, r2, r0)
                if (r0 == 0) goto L_0x0034
                android.util.ArrayMap r1 = r5.mClocks
                java.lang.Object r0 = r1.get(r0)
                com.android.systemui.plugins.ClockPlugin r0 = (com.android.systemui.plugins.ClockPlugin) r0
                if (r0 == 0) goto L_0x0035
                goto L_0x0062
            L_0x0034:
                r0 = 0
            L_0x0035:
                com.android.keyguard.clock.ClockManager r1 = com.android.keyguard.clock.ClockManager.this
                com.android.keyguard.clock.SettingsWrapper r2 = r1.mSettingsWrapper
                com.android.systemui.settings.CurrentUserObservable r1 = r1.mCurrentUserObservable
                com.android.systemui.settings.CurrentUserObservable$1 r1 = r1.getCurrentUser()
                java.lang.Object r1 = r1.getValue()
                java.lang.Integer r1 = (java.lang.Integer) r1
                int r1 = r1.intValue()
                java.util.Objects.requireNonNull(r2)
                android.content.ContentResolver r3 = r2.mContentResolver
                java.lang.String r4 = "lock_screen_custom_clock_face"
                java.lang.String r3 = android.provider.Settings.Secure.getStringForUser(r3, r4, r1)
                java.lang.String r1 = r2.decode(r3, r1)
                if (r1 == 0) goto L_0x0062
                android.util.ArrayMap r0 = r5.mClocks
                java.lang.Object r0 = r0.get(r1)
                com.android.systemui.plugins.ClockPlugin r0 = (com.android.systemui.plugins.ClockPlugin) r0
            L_0x0062:
                r5.mCurrentClock = r0
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.clock.ClockManager.AvailableClocks.reloadCurrentClock():void");
        }

        public final void addClockPlugin(final ClockPlugin clockPlugin) {
            String name = clockPlugin.getClass().getName();
            this.mClocks.put(clockPlugin.getClass().getName(), clockPlugin);
            this.mClockInfo.add(new ClockInfo(clockPlugin.getName(), new ClockManager$AvailableClocks$$ExternalSyntheticLambda0(clockPlugin, 0), name, new Supplier() { // from class: com.android.keyguard.clock.ClockManager$AvailableClocks$$ExternalSyntheticLambda2
                @Override // java.util.function.Supplier
                public final Object get() {
                    return ClockPlugin.this.getThumbnail();
                }
            }, new Supplier() { // from class: com.android.keyguard.clock.ClockManager$AvailableClocks$$ExternalSyntheticLambda1
                @Override // java.util.function.Supplier
                public final Object get() {
                    ClockManager.AvailableClocks availableClocks = ClockManager.AvailableClocks.this;
                    ClockPlugin clockPlugin2 = clockPlugin;
                    Objects.requireNonNull(availableClocks);
                    ClockManager clockManager = ClockManager.this;
                    return clockPlugin2.getPreview(clockManager.mWidth, clockManager.mHeight);
                }
            }));
        }
    }

    /* loaded from: classes.dex */
    public interface ClockChangedListener {
        void onClockChanged(ClockPlugin clockPlugin);
    }

    public final void reload() {
        this.mPreviewClocks.reloadCurrentClock();
        this.mListeners.forEach(new BiConsumer() { // from class: com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda2
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                ClockManager clockManager = ClockManager.this;
                ClockManager.ClockChangedListener clockChangedListener = (ClockManager.ClockChangedListener) obj;
                ClockManager.AvailableClocks availableClocks = (ClockManager.AvailableClocks) obj2;
                Objects.requireNonNull(clockManager);
                availableClocks.reloadCurrentClock();
                ClockPlugin clockPlugin = availableClocks.mCurrentClock;
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    if (clockPlugin instanceof DefaultClockController) {
                        clockPlugin = null;
                    }
                    clockChangedListener.onClockChanged(clockPlugin);
                    return;
                }
                clockManager.mMainHandler.post(new ClockManager$$ExternalSyntheticLambda1(clockChangedListener, clockPlugin, 0));
            }
        });
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.keyguard.clock.ClockManager$2] */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.keyguard.clock.ClockManager$1] */
    public ClockManager(Context context, final LayoutInflater layoutInflater, PluginManager pluginManager, final SysuiColorExtractor sysuiColorExtractor, ContentResolver contentResolver, CurrentUserObservable currentUserObservable, SettingsWrapper settingsWrapper, DockManager dockManager) {
        Handler handler = new Handler(Looper.getMainLooper());
        this.mMainHandler = handler;
        this.mContentObserver = new ContentObserver(handler) { // from class: com.android.keyguard.clock.ClockManager.1
            public final void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                if (Objects.equals(Integer.valueOf(i2), ClockManager.this.mCurrentUserObservable.getCurrentUser().getValue())) {
                    ClockManager.this.reload();
                }
            }
        };
        this.mPluginManager = pluginManager;
        this.mContentResolver = contentResolver;
        this.mSettingsWrapper = settingsWrapper;
        this.mCurrentUserObservable = currentUserObservable;
        this.mDockManager = dockManager;
        final Resources resources = context.getResources();
        addBuiltinClock(new Supplier() { // from class: com.android.keyguard.clock.ClockManager$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                return new DefaultClockController(resources, layoutInflater, sysuiColorExtractor);
            }
        });
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        this.mWidth = displayMetrics.widthPixels;
        this.mHeight = displayMetrics.heightPixels;
    }

    public void addBuiltinClock(Supplier<ClockPlugin> supplier) {
        this.mPreviewClocks.addClockPlugin(supplier.get());
        this.mBuiltinClocks.add(supplier);
    }

    public ContentObserver getContentObserver() {
        return this.mContentObserver;
    }

    public boolean isDocked() {
        return this.mIsDocked;
    }
}
