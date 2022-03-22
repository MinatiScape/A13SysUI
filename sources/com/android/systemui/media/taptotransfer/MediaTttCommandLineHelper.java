package com.android.systemui.media.taptotransfer;

import android.app.StatusBarManager;
import android.content.Context;
import android.media.MediaRoute2Info;
import android.util.Log;
import com.android.systemui.media.taptotransfer.sender.AlmostCloseToEndCast;
import com.android.systemui.media.taptotransfer.sender.AlmostCloseToStartCast;
import com.android.systemui.media.taptotransfer.sender.TransferFailed;
import com.android.systemui.media.taptotransfer.sender.TransferToReceiverSucceeded;
import com.android.systemui.media.taptotransfer.sender.TransferToReceiverTriggered;
import com.android.systemui.media.taptotransfer.sender.TransferToThisDeviceSucceeded;
import com.android.systemui.media.taptotransfer.sender.TransferToThisDeviceTriggered;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Pair;
import kotlin.collections.MapsKt___MapsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlin.jvm.internal.Reflection;
/* compiled from: MediaTttCommandLineHelper.kt */
/* loaded from: classes.dex */
public final class MediaTttCommandLineHelper {
    public final Context context;
    public final Executor mainExecutor;
    public final Map<String, Integer> stateStringToStateInt;

    /* compiled from: MediaTttCommandLineHelper.kt */
    /* renamed from: com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements Function0<Command> {
        public AnonymousClass1() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Command invoke() {
            return new SenderCommand();
        }
    }

    /* compiled from: MediaTttCommandLineHelper.kt */
    /* renamed from: com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass2 extends Lambda implements Function0<Command> {
        public AnonymousClass2() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Command invoke() {
            return new ReceiverCommand();
        }
    }

    /* compiled from: MediaTttCommandLineHelper.kt */
    /* loaded from: classes.dex */
    public final class ReceiverCommand implements Command {
        public ReceiverCommand() {
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public final void execute(PrintWriter printWriter, List<String> list) {
            Object systemService = MediaTttCommandLineHelper.this.context.getSystemService("statusbar");
            Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.app.StatusBarManager");
            StatusBarManager statusBarManager = (StatusBarManager) systemService;
            MediaRoute2Info build = new MediaRoute2Info.Builder("id", "Test Name").addFeature("feature").setPackageName(ThemeOverlayApplier.SYSUI_PACKAGE).build();
            String str = list.get(0);
            if (Intrinsics.areEqual(str, "CloseToSender")) {
                statusBarManager.updateMediaTapToTransferReceiverDisplay(0, build, null, null);
            } else if (Intrinsics.areEqual(str, "FarFromSender")) {
                statusBarManager.updateMediaTapToTransferReceiverDisplay(1, build, null, null);
            } else {
                printWriter.println(Intrinsics.stringPlus("Invalid command name ", str));
            }
        }
    }

    /* compiled from: MediaTttCommandLineHelper.kt */
    /* loaded from: classes.dex */
    public final class SenderCommand implements Command {
        public SenderCommand() {
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public final void execute(PrintWriter printWriter, List<String> list) {
            boolean z;
            Executor executor;
            boolean z2 = false;
            MediaRoute2Info build = new MediaRoute2Info.Builder("id", list.get(0)).addFeature("feature").setPackageName(ThemeOverlayApplier.SYSUI_PACKAGE).build();
            String str = list.get(1);
            Integer num = MediaTttCommandLineHelper.this.stateStringToStateInt.get(str);
            if (num == null) {
                printWriter.println(Intrinsics.stringPlus("Invalid command name ", str));
                return;
            }
            Object systemService = MediaTttCommandLineHelper.this.context.getSystemService("statusbar");
            Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.app.StatusBarManager");
            StatusBarManager statusBarManager = (StatusBarManager) systemService;
            int intValue = num.intValue();
            int intValue2 = num.intValue();
            if (intValue2 == 4 || intValue2 == 5) {
                z = true;
            } else {
                z = false;
            }
            Runnable runnable = null;
            if (z) {
                executor = MediaTttCommandLineHelper.this.mainExecutor;
            } else {
                executor = null;
            }
            final int intValue3 = num.intValue();
            if (intValue3 == 4 || intValue3 == 5) {
                z2 = true;
            }
            if (z2) {
                runnable = new Runnable() { // from class: com.android.systemui.media.taptotransfer.MediaTttCommandLineHelper$SenderCommand$getUndoCallback$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Log.i("MediaTransferCli", Intrinsics.stringPlus("Undo triggered for ", Integer.valueOf(intValue3)));
                    }
                };
            }
            statusBarManager.updateMediaTapToTransferSenderDisplay(intValue, build, executor, runnable);
        }
    }

    public MediaTttCommandLineHelper(CommandRegistry commandRegistry, Context context, Executor executor) {
        this.context = context;
        this.mainExecutor = executor;
        String simpleName = Reflection.getOrCreateKotlinClass(AlmostCloseToStartCast.class).getSimpleName();
        Intrinsics.checkNotNull(simpleName);
        String simpleName2 = Reflection.getOrCreateKotlinClass(AlmostCloseToEndCast.class).getSimpleName();
        Intrinsics.checkNotNull(simpleName2);
        String simpleName3 = Reflection.getOrCreateKotlinClass(TransferToReceiverTriggered.class).getSimpleName();
        Intrinsics.checkNotNull(simpleName3);
        String simpleName4 = Reflection.getOrCreateKotlinClass(TransferToThisDeviceTriggered.class).getSimpleName();
        Intrinsics.checkNotNull(simpleName4);
        String simpleName5 = Reflection.getOrCreateKotlinClass(TransferToReceiverSucceeded.class).getSimpleName();
        Intrinsics.checkNotNull(simpleName5);
        String simpleName6 = Reflection.getOrCreateKotlinClass(TransferToThisDeviceSucceeded.class).getSimpleName();
        Intrinsics.checkNotNull(simpleName6);
        String simpleName7 = Reflection.getOrCreateKotlinClass(TransferFailed.class).getSimpleName();
        Intrinsics.checkNotNull(simpleName7);
        this.stateStringToStateInt = MapsKt___MapsKt.mapOf(new Pair(simpleName, 0), new Pair(simpleName2, 1), new Pair(simpleName3, 2), new Pair(simpleName4, 3), new Pair(simpleName5, 4), new Pair(simpleName6, 5), new Pair(simpleName7, 6), new Pair("FarFromReceiver", 8));
        commandRegistry.registerCommand("media-ttt-chip-sender", new AnonymousClass1());
        commandRegistry.registerCommand("media-ttt-chip-receiver", new AnonymousClass2());
    }
}
