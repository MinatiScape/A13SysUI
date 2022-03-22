package com.android.systemui.statusbar.charging;

import android.content.Context;
import android.graphics.PointF;
import android.os.SystemProperties;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import androidx.preference.R$id;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.Utils;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.time.SystemClock;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: WiredChargingRippleController.kt */
/* loaded from: classes.dex */
public final class WiredChargingRippleController {
    public final BatteryController batteryController;
    public final ConfigurationController configurationController;
    public final Context context;
    public int debounceLevel;
    public Long lastTriggerTime;
    public float normalizedPortPosX;
    public float normalizedPortPosY;
    public Boolean pluggedIn;
    public ChargingRippleView rippleView;
    public final SystemClock systemClock;
    public final UiEventLogger uiEventLogger;
    public final WindowManager.LayoutParams windowLayoutParams;
    public final WindowManager windowManager;

    /* compiled from: WiredChargingRippleController.kt */
    /* renamed from: com.android.systemui.statusbar.charging.WiredChargingRippleController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements Function0<Command> {
        public AnonymousClass1() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Command invoke() {
            return new ChargingRippleCommand();
        }
    }

    /* compiled from: WiredChargingRippleController.kt */
    /* loaded from: classes.dex */
    public final class ChargingRippleCommand implements Command {
        public ChargingRippleCommand() {
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public final void execute(PrintWriter printWriter, List<String> list) {
            WiredChargingRippleController.this.startRipple();
        }
    }

    /* JADX WARN: Failed to restore enum class, 'enum' modifier removed */
    /* compiled from: WiredChargingRippleController.kt */
    /* loaded from: classes.dex */
    public static final class WiredChargingRippleEvent extends Enum<WiredChargingRippleEvent> implements UiEventLogger.UiEventEnum {
        public static final /* synthetic */ WiredChargingRippleEvent[] $VALUES;
        public static final WiredChargingRippleEvent CHARGING_RIPPLE_PLAYED;
        private final int _id = 829;

        public static WiredChargingRippleEvent valueOf(String str) {
            return (WiredChargingRippleEvent) Enum.valueOf(WiredChargingRippleEvent.class, str);
        }

        public static WiredChargingRippleEvent[] values() {
            return (WiredChargingRippleEvent[]) $VALUES.clone();
        }

        static {
            WiredChargingRippleEvent wiredChargingRippleEvent = new WiredChargingRippleEvent();
            CHARGING_RIPPLE_PLAYED = wiredChargingRippleEvent;
            $VALUES = new WiredChargingRippleEvent[]{wiredChargingRippleEvent};
        }

        public final int getId() {
            return this._id;
        }
    }

    @VisibleForTesting
    public static /* synthetic */ void getRippleView$annotations() {
    }

