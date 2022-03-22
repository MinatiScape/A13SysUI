package com.google.common.util.concurrent;
/* loaded from: classes.dex */
class MoreExecutors$Application {
    public void addShutdownHook(Thread thread) {
        Runtime.getRuntime().addShutdownHook(thread);
    }
}
