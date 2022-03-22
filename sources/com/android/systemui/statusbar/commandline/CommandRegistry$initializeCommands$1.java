package com.android.systemui.statusbar.commandline;

import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public final class CommandRegistry$initializeCommands$1 extends Lambda implements Function0<Command> {
    public final /* synthetic */ CommandRegistry this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CommandRegistry$initializeCommands$1(CommandRegistry commandRegistry) {
        super(0);
        this.this$0 = commandRegistry;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Command invoke() {
        Objects.requireNonNull(this.this$0);
        return new PrefsCommand();
    }
}
