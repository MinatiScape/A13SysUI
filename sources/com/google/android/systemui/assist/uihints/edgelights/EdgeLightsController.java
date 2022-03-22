package com.google.android.systemui.assist.uihints.edgelights;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import com.android.systemui.assist.AssistLogger;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda21;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.NgaUiController;
import com.google.android.systemui.assist.uihints.NgaUiController$$ExternalSyntheticLambda2;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FulfillBottom;
import com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter;
import com.google.android.systemui.assist.uihints.edgelights.mode.FullListening;
import com.google.android.systemui.assist.uihints.edgelights.mode.Gone;
import java.util.Objects;
/* loaded from: classes.dex */
public final class EdgeLightsController implements NgaMessageHandler.AudioInfoListener, NgaMessageHandler.EdgeLightsInfoListener {
    public final AssistLogger mAssistLogger;
    public final Context mContext;
    public final EdgeLightsView mEdgeLightsView;
    public ModeChangeThrottler mThrottler;

    /* loaded from: classes.dex */
    public interface ModeChangeThrottler {
    }

    public final EdgeLightsView.Mode getMode() {
        EdgeLightsView edgeLightsView = this.mEdgeLightsView;
        Objects.requireNonNull(edgeLightsView);
        return edgeLightsView.mMode;
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.AudioInfoListener
    public final void onAudioInfo(float f, float f2) {
        EdgeLightsView edgeLightsView = this.mEdgeLightsView;
        Objects.requireNonNull(edgeLightsView);
        edgeLightsView.mMode.onAudioLevelUpdate(f2);
    }

    public EdgeLightsController(Context context, ViewGroup viewGroup, AssistLogger assistLogger) {
        this.mContext = context;
        this.mAssistLogger = assistLogger;
        this.mEdgeLightsView = (EdgeLightsView) viewGroup.findViewById(2131427893);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.EdgeLightsInfoListener
    public final void onEdgeLightsInfo(String str, boolean z) {
        char c;
        Object obj;
        Objects.requireNonNull(str);
        switch (str.hashCode()) {
            case -1911007510:
                if (str.equals("FULFILL_BOTTOM")) {
                    c = 0;
                    break;
                }
                c = 65535;
                break;
            case 2193567:
                if (str.equals("GONE")) {
                    c = 1;
                    break;
                }
                c = 65535;
                break;
            case 429932431:
                if (str.equals("HALF_LISTENING")) {
                    c = 2;
                    break;
                }
                c = 65535;
                break;
            case 1387022046:
                if (str.equals("FULFILL_PERIMETER")) {
                    c = 3;
                    break;
                }
                c = 65535;
                break;
            case 1971150571:
                if (str.equals("FULL_LISTENING")) {
                    c = 4;
                    break;
                }
                c = 65535;
                break;
            default:
                c = 65535;
                break;
        }
        if (c == 0) {
            obj = new FulfillBottom(this.mContext, z);
        } else if (c == 1) {
            obj = new Gone();
        } else if (c == 2) {
            Log.i("EdgeLightsController", "Rendering full instead of half listening for now.");
            obj = new FullListening(this.mContext, true);
        } else if (c == 3) {
            obj = new FulfillPerimeter(this.mContext);
        } else if (c != 4) {
            obj = null;
        } else {
            obj = new FullListening(this.mContext, false);
        }
        if (obj == null) {
            Log.e("EdgeLightsController", "Invalid edge lights mode: " + str);
            return;
        }
        BubbleStackView$$ExternalSyntheticLambda21 bubbleStackView$$ExternalSyntheticLambda21 = new BubbleStackView$$ExternalSyntheticLambda21(this, obj, 5);
        ModeChangeThrottler modeChangeThrottler = this.mThrottler;
        if (modeChangeThrottler == null) {
            bubbleStackView$$ExternalSyntheticLambda21.run();
            return;
        }
        NgaUiController ngaUiController = (NgaUiController) ((NgaUiController$$ExternalSyntheticLambda2) modeChangeThrottler).f$0;
        Objects.requireNonNull(ngaUiController);
        ValueAnimator valueAnimator = ngaUiController.mInvocationAnimator;
        if (valueAnimator != null && valueAnimator.isStarted()) {
            ngaUiController.mPendingEdgeLightsModeChange = bubbleStackView$$ExternalSyntheticLambda21;
        } else if (ngaUiController.mShowingAssistUi || !"FULL_LISTENING".equals(str)) {
            ngaUiController.mPendingEdgeLightsModeChange = null;
            bubbleStackView$$ExternalSyntheticLambda21.run();
        } else {
            ngaUiController.mInvocationInProgress = true;
            ngaUiController.onInvocationProgress(0, 1.0f);
            ngaUiController.mPendingEdgeLightsModeChange = bubbleStackView$$ExternalSyntheticLambda21;
        }
    }
}
