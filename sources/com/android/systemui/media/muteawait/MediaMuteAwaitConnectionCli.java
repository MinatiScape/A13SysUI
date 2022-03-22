package com.android.systemui.media.muteawait;

import android.content.Context;
import android.media.AudioDeviceAttributes;
import android.media.AudioManager;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import kotlin.collections.EmptyList;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: MediaMuteAwaitConnectionCli.kt */
/* loaded from: classes.dex */
public final class MediaMuteAwaitConnectionCli {
    public final Context context;

    /* compiled from: MediaMuteAwaitConnectionCli.kt */
    /* renamed from: com.android.systemui.media.muteawait.MediaMuteAwaitConnectionCli$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements Function0<Command> {
        public AnonymousClass1() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Command invoke() {
            return new MuteAwaitCommand();
        }
    }

    /* compiled from: MediaMuteAwaitConnectionCli.kt */
    /* loaded from: classes.dex */
    public final class MuteAwaitCommand implements Command {
        public MuteAwaitCommand() {
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public final void execute(PrintWriter printWriter, List<String> list) {
            EmptyList emptyList = EmptyList.INSTANCE;
            AudioDeviceAttributes audioDeviceAttributes = new AudioDeviceAttributes(2, Integer.parseInt(list.get(0)), "address", list.get(1), emptyList, emptyList);
            String str = list.get(2);
            Object systemService = MediaMuteAwaitConnectionCli.this.context.getSystemService("audio");
            Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.media.AudioManager");
            AudioManager audioManager = (AudioManager) systemService;
            if (Intrinsics.areEqual(str, "start")) {
                audioManager.muteAwaitConnection(new int[]{1}, audioDeviceAttributes, 5L, MediaMuteAwaitConnectionCliKt.TIMEOUT_UNITS);
            } else if (Intrinsics.areEqual(str, "cancel")) {
                audioManager.cancelMuteAwaitConnection(audioDeviceAttributes);
            } else {
                printWriter.println(Intrinsics.stringPlus("Must specify `start` or `cancel`; was ", str));
            }
        }
    }

    public MediaMuteAwaitConnectionCli(CommandRegistry commandRegistry, Context context) {
        this.context = context;
        commandRegistry.registerCommand("media-mute-await", new AnonymousClass1());
    }
}
