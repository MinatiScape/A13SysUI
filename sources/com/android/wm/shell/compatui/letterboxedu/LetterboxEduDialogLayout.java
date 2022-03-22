package com.android.wm.shell.compatui.letterboxedu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda5;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class LetterboxEduDialogLayout extends ConstraintLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Drawable mBackgroundDim;
    public View mDialogContainer;

    public LetterboxEduDialogLayout(Context context) {
        this(context, null);
    }

    public final void setDismissOnClickListener(final TaskView$$ExternalSyntheticLambda5 taskView$$ExternalSyntheticLambda5) {
        View.OnClickListener onClickListener;
        LetterboxEduDialogLayout$$ExternalSyntheticLambda1 letterboxEduDialogLayout$$ExternalSyntheticLambda1 = null;
        if (taskView$$ExternalSyntheticLambda5 == null) {
            onClickListener = null;
        } else {
            onClickListener = new View.OnClickListener() { // from class: com.android.wm.shell.compatui.letterboxedu.LetterboxEduDialogLayout$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    Runnable runnable = taskView$$ExternalSyntheticLambda5;
                    int i = LetterboxEduDialogLayout.$r8$clinit;
                    runnable.run();
                }
            };
        }
        findViewById(2131428252).setOnClickListener(onClickListener);
        setOnClickListener(onClickListener);
        View view = this.mDialogContainer;
        if (taskView$$ExternalSyntheticLambda5 != null) {
            letterboxEduDialogLayout$$ExternalSyntheticLambda1 = LetterboxEduDialogLayout$$ExternalSyntheticLambda1.INSTANCE;
        }
        view.setOnClickListener(letterboxEduDialogLayout$$ExternalSyntheticLambda1);
    }

    public LetterboxEduDialogLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LetterboxEduDialogLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mDialogContainer = findViewById(2131428251);
        Drawable mutate = getBackground().mutate();
        this.mBackgroundDim = mutate;
        mutate.setAlpha(0);
    }

    public LetterboxEduDialogLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
