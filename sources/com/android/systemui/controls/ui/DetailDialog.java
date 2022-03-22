package com.android.systemui.controls.ui;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.view.View;
import android.view.WindowInsets;
import com.android.wm.shell.TaskView;
import java.util.Objects;
/* compiled from: DetailDialog.kt */
/* loaded from: classes.dex */
public final class DetailDialog extends Dialog {
    public final Context activityContext;
    public final Intent fillInIntent;
    public final PendingIntent pendingIntent;
    public final DetailDialog$stateCallback$1 stateCallback;
    public final TaskView taskView;
    public final float taskWidthPercentWidth;
    public int detailTaskId = -1;
    public View taskViewContainer = requireViewById(2131427754);

    /* compiled from: DetailDialog.kt */
    /* renamed from: com.android.systemui.controls.ui.DetailDialog$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass5 implements View.OnApplyWindowInsetsListener {
        public static final AnonymousClass5 INSTANCE = new AnonymousClass5();

        @Override // android.view.View.OnApplyWindowInsetsListener
        public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            int paddingLeft = view.getPaddingLeft();
            int paddingRight = view.getPaddingRight();
            Insets insets = windowInsets.getInsets(WindowInsets.Type.systemBars());
            view.setPadding(paddingLeft, insets.top, paddingRight, insets.bottom);
            return WindowInsets.CONSUMED;
        }
    }

    /* JADX WARN: Type inference failed for: r2v4, types: [com.android.systemui.controls.ui.DetailDialog$stateCallback$1, com.android.wm.shell.TaskView$Listener] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public DetailDialog(android.content.Context r2, com.android.wm.shell.TaskView r3, android.app.PendingIntent r4, com.android.systemui.controls.ui.ControlViewHolder r5) {
        /*
            r1 = this;
            r0 = 2132018185(0x7f140409, float:1.967467E38)
            r1.<init>(r2, r0)
            r1.activityContext = r2
            r1.taskView = r3
            r1.pendingIntent = r4
            r4 = -1
            r1.detailTaskId = r4
            android.content.res.Resources r2 = r2.getResources()
            r4 = 2131165588(0x7f070194, float:1.7945397E38)
            float r2 = r2.getFloat(r4)
            r1.taskWidthPercentWidth = r2
            android.content.Intent r2 = new android.content.Intent
            r2.<init>()
            java.lang.String r4 = "controls.DISPLAY_IN_PANEL"
            r0 = 1
            r2.putExtra(r4, r0)
            r4 = 524288(0x80000, float:7.34684E-40)
            r2.addFlags(r4)
            r4 = 134217728(0x8000000, float:3.85186E-34)
            r2.addFlags(r4)
            r1.fillInIntent = r2
            com.android.systemui.controls.ui.DetailDialog$stateCallback$1 r2 = new com.android.systemui.controls.ui.DetailDialog$stateCallback$1
            r2.<init>()
            r1.stateCallback = r2
            android.view.Window r4 = r1.getWindow()
            r0 = 32
            r4.addFlags(r0)
            android.view.Window r4 = r1.getWindow()
            r0 = 536870912(0x20000000, float:1.0842022E-19)
            r4.addPrivateFlags(r0)
            r4 = 2131624045(0x7f0e006d, float:1.8875259E38)
            r1.setContentView(r4)
            r4 = 2131427754(0x7f0b01aa, float:1.8477133E38)
            android.view.View r4 = r1.requireViewById(r4)
            r1.taskViewContainer = r4
            r4 = 2131427756(0x7f0b01ac, float:1.8477137E38)
            android.view.View r4 = r1.requireViewById(r4)
            android.view.ViewGroup r4 = (android.view.ViewGroup) r4
            r4.addView(r3)
            r0 = 0
            r4.setAlpha(r0)
            r4 = 2131427751(0x7f0b01a7, float:1.8477127E38)
            android.view.View r4 = r1.requireViewById(r4)
            android.widget.ImageView r4 = (android.widget.ImageView) r4
            com.android.systemui.controls.ui.DetailDialog$2$1 r0 = new com.android.systemui.controls.ui.DetailDialog$2$1
            r0.<init>()
            r4.setOnClickListener(r0)
            r4 = 2131427753(0x7f0b01a9, float:1.8477131E38)
            android.view.View r4 = r1.requireViewById(r4)
            com.android.systemui.controls.ui.DetailDialog$3$1 r0 = new com.android.systemui.controls.ui.DetailDialog$3$1
            r0.<init>()
            r4.setOnClickListener(r0)
            r4 = 2131427752(0x7f0b01a8, float:1.847713E38)
            android.view.View r4 = r1.requireViewById(r4)
            android.widget.ImageView r4 = (android.widget.ImageView) r4
            com.android.systemui.controls.ui.DetailDialog$4$1 r0 = new com.android.systemui.controls.ui.DetailDialog$4$1
            r0.<init>()
            r4.setOnClickListener(r0)
            android.view.Window r4 = r1.getWindow()
            android.view.View r4 = r4.getDecorView()
            com.android.systemui.controls.ui.DetailDialog$5 r0 = com.android.systemui.controls.ui.DetailDialog.AnonymousClass5.INSTANCE
            r4.setOnApplyWindowInsetsListener(r0)
            android.content.Context r4 = r1.getContext()
            android.content.res.Resources r4 = r4.getResources()
            boolean r4 = com.android.internal.policy.ScreenDecorationsUtils.supportsRoundedCornersOnWindows(r4)
            if (r4 == 0) goto L_0x00ca
            android.content.Context r1 = r1.getContext()
            android.content.res.Resources r1 = r1.getResources()
            r4 = 2131165551(0x7f07016f, float:1.7945322E38)
            int r1 = r1.getDimensionPixelSize(r4)
            float r1 = (float) r1
            r3.setCornerRadius(r1)
        L_0x00ca:
            java.util.Objects.requireNonNull(r5)
            com.android.systemui.util.concurrency.DelayableExecutor r1 = r5.uiExecutor
            java.util.Objects.requireNonNull(r3)
            com.android.wm.shell.TaskView$Listener r4 = r3.mListener
            if (r4 != 0) goto L_0x00db
            r3.mListener = r2
            r3.mListenerExecutor = r1
            return
        L_0x00db:
            java.lang.IllegalStateException r1 = new java.lang.IllegalStateException
            java.lang.String r2 = "Trying to set a listener when one has already been set"
            r1.<init>(r2)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.controls.ui.DetailDialog.<init>(android.content.Context, com.android.wm.shell.TaskView, android.app.PendingIntent, com.android.systemui.controls.ui.ControlViewHolder):void");
    }

    @Override // android.app.Dialog, android.content.DialogInterface
    public final void dismiss() {
        if (isShowing()) {
            TaskView taskView = this.taskView;
            Objects.requireNonNull(taskView);
            taskView.performRelease();
            super.dismiss();
        }
    }
}
