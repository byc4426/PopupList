package com.frank.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.frank.popuplist.PopupList;
import com.frank.popuplist.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv_main;
    private List<String> mDataList = new ArrayList<>();
    private ArrayAdapter<String> mDataAdapter;
    private List<String> popupMenuItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                Toast.makeText(MainActivity.this, "您点击了第"+contextPosition+"个列表项的第"+position+"个菜单："+popupMenuItemList.get(position),
                        Toast.LENGTH_LONG).show();
            }
        });
        getData();
    }



    private void getData() {
        for (int i = 0; i < 40; i++) {
            mDataList.add("我是第"+i+"个列表项");
        }
        mDataAdapter.notifyDataSetChanged();
    }

}
