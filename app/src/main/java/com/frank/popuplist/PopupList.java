package com.frank.popuplist;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

/**
 * 列表项的长按弹出菜单
 */
public class PopupList implements View.OnTouchListener {

    private volatile static PopupList instance;

    private Context mContext;
    private PopupWindow popupListWindow;
    PopupListAdapter.OnPopupListClickListener onPopupListClickListener;
    float rawX;
    float rawY;

    public static PopupList getInstance() {
        if (instance == null) {
            synchronized (PopupList.class) {
                if (instance == null) {
                    instance = new PopupList();
                }
            }
        }
        return instance;
    }

    private PopupList() {

    }

    public PopupListAdapter.OnPopupListClickListener getOnPopupListClickListener() {
        return onPopupListClickListener;
    }

    public void setOnPopupListClickListener(PopupListAdapter.OnPopupListClickListener onPopupListClickListener) {
        this.onPopupListClickListener = onPopupListClickListener;
    }

    /**
     * 在手指按下的位置显示弹出菜单
     *
     * @param context   上下文，一般为Activity
     * @param parent    上下文view，同时也是用来寻找窗口token的view，一般为ListView中的列表项ItemView
     * @param position  点击的列表项在列表数据集中的位置
     * @param popupList 要显示的列表
     * @param rawX      手指按下的屏幕绝对坐标X
     * @param rawY      手指按下的屏幕绝对坐标Y
     */
    public void showPopupWindow(final Context context, View parent, int position, List popupList, float rawX, float rawY) {

        this.mContext = context;
        // popupWindow要显示的内容
        ViewGroup layoutView = (ViewGroup) LayoutInflater.from(mContext).inflate(
                R.layout.popup_list, null);
        RecyclerView recyclerView = (RecyclerView) layoutView.findViewById(R.id.rv_popup);
        //如果item view内容的改变不会影响RecyclerView大小，设置成true以提升表现
        //contentView.setHasFixedSize(true);
        //设置布局管理器，LinearLayoutManager，水平线性布局
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //设置适配器
        PopupListAdapter popupListAdapter = new PopupListAdapter(this, popupList);
        //设置item点击监听
        popupListAdapter.setContextView(parent);
        popupListAdapter.setContextPosition(position);
        popupListAdapter.setOnPopupListClickListener(this.onPopupListClickListener);
        recyclerView.setAdapter(popupListAdapter);
        //设置列表分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, LinearLayoutManager.HORIZONTAL));
        //recyclerView的item view宽50dp高40dp，列表分割线宽1dp，箭头宽15dp高7dp
        //可计算出弹出窗口的宽高
        int PopupWindowWidth = recyclerView.getAdapter().getItemCount() * ScreenUtils.dp2px(50 + 1);
        int PopupWindowHeight = ScreenUtils.dp2px(40 + 7);
        //为水平列表添加箭头指示器，并计算其位置为手指按下位置
        ImageView iv = new ImageView(mContext);
        iv.setImageResource(R.drawable.popup_arrow);
        float leftEdgeOffset = rawX;
        float rightEdgeOffset = ScreenUtils.getScreenWidth(mContext) - rawX;
        if (leftEdgeOffset < PopupWindowWidth / 2) {
            iv.setTranslationX(leftEdgeOffset - ScreenUtils.dp2px(15 / 2.0f));
        } else if (rightEdgeOffset < PopupWindowWidth / 2) {
            iv.setTranslationX(PopupWindowWidth - rightEdgeOffset - ScreenUtils.dp2px(15 / 2.0f));
        } else {
            iv.setTranslationX(PopupWindowWidth / 2 - ScreenUtils.dp2px(15 / 2.0f));
        }
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtils.dp2px(15), ScreenUtils.dp2px(7));
        layoutView.addView(iv, layoutParams);
        //实例化弹出窗口并显示
        popupListWindow = new PopupWindow(layoutView, PopupWindowWidth,
                PopupWindowHeight, true);
        popupListWindow.setTouchable(true);
        //设置背景以便在外面包裹一层可监听触屏等事件的容器
        popupListWindow.setBackgroundDrawable(new BitmapDrawable());
        //预防性Bug修复，详见http://blog.csdn.net/shangmingchao/article/details/50947418
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
        //确保可以显示后，进行最终的显示
        popupListWindow.showAtLocation(parent, Gravity.CENTER,
                (int) rawX - ScreenUtils.getScreenWidth(mContext) / 2,
                (int) rawY - ScreenUtils.getScreenHeight(mContext) / 2 - PopupWindowHeight / 2);
    }

    /**
     * 隐藏气泡式弹出菜单PopupWindow
     */
    public void hiddenPopupWindow() {
        //预防性Bug修复，详见http://blog.csdn.net/shangmingchao/article/details/50947418
        if (mContext instanceof Activity && ((Activity) mContext).isFinishing()) {
            return;
        }
        if (popupListWindow != null) {
            popupListWindow.dismiss();
        }
    }

    /**
     * 给ListView绑定长按弹出(在手指按下的位置)横向气泡式菜单
     *
     * @param context                  上下文，一般为Activity
     * @param listView                 要弹出菜单的ListView
     * @param popupMenuItemList        要弹出的菜单项列表
     * @param popupListOnClickListener 弹出菜单的点击监听接口
     */
    public void initPopupList(final Context context, final ListView listView, final List<String> popupMenuItemList, PopupListAdapter.OnPopupListClickListener popupListOnClickListener) {
        listView.setOnTouchListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showPopupWindow(context, view, position, popupMenuItemList, rawX, rawY);
                return true;
            }
        });
        setOnPopupListClickListener(popupListOnClickListener);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        rawX = event.getRawX();
        rawY = event.getRawY();
        return false;
    }

}
