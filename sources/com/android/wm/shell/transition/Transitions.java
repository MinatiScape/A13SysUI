package com.android.wm.shell.transition;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.view.SurfaceControl;
import android.window.ITransitionPlayer;
import android.window.RemoteTransition;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowOrganizer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController$$ExternalSyntheticLambda4;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda7;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.IShellTransitions;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Transitions implements RemoteCallable<Transitions> {
    public static final boolean ENABLE_SHELL_TRANSITIONS;
    public static final boolean SHELL_TRANSITIONS_ROTATION;
    public final ArrayList<ActiveTransition> mActiveTransitions;
    public final ShellExecutor mAnimExecutor;
    public final Context mContext;
    public final DisplayController mDisplayController;
    public final ArrayList<TransitionHandler> mHandlers;
    public final ShellTransitionImpl mImpl;
    public final ShellExecutor mMainExecutor;
    public final WindowOrganizer mOrganizer;
    public final TransitionPlayerImpl mPlayerImpl;
    public final RemoteTransitionHandler mRemoteTransitionHandler;
    public float mTransitionAnimationScaleSetting;

    /* loaded from: classes.dex */
    public static final class ActiveTransition {
        public boolean mAborted;
        public SurfaceControl.Transaction mFinishT;
        public TransitionHandler mHandler;
        public TransitionInfo mInfo;
        public boolean mMerged;
        public SurfaceControl.Transaction mStartT;
        public IBinder mToken;

        public ActiveTransition() {
        }

        public ActiveTransition(int i) {
        }
    }

    /* loaded from: classes.dex */
    public class SettingsObserver extends ContentObserver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public SettingsObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            super.onChange(z);
            Transitions transitions = Transitions.this;
            transitions.mTransitionAnimationScaleSetting = Settings.Global.getFloat(transitions.mContext.getContentResolver(), "transition_animation_scale", Transitions.this.mTransitionAnimationScaleSetting);
            Transitions.this.mMainExecutor.execute(new PipMenuView$$ExternalSyntheticLambda7(this, 5));
        }
    }

    /* loaded from: classes.dex */
    public class ShellTransitionImpl implements ShellTransitions {
        public IShellTransitionsImpl mIShellTransitions;

        public ShellTransitionImpl() {
        }

        @Override // com.android.wm.shell.transition.ShellTransitions
        public final IShellTransitions createExternalInterface() {
            IShellTransitionsImpl iShellTransitionsImpl = this.mIShellTransitions;
            if (iShellTransitionsImpl != null) {
                Objects.requireNonNull(iShellTransitionsImpl);
                iShellTransitionsImpl.mTransitions = null;
            }
            IShellTransitionsImpl iShellTransitionsImpl2 = new IShellTransitionsImpl(Transitions.this);
            this.mIShellTransitions = iShellTransitionsImpl2;
            return iShellTransitionsImpl2;
        }

        @Override // com.android.wm.shell.transition.ShellTransitions
        public final void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
            Transitions.this.mMainExecutor.execute(new Transitions$ShellTransitionImpl$$ExternalSyntheticLambda0(this, transitionFilter, remoteTransition, 0));
        }
    }

    /* loaded from: classes.dex */
    public interface TransitionFinishCallback {
        void onTransitionFinished(WindowContainerTransaction windowContainerTransaction);
    }

    /* loaded from: classes.dex */
    public interface TransitionHandler {
        WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo);

        default void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, Transitions$$ExternalSyntheticLambda0 transitions$$ExternalSyntheticLambda0) {
        }

        default void onTransitionMerged(IBinder iBinder) {
        }

        default void setAnimScaleSetting(float f) {
        }

        boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2, TransitionFinishCallback transitionFinishCallback);
    }

    /* loaded from: classes.dex */
    public class TransitionPlayerImpl extends ITransitionPlayer.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;

        public TransitionPlayerImpl() {
        }

        public final void onTransitionReady(final IBinder iBinder, final TransitionInfo transitionInfo, final SurfaceControl.Transaction transaction, final SurfaceControl.Transaction transaction2) throws RemoteException {
            Transitions.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Transitions.TransitionPlayerImpl transitionPlayerImpl = Transitions.TransitionPlayerImpl.this;
                    IBinder iBinder2 = iBinder;
                    TransitionInfo transitionInfo2 = transitionInfo;
                    SurfaceControl.Transaction transaction3 = transaction;
                    SurfaceControl.Transaction transaction4 = transaction2;
                    Objects.requireNonNull(transitionPlayerImpl);
                    Transitions.this.onTransitionReady(iBinder2, transitionInfo2, transaction3, transaction4);
                }
            });
        }

        public final void requestStartTransition(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) throws RemoteException {
            Transitions.this.mMainExecutor.execute(new ClipboardOverlayController$$ExternalSyntheticLambda4(this, iBinder, transitionRequestInfo, 1));
        }
    }

    public Transitions(ShellTaskOrganizer shellTaskOrganizer, TransactionPool transactionPool, DisplayController displayController, Context context, ShellExecutor shellExecutor, Handler handler, ShellExecutor shellExecutor2) {
        this.mImpl = new ShellTransitionImpl();
        ArrayList<TransitionHandler> arrayList = new ArrayList<>();
        this.mHandlers = arrayList;
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mActiveTransitions = new ArrayList<>();
        this.mOrganizer = shellTaskOrganizer;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mAnimExecutor = shellExecutor2;
        this.mDisplayController = displayController;
        this.mPlayerImpl = new TransitionPlayerImpl();
        arrayList.add(new DefaultTransitionHandler(displayController, transactionPool, context, shellExecutor, handler, shellExecutor2));
        RemoteTransitionHandler remoteTransitionHandler = new RemoteTransitionHandler(shellExecutor);
        this.mRemoteTransitionHandler = remoteTransitionHandler;
        arrayList.add(remoteTransitionHandler);
        ContentResolver contentResolver = context.getContentResolver();
        float f = Settings.Global.getFloat(contentResolver, "transition_animation_scale", context.getResources().getFloat(17105061));
        this.mTransitionAnimationScaleSetting = f;
        int size = arrayList.size();
        while (true) {
            size--;
            if (size >= 0) {
                this.mHandlers.get(size).setAnimScaleSetting(f);
            } else {
                contentResolver.registerContentObserver(Settings.Global.getUriFor("transition_animation_scale"), false, new SettingsObserver());
                return;
            }
        }
    }

    public static boolean isClosingType(int i) {
        return i == 2 || i == 4;
    }

    public static boolean isOpeningType(int i) {
        return i == 1 || i == 3 || i == 7;
    }

    /* loaded from: classes.dex */
    public static class IShellTransitionsImpl extends IShellTransitions.Stub {
        public Transitions mTransitions;

        public IShellTransitionsImpl(Transitions transitions) {
            this.mTransitions = transitions;
        }
    }

    static {
        boolean z = false;
        boolean z2 = SystemProperties.getBoolean("persist.debug.shell_transit", false);
        ENABLE_SHELL_TRANSITIONS = z2;
        if (z2 && SystemProperties.getBoolean("persist.debug.shell_transit_rotate", false)) {
            z = true;
        }
        SHELL_TRANSITIONS_ROTATION = z;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r11v0, types: [com.android.wm.shell.transition.Transitions$$ExternalSyntheticLambda0] */
    public final void attemptMergeTransition(ActiveTransition activeTransition, final ActiveTransition activeTransition2) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 313857748, 0, "Transition %s ready while another transition %s is still animating. Notify the animating transition in case they can be merged", String.valueOf(activeTransition2.mToken), String.valueOf(activeTransition.mToken));
        }
        activeTransition.mHandler.mergeAnimation(activeTransition2.mToken, activeTransition2.mInfo, activeTransition2.mStartT, activeTransition.mToken, new TransitionFinishCallback() { // from class: com.android.wm.shell.transition.Transitions$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
            public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
                Transitions transitions = Transitions.this;
                Transitions.ActiveTransition activeTransition3 = activeTransition2;
                Objects.requireNonNull(transitions);
                transitions.onFinish(activeTransition3.mToken, windowContainerTransaction, false);
            }
        });
    }

    public final int findActiveTransition(IBinder iBinder) {
        for (int size = this.mActiveTransitions.size() - 1; size >= 0; size--) {
            if (this.mActiveTransitions.get(size).mToken == iBinder) {
                return size;
            }
        }
        return -1;
    }

    @VisibleForTesting
    public void onTransitionReady(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        char c;
        int i;
        boolean z;
        char c2 = 2;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1070270131, 0, "onTransitionReady %s: %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
        }
        int findActiveTransition = findActiveTransition(iBinder);
        if (findActiveTransition < 0) {
            throw new IllegalStateException("Got transitionReady for non-active transition " + iBinder + ". expecting one of " + Arrays.toString(this.mActiveTransitions.stream().map(Transitions$$ExternalSyntheticLambda2.INSTANCE).toArray()));
        } else if (!transitionInfo.getRootLeash().isValid()) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 410592459, 0, "Invalid root leash (%s): %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
            }
            transaction.apply();
            onFinish(iBinder, null, true);
        } else {
            int size = transitionInfo.getChanges().size();
            if (size == 2) {
                int i2 = size - 1;
                boolean z2 = false;
                while (true) {
                    if (i2 < 0) {
                        z = true;
                        break;
                    }
                    TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(i2);
                    if (change.getTaskInfo() != null) {
                        z = false;
                        break;
                    }
                    if ((change.getFlags() & 8) != 0) {
                        z2 = true;
                    }
                    i2--;
                }
                if (z && z2) {
                    transaction.apply();
                    onFinish(iBinder, null, false);
                    return;
                }
            }
            ActiveTransition activeTransition = this.mActiveTransitions.get(findActiveTransition);
            activeTransition.mInfo = transitionInfo;
            activeTransition.mStartT = transaction;
            activeTransition.mFinishT = transaction2;
            boolean isOpeningType = isOpeningType(transitionInfo.getType());
            int size2 = transitionInfo.getChanges().size() - 1;
            while (size2 >= 0) {
                TransitionInfo.Change change2 = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
                SurfaceControl leash = change2.getLeash();
                int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size2)).getMode();
                if (TransitionInfo.isIndependent(change2, transitionInfo)) {
                    i = size2;
                    if (mode == 1 || mode == 3) {
                        transaction.show(leash);
                        c = 2;
                        transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
                        if (isOpeningType && (change2.getFlags() & 8) == 0) {
                            transaction.setAlpha(leash, 0.0f);
                            transaction2.setAlpha(leash, 1.0f);
                        }
                    } else {
                        if ((mode == 2 || mode == 4) && (change2.getFlags() & 2) == 0) {
                            transaction2.hide(leash);
                        }
                        c = 2;
                    }
                } else if (mode == 1 || mode == 3 || mode == 6) {
                    transaction.show(leash);
                    i = size2;
                    transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
                    transaction.setAlpha(leash, 1.0f);
                    transaction.setPosition(leash, change2.getEndRelOffset().x, change2.getEndRelOffset().y);
                    c = 2;
                } else {
                    i = size2;
                    c = c2;
                }
                size2 = i - 1;
                c2 = c;
            }
            if (findActiveTransition > 0) {
                attemptMergeTransition(this.mActiveTransitions.get(0), activeTransition);
            } else {
                playTransition(activeTransition);
            }
        }
    }

    public final void playTransition(ActiveTransition activeTransition) {
        TransitionInfo transitionInfo = activeTransition.mInfo;
        SurfaceControl.Transaction transaction = activeTransition.mStartT;
        boolean isOpeningType = isOpeningType(transitionInfo.getType());
        if (transitionInfo.getRootLeash().isValid()) {
            transaction.show(transitionInfo.getRootLeash());
        }
        int size = transitionInfo.getChanges().size();
        int size2 = transitionInfo.getChanges().size() - 1;
        while (true) {
            boolean z = false;
            if (size2 < 0) {
                break;
            }
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size2)).getMode();
            if (TransitionInfo.isIndependent(change, transitionInfo)) {
                if (change.getParent() != null) {
                    z = true;
                }
                if (!z) {
                    transaction.reparent(leash, transitionInfo.getRootLeash());
                    transaction.setPosition(leash, change.getStartAbsBounds().left - transitionInfo.getRootOffset().x, change.getStartAbsBounds().top - transitionInfo.getRootOffset().y);
                }
                if (mode == 1 || mode == 3) {
                    if (isOpeningType) {
                        transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                    } else {
                        transaction.setLayer(leash, size - size2);
                    }
                } else if (mode != 2 && mode != 4) {
                    transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                } else if (isOpeningType) {
                    transaction.setLayer(leash, size - size2);
                } else {
                    transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                }
            }
            size2--;
        }
        TransitionHandler transitionHandler = activeTransition.mHandler;
        if (transitionHandler != null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 138343607, 0, " try firstHandler %s", String.valueOf(transitionHandler));
            }
            if (activeTransition.mHandler.startAnimation(activeTransition.mToken, activeTransition.mInfo, activeTransition.mStartT, activeTransition.mFinishT, new Transitions$$ExternalSyntheticLambda1(this, activeTransition))) {
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 707170340, 0, " animated by firstHandler", null);
                    return;
                }
                return;
            }
        }
        for (int size3 = this.mHandlers.size() - 1; size3 >= 0; size3--) {
            if (this.mHandlers.get(size3) != activeTransition.mHandler) {
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1308483871, 0, " try handler %s", String.valueOf(this.mHandlers.get(size3)));
                }
                if (this.mHandlers.get(size3).startAnimation(activeTransition.mToken, activeTransition.mInfo, activeTransition.mStartT, activeTransition.mFinishT, new Transitions$$ExternalSyntheticLambda1(this, activeTransition))) {
                    if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1297259344, 0, " animated by %s", String.valueOf(this.mHandlers.get(size3)));
                    }
                    activeTransition.mHandler = this.mHandlers.get(size3);
                    return;
                }
            }
        }
        throw new IllegalStateException("This shouldn't happen, maybe the default handler is broken.");
    }

    @VisibleForTesting
    public void replaceDefaultHandlerForTest(TransitionHandler transitionHandler) {
        this.mHandlers.set(0, transitionHandler);
    }

    public final IBinder startTransition(int i, WindowContainerTransaction windowContainerTransaction, TransitionHandler transitionHandler) {
        ActiveTransition activeTransition = new ActiveTransition(0);
        activeTransition.mHandler = transitionHandler;
        activeTransition.mToken = this.mOrganizer.startTransition(i, (IBinder) null, windowContainerTransaction);
        this.mActiveTransitions.add(activeTransition);
        return activeTransition.mToken;
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x00c5, code lost:
        r11.apply();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onFinish(android.os.IBinder r9, android.window.WindowContainerTransaction r10, boolean r11) {
        /*
            Method dump skipped, instructions count: 407
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.transition.Transitions.onFinish(android.os.IBinder, android.window.WindowContainerTransaction, boolean):void");
    }

    public Transitions() {
        this.mImpl = new ShellTransitionImpl();
        this.mHandlers = new ArrayList<>();
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mActiveTransitions = new ArrayList<>();
        this.mOrganizer = null;
        this.mContext = null;
        this.mMainExecutor = null;
        this.mAnimExecutor = null;
        this.mDisplayController = null;
        this.mPlayerImpl = null;
        this.mRemoteTransitionHandler = null;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }
}
