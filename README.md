# PopupList
<p>It can popup a horizontal menu when you press a View by a long-press guesture.</p>
<p>长按ListView, GridView 甚至View, 弹出横向气泡式菜单。</p>

<p>This utility class can bind a horizontal popup-menu for ListView, GridView, or other View easily.</p>
<p>该工具类可以很方便的为ListView, GridView, 甚至普通View绑定长按弹出横向气泡式菜单。</p>

** Welcome star, fork**
**欢迎star，fork**

##效果图(Screenshot)
![ScreenShot](https://github.com/shangmingchao/PopupList/blob/master/screenshots/screenshot_1.png)![ScreenShot](https://github.com/shangmingchao/PopupList/blob/master/screenshots/screenshot_2.png)<br />
![ScreenShot](https://github.com/shangmingchao/PopupList/blob/master/screenshots/screenshot_3.png)![ScreenShot](https://github.com/shangmingchao/PopupList/blob/master/screenshots/screenshot_4.png)
##使用方式(How do I use PopupList?)
Just need a `.java`file and a little code.<br />
只需要一个Java文件和几行代码即可。

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
##配置(Configuration)：
You can get more information from the [Wiki](https://github.com/shangmingchao/PopupList/wiki) page.
