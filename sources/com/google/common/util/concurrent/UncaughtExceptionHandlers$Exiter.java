package com.google.common.util.concurrent;

import java.lang.Thread;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
/* loaded from: classes.dex */
final class UncaughtExceptionHandlers$Exiter implements Thread.UncaughtExceptionHandler {
    public static final Logger logger = Logger.getLogger(UncaughtExceptionHandlers$Exiter.class.getName());

    @Override // java.lang.Thread.UncaughtExceptionHandler
    public final void uncaughtException(Thread thread, Throwable th) {
        try {
            logger.log(Level.SEVERE, String.format(Locale.ROOT, "Caught an exception in %s.  Shutting down.", thread), th);
            throw null;
        } catch (Throwable th2) {
            System.err.println(th.getMessage());
            System.err.println(th2.getMessage());
            throw null;
        }
    }
}
