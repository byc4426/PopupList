package com.frank.popuplist;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

public class PopupList {

    private Context mContext;
    private PopupWindow mPopupWindow;
    private ViewGroup mPopupListContainer;
    private ImageView mIndicator;
    private float mRawX;
    private float mRawY;
    private View mAnchorView;
    private View mContextView;
    private int mContextPosition;
    private List<String> mPopupItemList;
    private OnPopupListClickListener mOnPopupListClickListener;
    private int mPopupListContainerWidth;
    private int mIndicatorWidth;
    private int mIndicatorHeight;
    private int mPopupWindowHeight;
    private int mScreenWidth;
    private int mScreenHeight;

    public PopupList() {
    }

    public void init(Context context, View anchorView, List<String> popupItemList, OnPopupListClickListener onPopupListClickListener) {
        this.mContext = context;
        this.mAnchorView = anchorView;
        this.mPopupItemList = popupItemList;
        this.mOnPopupListClickListener = onPopupListClickListener;
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
    }

    private void showPopupListWindow() {
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
        if (mPopupWindow == null) {
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.popuplist_window, null);
            mPopupListContainer = (ViewGroup) contentView.findViewById(R.id.vg_popuplist_container);
            mIndicator = (ImageView) contentView.findViewById(R.id.iv_indicator);
            for (int i = 0; i < mPopupItemList.size(); i++) {
                TextView textView = new TextView(mContext);
                textView.setTextColor(Color.WHITE);
                textView.setTextSize(16);
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
                    textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.popuplist_left_item_bg));
                } else if (mPopupItemList.size() > 1 && i == mPopupItemList.size() - 1) {
                    textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.popuplist_right_item_bg));
                } else if (mPopupItemList.size() == 1) {
                    textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.popuplist_corner_item_bg));
                } else {
                    textView.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.popuplist_center_item_bg));
                }
                mPopupListContainer.addView(textView);
                if (mPopupItemList.size() > 1 && i != mPopupItemList.size() - 1) {
                    View view = new View(mContext);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(1, dp2px(16));
                    layoutParams.gravity = Gravity.CENTER;
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundColor(Color.GRAY);
                    mPopupListContainer.addView(view);
                }
            }
            if (mPopupListContainerWidth == 0) {
                mPopupListContainerWidth = getViewWidth(mPopupListContainer);
            }
            if (mIndicatorWidth == 0) {
                mIndicatorWidth = mIndicator.getLayoutParams().width;
            }
            if (mIndicatorHeight == 0) {
                mIndicatorHeight = mIndicator.getLayoutParams().height;
            }
            if (mPopupWindowHeight == 0) {
                mPopupWindowHeight = getViewHeight(mPopupListContainer) + mIndicatorHeight;
            }
            mPopupWindow = new PopupWindow(contentView, mPopupListContainerWidth, mPopupWindowHeight, true);
            mPopupWindow.setTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        }
        float leftEdgeOffset = mRawX;
        float rightEdgeOffset = mScreenWidth - mRawX;
        if (leftEdgeOffset < mPopupListContainerWidth / 2) {
            if (leftEdgeOffset < mIndicatorWidth / 2.0f) {
                mIndicator.setTranslationX(dp2px(8));
            } else {
                mIndicator.setTranslationX(leftEdgeOffset - mIndicatorWidth / 2.0f);
            }
        } else if (rightEdgeOffset < mPopupListContainerWidth / 2) {
            if (rightEdgeOffset < mIndicatorWidth / 2.0f) {
                mIndicator.setTranslationX(mPopupListContainerWidth - rightEdgeOffset - mIndicatorWidth / 2.0f - dp2px(8));
            } else {
                mIndicator.setTranslationX(mPopupListContainerWidth - rightEdgeOffset - mIndicatorWidth / 2.0f);
            }
        } else {
            mIndicator.setTranslationX(mPopupListContainerWidth / 2 - mIndicatorWidth / 2.0f);
        }
        mPopupWindow.showAtLocation(mAnchorView, Gravity.CENTER,
                (int) mRawX - mScreenWidth / 2,
                (int) mRawY - mScreenHeight / 2 - mPopupWindowHeight + mIndicatorHeight);
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

    public interface OnPopupListClickListener {
        void onPopupListClick(View contextView, int contextPosition, int position);
    }

}
