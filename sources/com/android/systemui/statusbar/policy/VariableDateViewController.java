package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.UserHandle;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.time.SystemClock;
import java.util.Date;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: VariableDateViewController.kt */
/* loaded from: classes.dex */
public final class VariableDateViewController extends ViewController<VariableDateView> {
    public final BroadcastDispatcher broadcastDispatcher;
    public DateFormat dateFormat;
    public String datePattern;
    public final SystemClock systemClock;
    public final Handler timeTickHandler;
    public int lastWidth = Integer.MAX_VALUE;
    public String lastText = "";
    public Date currentTime = new Date();
    public final VariableDateViewController$intentReceiver$1 intentReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.VariableDateViewController$intentReceiver$1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            Handler handler = ((VariableDateView) VariableDateViewController.this.mView).getHandler();
            if (handler != null) {
                String action = intent.getAction();
                if (Intrinsics.areEqual("android.intent.action.TIME_TICK", action) || Intrinsics.areEqual("android.intent.action.TIME_SET", action) || Intrinsics.areEqual("android.intent.action.TIMEZONE_CHANGED", action) || Intrinsics.areEqual("android.intent.action.LOCALE_CHANGED", action)) {
                    if (Intrinsics.areEqual("android.intent.action.LOCALE_CHANGED", action) || Intrinsics.areEqual("android.intent.action.TIMEZONE_CHANGED", action)) {
                        final VariableDateViewController variableDateViewController = VariableDateViewController.this;
                        handler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.VariableDateViewController$intentReceiver$1$onReceive$1
                            @Override // java.lang.Runnable
                            public final void run() {
                                VariableDateViewController.this.dateFormat = null;
                            }
                        });
                    }
                    final VariableDateViewController variableDateViewController2 = VariableDateViewController.this;
                    handler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.VariableDateViewController$intentReceiver$1$onReceive$2
                        @Override // java.lang.Runnable
                        public final void run() {
                            VariableDateViewController.access$updateClock(VariableDateViewController.this);
                        }
                    });
                }
            }
        }
    };
    public final VariableDateViewController$onMeasureListener$1 onMeasureListener = new VariableDateView.OnMeasureListener() { // from class: com.android.systemui.statusbar.policy.VariableDateViewController$onMeasureListener$1
        /* JADX WARN: Code restructure failed: missing block: B:9:0x0024, code lost:
            if (kotlin.jvm.internal.Intrinsics.areEqual(r1, r2.longerPattern) == false) goto L_0x0026;
         */
        @Override // com.android.systemui.statusbar.policy.VariableDateView.OnMeasureListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onMeasureAction(int r6) {
            /*
                r5 = this;
                com.android.systemui.statusbar.policy.VariableDateViewController r0 = com.android.systemui.statusbar.policy.VariableDateViewController.this
                int r1 = r0.lastWidth
                if (r6 == r1) goto L_0x00a3
                T extends android.view.View r1 = r0.mView
                com.android.systemui.statusbar.policy.VariableDateView r1 = (com.android.systemui.statusbar.policy.VariableDateView) r1
                java.util.Objects.requireNonNull(r1)
                boolean r1 = r1.freezeSwitching
                if (r1 != 0) goto L_0x009f
                int r1 = r0.lastWidth
                if (r6 <= r1) goto L_0x0026
                java.lang.String r1 = r0.datePattern
                T extends android.view.View r2 = r0.mView
                com.android.systemui.statusbar.policy.VariableDateView r2 = (com.android.systemui.statusbar.policy.VariableDateView) r2
                java.util.Objects.requireNonNull(r2)
                java.lang.String r2 = r2.longerPattern
                boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual(r1, r2)
                if (r1 != 0) goto L_0x009f
            L_0x0026:
                int r1 = r0.lastWidth
                java.lang.String r2 = ""
                if (r6 >= r1) goto L_0x0035
                java.lang.String r1 = r0.datePattern
                boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual(r1, r2)
                if (r1 == 0) goto L_0x0035
                goto L_0x009f
            L_0x0035:
                java.util.Date r1 = r0.currentTime
                T extends android.view.View r3 = r0.mView
                com.android.systemui.statusbar.policy.VariableDateView r3 = (com.android.systemui.statusbar.policy.VariableDateView) r3
                java.util.Objects.requireNonNull(r3)
                java.lang.String r3 = r3.longerPattern
                android.icu.text.DateFormat r3 = com.android.systemui.statusbar.policy.VariableDateViewControllerKt.getFormatFromPattern(r3)
                java.lang.String r1 = com.android.systemui.statusbar.policy.VariableDateViewControllerKt.getTextForFormat(r1, r3)
                T extends android.view.View r3 = r0.mView
                com.android.systemui.statusbar.policy.VariableDateView r3 = (com.android.systemui.statusbar.policy.VariableDateView) r3
                java.util.Objects.requireNonNull(r3)
                android.text.TextPaint r3 = r3.getPaint()
                float r1 = android.text.Layout.getDesiredWidth(r1, r3)
                float r3 = (float) r6
                int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r1 > 0) goto L_0x0069
                T extends android.view.View r1 = r0.mView
                com.android.systemui.statusbar.policy.VariableDateView r1 = (com.android.systemui.statusbar.policy.VariableDateView) r1
                java.util.Objects.requireNonNull(r1)
                java.lang.String r1 = r1.longerPattern
                r0.changePattern(r1)
                goto L_0x009f
            L_0x0069:
                java.util.Date r1 = r0.currentTime
                T extends android.view.View r4 = r0.mView
                com.android.systemui.statusbar.policy.VariableDateView r4 = (com.android.systemui.statusbar.policy.VariableDateView) r4
                java.util.Objects.requireNonNull(r4)
                java.lang.String r4 = r4.shorterPattern
                android.icu.text.DateFormat r4 = com.android.systemui.statusbar.policy.VariableDateViewControllerKt.getFormatFromPattern(r4)
                java.lang.String r1 = com.android.systemui.statusbar.policy.VariableDateViewControllerKt.getTextForFormat(r1, r4)
                T extends android.view.View r4 = r0.mView
                com.android.systemui.statusbar.policy.VariableDateView r4 = (com.android.systemui.statusbar.policy.VariableDateView) r4
                java.util.Objects.requireNonNull(r4)
                android.text.TextPaint r4 = r4.getPaint()
                float r1 = android.text.Layout.getDesiredWidth(r1, r4)
                int r1 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
                if (r1 > 0) goto L_0x009c
                T extends android.view.View r1 = r0.mView
                com.android.systemui.statusbar.policy.VariableDateView r1 = (com.android.systemui.statusbar.policy.VariableDateView) r1
                java.util.Objects.requireNonNull(r1)
                java.lang.String r1 = r1.shorterPattern
                r0.changePattern(r1)
                goto L_0x009f
            L_0x009c:
                r0.changePattern(r2)
            L_0x009f:
                com.android.systemui.statusbar.policy.VariableDateViewController r5 = com.android.systemui.statusbar.policy.VariableDateViewController.this
                r5.lastWidth = r6
            L_0x00a3:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.VariableDateViewController$onMeasureListener$1.onMeasureAction(int):void");
        }
    };

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.dateFormat = null;
        VariableDateView variableDateView = (VariableDateView) this.mView;
        Objects.requireNonNull(variableDateView);
        variableDateView.onMeasureListener = null;
        this.broadcastDispatcher.unregisterReceiver(this.intentReceiver);
    }

    /* compiled from: VariableDateViewController.kt */
    /* loaded from: classes.dex */
    public static final class Factory {
        public final BroadcastDispatcher broadcastDispatcher;
        public final Handler handler;
        public final SystemClock systemClock;

        public Factory(SystemClock systemClock, BroadcastDispatcher broadcastDispatcher, Handler handler) {
            this.systemClock = systemClock;
            this.broadcastDispatcher = broadcastDispatcher;
            this.handler = handler;
        }
    }

    public final void changePattern(String str) {
        boolean z;
        if (!str.equals(this.datePattern) && !Intrinsics.areEqual(this.datePattern, str)) {
            this.datePattern = str;
            this.dateFormat = null;
            T t = this.mView;
            if (t == 0 || !t.isAttachedToWindow()) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                VariableDateViewController$datePattern$1 variableDateViewController$datePattern$1 = new VariableDateViewController$datePattern$1(this);
                Handler handler = ((VariableDateView) this.mView).getHandler();
                if (handler != null) {
                    handler.post(new VariableDateViewControllerKt$sam$java_lang_Runnable$0(variableDateViewController$datePattern$1));
                }
            }
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_TICK");
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
        BroadcastDispatcher.registerReceiver$default(this.broadcastDispatcher, this.intentReceiver, intentFilter, new HandlerExecutor(this.timeTickHandler), UserHandle.SYSTEM, 16);
        VariableDateViewController$onViewAttached$1 variableDateViewController$onViewAttached$1 = new VariableDateViewController$onViewAttached$1(this);
        Handler handler = ((VariableDateView) this.mView).getHandler();
        if (handler != null) {
            handler.post(new VariableDateViewControllerKt$sam$java_lang_Runnable$0(variableDateViewController$onViewAttached$1));
        }
        VariableDateView variableDateView = (VariableDateView) this.mView;
        VariableDateViewController$onMeasureListener$1 variableDateViewController$onMeasureListener$1 = this.onMeasureListener;
        Objects.requireNonNull(variableDateView);
        variableDateView.onMeasureListener = variableDateViewController$onMeasureListener$1;
    }

    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.systemui.statusbar.policy.VariableDateViewController$intentReceiver$1] */
    /* JADX WARN: Type inference failed for: r1v6, types: [com.android.systemui.statusbar.policy.VariableDateViewController$onMeasureListener$1] */
    public VariableDateViewController(SystemClock systemClock, BroadcastDispatcher broadcastDispatcher, Handler handler, VariableDateView variableDateView) {
        super(variableDateView);
        this.systemClock = systemClock;
        this.broadcastDispatcher = broadcastDispatcher;
        this.timeTickHandler = handler;
        this.datePattern = variableDateView.longerPattern;
    }

    public static final void access$updateClock(VariableDateViewController variableDateViewController) {
        Objects.requireNonNull(variableDateViewController);
        if (variableDateViewController.dateFormat == null) {
            variableDateViewController.dateFormat = VariableDateViewControllerKt.getFormatFromPattern(variableDateViewController.datePattern);
        }
        variableDateViewController.currentTime.setTime(variableDateViewController.systemClock.currentTimeMillis());
        Date date = variableDateViewController.currentTime;
        DateFormat dateFormat = variableDateViewController.dateFormat;
        Intrinsics.checkNotNull(dateFormat);
        String textForFormat = VariableDateViewControllerKt.getTextForFormat(date, dateFormat);
        if (!Intrinsics.areEqual(textForFormat, variableDateViewController.lastText)) {
            ((VariableDateView) variableDateViewController.mView).setText(textForFormat);
            variableDateViewController.lastText = textForFormat;
        }
    }
}
