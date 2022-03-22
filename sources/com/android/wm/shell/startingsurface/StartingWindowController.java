package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.TaskInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import android.util.SparseIntArray;
import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.TriConsumer;
import com.android.launcher3.icons.IconProvider;
import com.android.systemui.qs.tiles.InternetTile$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda4;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda6;
import com.android.wm.shell.ShellInitImpl$$ExternalSyntheticLambda5;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SingleInstanceRemoteListener;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.startingsurface.IStartingWindow;
import com.android.wm.shell.startingsurface.SplashscreenContentDrawer;
import com.android.wm.shell.startingsurface.StartingWindowController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StartingWindowController implements RemoteCallable<StartingWindowController> {
    public final Context mContext;
    public final ShellExecutor mSplashScreenExecutor;
    public final StartingSurfaceDrawer mStartingSurfaceDrawer;
    public final StartingWindowTypeAlgorithm mStartingWindowTypeAlgorithm;
    public TriConsumer<Integer, Integer, Integer> mTaskLaunchingCallback;
    public final StartingSurfaceImpl mImpl = new StartingSurfaceImpl();
    @GuardedBy({"mTaskBackgroundColors"})
    public final SparseIntArray mTaskBackgroundColors = new SparseIntArray();

    /* loaded from: classes.dex */
    public class StartingSurfaceImpl implements StartingSurface {
        public IStartingWindowImpl mIStartingWindow;

        public StartingSurfaceImpl() {
        }

        @Override // com.android.wm.shell.startingsurface.StartingSurface
        public final IStartingWindow createExternalInterface() {
            IStartingWindowImpl iStartingWindowImpl = this.mIStartingWindow;
            if (iStartingWindowImpl != null) {
                iStartingWindowImpl.mController = null;
            }
            IStartingWindowImpl iStartingWindowImpl2 = new IStartingWindowImpl(StartingWindowController.this);
            this.mIStartingWindow = iStartingWindowImpl2;
            return iStartingWindowImpl2;
        }

        @Override // com.android.wm.shell.startingsurface.StartingSurface
        public final int getBackgroundColor(ActivityManager.RunningTaskInfo runningTaskInfo) {
            int i;
            synchronized (StartingWindowController.this.mTaskBackgroundColors) {
                int indexOfKey = StartingWindowController.this.mTaskBackgroundColors.indexOfKey(((TaskInfo) runningTaskInfo).taskId);
                if (indexOfKey >= 0) {
                    return StartingWindowController.this.mTaskBackgroundColors.valueAt(indexOfKey);
                }
                StartingSurfaceDrawer startingSurfaceDrawer = StartingWindowController.this.mStartingSurfaceDrawer;
                Objects.requireNonNull(startingSurfaceDrawer);
                ActivityInfo activityInfo = ((TaskInfo) runningTaskInfo).topActivityInfo;
                int i2 = 0;
                if (activityInfo != null) {
                    String str = activityInfo.packageName;
                    int i3 = ((TaskInfo) runningTaskInfo).userId;
                    try {
                        Context createPackageContextAsUser = startingSurfaceDrawer.mContext.createPackageContextAsUser(str, 4, UserHandle.of(i3));
                        try {
                            String splashScreenTheme = ActivityThread.getPackageManager().getSplashScreenTheme(str, i3);
                            if (splashScreenTheme != null) {
                                i = createPackageContextAsUser.getResources().getIdentifier(splashScreenTheme, null, null);
                            } else {
                                i = 0;
                            }
                            if (i == 0) {
                                if (activityInfo.getThemeResource() != 0) {
                                    i = activityInfo.getThemeResource();
                                } else {
                                    i = 16974563;
                                }
                            }
                            if (i != createPackageContextAsUser.getThemeResId()) {
                                createPackageContextAsUser.setTheme(i);
                            }
                            Objects.requireNonNull(startingSurfaceDrawer.mSplashscreenContentDrawer);
                            SplashscreenContentDrawer.SplashScreenWindowAttrs splashScreenWindowAttrs = new SplashscreenContentDrawer.SplashScreenWindowAttrs();
                            SplashscreenContentDrawer.getWindowAttrs(createPackageContextAsUser, splashScreenWindowAttrs);
                            i2 = SplashscreenContentDrawer.peekWindowBGColor(createPackageContextAsUser, splashScreenWindowAttrs);
                        } catch (RemoteException | RuntimeException e) {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("failed get starting window background color at taskId: ");
                            m.append(((TaskInfo) runningTaskInfo).taskId);
                            Slog.w("ShellStartingWindow", m.toString(), e);
                        }
                    } catch (PackageManager.NameNotFoundException e2) {
                        StringBuilder m2 = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Failed creating package context with package name ", str, " for user ");
                        m2.append(((TaskInfo) runningTaskInfo).userId);
                        Slog.w("ShellStartingWindow", m2.toString(), e2);
                    }
                }
                if (i2 != 0) {
                    return i2;
                }
                return SplashscreenContentDrawer.getSystemBGColor();
            }
        }

        @Override // com.android.wm.shell.startingsurface.StartingSurface
        public final void setSysuiProxy(StatusBar$$ExternalSyntheticLambda4 statusBar$$ExternalSyntheticLambda4) {
            StartingWindowController.this.mSplashScreenExecutor.execute(new InternetTile$$ExternalSyntheticLambda0(this, statusBar$$ExternalSyntheticLambda4, 2));
        }
    }

    /* loaded from: classes.dex */
    public static class IStartingWindowImpl extends IStartingWindow.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;
        public StartingWindowController mController;
        public SingleInstanceRemoteListener<StartingWindowController, IStartingWindowListener> mListener;
        public final StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0 mStartingWindowListener = new TriConsumer() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0
            public final void accept(Object obj, Object obj2, Object obj3) {
                StartingWindowController.IStartingWindowImpl iStartingWindowImpl = StartingWindowController.IStartingWindowImpl.this;
                Integer num = (Integer) obj;
                Integer num2 = (Integer) obj2;
                Integer num3 = (Integer) obj3;
                Objects.requireNonNull(iStartingWindowImpl);
                SingleInstanceRemoteListener<StartingWindowController, IStartingWindowListener> singleInstanceRemoteListener = iStartingWindowImpl.mListener;
                Objects.requireNonNull(singleInstanceRemoteListener);
                IStartingWindowListener iStartingWindowListener = singleInstanceRemoteListener.mListener;
                if (iStartingWindowListener == null) {
                    Slog.e("SingleInstanceRemoteListener", "Failed remote call on null listener");
                    return;
                }
                try {
                    iStartingWindowListener.onTaskLaunching(num.intValue(), num2.intValue(), num3.intValue());
                } catch (RemoteException e) {
                    Slog.e("SingleInstanceRemoteListener", "Failed remote call", e);
                }
            }
        };

        public IStartingWindowImpl(StartingWindowController startingWindowController) {
            this.mController = startingWindowController;
            this.mListener = new SingleInstanceRemoteListener<>(startingWindowController, new WMShell$$ExternalSyntheticLambda6(this, 4), ShellInitImpl$$ExternalSyntheticLambda5.INSTANCE$2);
        }
    }

    public StartingWindowController(Context context, ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, IconProvider iconProvider, TransactionPool transactionPool) {
        this.mContext = context;
        this.mStartingSurfaceDrawer = new StartingSurfaceDrawer(context, shellExecutor, iconProvider, transactionPool);
        this.mStartingWindowTypeAlgorithm = startingWindowTypeAlgorithm;
        this.mSplashScreenExecutor = shellExecutor;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final ShellExecutor getRemoteCallExecutor() {
        return this.mSplashScreenExecutor;
    }
}
