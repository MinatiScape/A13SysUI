package com.android.systemui.wmshell;

import android.content.Context;
import android.content.res.Configuration;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.os.ParcelFileDescriptor;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.CoreStartable;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.model.SysUiState;
import com.android.systemui.shared.tracing.FrameProtoTracer;
import com.android.systemui.shared.tracing.ProtoTraceable;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.SystemUiTraceEntryProto;
import com.android.systemui.tracing.nano.SystemUiTraceFileProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.android.wm.shell.ShellCommandHandler;
import com.android.wm.shell.ShellInitImpl$$ExternalSyntheticLambda0;
import com.android.wm.shell.compatui.CompatUI;
import com.android.wm.shell.draganddrop.DragAndDrop;
import com.android.wm.shell.hidedisplaycutout.HideDisplayCutout;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.nano.WmShellTraceProto;
import com.android.wm.shell.onehanded.OneHanded;
import com.android.wm.shell.onehanded.OneHandedEventCallback;
import com.android.wm.shell.onehanded.OneHandedTransitionCallback;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.google.protobuf.nano.MessageNano;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class WMShell extends CoreStartable implements CommandQueue.Callbacks, ProtoTraceable<SystemUiTraceProto> {
    public final CommandQueue mCommandQueue;
    public AnonymousClass15 mCompatUIKeyguardCallback;
    public final Optional<CompatUI> mCompatUIOptional;
    public final ConfigurationController mConfigurationController;
    public final Optional<DragAndDrop> mDragAndDropOptional;
    public final Optional<HideDisplayCutout> mHideDisplayCutoutOptional;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public AnonymousClass4 mLegacySplitScreenKeyguardCallback;
    public final Optional<LegacySplitScreen> mLegacySplitScreenOptional;
    public AnonymousClass9 mOneHandedKeyguardCallback;
    public final Optional<OneHanded> mOneHandedOptional;
    public AnonymousClass2 mPipKeyguardCallback;
    public final Optional<Pip> mPipOptional;
    public final ProtoTracer mProtoTracer;
    public final ScreenLifecycle mScreenLifecycle;
    public final Optional<ShellCommandHandler> mShellCommandHandler;
    public AnonymousClass5 mSplitScreenKeyguardCallback;
    public final Optional<SplitScreen> mSplitScreenOptional;
    public final Executor mSysUiMainExecutor;
    public final SysUiState mSysUiState;
    public final UserInfoController mUserInfoController;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public AnonymousClass10 mWakefulnessObserver;

    /* renamed from: com.android.systemui.wmshell.WMShell$7  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass7 implements OneHandedTransitionCallback {
        public AnonymousClass7() {
        }

        @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
        public final void onStartFinished(Rect rect) {
            WMShell.this.mSysUiMainExecutor.execute(new WMShell$7$$ExternalSyntheticLambda0(this, 0));
        }

        @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
        public final void onStartTransition() {
            WMShell.this.mSysUiMainExecutor.execute(new WMShell$7$$ExternalSyntheticLambda2(this, 0));
        }

        @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
        public final void onStopFinished(Rect rect) {
            WMShell.this.mSysUiMainExecutor.execute(new WMShell$7$$ExternalSyntheticLambda1(this, 0));
        }
    }

    /* renamed from: com.android.systemui.wmshell.WMShell$8  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass8 implements OneHandedEventCallback {
        public AnonymousClass8() {
        }
    }

    public WMShell(Context context, Optional optional, Optional optional2, Optional optional3, Optional optional4, Optional optional5, Optional optional6, Optional optional7, Optional optional8, CommandQueue commandQueue, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, ScreenLifecycle screenLifecycle, SysUiState sysUiState, ProtoTracer protoTracer, WakefulnessLifecycle wakefulnessLifecycle, UserInfoController userInfoController, Executor executor) {
        super(context);
        this.mCommandQueue = commandQueue;
        this.mConfigurationController = configurationController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mScreenLifecycle = screenLifecycle;
        this.mSysUiState = sysUiState;
        this.mPipOptional = optional;
        this.mLegacySplitScreenOptional = optional2;
        this.mSplitScreenOptional = optional3;
        this.mOneHandedOptional = optional4;
        this.mHideDisplayCutoutOptional = optional5;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mProtoTracer = protoTracer;
        this.mShellCommandHandler = optional6;
        this.mCompatUIOptional = optional7;
        this.mDragAndDropOptional = optional8;
        this.mUserInfoController = userInfoController;
        this.mSysUiMainExecutor = executor;
    }

    @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if ((!this.mShellCommandHandler.isPresent() || !this.mShellCommandHandler.get().handleCommand(strArr, printWriter)) && !handleLoggingCommand(strArr, printWriter)) {
            this.mShellCommandHandler.ifPresent(new WMShell$$ExternalSyntheticLambda7(printWriter, 0));
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void handleWindowManagerLoggingCommand(String[] strArr, ParcelFileDescriptor parcelFileDescriptor) {
        PrintWriter printWriter = new PrintWriter(new ParcelFileDescriptor.AutoCloseOutputStream(parcelFileDescriptor));
        handleLoggingCommand(strArr, printWriter);
        printWriter.flush();
        printWriter.close();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.wmshell.WMShell$15, com.android.keyguard.KeyguardUpdateMonitorCallback] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void initCompatUi(final com.android.wm.shell.compatui.CompatUI r2) {
        /*
            r1 = this;
            com.android.systemui.wmshell.WMShell$15 r0 = new com.android.systemui.wmshell.WMShell$15
            r0.<init>()
            r1.mCompatUIKeyguardCallback = r0
            com.android.keyguard.KeyguardUpdateMonitor r1 = r1.mKeyguardUpdateMonitor
            r1.registerCallback(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.wmshell.WMShell.initCompatUi(com.android.wm.shell.compatui.CompatUI):void");
    }

    @VisibleForTesting
    public void initHideDisplayCutout(final HideDisplayCutout hideDisplayCutout) {
        this.mConfigurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.wmshell.WMShell.14
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onConfigChanged(Configuration configuration) {
                HideDisplayCutout.this.onConfigurationChanged(configuration);
            }
        });
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.android.systemui.wmshell.WMShell$4] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void initLegacySplitScreen(final com.android.wm.shell.legacysplitscreen.LegacySplitScreen r2) {
        /*
            r1 = this;
            com.android.systemui.wmshell.WMShell$4 r0 = new com.android.systemui.wmshell.WMShell$4
            r0.<init>()
            r1.mLegacySplitScreenKeyguardCallback = r0
            com.android.keyguard.KeyguardUpdateMonitor r1 = r1.mKeyguardUpdateMonitor
            r1.registerCallback(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.wmshell.WMShell.initLegacySplitScreen(com.android.wm.shell.legacysplitscreen.LegacySplitScreen):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.wmshell.WMShell$9, com.android.keyguard.KeyguardUpdateMonitorCallback] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.wmshell.WMShell$10, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 2 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void initOneHanded(final com.android.wm.shell.onehanded.OneHanded r3) {
        /*
            r2 = this;
            com.android.systemui.wmshell.WMShell$7 r0 = new com.android.systemui.wmshell.WMShell$7
            r0.<init>()
            r3.registerTransitionCallback(r0)
            com.android.systemui.wmshell.WMShell$8 r0 = new com.android.systemui.wmshell.WMShell$8
            r0.<init>()
            r3.registerEventCallback(r0)
            com.android.systemui.wmshell.WMShell$9 r0 = new com.android.systemui.wmshell.WMShell$9
            r0.<init>()
            r2.mOneHandedKeyguardCallback = r0
            com.android.keyguard.KeyguardUpdateMonitor r1 = r2.mKeyguardUpdateMonitor
            r1.registerCallback(r0)
            com.android.systemui.wmshell.WMShell$10 r0 = new com.android.systemui.wmshell.WMShell$10
            r0.<init>()
            r2.mWakefulnessObserver = r0
            com.android.systemui.keyguard.WakefulnessLifecycle r1 = r2.mWakefulnessLifecycle
            java.util.Objects.requireNonNull(r1)
            java.util.ArrayList<T> r1 = r1.mObservers
            r1.add(r0)
            com.android.systemui.keyguard.ScreenLifecycle r0 = r2.mScreenLifecycle
            com.android.systemui.wmshell.WMShell$11 r1 = new com.android.systemui.wmshell.WMShell$11
            r1.<init>()
            java.util.Objects.requireNonNull(r0)
            java.util.ArrayList<T> r0 = r0.mObservers
            r0.add(r1)
            com.android.systemui.statusbar.CommandQueue r0 = r2.mCommandQueue
            com.android.systemui.wmshell.WMShell$12 r1 = new com.android.systemui.wmshell.WMShell$12
            r1.<init>()
            r0.addCallback(r1)
            com.android.systemui.statusbar.policy.ConfigurationController r2 = r2.mConfigurationController
            com.android.systemui.wmshell.WMShell$13 r0 = new com.android.systemui.wmshell.WMShell$13
            r0.<init>()
            r2.addCallback(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.wmshell.WMShell.initOneHanded(com.android.wm.shell.onehanded.OneHanded):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.android.systemui.wmshell.WMShell$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void initPip(final com.android.wm.shell.pip.Pip r4) {
        /*
            r3 = this;
            com.android.systemui.statusbar.CommandQueue r0 = r3.mCommandQueue
            com.android.systemui.wmshell.WMShell$1 r1 = new com.android.systemui.wmshell.WMShell$1
            r1.<init>()
            r0.addCallback(r1)
            com.android.systemui.wmshell.WMShell$2 r0 = new com.android.systemui.wmshell.WMShell$2
            r0.<init>()
            r3.mPipKeyguardCallback = r0
            com.android.keyguard.KeyguardUpdateMonitor r1 = r3.mKeyguardUpdateMonitor
            r1.registerCallback(r0)
            com.android.systemui.model.SysUiState r0 = r3.mSysUiState
            com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda0 r1 = new com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda0
            r1.<init>()
            java.util.Objects.requireNonNull(r0)
            java.util.ArrayList r2 = r0.mCallbacks
            r2.add(r1)
            int r0 = r0.mFlags
            r1.onSystemUiStateChanged(r0)
            com.android.systemui.statusbar.policy.ConfigurationController r0 = r3.mConfigurationController
            com.android.systemui.wmshell.WMShell$3 r1 = new com.android.systemui.wmshell.WMShell$3
            r1.<init>()
            r0.addCallback(r1)
            com.android.systemui.statusbar.policy.UserInfoController r3 = r3.mUserInfoController
            com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda1 r0 = new com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda1
            r0.<init>()
            r3.addCallback(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.wmshell.WMShell.initPip(com.android.wm.shell.pip.Pip):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.wmshell.WMShell$5, com.android.keyguard.KeyguardUpdateMonitorCallback] */
    /* JADX WARN: Unknown variable types count: 1 */
    @com.android.internal.annotations.VisibleForTesting
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void initSplitScreen(final com.android.wm.shell.splitscreen.SplitScreen r3) {
        /*
            r2 = this;
            com.android.systemui.wmshell.WMShell$5 r0 = new com.android.systemui.wmshell.WMShell$5
            r0.<init>()
            r2.mSplitScreenKeyguardCallback = r0
            com.android.keyguard.KeyguardUpdateMonitor r1 = r2.mKeyguardUpdateMonitor
            r1.registerCallback(r0)
            com.android.systemui.keyguard.WakefulnessLifecycle r2 = r2.mWakefulnessLifecycle
            com.android.systemui.wmshell.WMShell$6 r0 = new com.android.systemui.wmshell.WMShell$6
            r0.<init>()
            java.util.Objects.requireNonNull(r2)
            java.util.ArrayList<T> r2 = r2.mObservers
            r2.add(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.wmshell.WMShell.initSplitScreen(com.android.wm.shell.splitscreen.SplitScreen):void");
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        ProtoTracer protoTracer = this.mProtoTracer;
        Objects.requireNonNull(protoTracer);
        FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> frameProtoTracer = protoTracer.mProtoTracer;
        Objects.requireNonNull(frameProtoTracer);
        synchronized (frameProtoTracer.mLock) {
            frameProtoTracer.mTraceables.add(this);
        }
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mPipOptional.ifPresent(new WMShell$$ExternalSyntheticLambda6(this, 0));
        this.mLegacySplitScreenOptional.ifPresent(new WMShell$$ExternalSyntheticLambda3(this, 0));
        this.mSplitScreenOptional.ifPresent(new WMShell$$ExternalSyntheticLambda2(this, 0));
        this.mOneHandedOptional.ifPresent(new ShellInitImpl$$ExternalSyntheticLambda0(this, 2));
        this.mHideDisplayCutoutOptional.ifPresent(new WMShell$$ExternalSyntheticLambda4(this, 0));
        this.mCompatUIOptional.ifPresent(new Consumer() { // from class: com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda8
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                WMShell.this.initCompatUi((CompatUI) obj);
            }
        });
        this.mDragAndDropOptional.ifPresent(new WMShell$$ExternalSyntheticLambda5(this, 0));
    }

    @Override // com.android.systemui.shared.tracing.ProtoTraceable
    public final void writeToProto(SystemUiTraceProto systemUiTraceProto) {
        if (systemUiTraceProto.wmShell == null) {
            systemUiTraceProto.wmShell = new WmShellTraceProto();
        }
    }

    public static boolean handleLoggingCommand(String[] strArr, PrintWriter printWriter) {
        ShellProtoLogImpl singleInstance = ShellProtoLogImpl.getSingleInstance();
        for (int i = 0; i < strArr.length; i++) {
            String str = strArr[i];
            Objects.requireNonNull(str);
            if (str.equals("enable-text")) {
                String[] strArr2 = (String[]) Arrays.copyOfRange(strArr, i + 1, strArr.length);
                if (singleInstance.startTextLogging(strArr2, printWriter) == 0) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Starting logging on groups: ");
                    m.append(Arrays.toString(strArr2));
                    printWriter.println(m.toString());
                }
                return true;
            } else if (str.equals("disable-text")) {
                String[] strArr3 = (String[]) Arrays.copyOfRange(strArr, i + 1, strArr.length);
                if (singleInstance.stopTextLogging(strArr3, printWriter) == 0) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Stopping logging on groups: ");
                    m2.append(Arrays.toString(strArr3));
                    printWriter.println(m2.toString());
                }
                return true;
            }
        }
        return false;
    }
}
