package androidx.appcompat.app;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.appcompat.app.AlertController;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.widget.NestedScrollView;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class AlertDialog extends AppCompatDialog {
    public final AlertController mAlert = new AlertController(getContext(), this, getWindow());

    /* loaded from: classes.dex */
    public static class Builder {
        public final AlertController.AlertParams P;
        public final int mTheme;

        public Builder(Context context) {
            int resolveDialogTheme = AlertDialog.resolveDialogTheme(context, 0);
            this.P = new AlertController.AlertParams(new ContextThemeWrapper(context, AlertDialog.resolveDialogTheme(context, resolveDialogTheme)));
            this.mTheme = resolveDialogTheme;
        }

        public final AlertDialog create() {
            int i;
            AlertDialog alertDialog = new AlertDialog(this.P.mContext, this.mTheme);
            final AlertController.AlertParams alertParams = this.P;
            final AlertController alertController = alertDialog.mAlert;
            Objects.requireNonNull(alertParams);
            View view = alertParams.mCustomTitleView;
            if (view != null) {
                Objects.requireNonNull(alertController);
                alertController.mCustomTitleView = view;
            } else {
                CharSequence charSequence = alertParams.mTitle;
                if (charSequence != null) {
                    Objects.requireNonNull(alertController);
                    alertController.mTitle = charSequence;
                    TextView textView = alertController.mTitleView;
                    if (textView != null) {
                        textView.setText(charSequence);
                    }
                }
                Drawable drawable = alertParams.mIcon;
                if (drawable != null) {
                    Objects.requireNonNull(alertController);
                    alertController.mIcon = drawable;
                    alertController.mIconId = 0;
                    ImageView imageView = alertController.mIconView;
                    if (imageView != null) {
                        imageView.setVisibility(0);
                        alertController.mIconView.setImageDrawable(drawable);
                    }
                }
            }
            CharSequence charSequence2 = alertParams.mPositiveButtonText;
            if (charSequence2 != null) {
                alertController.setButton(-1, charSequence2, alertParams.mPositiveButtonListener);
            }
            CharSequence charSequence3 = alertParams.mNegativeButtonText;
            if (charSequence3 != null) {
                alertController.setButton(-2, charSequence3, alertParams.mNegativeButtonListener);
            }
            if (alertParams.mAdapter != null) {
                AlertController.RecycleListView recycleListView = (AlertController.RecycleListView) alertParams.mInflater.inflate(alertController.mListLayout, (ViewGroup) null);
                if (alertParams.mIsSingleChoice) {
                    i = alertController.mSingleChoiceItemLayout;
                } else {
                    i = alertController.mListItemLayout;
                }
                ListAdapter listAdapter = alertParams.mAdapter;
                if (listAdapter == null) {
                    listAdapter = new AlertController.CheckedItemAdapter(alertParams.mContext, i);
                }
                alertController.mAdapter = listAdapter;
                alertController.mCheckedItem = alertParams.mCheckedItem;
                if (alertParams.mOnClickListener != null) {
                    recycleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: androidx.appcompat.app.AlertController.AlertParams.3
                        @Override // android.widget.AdapterView.OnItemClickListener
                        public final void onItemClick(AdapterView<?> adapterView, View view2, int i2, long j) {
                            alertParams.mOnClickListener.onClick(alertController.mDialog, i2);
                            if (!alertParams.mIsSingleChoice) {
                                alertController.mDialog.dismiss();
                            }
                        }
                    });
                }
                if (alertParams.mIsSingleChoice) {
                    recycleListView.setChoiceMode(1);
                }
                alertController.mListView = recycleListView;
            }
            int i2 = alertParams.mViewLayoutResId;
            if (i2 != 0) {
                Objects.requireNonNull(alertController);
                alertController.mView = null;
                alertController.mViewLayoutResId = i2;
                alertController.mViewSpacingSpecified = false;
            }
            Objects.requireNonNull(this.P);
            alertDialog.setCancelable(true);
            Objects.requireNonNull(this.P);
            alertDialog.setCanceledOnTouchOutside(true);
            Objects.requireNonNull(this.P);
            alertDialog.setOnCancelListener(null);
            alertDialog.setOnDismissListener(this.P.mOnDismissListener);
            DialogInterface.OnKeyListener onKeyListener = this.P.mOnKeyListener;
            if (onKeyListener != null) {
                alertDialog.setOnKeyListener(onKeyListener);
            }
            return alertDialog;
        }
    }

    public static int resolveDialogTheme(Context context, int i) {
        if (((i >>> 24) & 255) >= 1) {
            return i;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(2130968621, typedValue, true);
        return typedValue.resourceId;
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        boolean z;
        AlertController alertController = this.mAlert;
        Objects.requireNonNull(alertController);
        NestedScrollView nestedScrollView = alertController.mScrollView;
        if (nestedScrollView == null || !nestedScrollView.executeKeyEvent(keyEvent)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // android.app.Dialog, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        boolean z;
        AlertController alertController = this.mAlert;
        Objects.requireNonNull(alertController);
        NestedScrollView nestedScrollView = alertController.mScrollView;
        if (nestedScrollView == null || !nestedScrollView.executeKeyEvent(keyEvent)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    public AlertDialog(Context context, int i) {
        super(context, resolveDialogTheme(context, i));
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        int i;
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        int i2;
        boolean z6;
        ListAdapter listAdapter;
        int i3;
        int i4;
        View findViewById;
        super.onCreate(bundle);
        AlertController alertController = this.mAlert;
        Objects.requireNonNull(alertController);
        if (alertController.mButtonPanelSideLayout == 0) {
            i = alertController.mAlertDialogLayout;
        } else {
            i = alertController.mAlertDialogLayout;
        }
        alertController.mDialog.setContentView(i);
        View findViewById2 = alertController.mWindow.findViewById(2131428562);
        View findViewById3 = findViewById2.findViewById(2131429069);
        View findViewById4 = findViewById2.findViewById(2131427739);
        View findViewById5 = findViewById2.findViewById(2131427642);
        ViewGroup viewGroup = (ViewGroup) findViewById2.findViewById(2131427791);
        View view = alertController.mView;
        View view2 = null;
        int i5 = 0;
        if (view == null) {
            if (alertController.mViewLayoutResId != 0) {
                view = LayoutInflater.from(alertController.mContext).inflate(alertController.mViewLayoutResId, viewGroup, false);
            } else {
                view = null;
            }
        }
        if (view != null) {
            z = true;
        } else {
            z = false;
        }
        if (!z || !AlertController.canTextInput(view)) {
            alertController.mWindow.setFlags(131072, 131072);
        }
        if (z) {
            FrameLayout frameLayout = (FrameLayout) alertController.mWindow.findViewById(2131427789);
            frameLayout.addView(view, new ViewGroup.LayoutParams(-1, -1));
            if (alertController.mViewSpacingSpecified) {
                frameLayout.setPadding(0, 0, 0, 0);
            }
            if (alertController.mListView != null) {
                ((LinearLayout.LayoutParams) ((LinearLayoutCompat.LayoutParams) viewGroup.getLayoutParams())).weight = 0.0f;
            }
        } else {
            viewGroup.setVisibility(8);
        }
        View findViewById6 = viewGroup.findViewById(2131429069);
        View findViewById7 = viewGroup.findViewById(2131427739);
        View findViewById8 = viewGroup.findViewById(2131427642);
        ViewGroup resolvePanel = AlertController.resolvePanel(findViewById6, findViewById3);
        ViewGroup resolvePanel2 = AlertController.resolvePanel(findViewById7, findViewById4);
        ViewGroup resolvePanel3 = AlertController.resolvePanel(findViewById8, findViewById5);
        NestedScrollView nestedScrollView = (NestedScrollView) alertController.mWindow.findViewById(2131428795);
        alertController.mScrollView = nestedScrollView;
        nestedScrollView.setFocusable(false);
        alertController.mScrollView.setNestedScrollingEnabled(false);
        TextView textView = (TextView) resolvePanel2.findViewById(16908299);
        alertController.mMessageView = textView;
        if (textView != null) {
            textView.setVisibility(8);
            alertController.mScrollView.removeView(alertController.mMessageView);
            if (alertController.mListView != null) {
                ViewGroup viewGroup2 = (ViewGroup) alertController.mScrollView.getParent();
                int indexOfChild = viewGroup2.indexOfChild(alertController.mScrollView);
                viewGroup2.removeViewAt(indexOfChild);
                viewGroup2.addView(alertController.mListView, indexOfChild, new ViewGroup.LayoutParams(-1, -1));
            } else {
                resolvePanel2.setVisibility(8);
            }
        }
        Button button = (Button) resolvePanel3.findViewById(16908313);
        alertController.mButtonPositive = button;
        button.setOnClickListener(alertController.mButtonHandler);
        if (!TextUtils.isEmpty(alertController.mButtonPositiveText) || alertController.mButtonPositiveIcon != null) {
            alertController.mButtonPositive.setText(alertController.mButtonPositiveText);
            Drawable drawable = alertController.mButtonPositiveIcon;
            if (drawable != null) {
                int i6 = alertController.mButtonIconDimen;
                drawable.setBounds(0, 0, i6, i6);
                alertController.mButtonPositive.setCompoundDrawables(alertController.mButtonPositiveIcon, null, null, null);
            }
            alertController.mButtonPositive.setVisibility(0);
            z2 = true;
        } else {
            alertController.mButtonPositive.setVisibility(8);
            z2 = false;
        }
        Button button2 = (Button) resolvePanel3.findViewById(16908314);
        alertController.mButtonNegative = button2;
        button2.setOnClickListener(alertController.mButtonHandler);
        if (!TextUtils.isEmpty(alertController.mButtonNegativeText) || alertController.mButtonNegativeIcon != null) {
            alertController.mButtonNegative.setText(alertController.mButtonNegativeText);
            Drawable drawable2 = alertController.mButtonNegativeIcon;
            if (drawable2 != null) {
                int i7 = alertController.mButtonIconDimen;
                drawable2.setBounds(0, 0, i7, i7);
                alertController.mButtonNegative.setCompoundDrawables(alertController.mButtonNegativeIcon, null, null, null);
            }
            alertController.mButtonNegative.setVisibility(0);
            z2 |= true;
        } else {
            alertController.mButtonNegative.setVisibility(8);
        }
        Button button3 = (Button) resolvePanel3.findViewById(16908315);
        alertController.mButtonNeutral = button3;
        button3.setOnClickListener(alertController.mButtonHandler);
        if (!TextUtils.isEmpty(alertController.mButtonNeutralText) || alertController.mButtonNeutralIcon != null) {
            alertController.mButtonNeutral.setText(alertController.mButtonNeutralText);
            Drawable drawable3 = alertController.mButtonNeutralIcon;
            if (drawable3 != null) {
                int i8 = alertController.mButtonIconDimen;
                drawable3.setBounds(0, 0, i8, i8);
                alertController.mButtonNeutral.setCompoundDrawables(alertController.mButtonNeutralIcon, null, null, null);
            }
            alertController.mButtonNeutral.setVisibility(0);
            z2 |= true;
        } else {
            alertController.mButtonNeutral.setVisibility(8);
        }
        Context context = alertController.mContext;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(2130968619, typedValue, true);
        if (typedValue.data != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z3) {
            if (z2) {
                AlertController.centerButton(alertController.mButtonPositive);
            } else if (z2) {
                AlertController.centerButton(alertController.mButtonNegative);
            } else if (z2) {
                AlertController.centerButton(alertController.mButtonNeutral);
            }
        }
        if (z2) {
            z4 = true;
        } else {
            z4 = false;
        }
        if (!z4) {
            resolvePanel3.setVisibility(8);
        }
        if (alertController.mCustomTitleView != null) {
            resolvePanel.addView(alertController.mCustomTitleView, 0, new ViewGroup.LayoutParams(-1, -2));
            alertController.mWindow.findViewById(2131429063).setVisibility(8);
        } else {
            alertController.mIconView = (ImageView) alertController.mWindow.findViewById(16908294);
            if (!(!TextUtils.isEmpty(alertController.mTitle)) || !alertController.mShowTitle) {
                alertController.mWindow.findViewById(2131429063).setVisibility(8);
                alertController.mIconView.setVisibility(8);
                resolvePanel.setVisibility(8);
            } else {
                TextView textView2 = (TextView) alertController.mWindow.findViewById(2131427470);
                alertController.mTitleView = textView2;
                textView2.setText(alertController.mTitle);
                int i9 = alertController.mIconId;
                if (i9 != 0) {
                    alertController.mIconView.setImageResource(i9);
                } else {
                    Drawable drawable4 = alertController.mIcon;
                    if (drawable4 != null) {
                        alertController.mIconView.setImageDrawable(drawable4);
                    } else {
                        alertController.mTitleView.setPadding(alertController.mIconView.getPaddingLeft(), alertController.mIconView.getPaddingTop(), alertController.mIconView.getPaddingRight(), alertController.mIconView.getPaddingBottom());
                        alertController.mIconView.setVisibility(8);
                    }
                }
            }
        }
        if (viewGroup.getVisibility() != 8) {
            z5 = true;
        } else {
            z5 = false;
        }
        if (resolvePanel == null || resolvePanel.getVisibility() == 8) {
            i2 = 0;
        } else {
            i2 = 1;
        }
        if (resolvePanel3.getVisibility() != 8) {
            z6 = true;
        } else {
            z6 = false;
        }
        if (!z6 && (findViewById = resolvePanel2.findViewById(2131429028)) != null) {
            findViewById.setVisibility(0);
        }
        if (i2 != 0) {
            NestedScrollView nestedScrollView2 = alertController.mScrollView;
            if (nestedScrollView2 != null) {
                nestedScrollView2.setClipToPadding(true);
            }
            if (alertController.mListView != null) {
                view2 = resolvePanel.findViewById(2131429058);
            }
            if (view2 != null) {
                view2.setVisibility(0);
            }
        } else {
            View findViewById9 = resolvePanel2.findViewById(2131429029);
            if (findViewById9 != null) {
                findViewById9.setVisibility(0);
            }
        }
        AlertController.RecycleListView recycleListView = alertController.mListView;
        if (recycleListView instanceof AlertController.RecycleListView) {
            if (!z6 || i2 == 0) {
                int paddingLeft = recycleListView.getPaddingLeft();
                if (i2 != 0) {
                    i3 = recycleListView.getPaddingTop();
                } else {
                    i3 = recycleListView.mPaddingTopNoTitle;
                }
                int paddingRight = recycleListView.getPaddingRight();
                if (z6) {
                    i4 = recycleListView.getPaddingBottom();
                } else {
                    i4 = recycleListView.mPaddingBottomNoButtons;
                }
                recycleListView.setPadding(paddingLeft, i3, paddingRight, i4);
            } else {
                Objects.requireNonNull(recycleListView);
            }
        }
        if (!z5) {
            View view3 = alertController.mListView;
            if (view3 == null) {
                view3 = alertController.mScrollView;
            }
            if (view3 != null) {
                if (z6) {
                    i5 = 2;
                }
                int i10 = i2 | i5;
                View findViewById10 = alertController.mWindow.findViewById(2131428794);
                View findViewById11 = alertController.mWindow.findViewById(2131428793);
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                ViewCompat.Api23Impl.setScrollIndicators(view3, i10, 3);
                if (findViewById10 != null) {
                    resolvePanel2.removeView(findViewById10);
                }
                if (findViewById11 != null) {
                    resolvePanel2.removeView(findViewById11);
                }
            }
        }
        AlertController.RecycleListView recycleListView2 = alertController.mListView;
        if (recycleListView2 != null && (listAdapter = alertController.mAdapter) != null) {
            recycleListView2.setAdapter(listAdapter);
            int i11 = alertController.mCheckedItem;
            if (i11 > -1) {
                recycleListView2.setItemChecked(i11, true);
                recycleListView2.setSelection(i11);
            }
        }
    }

    @Override // androidx.appcompat.app.AppCompatDialog, android.app.Dialog
    public final void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        AlertController alertController = this.mAlert;
        Objects.requireNonNull(alertController);
        alertController.mTitle = charSequence;
        TextView textView = alertController.mTitleView;
        if (textView != null) {
            textView.setText(charSequence);
        }
    }
}
