package com.android.keyguard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import com.android.internal.util.EmergencyAffordanceManager;
import com.android.internal.widget.LockPatternUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public class EmergencyButton extends Button {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int mDownX;
    public int mDownY;
    public final EmergencyAffordanceManager mEmergencyAffordanceManager;
    public final boolean mEnableEmergencyCallWhileSimLocked;
    public LockPatternUtils mLockPatternUtils;
    public boolean mLongPressWasDragged;

    public EmergencyButton(Context context) {
        this(context, null);
    }

    public EmergencyButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mEnableEmergencyCallWhileSimLocked = ((Button) this).mContext.getResources().getBoolean(17891649);
        this.mEmergencyAffordanceManager = new EmergencyAffordanceManager(context);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mLockPatternUtils = new LockPatternUtils(((Button) this).mContext);
        if (this.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
            setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.keyguard.EmergencyButton$$ExternalSyntheticLambda0
                @Override // android.view.View.OnLongClickListener
                public final boolean onLongClick(View view) {
                    EmergencyButton emergencyButton = EmergencyButton.this;
                    int i = EmergencyButton.$r8$clinit;
                    Objects.requireNonNull(emergencyButton);
                    if (emergencyButton.mLongPressWasDragged || !emergencyButton.mEmergencyAffordanceManager.needsEmergencyAffordance()) {
                        return false;
                    }
                    emergencyButton.mEmergencyAffordanceManager.performEmergencyCall();
                    return true;
                }
            });
        }
    }

    @Override // android.widget.TextView, android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (motionEvent.getActionMasked() == 0) {
            this.mDownX = x;
            this.mDownY = y;
            this.mLongPressWasDragged = false;
        } else {
            int abs = Math.abs(x - this.mDownX);
            int abs2 = Math.abs(y - this.mDownY);
            int scaledTouchSlop = ViewConfiguration.get(((Button) this).mContext).getScaledTouchSlop();
            if (Math.abs(abs2) > scaledTouchSlop || Math.abs(abs) > scaledTouchSlop) {
                this.mLongPressWasDragged = true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.widget.TextView, android.view.View
    public final boolean performLongClick() {
        return super.performLongClick();
    }
}
