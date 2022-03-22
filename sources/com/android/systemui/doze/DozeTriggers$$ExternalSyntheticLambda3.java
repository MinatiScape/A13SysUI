package com.android.systemui.doze;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.TextView;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.TimeLimitedMotionEventBuffer;
import com.android.systemui.doze.DozeTriggers;
import com.android.systemui.dreams.complication.DreamWeatherComplication;
import com.android.systemui.navigationbar.TaskbarDelegate;
import com.android.systemui.util.condition.Condition;
import com.android.systemui.util.condition.Monitor;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.startingsurface.IStartingWindow;
import com.android.wm.shell.startingsurface.StartingSurface;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class DozeTriggers$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ DozeTriggers$$ExternalSyntheticLambda3(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        SmartspaceAction headerAction;
        switch (this.$r8$classId) {
            case 0:
                ((UiEventLogger) this.f$0).log((DozeTriggers.DozingUpdateUiEvent) obj);
                return;
            case 1:
                FalsingDataProvider falsingDataProvider = (FalsingDataProvider) this.f$0;
                Objects.requireNonNull(falsingDataProvider);
                TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer = falsingDataProvider.mRecentMotionEvents;
                ((FalsingDataProvider.GestureFinalizedListener) obj).onGestureFinalized(timeLimitedMotionEventBuffer.mMotionEvents.get(timeLimitedMotionEventBuffer.size() - 1).getEventTime());
                return;
            case 2:
                DreamWeatherComplication.DreamWeatherViewController dreamWeatherViewController = (DreamWeatherComplication.DreamWeatherViewController) this.f$0;
                SmartspaceTarget smartspaceTarget = (Parcelable) obj;
                Objects.requireNonNull(dreamWeatherViewController);
                if (smartspaceTarget instanceof SmartspaceTarget) {
                    SmartspaceTarget smartspaceTarget2 = smartspaceTarget;
                    if (smartspaceTarget2.getFeatureType() == 1 && (headerAction = smartspaceTarget2.getHeaderAction()) != null && !TextUtils.isEmpty(headerAction.getTitle())) {
                        ((TextView) dreamWeatherViewController.mView).setText(headerAction.getTitle().toString());
                        Icon icon = headerAction.getIcon();
                        if (icon != null) {
                            int dimensionPixelSize = dreamWeatherViewController.getResources().getDimensionPixelSize(2131167021);
                            Drawable loadDrawable = icon.loadDrawable(dreamWeatherViewController.getContext());
                            loadDrawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                            ((TextView) dreamWeatherViewController.mView).setCompoundDrawables(loadDrawable, null, null, null);
                            ((TextView) dreamWeatherViewController.mView).setCompoundDrawablePadding(dreamWeatherViewController.getResources().getDimensionPixelSize(2131167020));
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            case 3:
                TaskbarDelegate taskbarDelegate = (TaskbarDelegate) this.f$0;
                Objects.requireNonNull(taskbarDelegate);
                ((Pip) obj).addPipExclusionBoundsChangeListener(taskbarDelegate.mPipListener);
                return;
            case 4:
                IStartingWindow.Stub stub = (IStartingWindow.Stub) ((StartingSurface) obj).createExternalInterface();
                Objects.requireNonNull(stub);
                ((Bundle) this.f$0).putBinder("extra_shell_starting_window", stub);
                return;
            default:
                Monitor monitor = (Monitor) this.f$0;
                Objects.requireNonNull(monitor);
                ((Condition) obj).addCallback((Condition.Callback) monitor.mConditionCallback);
                return;
        }
    }
}
