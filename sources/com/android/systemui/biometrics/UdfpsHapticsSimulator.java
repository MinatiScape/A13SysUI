package com.android.systemui.biometrics;

import android.media.AudioAttributes;
import android.os.VibrationEffect;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.io.PrintWriter;
import java.util.List;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: UdfpsHapticsSimulator.kt */
/* loaded from: classes.dex */
public final class UdfpsHapticsSimulator implements Command {
    public final AudioAttributes sonificationEffects = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    public UdfpsController udfpsController;
    public final VibratorHelper vibrator;

    public final void invalidCommand(PrintWriter printWriter) {
        printWriter.println("invalid command");
        printWriter.println("Usage: adb shell cmd statusbar udfps-haptic <haptic>");
        printWriter.println("Available commands:");
        printWriter.println("  start");
        printWriter.println("  success, always plays CLICK haptic");
        printWriter.println("  error, always plays DOUBLE_CLICK haptic");
    }

    public UdfpsHapticsSimulator(CommandRegistry commandRegistry, VibratorHelper vibratorHelper) {
        this.vibrator = vibratorHelper;
        commandRegistry.registerCommand("udfps-haptic", new AnonymousClass1());
    }

    @Override // com.android.systemui.statusbar.commandline.Command
    public final void execute(PrintWriter printWriter, List<String> list) {
        if (list.isEmpty()) {
            invalidCommand(printWriter);
            return;
        }
        String str = list.get(0);
        int hashCode = str.hashCode();
        if (hashCode != -1867169789) {
            if (hashCode != 96784904) {
                if (hashCode == 109757538 && str.equals("start")) {
                    UdfpsController udfpsController = this.udfpsController;
                    if (udfpsController != null) {
                        udfpsController.playStartHaptic();
                        return;
                    }
                    return;
                }
            } else if (str.equals("error")) {
                this.vibrator.vibrate(VibrationEffect.get(1), this.sonificationEffects);
                return;
            }
        } else if (str.equals("success")) {
            this.vibrator.vibrate(VibrationEffect.get(0), this.sonificationEffects);
            return;
        }
        invalidCommand(printWriter);
    }

    /* compiled from: UdfpsHapticsSimulator.kt */
    /* renamed from: com.android.systemui.biometrics.UdfpsHapticsSimulator$1  reason: invalid class name */
    /* loaded from: classes.dex */
    final class AnonymousClass1 extends Lambda implements Function0<Command> {
        public AnonymousClass1() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Command invoke() {
            return UdfpsHapticsSimulator.this;
        }
    }
}
