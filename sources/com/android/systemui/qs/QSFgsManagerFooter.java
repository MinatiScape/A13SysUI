package com.android.systemui.qs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.RemoteException;
import android.provider.DeviceConfig;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.qs.FgsManagerController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Unit;
/* loaded from: classes.dex */
public final class QSFgsManagerFooter implements View.OnClickListener, FgsManagerController.OnDialogDismissedListener, FgsManagerController.OnNumberOfPackagesChangedListener {
    public final Context mContext;
    public final ImageView mDotView;
    public final Executor mExecutor;
    public final FgsManagerController mFgsManagerController;
    public final TextView mFooterText;
    public boolean mIsInitialized = false;
    public final Executor mMainExecutor;
    public int mNumPackages;
    public final View mNumberContainer;
    public final TextView mNumberView;
    public final View mRootView;
    public final View mTextContainer;
    public VisibilityChangedDispatcher$OnVisibilityChangedListener mVisibilityChangedListener;

    public final void init() {
        if (!this.mIsInitialized) {
            final FgsManagerController fgsManagerController = this.mFgsManagerController;
            Objects.requireNonNull(fgsManagerController);
            synchronized (fgsManagerController.lock) {
                if (!fgsManagerController.initialized) {
                    try {
                        fgsManagerController.activityManager.registerForegroundServiceObserver(fgsManagerController);
                    } catch (RemoteException e) {
                        e.rethrowFromSystemServer();
                    }
                    DeviceConfigProxy deviceConfigProxy = fgsManagerController.deviceConfigProxy;
                    Executor executor = fgsManagerController.backgroundExecutor;
                    DeviceConfig.OnPropertiesChangedListener fgsManagerController$init$1$1 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.qs.FgsManagerController$init$1$1
                        public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                            FgsManagerController fgsManagerController2 = FgsManagerController.this;
                            fgsManagerController2.isAvailable = properties.getBoolean("task_manager_enabled", fgsManagerController2.isAvailable);
                        }
                    };
                    Objects.requireNonNull(deviceConfigProxy);
                    DeviceConfig.addOnPropertiesChangedListener("systemui", executor, fgsManagerController$init$1$1);
                    Objects.requireNonNull(fgsManagerController.deviceConfigProxy);
                    fgsManagerController.isAvailable = DeviceConfig.getBoolean("systemui", "task_manager_enabled", true);
                    fgsManagerController.dumpManager.registerDumpable(fgsManagerController);
                    fgsManagerController.initialized = true;
                }
            }
            this.mRootView.setOnClickListener(this);
            this.mIsInitialized = true;
        }
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        final FgsManagerController fgsManagerController = this.mFgsManagerController;
        final View view2 = this.mRootView;
        Objects.requireNonNull(fgsManagerController);
        synchronized (fgsManagerController.lock) {
            if (fgsManagerController.dialog == null) {
                final SystemUIDialog systemUIDialog = new SystemUIDialog(fgsManagerController.context);
                systemUIDialog.setTitle(2131952362);
                RecyclerView recyclerView = new RecyclerView(systemUIDialog.getContext());
                recyclerView.setLayoutManager(new LinearLayoutManager(1));
                recyclerView.setAdapter(fgsManagerController.appListAdapter);
                systemUIDialog.setView(recyclerView);
                fgsManagerController.dialog = systemUIDialog;
                systemUIDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.qs.FgsManagerController$showDialog$1$1
                    @Override // android.content.DialogInterface.OnDismissListener
                    public final void onDismiss(DialogInterface dialogInterface) {
                        FgsManagerController fgsManagerController2 = FgsManagerController.this;
                        fgsManagerController2.changesSinceDialog = false;
                        synchronized (fgsManagerController2.lock) {
                            fgsManagerController2.dialog = null;
                            fgsManagerController2.updateAppItemsLocked();
                        }
                        FgsManagerController fgsManagerController3 = FgsManagerController.this;
                        Objects.requireNonNull(fgsManagerController3);
                        LinkedHashSet<FgsManagerController.OnDialogDismissedListener> linkedHashSet = fgsManagerController3.onDialogDismissedListeners;
                        FgsManagerController fgsManagerController4 = FgsManagerController.this;
                        for (final FgsManagerController.OnDialogDismissedListener onDialogDismissedListener : linkedHashSet) {
                            fgsManagerController4.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.qs.FgsManagerController$showDialog$1$1$2$1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    FgsManagerController.OnDialogDismissedListener.this.onDialogDismissed();
                                }
                            });
                        }
                    }
                });
                fgsManagerController.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.qs.FgsManagerController$showDialog$1$2
                    @Override // java.lang.Runnable
                    public final void run() {
                        Unit unit;
                        View view3 = view2;
                        if (view3 == null) {
                            unit = null;
                        } else {
                            FgsManagerController fgsManagerController2 = fgsManagerController;
                            SystemUIDialog systemUIDialog2 = systemUIDialog;
                            DialogLaunchAnimator dialogLaunchAnimator = fgsManagerController2.dialogLaunchAnimator;
                            LaunchAnimator.Timings timings = DialogLaunchAnimator.TIMINGS;
                            dialogLaunchAnimator.showFromView(systemUIDialog2, view3, false);
                            unit = Unit.INSTANCE;
                        }
                        if (unit == null) {
                            systemUIDialog.show();
                        }
                    }
                });
                fgsManagerController.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.qs.FgsManagerController$showDialog$1$3
                    @Override // java.lang.Runnable
                    public final void run() {
                        FgsManagerController fgsManagerController2 = FgsManagerController.this;
                        synchronized (fgsManagerController2.lock) {
                            fgsManagerController2.updateAppItemsLocked();
                        }
                    }
                });
            }
        }
    }

    @Override // com.android.systemui.qs.FgsManagerController.OnDialogDismissedListener
    public final void onDialogDismissed() {
        this.mExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda0(this, 0));
    }

    @Override // com.android.systemui.qs.FgsManagerController.OnNumberOfPackagesChangedListener
    public final void onNumberOfPackagesChanged(int i) {
        this.mNumPackages = i;
        this.mExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda0(this, 0));
    }

    public final void setListening(boolean z) {
        if (z) {
            FgsManagerController fgsManagerController = this.mFgsManagerController;
            Objects.requireNonNull(fgsManagerController);
            synchronized (fgsManagerController.lock) {
                fgsManagerController.onDialogDismissedListeners.add(this);
            }
            FgsManagerController fgsManagerController2 = this.mFgsManagerController;
            Objects.requireNonNull(fgsManagerController2);
            synchronized (fgsManagerController2.lock) {
                fgsManagerController2.onNumberOfPackagesChangedListeners.add(this);
            }
            this.mNumPackages = this.mFgsManagerController.getNumRunningPackages();
            this.mExecutor.execute(new QSFgsManagerFooter$$ExternalSyntheticLambda0(this, 0));
            return;
        }
        FgsManagerController fgsManagerController3 = this.mFgsManagerController;
        Objects.requireNonNull(fgsManagerController3);
        synchronized (fgsManagerController3.lock) {
            fgsManagerController3.onDialogDismissedListeners.remove(this);
        }
        FgsManagerController fgsManagerController4 = this.mFgsManagerController;
        Objects.requireNonNull(fgsManagerController4);
        synchronized (fgsManagerController4.lock) {
            fgsManagerController4.onNumberOfPackagesChangedListeners.remove(this);
        }
    }

    public QSFgsManagerFooter(View view, Executor executor, Executor executor2, FgsManagerController fgsManagerController) {
        this.mRootView = view;
        this.mFooterText = (TextView) view.findViewById(2131427986);
        this.mTextContainer = view.findViewById(2131427968);
        this.mNumberContainer = view.findViewById(2131427967);
        this.mNumberView = (TextView) view.findViewById(2131427966);
        this.mDotView = (ImageView) view.findViewById(2131427965);
        this.mContext = view.getContext();
        this.mMainExecutor = executor;
        this.mExecutor = executor2;
        this.mFgsManagerController = fgsManagerController;
    }
}
