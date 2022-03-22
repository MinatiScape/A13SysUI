package com.android.wm.shell.compatui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceControlViewHost;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.window.TaskAppearedInfo;
import com.android.internal.util.FrameworkStatsLog;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.compatui.CompatUIController;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class CompatUILayout extends LinearLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public CompatUIWindowManager mWindowManager;

    public CompatUILayout(Context context) {
        this(context, null);
    }

    public final void updateCameraTreatmentButton(int i) {
        int i2;
        int i3;
        if (i == 1) {
            i2 = 2131231653;
        } else {
            i2 = 2131231651;
        }
        if (i == 1) {
            i3 = 2131952089;
        } else {
            i3 = 2131952088;
        }
        ImageButton imageButton = (ImageButton) findViewById(2131427658);
        imageButton.setImageResource(i2);
        imageButton.setContentDescription(getResources().getString(i3));
        ((TextView) ((LinearLayout) findViewById(2131427657)).findViewById(2131427724)).setText(i3);
    }

    public CompatUILayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CompatUILayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        ImageButton imageButton = (ImageButton) findViewById(2131428868);
        imageButton.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.compatui.CompatUILayout$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TaskAppearedInfo taskAppearedInfo;
                CompatUILayout compatUILayout = CompatUILayout.this;
                int i = CompatUILayout.$r8$clinit;
                Objects.requireNonNull(compatUILayout);
                CompatUIWindowManager compatUIWindowManager = compatUILayout.mWindowManager;
                Objects.requireNonNull(compatUIWindowManager);
                CompatUIController.CompatUICallback compatUICallback = compatUIWindowManager.mCallback;
                int i2 = compatUIWindowManager.mTaskId;
                ShellTaskOrganizer shellTaskOrganizer = (ShellTaskOrganizer) compatUICallback;
                Objects.requireNonNull(shellTaskOrganizer);
                synchronized (shellTaskOrganizer.mLock) {
                    taskAppearedInfo = shellTaskOrganizer.mTasks.get(i2);
                }
                if (taskAppearedInfo != null) {
                    ActivityInfo activityInfo = taskAppearedInfo.getTaskInfo().topActivityInfo;
                    if (activityInfo != null) {
                        FrameworkStatsLog.write(387, activityInfo.applicationInfo.uid, 2);
                    }
                    shellTaskOrganizer.restartTaskTopActivityProcessIfVisible(taskAppearedInfo.getTaskInfo().token);
                }
            }
        });
        imageButton.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.wm.shell.compatui.CompatUILayout$$ExternalSyntheticLambda5
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                CompatUILayout compatUILayout = CompatUILayout.this;
                int i = CompatUILayout.$r8$clinit;
                Objects.requireNonNull(compatUILayout);
                CompatUIWindowManager compatUIWindowManager = compatUILayout.mWindowManager;
                Objects.requireNonNull(compatUIWindowManager);
                CompatUILayout compatUILayout2 = compatUIWindowManager.mLayout;
                if (compatUILayout2 != null) {
                    compatUILayout2.setViewVisibility(2131428867, true);
                }
                return true;
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(2131428867);
        ((TextView) linearLayout.findViewById(2131427724)).setText(2131953158);
        linearLayout.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.compatui.CompatUILayout$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CompatUILayout compatUILayout = CompatUILayout.this;
                int i = CompatUILayout.$r8$clinit;
                Objects.requireNonNull(compatUILayout);
                compatUILayout.setViewVisibility(2131428867, false);
            }
        });
        ImageButton imageButton2 = (ImageButton) findViewById(2131427658);
        imageButton2.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.compatui.CompatUILayout$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TaskAppearedInfo taskAppearedInfo;
                CompatUILayout compatUILayout = CompatUILayout.this;
                int i = CompatUILayout.$r8$clinit;
                Objects.requireNonNull(compatUILayout);
                CompatUIWindowManager compatUIWindowManager = compatUILayout.mWindowManager;
                Objects.requireNonNull(compatUIWindowManager);
                if (!compatUIWindowManager.shouldShowCameraControl()) {
                    Log.w("CompatUIWindowManager", "Camera compat shouldn't receive clicks in the hidden state.");
                    return;
                }
                int i2 = 1;
                if (compatUIWindowManager.mCameraCompatControlState == 1) {
                    i2 = 2;
                }
                compatUIWindowManager.mCameraCompatControlState = i2;
                CompatUIController.CompatUICallback compatUICallback = compatUIWindowManager.mCallback;
                int i3 = compatUIWindowManager.mTaskId;
                ShellTaskOrganizer shellTaskOrganizer = (ShellTaskOrganizer) compatUICallback;
                Objects.requireNonNull(shellTaskOrganizer);
                synchronized (shellTaskOrganizer.mLock) {
                    taskAppearedInfo = shellTaskOrganizer.mTasks.get(i3);
                }
                if (taskAppearedInfo != null) {
                    shellTaskOrganizer.updateCameraCompatControlState(taskAppearedInfo.getTaskInfo().token, i2);
                }
                compatUIWindowManager.mLayout.updateCameraTreatmentButton(compatUIWindowManager.mCameraCompatControlState);
            }
        });
        imageButton2.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.wm.shell.compatui.CompatUILayout$$ExternalSyntheticLambda6
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                CompatUILayout compatUILayout = CompatUILayout.this;
                int i = CompatUILayout.$r8$clinit;
                Objects.requireNonNull(compatUILayout);
                CompatUIWindowManager compatUIWindowManager = compatUILayout.mWindowManager;
                Objects.requireNonNull(compatUIWindowManager);
                CompatUILayout compatUILayout2 = compatUIWindowManager.mLayout;
                if (compatUILayout2 != null) {
                    compatUILayout2.setViewVisibility(2131427657, true);
                }
                return true;
            }
        });
        ImageButton imageButton3 = (ImageButton) findViewById(2131427656);
        imageButton3.setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.compatui.CompatUILayout$$ExternalSyntheticLambda3
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                TaskAppearedInfo taskAppearedInfo;
                CompatUILayout compatUILayout = CompatUILayout.this;
                int i = CompatUILayout.$r8$clinit;
                Objects.requireNonNull(compatUILayout);
                CompatUIWindowManager compatUIWindowManager = compatUILayout.mWindowManager;
                Objects.requireNonNull(compatUIWindowManager);
                if (!compatUIWindowManager.shouldShowCameraControl()) {
                    Log.w("CompatUIWindowManager", "Camera compat shouldn't receive clicks in the hidden state.");
                    return;
                }
                compatUIWindowManager.mCameraCompatControlState = 3;
                CompatUIController.CompatUICallback compatUICallback = compatUIWindowManager.mCallback;
                int i2 = compatUIWindowManager.mTaskId;
                ShellTaskOrganizer shellTaskOrganizer = (ShellTaskOrganizer) compatUICallback;
                Objects.requireNonNull(shellTaskOrganizer);
                synchronized (shellTaskOrganizer.mLock) {
                    taskAppearedInfo = shellTaskOrganizer.mTasks.get(i2);
                }
                if (taskAppearedInfo != null) {
                    shellTaskOrganizer.updateCameraCompatControlState(taskAppearedInfo.getTaskInfo().token, 3);
                }
                CompatUILayout compatUILayout2 = compatUIWindowManager.mLayout;
                Objects.requireNonNull(compatUILayout2);
                compatUILayout2.setViewVisibility(2131427655, false);
                compatUILayout2.setViewVisibility(2131427657, false);
            }
        });
        imageButton3.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.wm.shell.compatui.CompatUILayout$$ExternalSyntheticLambda7
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                CompatUILayout compatUILayout = CompatUILayout.this;
                int i = CompatUILayout.$r8$clinit;
                Objects.requireNonNull(compatUILayout);
                CompatUIWindowManager compatUIWindowManager = compatUILayout.mWindowManager;
                Objects.requireNonNull(compatUIWindowManager);
                CompatUILayout compatUILayout2 = compatUIWindowManager.mLayout;
                if (compatUILayout2 != null) {
                    compatUILayout2.setViewVisibility(2131427657, true);
                }
                return true;
            }
        });
        ((LinearLayout) findViewById(2131427657)).setOnClickListener(new View.OnClickListener() { // from class: com.android.wm.shell.compatui.CompatUILayout$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                CompatUILayout compatUILayout = CompatUILayout.this;
                int i = CompatUILayout.$r8$clinit;
                Objects.requireNonNull(compatUILayout);
                compatUILayout.setViewVisibility(2131427657, false);
            }
        });
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        CompatUIWindowManager compatUIWindowManager = this.mWindowManager;
        Objects.requireNonNull(compatUIWindowManager);
        WindowManager.LayoutParams windowLayoutParams = compatUIWindowManager.getWindowLayoutParams();
        SurfaceControlViewHost surfaceControlViewHost = compatUIWindowManager.mViewHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.relayout(windowLayoutParams);
            compatUIWindowManager.updateSurfacePosition();
        }
    }

    public final void setViewVisibility(int i, boolean z) {
        int i2;
        View findViewById = findViewById(i);
        if (z) {
            i2 = 0;
        } else {
            i2 = 8;
        }
        if (findViewById.getVisibility() != i2) {
            findViewById.setVisibility(i2);
        }
    }

    public CompatUILayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
