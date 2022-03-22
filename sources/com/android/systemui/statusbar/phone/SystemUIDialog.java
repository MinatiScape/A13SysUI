package com.android.systemui.statusbar.phone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Insets;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import com.android.systemui.Dependency;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class SystemUIDialog extends AlertDialog implements ViewRootImpl.ConfigChangedCallback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final Context mContext;
    public final SystemUIDialogManager mDialogManager;
    public final DismissReceiver mDismissReceiver;
    public final Handler mHandler;
    public int mLastConfigurationHeightDp;
    public int mLastConfigurationWidthDp;
    public int mLastHeight;
    public int mLastWidth;
    public ArrayList mOnCreateRunnables;

    /* loaded from: classes.dex */
    public static class DismissReceiver extends BroadcastReceiver {
        public static final IntentFilter INTENT_FILTER;
        public final BroadcastDispatcher mBroadcastDispatcher = (BroadcastDispatcher) Dependency.get(BroadcastDispatcher.class);
        public final Dialog mDialog;
        public boolean mRegistered;

        static {
            IntentFilter intentFilter = new IntentFilter();
            INTENT_FILTER = intentFilter;
            intentFilter.addAction("android.intent.action.CLOSE_SYSTEM_DIALOGS");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
        }

        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            this.mDialog.dismiss();
        }

        public DismissReceiver(Dialog dialog) {
            this.mDialog = dialog;
        }
    }

    public SystemUIDialog(Context context) {
        this(context, 2132018183);
    }

    public static int calculateDialogWidthWithInsets(Dialog dialog, int i) {
        return Math.round(TypedValue.applyDimension(1, i, dialog.getContext().getResources().getDisplayMetrics()) + getHorizontalInsets(dialog));
    }

    public int getHeight() {
        return -2;
    }

    public void setNegativeButton(int i, DialogInterface.OnClickListener onClickListener) {
        setButton(-2, i, onClickListener, true);
    }

    public void setPositiveButton(int i, DialogInterface.OnClickListener onClickListener) {
        setButton(-1, i, onClickListener, true);
    }

    public SystemUIDialog(Context context, int i) {
        this(context, i, true, null);
    }

    public static void registerDismissListener(Dialog dialog) {
        final DismissReceiver dismissReceiver = new DismissReceiver(dialog);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.statusbar.phone.SystemUIDialog$$ExternalSyntheticLambda0
            public final /* synthetic */ Runnable f$1 = null;

            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                SystemUIDialog.DismissReceiver dismissReceiver2 = SystemUIDialog.DismissReceiver.this;
                Runnable runnable = this.f$1;
                Objects.requireNonNull(dismissReceiver2);
                if (dismissReceiver2.mRegistered) {
                    dismissReceiver2.mBroadcastDispatcher.unregisterReceiver(dismissReceiver2);
                    dismissReceiver2.mRegistered = false;
                }
                if (runnable != null) {
                    runnable.run();
                }
            }
        });
        dismissReceiver.mBroadcastDispatcher.registerReceiver(dismissReceiver, DismissReceiver.INTENT_FILTER, null, UserHandle.CURRENT);
        dismissReceiver.mRegistered = true;
    }

    public final void onConfigurationChanged(Configuration configuration) {
        int i = this.mLastConfigurationWidthDp;
        int i2 = configuration.screenWidthDp;
        if (i != i2 || this.mLastConfigurationHeightDp != configuration.screenHeightDp) {
            this.mLastConfigurationWidthDp = i2;
            this.mLastConfigurationHeightDp = configuration.compatScreenWidthDp;
            updateWindowSize();
        }
    }

    public final void setButton(final int i, int i2, final DialogInterface.OnClickListener onClickListener, boolean z) {
        if (z) {
            setButton(i, this.mContext.getString(i2), onClickListener);
            return;
        }
        setButton(i, this.mContext.getString(i2), (DialogInterface.OnClickListener) null);
        this.mOnCreateRunnables.add(new Runnable() { // from class: com.android.systemui.statusbar.phone.SystemUIDialog$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                final SystemUIDialog systemUIDialog = SystemUIDialog.this;
                final int i3 = i;
                final DialogInterface.OnClickListener onClickListener2 = onClickListener;
                Objects.requireNonNull(systemUIDialog);
                systemUIDialog.getButton(i3).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.phone.SystemUIDialog$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        SystemUIDialog systemUIDialog2 = SystemUIDialog.this;
                        DialogInterface.OnClickListener onClickListener3 = onClickListener2;
                        int i4 = i3;
                        Objects.requireNonNull(systemUIDialog2);
                        onClickListener3.onClick(systemUIDialog2, i4);
                    }
                });
            }
        });
    }

    public final void setMessage(int i) {
        setMessage(this.mContext.getString(i));
    }

    public SystemUIDialog(Context context, int i, boolean z, SystemUIDialogManager systemUIDialogManager) {
        super(context, i);
        this.mHandler = new Handler();
        this.mLastWidth = Integer.MIN_VALUE;
        this.mLastHeight = Integer.MIN_VALUE;
        this.mLastConfigurationWidthDp = -1;
        this.mLastConfigurationHeightDp = -1;
        this.mOnCreateRunnables = new ArrayList();
        this.mContext = context;
        applyFlags(this);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.setTitle(getClass().getSimpleName());
        getWindow().setAttributes(attributes);
        this.mDismissReceiver = z ? new DismissReceiver(this) : null;
        this.mDialogManager = systemUIDialogManager;
    }

    public static AlertDialog applyFlags(AlertDialog alertDialog) {
        Window window = alertDialog.getWindow();
        window.setType(2017);
        window.addFlags(655360);
        window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
        return alertDialog;
    }

    public static int getDefaultDialogWidth(Dialog dialog) {
        Context context = dialog.getContext();
        int i = SystemProperties.getInt("persist.systemui.flag_tablet_dialog_width", 0);
        if (i == -1) {
            return calculateDialogWidthWithInsets(dialog, 624);
        }
        if (i == -2) {
            return calculateDialogWidthWithInsets(dialog, 348);
        }
        if (i > 0) {
            return calculateDialogWidthWithInsets(dialog, i);
        }
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(2131165874);
        if (dimensionPixelSize > 0) {
            return dimensionPixelSize + getHorizontalInsets(dialog);
        }
        return dimensionPixelSize;
    }

    public static int getHorizontalInsets(Dialog dialog) {
        Insets insets;
        if (dialog.getWindow().getDecorView() == null) {
            return 0;
        }
        Drawable background = dialog.getWindow().getDecorView().getBackground();
        if (background != null) {
            insets = background.getOpticalInsets();
        } else {
            insets = Insets.NONE;
        }
        return insets.left + insets.right;
    }

    public static void setShowForAllUsers(Dialog dialog) {
        dialog.getWindow().getAttributes().privateFlags |= 16;
    }

    public static void setWindowOnTop(Dialog dialog, boolean z) {
        Window window = dialog.getWindow();
        window.setType(2017);
        if (z) {
            window.getAttributes().setFitInsetsTypes(window.getAttributes().getFitInsetsTypes() & (~WindowInsets.Type.statusBars()));
        }
    }

    public int getWidth() {
        return getDefaultDialogWidth(this);
    }

    @Override // android.app.AlertDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Configuration configuration = getContext().getResources().getConfiguration();
        this.mLastConfigurationWidthDp = configuration.screenWidthDp;
        this.mLastConfigurationHeightDp = configuration.screenHeightDp;
        updateWindowSize();
        for (int i = 0; i < this.mOnCreateRunnables.size(); i++) {
            ((Runnable) this.mOnCreateRunnables.get(i)).run();
        }
    }

    @Override // android.app.Dialog
    public void onStart() {
        super.onStart();
        DismissReceiver dismissReceiver = this.mDismissReceiver;
        if (dismissReceiver != null) {
            dismissReceiver.mBroadcastDispatcher.registerReceiver(dismissReceiver, DismissReceiver.INTENT_FILTER, null, UserHandle.CURRENT);
            dismissReceiver.mRegistered = true;
        }
        SystemUIDialogManager systemUIDialogManager = this.mDialogManager;
        if (systemUIDialogManager != null) {
            systemUIDialogManager.setShowing(this, true);
        }
        ViewRootImpl.addConfigCallback(this);
    }

    @Override // android.app.Dialog
    public void onStop() {
        super.onStop();
        DismissReceiver dismissReceiver = this.mDismissReceiver;
        if (dismissReceiver != null && dismissReceiver.mRegistered) {
            dismissReceiver.mBroadcastDispatcher.unregisterReceiver(dismissReceiver);
            dismissReceiver.mRegistered = false;
        }
        SystemUIDialogManager systemUIDialogManager = this.mDialogManager;
        if (systemUIDialogManager != null) {
            systemUIDialogManager.setShowing(this, false);
        }
        ViewRootImpl.removeConfigCallback(this);
    }

    public final void updateWindowSize() {
        if (Looper.myLooper() != this.mHandler.getLooper()) {
            this.mHandler.post(new TaskView$$ExternalSyntheticLambda3(this, 5));
            return;
        }
        int width = getWidth();
        int height = getHeight();
        if (width != this.mLastWidth || height != this.mLastHeight) {
            this.mLastWidth = width;
            this.mLastHeight = height;
            getWindow().setLayout(width, height);
        }
    }
}
