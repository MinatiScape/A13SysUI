package com.android.systemui.util.concurrency;

import com.android.systemui.statusbar.phone.StatusBar;
/* loaded from: classes.dex */
public interface MessageRouter {

    /* loaded from: classes.dex */
    public interface DataMessageListener<T> {
        void onMessage(T t);
    }

    /* loaded from: classes.dex */
    public interface SimpleMessageListener {
        void onMessage();
    }

    void cancelMessages(int i);

    <T> void cancelMessages(Class<T> cls);

    default void sendMessage(int i) {
        sendMessageDelayed(i, 0L);
    }

    void sendMessageDelayed(int i, long j);

    void sendMessageDelayed(StatusBar.KeyboardShortcutsMessage keyboardShortcutsMessage);

    void subscribeTo(int i, SimpleMessageListener simpleMessageListener);

    <T> void subscribeTo(Class<T> cls, DataMessageListener<T> dataMessageListener);

    default void sendMessage(StatusBar.KeyboardShortcutsMessage keyboardShortcutsMessage) {
        sendMessageDelayed(keyboardShortcutsMessage);
    }
}