    public final void startRipple() {
        ChargingRippleView chargingRippleView = this.rippleView;
        Objects.requireNonNull(chargingRippleView);
        if (!chargingRippleView.rippleInProgress && this.rippleView.getParent() == null) {
            this.windowLayoutParams.packageName = this.context.getOpPackageName();
            this.rippleView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1
                @Override // android.view.View.OnAttachStateChangeListener
                public final void onViewDetachedFromWindow(View view) {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public final void onViewAttachedToWindow(View view) {
                    PointF pointF;
                    WiredChargingRippleController wiredChargingRippleController = WiredChargingRippleController.this;
                    Objects.requireNonNull(wiredChargingRippleController);
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    wiredChargingRippleController.context.getDisplay().getRealMetrics(displayMetrics);
                    int i = displayMetrics.widthPixels;
                    int i2 = displayMetrics.heightPixels;
                    ChargingRippleView chargingRippleView2 = wiredChargingRippleController.rippleView;
                    float max = Integer.max(i, i2);
                    Objects.requireNonNull(chargingRippleView2);
                    RippleShader rippleShader = chargingRippleView2.rippleShader;
                    Objects.requireNonNull(rippleShader);
                    rippleShader.radius = max;
                    rippleShader.setFloatUniform("in_maxRadius", max);
                    chargingRippleView2.radius = max;
                    ChargingRippleView chargingRippleView3 = wiredChargingRippleController.rippleView;
                    int rotation = R$id.getRotation(wiredChargingRippleController.context);
                    if (rotation == 1) {
                        pointF = new PointF(i * wiredChargingRippleController.normalizedPortPosY, (1 - wiredChargingRippleController.normalizedPortPosX) * i2);
                    } else if (rotation == 2) {
                        float f = 1;
                        pointF = new PointF((f - wiredChargingRippleController.normalizedPortPosX) * i, (f - wiredChargingRippleController.normalizedPortPosY) * i2);
                    } else if (rotation != 3) {
                        pointF = new PointF(i * wiredChargingRippleController.normalizedPortPosX, i2 * wiredChargingRippleController.normalizedPortPosY);
                    } else {
                        pointF = new PointF((1 - wiredChargingRippleController.normalizedPortPosY) * i, i2 * wiredChargingRippleController.normalizedPortPosX);
                    }
                    Objects.requireNonNull(chargingRippleView3);
                    RippleShader rippleShader2 = chargingRippleView3.rippleShader;
                    Objects.requireNonNull(rippleShader2);
                    rippleShader2.setFloatUniform("in_origin", pointF.x, pointF.y);
                    chargingRippleView3.origin = pointF;
                    WiredChargingRippleController wiredChargingRippleController2 = WiredChargingRippleController.this;
                    Objects.requireNonNull(wiredChargingRippleController2);
                    ChargingRippleView chargingRippleView4 = wiredChargingRippleController2.rippleView;
                    final WiredChargingRippleController wiredChargingRippleController3 = WiredChargingRippleController.this;
                    chargingRippleView4.startRipple(new Runnable() { // from class: com.android.systemui.statusbar.charging.WiredChargingRippleController$startRipple$1$onViewAttachedToWindow$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            WiredChargingRippleController wiredChargingRippleController4 = WiredChargingRippleController.this;
                            WindowManager windowManager = wiredChargingRippleController4.windowManager;
                            Objects.requireNonNull(wiredChargingRippleController4);
                            windowManager.removeView(wiredChargingRippleController4.rippleView);
                        }
                    });
                    WiredChargingRippleController wiredChargingRippleController4 = WiredChargingRippleController.this;
                    Objects.requireNonNull(wiredChargingRippleController4);
                    wiredChargingRippleController4.rippleView.removeOnAttachStateChangeListener(this);
                }
            });
            this.windowManager.addView(this.rippleView, this.windowLayoutParams);
            this.uiEventLogger.log(WiredChargingRippleEvent.CHARGING_RIPPLE_PLAYED);
        }
    }

    public final void updateRippleColor() {
        ChargingRippleView chargingRippleView = this.rippleView;
        int defaultColor = Utils.getColorAttr(this.context, 16843829).getDefaultColor();
        Objects.requireNonNull(chargingRippleView);
        chargingRippleView.rippleShader.setColor(defaultColor);
    }

    public WiredChargingRippleController(CommandRegistry commandRegistry, BatteryController batteryController, ConfigurationController configurationController, FeatureFlags featureFlags, Context context, WindowManager windowManager, SystemClock systemClock, UiEventLogger uiEventLogger) {
        this.batteryController = batteryController;
        this.configurationController = configurationController;
        this.context = context;
        this.windowManager = windowManager;
        this.systemClock = systemClock;
        this.uiEventLogger = uiEventLogger;
        if (featureFlags.isEnabled(Flags.CHARGING_RIPPLE)) {
            SystemProperties.getBoolean("persist.debug.suppress-charging-ripple", false);
        }
        this.normalizedPortPosX = context.getResources().getFloat(2131166765);
        this.normalizedPortPosY = context.getResources().getFloat(2131166766);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        layoutParams.layoutInDisplayCutoutMode = 3;
        layoutParams.format = -3;
        layoutParams.type = 2006;
        layoutParams.setFitInsetsTypes(0);
        layoutParams.setTitle("Wired Charging Animation");
        layoutParams.flags = 24;
        layoutParams.setTrustedOverlay();
        this.windowLayoutParams = layoutParams;
        this.rippleView = new ChargingRippleView(context, null);
        this.pluggedIn = Boolean.valueOf(batteryController.isPluggedIn());
        commandRegistry.registerCommand("charging-ripple", new AnonymousClass1());
        updateRippleColor();
    }
}
