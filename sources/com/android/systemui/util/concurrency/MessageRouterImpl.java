package com.android.systemui.util.concurrency;

import com.android.systemui.qs.tiles.QuickAccessWalletTile$$ExternalSyntheticLambda0;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.concurrency.MessageRouter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public final class MessageRouterImpl implements MessageRouter {
    public final DelayableExecutor mDelayableExecutor;
    public final HashMap mIdMessageCancelers = new HashMap();
    public final HashMap mDataMessageCancelers = new HashMap();
    public final HashMap mSimpleMessageListenerMap = new HashMap();
    public final HashMap mDataMessageListenerMap = new HashMap();

    @Override // com.android.systemui.util.concurrency.MessageRouter
    public final void cancelMessages(int i) {
        synchronized (this.mIdMessageCancelers) {
            if (this.mIdMessageCancelers.containsKey(Integer.valueOf(i))) {
                for (Runnable runnable : (List) this.mIdMessageCancelers.get(Integer.valueOf(i))) {
                    runnable.run();
                }
                this.mIdMessageCancelers.remove(Integer.valueOf(i));
            }
        }
    }

    @Override // com.android.systemui.util.concurrency.MessageRouter
    public final void sendMessageDelayed(int i, long j) {
        Runnable executeDelayed = this.mDelayableExecutor.executeDelayed(new OverviewProxyService$1$$ExternalSyntheticLambda3(this, i, 2), j);
        synchronized (this.mIdMessageCancelers) {
            this.mIdMessageCancelers.putIfAbsent(Integer.valueOf(i), new ArrayList());
            ((List) this.mIdMessageCancelers.get(Integer.valueOf(i))).add(executeDelayed);
        }
    }

    @Override // com.android.systemui.util.concurrency.MessageRouter
    public final void subscribeTo(int i, MessageRouter.SimpleMessageListener simpleMessageListener) {
        synchronized (this.mSimpleMessageListenerMap) {
            this.mSimpleMessageListenerMap.putIfAbsent(Integer.valueOf(i), new ArrayList());
            ((List) this.mSimpleMessageListenerMap.get(Integer.valueOf(i))).add(simpleMessageListener);
        }
    }

    public MessageRouterImpl(DelayableExecutor delayableExecutor) {
        this.mDelayableExecutor = delayableExecutor;
    }

    @Override // com.android.systemui.util.concurrency.MessageRouter
    public final <T> void subscribeTo(Class<T> cls, MessageRouter.DataMessageListener<T> dataMessageListener) {
        synchronized (this.mDataMessageListenerMap) {
            this.mDataMessageListenerMap.putIfAbsent(cls, new ArrayList());
            ((List) this.mDataMessageListenerMap.get(cls)).add(dataMessageListener);
        }
    }

    @Override // com.android.systemui.util.concurrency.MessageRouter
    public final void sendMessageDelayed(StatusBar.KeyboardShortcutsMessage keyboardShortcutsMessage) {
        Runnable executeDelayed = this.mDelayableExecutor.executeDelayed(new QuickAccessWalletTile$$ExternalSyntheticLambda0(this, keyboardShortcutsMessage, 3), 0L);
        synchronized (this.mDataMessageCancelers) {
            this.mDataMessageCancelers.putIfAbsent(StatusBar.KeyboardShortcutsMessage.class, new ArrayList());
            ((List) this.mDataMessageCancelers.get(StatusBar.KeyboardShortcutsMessage.class)).add(executeDelayed);
        }
    }

    @Override // com.android.systemui.util.concurrency.MessageRouter
    public final <T> void cancelMessages(Class<T> cls) {
        synchronized (this.mDataMessageCancelers) {
            if (this.mDataMessageCancelers.containsKey(cls)) {
                for (Runnable runnable : (List) this.mDataMessageCancelers.get(cls)) {
                    runnable.run();
                }
                this.mDataMessageCancelers.remove(cls);
            }
        }
    }
}
