package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.assist.ui.DefaultUiController;
import com.google.android.systemui.assist.GoogleAssistLogger;
import dagger.Lazy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GoogleDefaultUiController extends DefaultUiController {
    public GoogleDefaultUiController(Context context, GoogleAssistLogger googleAssistLogger, WindowManager windowManager, MetricsLogger metricsLogger, Lazy<AssistManager> lazy) {
        super(context, googleAssistLogger, windowManager, metricsLogger, lazy);
        context.getResources();
        AssistantInvocationLightsView assistantInvocationLightsView = (AssistantInvocationLightsView) this.mInvocationLightsView;
        Objects.requireNonNull(assistantInvocationLightsView);
        assistantInvocationLightsView.mUseNavBarColor = true;
        assistantInvocationLightsView.mPaint.setStrokeCap(Paint.Cap.BUTT);
        assistantInvocationLightsView.attemptRegisterNavBarListener();
        AssistantInvocationLightsView assistantInvocationLightsView2 = (AssistantInvocationLightsView) LayoutInflater.from(context).inflate(2131624143, (ViewGroup) this.mRoot, false);
        this.mInvocationLightsView = assistantInvocationLightsView2;
        this.mRoot.addView(assistantInvocationLightsView2);
    }
}
