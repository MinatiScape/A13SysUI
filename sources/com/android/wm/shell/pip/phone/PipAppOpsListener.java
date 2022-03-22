package com.android.wm.shell.pip.phone;

import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Pair;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PipUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public final class PipAppOpsListener {
    public AnonymousClass1 mAppOpsChangedListener = new AnonymousClass1();
    public AppOpsManager mAppOpsManager;
    public Callback mCallback;
    public Context mContext;
    public ShellExecutor mMainExecutor;

    /* renamed from: com.android.wm.shell.pip.phone.PipAppOpsListener$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements AppOpsManager.OnOpChangedListener {
        public AnonymousClass1() {
        }

        @Override // android.app.AppOpsManager.OnOpChangedListener
        public final void onOpChanged(String str, String str2) {
            try {
                Pair<ComponentName, Integer> topPipActivity = PipUtils.getTopPipActivity(PipAppOpsListener.this.mContext);
                if (topPipActivity.first != null) {
                    ApplicationInfo applicationInfoAsUser = PipAppOpsListener.this.mContext.getPackageManager().getApplicationInfoAsUser(str2, 0, ((Integer) topPipActivity.second).intValue());
                    if (applicationInfoAsUser.packageName.equals(((ComponentName) topPipActivity.first).getPackageName()) && PipAppOpsListener.this.mAppOpsManager.checkOpNoThrow(67, applicationInfoAsUser.uid, str2) != 0) {
                        PipAppOpsListener.this.mMainExecutor.execute(new PipMenuView$$ExternalSyntheticLambda7(this, 4));
                    }
                }
            } catch (PackageManager.NameNotFoundException unused) {
                PipAppOpsListener pipAppOpsListener = PipAppOpsListener.this;
                Objects.requireNonNull(pipAppOpsListener);
                pipAppOpsListener.mAppOpsManager.stopWatchingMode(pipAppOpsListener.mAppOpsChangedListener);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface Callback {
    }

    public PipAppOpsListener(Context context, PipMotionHelper pipMotionHelper, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mAppOpsManager = (AppOpsManager) context.getSystemService("appops");
        this.mCallback = pipMotionHelper;
    }
}
