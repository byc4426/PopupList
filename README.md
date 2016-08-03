# PopupList
气泡式上下文菜单，用于列表长按弹出的横向气泡式菜单列表。
在手指按下的位置弹出气泡框，触摸气泡外部或点击列表项可以隐藏。

**欢迎star，接受pull request。**

##效果图
![效果图](http://img.blog.csdn.net/20151209235806657)
![效果图](http://img.blog.csdn.net/20151209235749053)
![效果图](http://img.blog.csdn.net/20151209235714401)
##使用方式(How do I use PopupList?)
```java
    PopupList popupList = new PopupList();
    popupList.init(context, view, popupMenuItemList, PopupList.OnPopupListClickListener);
```

##例子(Sample)：
```java
    lv_main = (ListView) findViewById(R.id.lv_main);
    mDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mDataList);
    lv_main.setAdapter(mDataAdapter);

    popupMenuItemList.add("复制");
    popupMenuItemList.add("删除");
    popupMenuItemList.add("更多...");
    PopupList popupList = new PopupList();
    popupList.init(this, lv_main, popupMenuItemList, new PopupList.OnPopupListClickListener() {
        @Override
        public void onPopupListClick(View contextView, int contextPosition, int position) {
            Toast.makeText(MainActivity.this, "您点击了第" + contextPosition + "个列表项的第" + position + "个菜单：" + popupMenuItemList.get(position),
                    Toast.LENGTH_LONG).show();
        }
    });
```
