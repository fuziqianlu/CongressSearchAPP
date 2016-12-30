package com.ziqianfu.congresssearch;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ziqianfu.congresssearch.R.layout.legis_layout;

/**
 * Created by lenovo on 2016/11/15.
 */

public class LegisFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener, View.OnClickListener{
    View legisView;
    private ListView lstView ;
    private ListView lstHouse;
    private ListView lstSenate;
    private int tabIndex;

    private String jsonUrl="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=legis&cham=all";
    private String jsonUrlHouse="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=legis&chamber=senate";
    private List<ItemBean> itemBeanList;
    private List<ItemBean> itemHouseList;
    private List<ItemBean> itemSenateList;
    TabHost tabHost;
    Map<String, Integer> mapIndex;
    Map<String, Integer> mapIndex2;
    Map<String, Integer> mapIndex3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        legisView = inflater.inflate(legis_layout, container,false);
        lstView = (ListView) legisView.findViewById(R.id.legisList);
        lstHouse=(ListView) legisView.findViewById(R.id.houseLegisList);
        lstSenate= (ListView) legisView.findViewById(R.id.legisSenateList);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Legislators");
        tabIndex=1;
        new LegisAsyncTask().execute(jsonUrl);
        tabHost =(TabHost) legisView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("By State").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("House").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Senate").setContent(R.id.tab3));

        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        lstView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemBean data=(ItemBean)parent.getItemAtPosition(position);
                Log.e("TAGG", "" + data.bioId.toString());
                Intent intent1=new Intent("com.ziqianfu.congresssearch.viewlegis");
                intent1.putExtra("bioguide_id", data.bioId.toString());
                startActivity(intent1);
            }
        });
        lstHouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemBean data=(ItemBean)parent.getItemAtPosition(position);
                Log.e("TAGG", "" + data.bioId.toString());
//                String tmp=((TextView)view.findViewById(R.id.tv_name)).getText().toString();
//                Log.e("TEST", "a");
                Intent intent2=new Intent("com.ziqianfu.congresssearch.viewlegis");
                intent2.putExtra("bioguide_id", data.bioId.toString());
                startActivity(intent2);
            }
        });
        lstSenate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemBean data=(ItemBean)parent.getItemAtPosition(position);
                Log.e("TAGG", "" + data.bioId.toString());
