package com.android.systemui.statusbar.commandline;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CommandRegistry.kt */
/* loaded from: classes.dex */
public final class CommandWrapper {
    public final Function0<Command> commandFactory;
    public final Executor executor;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CommandWrapper)) {
            return false;
        }
        CommandWrapper commandWrapper = (CommandWrapper) obj;
        return Intrinsics.areEqual(this.commandFactory, commandWrapper.commandFactory) && Intrinsics.areEqual(this.executor, commandWrapper.executor);
    }

    public final int hashCode() {
        return this.executor.hashCode() + (this.commandFactory.hashCode() * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CommandWrapper(commandFactory=");
        m.append(this.commandFactory);
        m.append(", executor=");
        m.append(this.executor);
        m.append(')');
        return m.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public CommandWrapper(Function0<? extends Command> function0, Executor executor) {
        this.commandFactory = function0;
        this.executor = executor;
    }
}
