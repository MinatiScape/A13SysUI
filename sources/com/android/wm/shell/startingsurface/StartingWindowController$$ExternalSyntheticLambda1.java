package com.android.wm.shell.startingsurface;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StartingWindowController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ StartingWindowController$$ExternalSyntheticLambda1(Object obj, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = i;
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x00a4  */
    @Override // java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void run() {
        /*
            r11 = this;
            int r0 = r11.$r8$classId
            r1 = 1
            r2 = 0
            switch(r0) {
                case 0: goto L_0x0009;
                default: goto L_0x0007;
            }
        L_0x0007:
            goto L_0x0079
        L_0x0009:
            java.lang.Object r0 = r11.f$0
            com.android.wm.shell.startingsurface.StartingWindowController r0 = (com.android.wm.shell.startingsurface.StartingWindowController) r0
            int r11 = r11.f$1
            java.util.Objects.requireNonNull(r0)
            com.android.wm.shell.startingsurface.StartingSurfaceDrawer r0 = r0.mStartingSurfaceDrawer
            java.util.Objects.requireNonNull(r0)
            android.util.SparseArray<com.android.wm.shell.startingsurface.StartingSurfaceDrawer$StartingWindowRecord> r3 = r0.mStartingWindowRecords
            java.lang.Object r3 = r3.get(r11)
            com.android.wm.shell.startingsurface.StartingSurfaceDrawer$StartingWindowRecord r3 = (com.android.wm.shell.startingsurface.StartingSurfaceDrawer.StartingWindowRecord) r3
            r4 = 0
            if (r3 == 0) goto L_0x0025
            android.window.SplashScreenView r3 = r3.mContentView
            goto L_0x0026
        L_0x0025:
            r3 = r4
        L_0x0026:
            if (r3 == 0) goto L_0x004d
            boolean r5 = r3.isCopyable()
            if (r5 == 0) goto L_0x004d
            android.window.SplashScreenView$SplashScreenViewParcelable r5 = new android.window.SplashScreenView$SplashScreenViewParcelable
            r5.<init>(r3)
            android.os.RemoteCallback r6 = new android.os.RemoteCallback
            com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda0 r7 = new com.android.wm.shell.startingsurface.StartingSurfaceDrawer$$ExternalSyntheticLambda0
            r7.<init>()
            r6.<init>(r7)
            r5.setClientCallback(r6)
            r3.onCopied()
            android.util.SparseArray<android.view.SurfaceControlViewHost> r0 = r0.mAnimatedSplashScreenSurfaceHosts
            android.view.SurfaceControlViewHost r3 = r3.getSurfaceHost()
            r0.append(r11, r3)
            goto L_0x004e
        L_0x004d:
            r5 = r4
        L_0x004e:
            boolean r0 = com.android.wm.shell.protolog.ShellProtoLogCache.WM_SHELL_STARTING_WINDOW_enabled
            if (r0 == 0) goto L_0x0071
            long r6 = (long) r11
            if (r5 == 0) goto L_0x0057
            r0 = r1
            goto L_0x0058
        L_0x0057:
            r0 = r2
        L_0x0058:
            com.android.wm.shell.protolog.ShellProtoLogGroup r3 = com.android.wm.shell.protolog.ShellProtoLogGroup.WM_SHELL_STARTING_WINDOW
            r8 = 807939633(0x30282e31, float:6.1183686E-10)
            r9 = 13
            r10 = 2
            java.lang.Object[] r10 = new java.lang.Object[r10]
            java.lang.Long r6 = java.lang.Long.valueOf(r6)
            r10[r2] = r6
            java.lang.Boolean r0 = java.lang.Boolean.valueOf(r0)
            r10[r1] = r0
            com.android.wm.shell.protolog.ShellProtoLogImpl.v(r3, r8, r9, r4, r10)
        L_0x0071:
            android.app.ActivityTaskManager r0 = android.app.ActivityTaskManager.getInstance()
            r0.onSplashScreenViewCopyFinished(r11, r5)
            return
        L_0x0079:
            java.lang.Object r0 = r11.f$0
            com.android.systemui.shared.rotation.RotationButtonController$1 r0 = (com.android.systemui.shared.rotation.RotationButtonController.AnonymousClass1) r0
            int r11 = r11.f$1
            int r3 = com.android.systemui.shared.rotation.RotationButtonController.AnonymousClass1.$r8$clinit
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.shared.rotation.RotationButtonController r3 = com.android.systemui.shared.rotation.RotationButtonController.this
            java.util.Objects.requireNonNull(r3)
            android.content.Context r3 = r3.mContext
            boolean r3 = com.android.internal.view.RotationPolicy.isRotationLocked(r3)
            if (r3 == 0) goto L_0x00b3
            com.android.systemui.shared.rotation.RotationButtonController r3 = com.android.systemui.shared.rotation.RotationButtonController.this
            java.util.Objects.requireNonNull(r3)
            boolean r4 = r3.mSkipOverrideUserLockPrefsOnce
            if (r4 == 0) goto L_0x009d
            r3.mSkipOverrideUserLockPrefsOnce = r2
            goto L_0x00a1
        L_0x009d:
            if (r11 != 0) goto L_0x00a1
            r3 = r1
            goto L_0x00a2
        L_0x00a1:
            r3 = r2
        L_0x00a2:
            if (r3 == 0) goto L_0x00ae
            com.android.systemui.shared.rotation.RotationButtonController r3 = com.android.systemui.shared.rotation.RotationButtonController.this
            java.util.Objects.requireNonNull(r3)
            android.content.Context r3 = r3.mContext
            com.android.internal.view.RotationPolicy.setRotationLockAtAngle(r3, r1, r11)
        L_0x00ae:
            com.android.systemui.shared.rotation.RotationButtonController r3 = com.android.systemui.shared.rotation.RotationButtonController.this
            r3.setRotateSuggestionButtonState(r2, r1)
        L_0x00b3:
            com.android.systemui.shared.rotation.RotationButtonController r0 = com.android.systemui.shared.rotation.RotationButtonController.this
            java.util.function.Consumer<java.lang.Integer> r0 = r0.mRotWatcherListener
            if (r0 == 0) goto L_0x00c0
            java.lang.Integer r11 = java.lang.Integer.valueOf(r11)
            r0.accept(r11)
        L_0x00c0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda1.run():void");
    }
}
