package com.android.systemui.clipboardoverlay;

import android.app.RemoteAction;
import android.content.ClipData;
import android.content.ComponentName;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.systemui.screenshot.OverlayActionChip;
import com.android.systemui.screenshot.OverlayActionChip$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda17;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ClipboardOverlayController$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ ClipboardOverlayController$$ExternalSyntheticLambda4(Object obj, Object obj2, Object obj3, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = obj3;
    }

    @Override // java.lang.Runnable
    public final void run() {
        WindowContainerTransaction windowContainerTransaction = null;
        switch (this.$r8$classId) {
            case 0:
                final ClipboardOverlayController clipboardOverlayController = (ClipboardOverlayController) this.f$0;
                final String str = (String) this.f$2;
                Objects.requireNonNull(clipboardOverlayController);
                ClipData.Item itemAt = ((ClipData) this.f$1).getItemAt(0);
                final ArrayList arrayList = new ArrayList();
                for (TextLinks.TextLink textLink : itemAt.getTextLinks().getLinks()) {
                    arrayList.addAll(clipboardOverlayController.mTextClassifier.classifyText(itemAt.getText(), textLink.getStart(), textLink.getEnd(), null).getActions());
                }
                clipboardOverlayController.mView.post(new Runnable() { // from class: com.android.systemui.clipboardoverlay.ClipboardOverlayController$$ExternalSyntheticLambda5
                    @Override // java.lang.Runnable
                    public final void run() {
                        ClipboardOverlayController clipboardOverlayController2 = ClipboardOverlayController.this;
                        ArrayList arrayList2 = arrayList;
                        String str2 = str;
                        Objects.requireNonNull(clipboardOverlayController2);
                        clipboardOverlayController2.resetActionChips();
                        Iterator it = arrayList2.iterator();
                        while (it.hasNext()) {
                            RemoteAction remoteAction = (RemoteAction) it.next();
                            ComponentName component = remoteAction.getActionIntent().getIntent().getComponent();
                            if (component != null && !TextUtils.equals(str2, component.getPackageName())) {
                                OverlayActionChip overlayActionChip = (OverlayActionChip) LayoutInflater.from(clipboardOverlayController2.mContext).inflate(2131624350, (ViewGroup) clipboardOverlayController2.mActionContainer, false);
                                overlayActionChip.setText(remoteAction.getTitle());
                                overlayActionChip.setContentDescription(remoteAction.getTitle());
                                overlayActionChip.setIcon(remoteAction.getIcon(), false);
                                overlayActionChip.setOnClickListener(new OverlayActionChip$$ExternalSyntheticLambda0(remoteAction.getActionIntent(), new BubbleStackView$$ExternalSyntheticLambda17(clipboardOverlayController2, 3)));
                                overlayActionChip.setAlpha(1.0f);
                                clipboardOverlayController2.mActionContainer.addView(overlayActionChip);
                                clipboardOverlayController2.mActionChips.add(overlayActionChip);
                            }
                        }
                    }
                });
                return;
            default:
                Transitions.TransitionPlayerImpl transitionPlayerImpl = (Transitions.TransitionPlayerImpl) this.f$0;
                IBinder iBinder = (IBinder) this.f$1;
                TransitionRequestInfo transitionRequestInfo = (TransitionRequestInfo) this.f$2;
                int i = Transitions.TransitionPlayerImpl.$r8$clinit;
                Objects.requireNonNull(transitionPlayerImpl);
                Transitions transitions = Transitions.this;
                Objects.requireNonNull(transitions);
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -2076257741, 0, "Transition requested: %s %s", String.valueOf(iBinder), String.valueOf(transitionRequestInfo));
                }
                if (transitions.findActiveTransition(iBinder) < 0) {
                    Transitions.ActiveTransition activeTransition = new Transitions.ActiveTransition(0);
                    int size = transitions.mHandlers.size() - 1;
                    while (true) {
                        if (size >= 0) {
                            windowContainerTransaction = transitions.mHandlers.get(size).handleRequest(iBinder, transitionRequestInfo);
                            if (windowContainerTransaction != null) {
                                activeTransition.mHandler = transitions.mHandlers.get(size);
                            } else {
                                size--;
                            }
                        }
                    }
                    if (transitionRequestInfo.getDisplayChange() != null) {
                        TransitionRequestInfo.DisplayChange displayChange = transitionRequestInfo.getDisplayChange();
                        if (displayChange.getEndRotation() != displayChange.getStartRotation()) {
                            if (windowContainerTransaction == null) {
                                windowContainerTransaction = new WindowContainerTransaction();
                            }
                            DisplayController displayController = transitions.mDisplayController;
                            Objects.requireNonNull(displayController);
                            DisplayChangeController displayChangeController = displayController.mChangeController;
                            int displayId = displayChange.getDisplayId();
                            int startRotation = displayChange.getStartRotation();
                            int endRotation = displayChange.getEndRotation();
                            Objects.requireNonNull(displayChangeController);
                            Iterator<DisplayChangeController.OnDisplayChangingListener> it = displayChangeController.mRotationListener.iterator();
                            while (it.hasNext()) {
                                it.next().onRotateDisplay(displayId, startRotation, endRotation, windowContainerTransaction);
                            }
                        }
                    }
                    activeTransition.mToken = transitions.mOrganizer.startTransition(transitionRequestInfo.getType(), iBinder, windowContainerTransaction);
                    transitions.mActiveTransitions.add(activeTransition);
                    return;
                }
                throw new RuntimeException("Transition already started " + iBinder);
        }
    }
}
