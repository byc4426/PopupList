package com.frank.popuplist;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;
/**
 * This utility class can add a horizontal popup-menu easily
 * 该工具类可以很方便的为View、ListView/GridView绑定长按弹出横向气泡菜单
 */
public class PopupList {

    private static final int DEFAULT_NORMAL_TEXT_COLOR = Color.WHITE;
    private static final int DEFAULT_PRESSED_TEXT_COLOR = Color.WHITE;
    private static final float DEFAULT_TEXT_SIZE_PIXEL = 30;
    private static final int DEFAULT_NORMAL_BACKGROUND_COLOR = 0xFF444444;
    private static final int DEFAULT_PRESSED_BACKGROUND_COLOR = 0xFF777777;
    private static final int DEFAULT_BACKGROUND_RADIUS_PIXEL = 16;
    private static final int DEFAULT_DIVIDER_COLOR = 0xFF888888;
    private static final int DEFAULT_DIVIDER_WIDTH_PIXEL = 1;
    private static final int DEFAULT_DIVIDER_HEIGHT_PIXEL = 20;

    private Context mContext;
    private PopupWindow mPopupWindow;
    private View mAnchorView;
    private View mContextView;
    private View mIndicatorView;
    private List<String> mPopupItemList;
    private OnPopupListClickListener mOnPopupListClickListener;
    private int mContextPosition;
    private float mRawX;
    private float mRawY;
    private StateListDrawable mLeftItemBackground;
    private StateListDrawable mCenterItemBackground;
    private StateListDrawable mRightItemBackground;
    private StateListDrawable mCornerItemBackground;
    private ColorStateList mTextColorStateList;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mPopupWindowWidth;
    private int mPopupWindowHeight;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mNormalTextColor;
    private int mPressedTextColor;
    private float mTextSizePixel;
    private int mNormalBackgroundColor;
    private int mPressedBackgroundColor;
    private int mBackgroundCornerRadiusPixel;
    private int mDividerColor;
    private int mDividerWidthPixel;
    private int mDividerHeightPixel;