//                String tmp=((TextView)view.findViewById(R.id.tv_name)).getText().toString();
//                Log.e("TEST", "a");
                Intent intent3=new Intent("com.ziqianfu.congresssearch.viewlegis");
                intent3.putExtra("bioguide_id", data.bioId.toString());
                startActivity(intent3);
            }
        });
        return legisView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onTabChanged(String tabId) {
        if(tabId.equals("tab1")){
            tabIndex=1;
        }
        else if(tabId.equals("tab2")){
            tabIndex=2;
        }
        else if(tabId.equals("tab3")){
            tabIndex=3;
        }
    }


    @Override
    public View createTabContent(String tag) {
        return null;
    }

    @Override
    public void onClick(View v) {
        v.getLayoutParams();
        TextView selectedIndex = (TextView) v;
        if(tabIndex==1)
        lstView.setSelection(mapIndex.get(selectedIndex.getText()));
        if(tabIndex==2)
        lstHouse.setSelection(mapIndex2.get(selectedIndex.getText()));
        if(tabIndex==3)
        lstSenate.setSelection(mapIndex3.get(selectedIndex.getText()));
    }

    class LegisAsyncTask extends AsyncTask<String, Void, List<ItemBean>>{
        @Override
        protected List<ItemBean> doInBackground(String... params) {
            return getJsonData(jsonUrl);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<ItemBean> result) {
            super.onPostExecute(result);
            //解析完毕后，进行适配器的数据设置填充
            JsonAdapter adapter = new JsonAdapter(getActivity(), itemBeanList);
            lstView.setAdapter(adapter);
            JsonAdapter adapter2 = new JsonAdapter(getActivity(), itemHouseList);
            lstHouse.setAdapter(adapter2);
            JsonAdapter adapter3 = new JsonAdapter(getActivity(), itemSenateList);
            lstSenate.setAdapter(adapter3);
            getIndexList(itemBeanList);
            displayIndex("All", mapIndex);
            getIndexListHouse(itemHouseList);
            displayIndex("House", mapIndex2);
            getIndexListSenate(itemSenateList);
            displayIndex("Senate", mapIndex3);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
    public List<ItemBean> getJsonData(String jsonUrl) {
        try {
            //创建url http地址
            URL httpUrl = new URL(jsonUrl);
            //打开http 链接
            HttpURLConnection connection = (HttpURLConnection) httpUrl
                    .openConnection();
            //设置参数  请求为get请求
            connection.setReadTimeout(5000);
            connection.setRequestMethod("GET");

            //connection.getInputStream()得到字节输入流，InputStreamReader从字节到字符的桥梁，外加包装字符流
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            //创建字符串容器
            StringBuffer sb = new StringBuffer();
            String str = "";
            //行读取
            while ((str = bufferedReader.readLine()) != null) {
                // 当读取完毕，就添加到容器中
                sb.append(str);
            }
            //测试是否得到json字符串
            Log.e("TAG", ""+sb.toString());
            //创建本地对象的集合
            itemBeanList = new ArrayList<ItemBean>();
            itemHouseList = new ArrayList<ItemBean>();
            itemSenateList = new ArrayList<ItemBean>();
            // 整体是一个jsonObject
            JSONObject jsonObject = new JSONObject(sb.toString());
            // 键是jsonArray数组
            JSONArray jsonArray = jsonObject.getJSONArray("results");
            for (int i = 0; i < jsonArray.length(); i++) {
                //获取jsonArray中的每个对象
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                //创建本地的newsBean对象
                ItemBean itemsBean = new ItemBean();
                //为该对象进行属性值的设置操作
//				newsBean.imageViewUrl = jsonObject2
//						.getString("picSmall");
                itemsBean.name = jsonObject2.getString("last_name")+", "+jsonObject2.getString("first_name");
                itemsBean.state = jsonObject2.getString("state_name");
                itemsBean.party = jsonObject2.getString("party");
                itemsBean.chamber = jsonObject2.getString("chamber");
                itemsBean.district = jsonObject2.getString("district");
                itemsBean.bioId = jsonObject2.getString("bioguide_id");
                //添加对象，组建集合
                itemBeanList.add(itemsBean);
                if(itemsBean.chamber.equals("house")){
                    itemHouseList.add(itemsBean);
                }
                if(itemsBean.chamber.equals("senate")){
                    itemSenateList.add(itemsBean);
                }

            }
            Collections.sort(itemBeanList, new Comparator<ItemBean>(){
                public int compare(ItemBean o1, ItemBean o2){
                    if(o1.state == o2.state)
                        return 0;
                    return o1.state.compareTo(o2.state);
                }
            });
            Collections.sort(itemHouseList, new Comparator<ItemBean>(){
                public int compare(ItemBean o1, ItemBean o2){
                    if(o1.name == o2.name)
                        return 0;
                    return o1.name.compareTo(o2.name);
                }
            });
            Collections.sort(itemSenateList, new Comparator<ItemBean>(){
                public int compare(ItemBean o1, ItemBean o2){
                    if(o1.name == o2.name)
                        return 0;
                    return o1.name.compareTo(o2.name);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemBeanList;
    }

    private void getIndexList(List<ItemBean> list) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            String stateName = list.get(i).state;
            String index = stateName.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }
    private void getIndexListHouse(List<ItemBean> list) {
        mapIndex2 = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            String stateName = list.get(i).name;
            String index = stateName.substring(0, 1);

            if (mapIndex2.get(index) == null)
                mapIndex2.put(index, i);
        }
    }
    private void getIndexListSenate(List<ItemBean> list) {
        mapIndex3 = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            String stateName = list.get(i).name;
            String index = stateName.substring(0, 1);

            if (mapIndex3.get(index) == null)
                mapIndex3.put(index, i);
        }
    }
    private void displayIndex(String sideId, Map<String, Integer> mapIndex) {
        LinearLayout indexLayout;
        if(sideId.equals("All")) {
             indexLayout = (LinearLayout) getView().findViewById(R.id.side_index);
        }
        else if(sideId.equals("House")){
             indexLayout = (LinearLayout) getView().findViewById(R.id.side_index2);
        }
        else{
             indexLayout = (LinearLayout) getView().findViewById(R.id.side_index3);
        }

        TextView textView;
        List<String> indexList = new ArrayList<String>(mapIndex.keySet());
        for (String index : indexList) {
            textView = (TextView)getActivity().getLayoutInflater().inflate(
                    R.layout.side_index_item, null);
            textView.setText(index);
            textView.setOnClickListener(this);
            indexLayout.addView(textView);
        }
    }
}
