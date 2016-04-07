# PopupList
气泡式上下文菜单，用于列表长按弹出的横向气泡式菜单列表。
在手指按下的位置弹出气泡框，触摸气泡外部或点击列表项可以隐藏。
##效果图
![效果图](http://img.blog.csdn.net/20151209235806657)
![效果图](http://img.blog.csdn.net/20151209235749053)
![效果图](http://img.blog.csdn.net/20151209235714401)
##使用方式
    只需要调用该方法即可完成绑定：
    PopupList.getInstance().initPopupList(上下文，ListView或GridView，要弹出的菜单项列表，实现了菜单点击事件接口的类实例);
##例子：
    PopupList.getInstance().initPopupList(this, lv_main, popupMenuItemList, new PopupListAdapter.OnPopupListClickListener() {
        @Override
        public void onPopupListItemClick(View contextView, int contextPosition, View view, int position) {
            Toast.makeText(MainActivity.this, "点击了第"+contextPosition+"个列表项的第"+position+"个菜单："+popupMenuItemList.get(position),
                    Toast.LENGTH_LONG).show();
        }
    });
