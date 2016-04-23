# PopupList
气泡式上下文菜单，用于列表长按弹出的横向气泡式菜单列表。
在手指按下的位置弹出气泡框，触摸气泡外部或点击列表项可以隐藏。

欢迎star，接受pull request。
##效果图
![效果图](http://img.blog.csdn.net/20151209235806657)
![效果图](http://img.blog.csdn.net/20151209235749053)
![效果图](http://img.blog.csdn.net/20151209235714401)
##使用方式(How do I use PopupList?)
 > PopupList.getInstance().initPopupList(context，ListView/GridView，menuList，onPopupListClickListener);

##例子(Sample)：
```java
    lv_main = (ListView) findViewById(R.id.lv_main);
    mDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mDataList);
    lv_main.setAdapter(mDataAdapter);
    
    List<String> popupMenuItemList = new ArrayList<>();
    popupMenuItemList.add("转发");
    popupMenuItemList.add("删除");
    
    PopupList.getInstance().initPopupList(this, lv_main, popupMenuItemList, new PopupListAdapter.OnPopupListClickListener() {
        @Override
        public void onPopupListItemClick(View contextView, int contextPosition, View view, int position) {
            Toast.makeText(MainActivity.this,contextPosition+","+position+popupMenuItemList.get(position),Toast.LENGTH_LONG).show();
        }
    });
```
