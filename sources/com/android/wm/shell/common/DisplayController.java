package com.android.wm.shell.common;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.util.Slog;
import android.util.SparseArray;
import android.view.Display;
import android.view.IDisplayWindowListener;
import android.view.IWindowManager;
import android.view.InsetsState;
import com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.VibratorHelper$$ExternalSyntheticLambda1;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DisplayController {
    public final DisplayChangeController mChangeController;
    public final Context mContext;
    public final ShellExecutor mMainExecutor;
    public final IWindowManager mWmService;
    public final SparseArray<DisplayRecord> mDisplays = new SparseArray<>();
    public final ArrayList<OnDisplaysChangedListener> mDisplayChangedListeners = new ArrayList<>();
    public final DisplayWindowListenerImpl mDisplayContainerListener = new DisplayWindowListenerImpl();

    /* loaded from: classes.dex */
    public static class DisplayRecord {
        public Context mContext;
        public DisplayLayout mDisplayLayout;
        public InsetsState mInsetsState = new InsetsState();
    }

    /* loaded from: classes.dex */
    public class DisplayWindowListenerImpl extends IDisplayWindowListener.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;

        public DisplayWindowListenerImpl() {
        }

        public final void onDisplayAdded(int i) {
            DisplayController.this.mMainExecutor.execute(new VibratorHelper$$ExternalSyntheticLambda1(this, i, 2));
        }

        public final void onDisplayConfigurationChanged(final int i, final Configuration configuration) {
            DisplayController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.common.DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    Context context;
                    DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl = DisplayController.DisplayWindowListenerImpl.this;
                    int i2 = i;
                    Configuration configuration2 = configuration;
                    Objects.requireNonNull(displayWindowListenerImpl);
                    DisplayController displayController = DisplayController.this;
                    Objects.requireNonNull(displayController);
                    synchronized (displayController.mDisplays) {
                        DisplayController.DisplayRecord displayRecord = displayController.mDisplays.get(i2);
                        if (displayRecord == null) {
                            Slog.w("DisplayController", "Skipping Display Configuration change on non-added display.");
                            return;
                        }
                        Display display = displayController.getDisplay(i2);
                        if (display == null) {
                            Slog.w("DisplayController", "Skipping Display Configuration change on invalid display. It may have been removed.");
                            return;
                        }
                        if (i2 == 0) {
                            context = displayController.mContext;
                        } else {
                            context = displayController.mContext.createDisplayContext(display);
                        }
                        Context createConfigurationContext = context.createConfigurationContext(configuration2);
                        DisplayLayout displayLayout = new DisplayLayout(createConfigurationContext, display);
                        displayRecord.mContext = createConfigurationContext;
                        displayRecord.mDisplayLayout = displayLayout;
                        Resources resources = createConfigurationContext.getResources();
                        displayLayout.mInsetsState = displayRecord.mInsetsState;
                        displayLayout.recalcInsets(resources);
                        for (int i3 = 0; i3 < displayController.mDisplayChangedListeners.size(); i3++) {
                            displayController.mDisplayChangedListeners.get(i3).onDisplayConfigurationChanged(i2, configuration2);
                        }
                    }
                }
            });
        }

        public final void onDisplayRemoved(final int i) {
            DisplayController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.common.DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl = DisplayController.DisplayWindowListenerImpl.this;
                    int i2 = i;
                    Objects.requireNonNull(displayWindowListenerImpl);
                    DisplayController displayController = DisplayController.this;
                    Objects.requireNonNull(displayController);
                    synchronized (displayController.mDisplays) {
                        if (displayController.mDisplays.get(i2) != null) {
                            int size = displayController.mDisplayChangedListeners.size();
                            while (true) {
                                size--;
                                if (size >= 0) {
                                    displayController.mDisplayChangedListeners.get(size).onDisplayRemoved(i2);
                                } else {
                                    displayController.mDisplays.remove(i2);
                                    return;
                                }
                            }
                        }
                    }
                }
            });
        }

        public final void onFixedRotationFinished(int i) {
            DisplayController.this.mMainExecutor.execute(new KeyguardIndicationController$$ExternalSyntheticLambda1(this, i, 1));
        }

        public final void onFixedRotationStarted(final int i, final int i2) {
            DisplayController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.common.DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl = DisplayController.DisplayWindowListenerImpl.this;
                    int i3 = i;
                    int i4 = i2;
                    Objects.requireNonNull(displayWindowListenerImpl);
                    DisplayController displayController = DisplayController.this;
                    Objects.requireNonNull(displayController);
                    synchronized (displayController.mDisplays) {
                        if (!(displayController.mDisplays.get(i3) == null || displayController.getDisplay(i3) == null)) {
                            int size = displayController.mDisplayChangedListeners.size();
                            while (true) {
                                size--;
                                if (size >= 0) {
                                    displayController.mDisplayChangedListeners.get(size).onFixedRotationStarted(i4);
                                } else {
                                    return;
                                }
                            }
                        }
                        Slog.w("DisplayController", "Skipping onFixedRotationStarted on unknown display, displayId=" + i3);
                    }
                }
            });
        }

        public final void onKeepClearAreasChanged(final int i, final List<Rect> list, final List<Rect> list2) {
            DisplayController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.common.DisplayController$DisplayWindowListenerImpl$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl = DisplayController.DisplayWindowListenerImpl.this;
                    int i2 = i;
                    Objects.requireNonNull(displayWindowListenerImpl);
                    DisplayController displayController = DisplayController.this;
                    Objects.requireNonNull(displayController);
                    synchronized (displayController.mDisplays) {
                        if (!(displayController.mDisplays.get(i2) == null || displayController.getDisplay(i2) == null)) {
                            int size = displayController.mDisplayChangedListeners.size();
                            while (true) {
                                size--;
                                if (size >= 0) {
                                    Objects.requireNonNull(displayController.mDisplayChangedListeners.get(size));
                                } else {
                                    return;
                                }
                            }
                        }
                        Slog.w("DisplayController", "Skipping onKeepClearAreasChanged on unknown display, displayId=" + i2);
                    }
                }
            });
        }
    }

    /* loaded from: classes.dex */
    public interface OnDisplaysChangedListener {
        default void onDisplayAdded(int i) {
        }

        default void onDisplayConfigurationChanged(int i, Configuration configuration) {
        }

        default void onDisplayRemoved(int i) {
        }

        default void onFixedRotationFinished() {
        }

        default void onFixedRotationStarted(int i) {
        }
    }

    public final void addDisplayChangingController(DisplayChangeController.OnDisplayChangingListener onDisplayChangingListener) {
        DisplayChangeController displayChangeController = this.mChangeController;
        Objects.requireNonNull(displayChangeController);
        displayChangeController.mRotationListener.add(onDisplayChangingListener);
    }

    public final void addDisplayWindowListener(OnDisplaysChangedListener onDisplaysChangedListener) {
        synchronized (this.mDisplays) {
            if (!this.mDisplayChangedListeners.contains(onDisplaysChangedListener)) {
                this.mDisplayChangedListeners.add(onDisplaysChangedListener);
                for (int i = 0; i < this.mDisplays.size(); i++) {
                    onDisplaysChangedListener.onDisplayAdded(this.mDisplays.keyAt(i));
                }
            }
        }
    }

    public final Display getDisplay(int i) {
        return ((DisplayManager) this.mContext.getSystemService(DisplayManager.class)).getDisplay(i);
    }

    public final Context getDisplayContext(int i) {
        DisplayRecord displayRecord = this.mDisplays.get(i);
        if (displayRecord != null) {
            return displayRecord.mContext;
        }
        return null;
    }

    public final DisplayLayout getDisplayLayout(int i) {
        DisplayRecord displayRecord = this.mDisplays.get(i);
        if (displayRecord != null) {
            return displayRecord.mDisplayLayout;
        }
        return null;
    }

    public final void onDisplayAdded(int i) {
        Context context;
        synchronized (this.mDisplays) {
            if (this.mDisplays.get(i) == null) {
                Display display = getDisplay(i);
                if (display != null) {
                    if (i == 0) {
                        context = this.mContext;
                    } else {
                        context = this.mContext.createDisplayContext(display);
                    }
                    DisplayRecord displayRecord = new DisplayRecord();
                    DisplayLayout displayLayout = new DisplayLayout(context, display);
                    displayRecord.mContext = context;
                    displayRecord.mDisplayLayout = displayLayout;
                    Resources resources = context.getResources();
                    displayLayout.mInsetsState = displayRecord.mInsetsState;
                    displayLayout.recalcInsets(resources);
                    this.mDisplays.put(i, displayRecord);
                    for (int i2 = 0; i2 < this.mDisplayChangedListeners.size(); i2++) {
                        this.mDisplayChangedListeners.get(i2).onDisplayAdded(i);
                    }
                }
            }
        }
    }

    public DisplayController(Context context, IWindowManager iWindowManager, ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
        this.mContext = context;
        this.mWmService = iWindowManager;
        this.mChangeController = new DisplayChangeController(iWindowManager, shellExecutor);
    }
}
