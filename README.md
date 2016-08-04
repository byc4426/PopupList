# PopupList
气泡式上下文菜单，用于列表长按弹出的横向气泡式菜单列表。
在手指按下的位置弹出气泡框，触摸气泡外部或点击列表项可以隐藏。

This utility class can add a horizontal popup-menu easily.
该工具类可以很方便的为View、ListView/GridView绑定长按弹出横向气泡菜单。

**欢迎star，接受pull request。**

##效果图
![ScreenShot](https://github.com/shangmingchao/PopupList/blob/master/screenshots/screenshot_1.png)![ScreenShot](https://github.com/shangmingchao/PopupList/blob/master/screenshots/screenshot_2.png)
![ScreenShot](https://github.com/shangmingchao/PopupList/blob/master/screenshots/screenshot_3.png)![ScreenShot](https://github.com/shangmingchao/PopupList/blob/master/screenshots/screenshot_4.png)
##使用方式(How do I use PopupList?)
Just need a `.java`file and one line code.
只需要一个Java文件和一行代码即可。

```java
    PopupList popupList = new PopupList();
    popupList.init(context, view, popupMenuItemList, OnPopupListClickListener);
```

##例子(Sample)：
```java
    lv_main = (ListView) findViewById(R.id.lv_main);
    mDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mDataList);
    lv_main.setAdapter(mDataAdapter);
    popupMenuItemList.add(getString(R.string.copy));
    popupMenuItemList.add(getString(R.string.delete));
    popupMenuItemList.add(getString(R.string.share));
    popupMenuItemList.add(getString(R.string.more));
    PopupList popupList = new PopupList();
    popupList.init(this, lv_main, popupMenuItemList, new PopupList.OnPopupListClickListener() {
        @Override
        public void onPopupListClick(View contextView, int contextPosition, int position) {
            Toast.makeText(MainActivity.this, contextPosition + "," + position, Toast.LENGTH_LONG).show();
        }
    });
    ImageView indicator = new ImageView(this);
    indicator.setImageResource(R.drawable.popuplist_default_arrow);
    popupList.setIndicatorView(indicator);
    popupList.setIndicatorSize(dp2px(16), dp2px(8));
```
