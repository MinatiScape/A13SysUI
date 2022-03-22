package com.android.systemui.keyguard;

import android.content.res.ColorStateList;
import android.os.SystemClock;
import android.text.TextUtils;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda7;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.KeyguardIndicationTextView;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.wifitrackerlib.Utils$$ExternalSyntheticLambda0;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class KeyguardIndicationRotateTextViewController extends ViewController<KeyguardIndicationTextView> implements Dumpable {
    public CharSequence mCurrMessage;
    public final DelayableExecutor mExecutor;
    public final ColorStateList mInitialTextColorState;
    public boolean mIsDozing;
    public long mLastIndicationSwitch;
    public final float mMaxAlpha;
    public ShowNextIndication mShowNextIndicationRunnable;
    public final StatusBarStateController mStatusBarStateController;
    public final HashMap mIndicationMessages = new HashMap();
    public final LinkedList mIndicationQueue = new LinkedList();
    public int mCurrIndicationType = -1;
    public AnonymousClass1 mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController.1
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozeAmountChanged(float f, float f2) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = KeyguardIndicationRotateTextViewController.this;
            ((KeyguardIndicationTextView) keyguardIndicationRotateTextViewController.mView).setAlpha((1.0f - f) * keyguardIndicationRotateTextViewController.mMaxAlpha);
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozingChanged(boolean z) {
            KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController = KeyguardIndicationRotateTextViewController.this;
            if (z != keyguardIndicationRotateTextViewController.mIsDozing) {
                keyguardIndicationRotateTextViewController.mIsDozing = z;
                if (z) {
                    keyguardIndicationRotateTextViewController.showIndication(-1);
                } else if (keyguardIndicationRotateTextViewController.mIndicationQueue.size() > 0) {
                    KeyguardIndicationRotateTextViewController keyguardIndicationRotateTextViewController2 = KeyguardIndicationRotateTextViewController.this;
                    keyguardIndicationRotateTextViewController2.showIndication(((Integer) keyguardIndicationRotateTextViewController2.mIndicationQueue.get(0)).intValue());
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public class ShowNextIndication {
        public Runnable mCancelDelayedRunnable;
        public final KeyguardUpdateMonitor$$ExternalSyntheticLambda7 mShowIndicationRunnable;

        public ShowNextIndication(long j) {
            KeyguardUpdateMonitor$$ExternalSyntheticLambda7 keyguardUpdateMonitor$$ExternalSyntheticLambda7 = new KeyguardUpdateMonitor$$ExternalSyntheticLambda7(this, 1);
            this.mShowIndicationRunnable = keyguardUpdateMonitor$$ExternalSyntheticLambda7;
            this.mCancelDelayedRunnable = KeyguardIndicationRotateTextViewController.this.mExecutor.executeDelayed(keyguardUpdateMonitor$$ExternalSyntheticLambda7, j);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z;
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "KeyguardIndicationRotatingTextViewController:", "    currentMessage=");
        m.append((Object) ((KeyguardIndicationTextView) this.mView).getText());
        printWriter.println(m.toString());
        StringBuilder sb = new StringBuilder();
        sb.append("    dozing:");
        StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, this.mIsDozing, printWriter, "    queue:");
        m2.append(this.mIndicationQueue.toString());
        printWriter.println(m2.toString());
        printWriter.println("    showNextIndicationRunnable:" + this.mShowNextIndicationRunnable);
        if (this.mIndicationMessages.keySet().size() > 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            printWriter.println("    All messages:");
            for (Integer num : this.mIndicationMessages.keySet()) {
                int intValue = num.intValue();
                StringBuilder m3 = ExifInterface$$ExternalSyntheticOutline0.m("        type=", intValue, " ");
                m3.append(this.mIndicationMessages.get(Integer.valueOf(intValue)));
                printWriter.println(m3.toString());
            }
        }
    }

    public final void hideIndication(int i) {
        if (this.mIndicationMessages.containsKey(Integer.valueOf(i))) {
            KeyguardIndication keyguardIndication = (KeyguardIndication) this.mIndicationMessages.get(Integer.valueOf(i));
            Objects.requireNonNull(keyguardIndication);
            if (!TextUtils.isEmpty(keyguardIndication.mMessage)) {
                updateIndication(i, null, true);
            }
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        ShowNextIndication showNextIndication = this.mShowNextIndicationRunnable;
        if (showNextIndication != null) {
            Runnable runnable = showNextIndication.mCancelDelayedRunnable;
            if (runnable != null) {
                runnable.run();
                showNextIndication.mCancelDelayedRunnable = null;
            }
            this.mShowNextIndicationRunnable = null;
        }
    }

    public final void showIndication(final int i) {
        CharSequence charSequence;
        Long l;
        CharSequence charSequence2;
        ShowNextIndication showNextIndication = this.mShowNextIndicationRunnable;
        if (showNextIndication != null) {
            Runnable runnable = showNextIndication.mCancelDelayedRunnable;
            if (runnable != null) {
                runnable.run();
                showNextIndication.mCancelDelayedRunnable = null;
            }
            this.mShowNextIndicationRunnable = null;
        }
        CharSequence charSequence3 = this.mCurrMessage;
        int i2 = this.mCurrIndicationType;
        this.mCurrIndicationType = i;
        if (this.mIndicationMessages.get(Integer.valueOf(i)) != null) {
            KeyguardIndication keyguardIndication = (KeyguardIndication) this.mIndicationMessages.get(Integer.valueOf(i));
            Objects.requireNonNull(keyguardIndication);
            charSequence = keyguardIndication.mMessage;
        } else {
            charSequence = null;
        }
        this.mCurrMessage = charSequence;
        this.mIndicationQueue.removeIf(new Predicate() { // from class: com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController$$ExternalSyntheticLambda1
            @Override // java.util.function.Predicate
            public final boolean test(Object obj) {
                if (((Integer) obj).intValue() == i) {
                    return true;
                }
                return false;
            }
        });
        if (this.mCurrIndicationType != -1) {
            this.mIndicationQueue.add(Integer.valueOf(i));
        }
        this.mLastIndicationSwitch = SystemClock.uptimeMillis();
        if (!TextUtils.equals(charSequence3, this.mCurrMessage) || i2 != this.mCurrIndicationType) {
            KeyguardIndicationTextView keyguardIndicationTextView = (KeyguardIndicationTextView) this.mView;
            KeyguardIndication keyguardIndication2 = (KeyguardIndication) this.mIndicationMessages.get(Integer.valueOf(i));
            if (keyguardIndication2 == null) {
                charSequence2 = null;
            } else {
                Objects.requireNonNull(keyguardIndicationTextView);
                charSequence2 = keyguardIndication2.mMessage;
            }
            keyguardIndicationTextView.switchIndication(charSequence2, keyguardIndication2, true, null);
        }
        if (this.mCurrIndicationType != -1 && this.mIndicationQueue.size() > 1) {
            KeyguardIndication keyguardIndication3 = (KeyguardIndication) this.mIndicationMessages.get(Integer.valueOf(i));
            long j = 0;
            if (!(keyguardIndication3 == null || (l = keyguardIndication3.mMinVisibilityMillis) == null)) {
                j = l.longValue();
            }
            long max = Math.max(j, 3500L);
            ShowNextIndication showNextIndication2 = this.mShowNextIndicationRunnable;
            if (showNextIndication2 != null) {
                Runnable runnable2 = showNextIndication2.mCancelDelayedRunnable;
                if (runnable2 != null) {
                    runnable2.run();
                    showNextIndication2.mCancelDelayedRunnable = null;
                }
                this.mShowNextIndicationRunnable = null;
            }
            this.mShowNextIndicationRunnable = new ShowNextIndication(max);
        }
    }

    public final void updateIndication(final int i, KeyguardIndication keyguardIndication, boolean z) {
        long j;
        boolean z2;
        boolean z3;
        Long l;
        Long l2;
        if (i != 10) {
            KeyguardIndication keyguardIndication2 = (KeyguardIndication) this.mIndicationMessages.get(Integer.valueOf(this.mCurrIndicationType));
            long j2 = 0;
            if (keyguardIndication2 == null || (l2 = keyguardIndication2.mMinVisibilityMillis) == null) {
                j = 0;
            } else {
                j = l2.longValue();
            }
            boolean z4 = true;
            if (keyguardIndication == null || TextUtils.isEmpty(keyguardIndication.mMessage)) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (!z2) {
                this.mIndicationMessages.remove(Integer.valueOf(i));
                this.mIndicationQueue.removeIf(new Predicate() { // from class: com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController$$ExternalSyntheticLambda0
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        if (((Integer) obj).intValue() == i) {
                            return true;
                        }
                        return false;
                    }
                });
            } else {
                if (!this.mIndicationQueue.contains(Integer.valueOf(i))) {
                    this.mIndicationQueue.add(Integer.valueOf(i));
                }
                this.mIndicationMessages.put(Integer.valueOf(i), keyguardIndication);
            }
            if (!this.mIsDozing) {
                long uptimeMillis = SystemClock.uptimeMillis() - this.mLastIndicationSwitch;
                if (uptimeMillis >= j) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (z2) {
                    int i2 = this.mCurrIndicationType;
                    if (i2 == -1 || i2 == i) {
                        showIndication(i);
                    } else if (!z) {
                        if (this.mShowNextIndicationRunnable == null) {
                            z4 = false;
                        }
                        if (!z4) {
                            KeyguardIndication keyguardIndication3 = (KeyguardIndication) this.mIndicationMessages.get(Integer.valueOf(i));
                            if (!(keyguardIndication3 == null || (l = keyguardIndication3.mMinVisibilityMillis) == null)) {
                                j2 = l.longValue();
                            }
                            long max = Math.max(j2, 3500L);
                            if (uptimeMillis >= max) {
                                showIndication(i);
                                return;
                            }
                            long j3 = max - uptimeMillis;
                            ShowNextIndication showNextIndication = this.mShowNextIndicationRunnable;
                            if (showNextIndication != null) {
                                Runnable runnable = showNextIndication.mCancelDelayedRunnable;
                                if (runnable != null) {
                                    runnable.run();
                                    showNextIndication.mCancelDelayedRunnable = null;
                                }
                                this.mShowNextIndicationRunnable = null;
                            }
                            this.mShowNextIndicationRunnable = new ShowNextIndication(j3);
                        }
                    } else if (z3) {
                        showIndication(i);
                    } else {
                        this.mIndicationQueue.removeIf(new Utils$$ExternalSyntheticLambda0(i, 1));
                        this.mIndicationQueue.add(0, Integer.valueOf(i));
                        long j4 = j - uptimeMillis;
                        ShowNextIndication showNextIndication2 = this.mShowNextIndicationRunnable;
                        if (showNextIndication2 != null) {
                            Runnable runnable2 = showNextIndication2.mCancelDelayedRunnable;
                            if (runnable2 != null) {
                                runnable2.run();
                                showNextIndication2.mCancelDelayedRunnable = null;
                            }
                            this.mShowNextIndicationRunnable = null;
                        }
                        this.mShowNextIndicationRunnable = new ShowNextIndication(j4);
                    }
                } else if (this.mCurrIndicationType == i && !z2 && z) {
                    if (z3) {
                        ShowNextIndication showNextIndication3 = this.mShowNextIndicationRunnable;
                        if (showNextIndication3 != null) {
                            Runnable runnable3 = showNextIndication3.mCancelDelayedRunnable;
                            if (runnable3 != null) {
                                runnable3.run();
                                showNextIndication3.mCancelDelayedRunnable = null;
                            }
                            showNextIndication3.mShowIndicationRunnable.run();
                            return;
                        }
                        showIndication(-1);
                        return;
                    }
                    long j5 = j - uptimeMillis;
                    ShowNextIndication showNextIndication4 = this.mShowNextIndicationRunnable;
                    if (showNextIndication4 != null) {
                        Runnable runnable4 = showNextIndication4.mCancelDelayedRunnable;
                        if (runnable4 != null) {
                            runnable4.run();
                            showNextIndication4.mCancelDelayedRunnable = null;
                        }
                        this.mShowNextIndicationRunnable = null;
                    }
                    this.mShowNextIndicationRunnable = new ShowNextIndication(j5);
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.keyguard.KeyguardIndicationRotateTextViewController$1] */
    public KeyguardIndicationRotateTextViewController(KeyguardIndicationTextView keyguardIndicationTextView, DelayableExecutor delayableExecutor, StatusBarStateController statusBarStateController) {
        super(keyguardIndicationTextView);
        this.mMaxAlpha = keyguardIndicationTextView.getAlpha();
        this.mExecutor = delayableExecutor;
        this.mInitialTextColorState = keyguardIndicationTextView.getTextColors();
        this.mStatusBarStateController = statusBarStateController;
        init();
    }
}