    public void init(Context context, View anchorView, List<String> popupItemList, OnPopupListClickListener onPopupListClickListener) {
        this.mNormalTextColor = DEFAULT_NORMAL_TEXT_COLOR;
        this.mPressedTextColor = DEFAULT_PRESSED_TEXT_COLOR;
        this.mTextSizePixel = DEFAULT_TEXT_SIZE_PIXEL;
        this.mNormalBackgroundColor = DEFAULT_NORMAL_BACKGROUND_COLOR;
        this.mPressedBackgroundColor = DEFAULT_PRESSED_BACKGROUND_COLOR;
        this.mBackgroundCornerRadiusPixel = DEFAULT_BACKGROUND_RADIUS_PIXEL;
        this.mDividerColor = DEFAULT_DIVIDER_COLOR;
        this.mDividerWidthPixel = DEFAULT_DIVIDER_WIDTH_PIXEL;
        this.mDividerHeightPixel = DEFAULT_DIVIDER_HEIGHT_PIXEL;
        this.mContext = context;
        this.mAnchorView = anchorView;
        this.mPopupItemList = popupItemList;
        this.mOnPopupListClickListener = onPopupListClickListener;
        this.mPopupWindow = null;
        mAnchorView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mRawX = event.getRawX();
                mRawY = event.getRawY();
                return false;
            }
        });
        if (mAnchorView instanceof AbsListView) {
            ((AbsListView) mAnchorView).setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    mContextView = view;
                    mContextPosition = position;
                    showPopupListWindow();
                    return true;
                }
            });
        } else {
            mAnchorView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContextView = v;
                    showPopupListWindow();
                }
            });
        }
        if (mScreenWidth == 0) {
            mScreenWidth = getScreenWidth();
        }
        if (mScreenHeight == 0) {
            mScreenHeight = getScreenHeight();
        }
        refreshBackgroundOrRadiusStateList(mPressedBackgroundColor, mNormalBackgroundColor, mBackgroundCornerRadiusPixel);
        refreshTextColorStateList(mPressedTextColor, mNormalTextColor);
    }

    private void showPopupListWindow() {
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
        if (mPopupWindow == null) {
            LinearLayout contentView = new LinearLayout(mContext);
            contentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            contentView.setOrientation(LinearLayout.VERTICAL);
            LinearLayout popupListContainer = new LinearLayout(mContext);
            popupListContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            popupListContainer.setOrientation(LinearLayout.HORIZONTAL);
            popupListContainer.setBackgroundDrawable(mCornerItemBackground);
            contentView.addView(popupListContainer);
            if (mIndicatorView != null) {
                LinearLayout.LayoutParams layoutParams;
                if (mIndicatorView.getLayoutParams() == null) {
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                } else {
                    layoutParams = (LinearLayout.LayoutParams) mIndicatorView.getLayoutParams();
                }
                layoutParams.gravity = Gravity.CENTER;
                mIndicatorView.setLayoutParams(layoutParams);
                contentView.addView(mIndicatorView);
            }
            for (int i = 0; i < mPopupItemList.size(); i++) {
                TextView textView = new TextView(mContext);
                textView.setTextColor(mTextColorStateList);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizePixel);
                textView.setPadding(dp2px(7), dp2px(5), dp2px(7), dp2px(5));
                textView.setClickable(true);
                final int finalI = i;
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnPopupListClickListener != null) {
                            mOnPopupListClickListener.onPopupListClick(mContextView, mContextPosition, finalI);
                            hidePopupListWindow();
                        }
                    }
                });
                textView.setText(mPopupItemList.get(i));
                if (mPopupItemList.size() > 1 && i == 0) {
                    textView.setBackgroundDrawable(mLeftItemBackground);
                } else if (mPopupItemList.size() > 1 && i == mPopupItemList.size() - 1) {
                    textView.setBackgroundDrawable(mRightItemBackground);
                } else if (mPopupItemList.size() == 1) {
                    textView.setBackgroundDrawable(mCornerItemBackground);
                } else {
                    textView.setBackgroundDrawable(mCenterItemBackground);
                }
                popupListContainer.addView(textView);
                if (mPopupItemList.size() > 1 && i != mPopupItemList.size() - 1) {
                    View divider = new View(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mDividerWidthPixel, mDividerHeightPixel);
                    layoutParams.gravity = Gravity.CENTER;
                    divider.setLayoutParams(layoutParams);
                    divider.setBackgroundColor(mDividerColor);
                    popupListContainer.addView(divider);
                }
            }
            if (mPopupWindowWidth == 0) {
                mPopupWindowWidth = getViewWidth(popupListContainer);
            }
            if (mIndicatorView != null && mIndicatorWidth == 0) {
                if (mIndicatorView.getLayoutParams().width > 0) {
                    mIndicatorWidth = mIndicatorView.getLayoutParams().width;
                } else {
                    mIndicatorWidth = getViewWidth(mIndicatorView);
                }
            }
            if (mIndicatorView != null && mIndicatorHeight == 0) {
                if (mIndicatorView.getLayoutParams().height > 0) {
                    mIndicatorHeight = mIndicatorView.getLayoutParams().height;
                } else {
                    mIndicatorHeight = getViewHeight(mIndicatorView);
                }
            }
            if (mPopupWindowHeight == 0) {
                mPopupWindowHeight = getViewHeight(popupListContainer) + mIndicatorHeight;
            }
            mPopupWindow = new PopupWindow(contentView, mPopupWindowWidth, mPopupWindowHeight, true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        if (mIndicatorView != null) {
            float marginLeftScreenEdge = mRawX;
            float marginRightScreenEdge = mScreenWidth - mRawX;
            if (marginLeftScreenEdge < mPopupWindowWidth / 2f) {
                if (marginLeftScreenEdge < mIndicatorWidth / 2f + mBackgroundCornerRadiusPixel) {
                    mIndicatorView.setTranslationX(mIndicatorWidth / 2f + mBackgroundCornerRadiusPixel - mPopupWindowWidth / 2f);
                } else {
                    mIndicatorView.setTranslationX(marginLeftScreenEdge - mPopupWindowWidth / 2f);
                }
            } else if (marginRightScreenEdge < mPopupWindowWidth / 2f) {
                if (marginRightScreenEdge < mIndicatorWidth / 2f + mBackgroundCornerRadiusPixel) {
                    mIndicatorView.setTranslationX(mPopupWindowWidth / 2f - mIndicatorWidth / 2f - mBackgroundCornerRadiusPixel);
                } else {
                    mIndicatorView.setTranslationX(mPopupWindowWidth / 2f - marginRightScreenEdge);
                }
            } else {
                mIndicatorView.setTranslationX(0);
            }
        }
        mPopupWindow.showAtLocation(mAnchorView, Gravity.CENTER,
                (int) mRawX - mScreenWidth / 2,
                (int) mRawY - mScreenHeight / 2 - mPopupWindowHeight + mIndicatorHeight);
    }

    private void refreshBackgroundOrRadiusStateList(int pressedBackgroundColor, int normalBackgroundColor, int backgroundCornerRadiusPixel) {
        // left
        GradientDrawable leftItemPressedDrawable = new GradientDrawable();
        leftItemPressedDrawable.setColor(pressedBackgroundColor);
        leftItemPressedDrawable.setCornerRadii(new float[]{
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                0, 0,
                0, 0,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel});
        GradientDrawable leftItemNormalDrawable = new GradientDrawable();
        leftItemNormalDrawable.setColor(normalBackgroundColor);
        leftItemNormalDrawable.setCornerRadii(new float[]{
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                0, 0,
                0, 0,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel});
        mLeftItemBackground = new StateListDrawable();
        mLeftItemBackground.addState(new int[]{android.R.attr.state_pressed}, leftItemPressedDrawable);
        mLeftItemBackground.addState(new int[]{}, leftItemNormalDrawable);
        // center
        GradientDrawable centerItemPressedDrawable = new GradientDrawable();
        centerItemPressedDrawable.setColor(pressedBackgroundColor);
        GradientDrawable centerItemNormalDrawable = new GradientDrawable();
        centerItemNormalDrawable.setColor(normalBackgroundColor);
        mCenterItemBackground = new StateListDrawable();
        mCenterItemBackground.addState(new int[]{android.R.attr.state_pressed}, centerItemPressedDrawable);
        mCenterItemBackground.addState(new int[]{}, centerItemNormalDrawable);
        // right
        GradientDrawable rightItemPressedDrawable = new GradientDrawable();
        rightItemPressedDrawable.setColor(pressedBackgroundColor);
        rightItemPressedDrawable.setCornerRadii(new float[]{
                0, 0,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                0, 0});
        GradientDrawable rightItemNormalDrawable = new GradientDrawable();
        rightItemNormalDrawable.setColor(normalBackgroundColor);
        rightItemNormalDrawable.setCornerRadii(new float[]{
                0, 0,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                0, 0});
        mRightItemBackground = new StateListDrawable();
        mRightItemBackground.addState(new int[]{android.R.attr.state_pressed}, rightItemPressedDrawable);
        mRightItemBackground.addState(new int[]{}, rightItemNormalDrawable);
        // corner
        GradientDrawable cornerItemPressedDrawable = new GradientDrawable();
        cornerItemPressedDrawable.setColor(pressedBackgroundColor);
        cornerItemPressedDrawable.setCornerRadii(new float[]{
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel});
        GradientDrawable cornerItemNormalDrawable = new GradientDrawable();
        cornerItemNormalDrawable.setColor(normalBackgroundColor);
        cornerItemNormalDrawable.setCornerRadii(new float[]{
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel,
                backgroundCornerRadiusPixel, backgroundCornerRadiusPixel});
        mCornerItemBackground = new StateListDrawable();
        mCornerItemBackground.addState(new int[]{android.R.attr.state_pressed}, cornerItemPressedDrawable);
        mCornerItemBackground.addState(new int[]{}, cornerItemNormalDrawable);
    }

    private void refreshTextColorStateList(int pressedTextColor, int normalTextColor) {
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_pressed};
        states[1] = new int[]{};
        int[] colors = new int[]{pressedTextColor, normalTextColor};
        mTextColorStateList = new ColorStateList(states, colors);
    }

    private int dp2px(float value) {
        final float scale = mContext.getResources().getDisplayMetrics().densityDpi;
        return (int) (value * (scale / 160) + 0.5f);
    }

    private int getScreenWidth() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private int getScreenHeight() {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    private int getViewWidth(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return view.getMeasuredWidth();
    }

    private int getViewHeight(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        return view.getMeasuredHeight();
    }

    public void hidePopupListWindow() {
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

    public View getIndicatorView() {
        return mIndicatorView;
    }

    public void setIndicatorView(View indicatorView) {
        this.mIndicatorView = indicatorView;
    }

    public void setIndicatorSize(int widthPixel, int heightPixel) {
        this.mIndicatorWidth = widthPixel;
        this.mIndicatorHeight = heightPixel;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(mIndicatorWidth, mIndicatorHeight);
        layoutParams.gravity = Gravity.CENTER;
        if (mIndicatorView != null) {
            mIndicatorView.setLayoutParams(layoutParams);
        }
    }

    public int getNormalTextColor() {
        return mNormalTextColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.mNormalTextColor = normalTextColor;
        refreshTextColorStateList(mPressedTextColor, mNormalTextColor);
    }

    public int getPressedTextColor() {
        return mPressedTextColor;
    }

    public void setPressedTextColor(int pressedTextColor) {
        this.mPressedTextColor = pressedTextColor;
        refreshTextColorStateList(mPressedTextColor, mNormalTextColor);
    }

    public float getTextSizePixel() {
        return mTextSizePixel;
    }

    public void setTextSizePixel(float textSizePixel) {
        this.mTextSizePixel = textSizePixel;
    }

    public int getNormalBackgroundColor() {
        return mNormalBackgroundColor;
    }

    public void setNormalBackgroundColor(int normalBackgroundColor) {
        this.mNormalBackgroundColor = normalBackgroundColor;
        refreshBackgroundOrRadiusStateList(mPressedBackgroundColor, mNormalBackgroundColor, mBackgroundCornerRadiusPixel);
    }

    public int getPressedBackgroundColor() {
        return mPressedBackgroundColor;
    }

    public void setPressedBackgroundColor(int pressedBackgroundColor) {
        this.mPressedBackgroundColor = pressedBackgroundColor;
        refreshBackgroundOrRadiusStateList(mPressedBackgroundColor, mNormalBackgroundColor, mBackgroundCornerRadiusPixel);
    }

    public int getBackgroundCornerRadiusPixel() {
        return mBackgroundCornerRadiusPixel;
    }

    public void setBackgroundCornerRadiusPixel(int backgroundCornerRadiusPixel) {
        this.mBackgroundCornerRadiusPixel = backgroundCornerRadiusPixel;
        refreshBackgroundOrRadiusStateList(mPressedBackgroundColor, mNormalBackgroundColor, mBackgroundCornerRadiusPixel);
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
    }

    public int getDividerWidthPixel() {
        return mDividerWidthPixel;
    }

    public void setDividerWidthPixel(int dividerWidthPixel) {
        this.mDividerWidthPixel = dividerWidthPixel;
    }

    public int getDividerHeightPixel() {
        return mDividerHeightPixel;
    }

    public void setDividerHeightPixel(int dividerHeightPixel) {
        this.mDividerHeightPixel = dividerHeightPixel;
    }

    public interface OnPopupListClickListener {
        void onPopupListClick(View contextView, int contextPosition, int position);
    }

}
