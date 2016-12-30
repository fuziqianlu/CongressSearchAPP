package com.ziqianfu.congresssearch;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lenovo on 2016/11/20.
 */

public class favorFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener, View.OnClickListener {
    View favorView;
    private ListView lvFavorLegis;
    private ListView lvFavorBills;
    private ListView lvFavorComs;
    private List<DetailBean> favorLegisList;
    private List<BillItem> favorBillsList;
    private List<CommitteeItem> favorComList;
    TabHost tabHost;
    Map<String, Integer> mapIndex;
    private SharedPreferences preLegis;
    private SharedPreferences preBills;
    private SharedPreferences preComs;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        favorView= inflater.inflate(R.layout.favorite_layout, container,false);
        lvFavorLegis=(ListView)favorView.findViewById(R.id.favorlegisList);
        lvFavorBills=(ListView)favorView.findViewById(R.id.favoriteBillList);
        lvFavorComs=(ListView)favorView.findViewById(R.id.favoriteCommitteeList);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Favorites");
        tabHost=(TabHost)favorView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Legislators").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Bills").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Committees").setContent(R.id.tab3));
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        preLegis=this.getActivity().getSharedPreferences("favor_legis", Context.MODE_APPEND);
        preBills=this.getActivity().getSharedPreferences("favor_bills", Context.MODE_APPEND);
        preComs=this.getActivity().getSharedPreferences("favor_committees",Context.MODE_APPEND);
        Map<String, String> legisMap= (Map<String, String>) preLegis.getAll();
        Map<String, String> billsMap=(Map<String, String>)preBills.getAll();
        Map<String, String> comsMap=(Map<String, String>)preComs.getAll();
        final Gson gson=new Gson();
        favorLegisList=new ArrayList<DetailBean>();
        favorBillsList=new ArrayList<BillItem>();
        favorComList=new ArrayList<CommitteeItem>();
        for(Map.Entry<String, String> entry:legisMap.entrySet()){
            favorLegisList.add(gson.fromJson(entry.getValue(), DetailBean.class));
        }
        for(Map.Entry<String, String> entry:billsMap.entrySet()){
            favorBillsList.add(gson.fromJson(entry.getValue(), BillItem.class));
        }
        for(Map.Entry<String, String> entry:comsMap.entrySet()){
            favorComList.add(gson.fromJson(entry.getValue(), CommitteeItem.class));
        }
        DetailBeanJsonAdapter adapter1=new DetailBeanJsonAdapter(getActivity(), favorLegisList);
        BillJsonAdapter adapter2=new BillJsonAdapter(getActivity(),favorBillsList);
        ComJsonAdapter adapter3=new ComJsonAdapter(getActivity(), favorComList);
        lvFavorBills.setAdapter(adapter2);
        lvFavorLegis.setAdapter(adapter1);
        lvFavorComs.setAdapter(adapter3);
        lvFavorLegis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailBean data=(DetailBean) parent.getItemAtPosition(position);
                Log.e("TAGG", "" + data.bioId.toString());
                Intent intent1=new Intent("com.ziqianfu.congresssearch.viewlegis");
                intent1.putExtra("bioguide_id", data.bioId.toString());
                startActivity(intent1);
            }
        });
        lvFavorBills.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BillItem data=(BillItem)parent.getItemAtPosition(position);
                Intent intent2=new Intent("com.ziqianfu.congresssearch.bills");
                intent2.putExtra("bill_detail", gson.toJson(data, BillItem.class));
                startActivity(intent2);
            }
        });
        lvFavorComs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3=new Intent("com.ziqianfu.congresssearch.committees");
                CommitteeItem item=(CommitteeItem)parent.getItemAtPosition(position);
                intent3.putExtra("committee_detail", gson.toJson(item, CommitteeItem.class));
                startActivity(intent3);
            }
        });
        return favorView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onResume() {
        super.onResume();
        preLegis=this.getActivity().getSharedPreferences("favor_legis", Context.MODE_APPEND);
        preBills=this.getActivity().getSharedPreferences("favor_bills", Context.MODE_APPEND);
        preComs=this.getActivity().getSharedPreferences("favor_committees",Context.MODE_APPEND);
        Map<String, String> legisMap= (Map<String, String>) preLegis.getAll();
        Map<String, String> billsMap=(Map<String, String>)preBills.getAll();
        Map<String, String> comsMap=(Map<String, String>)preComs.getAll();
        final Gson gson=new Gson();
        favorLegisList=new ArrayList<DetailBean>();
        favorBillsList=new ArrayList<BillItem>();
        favorComList=new ArrayList<CommitteeItem>();
        for(Map.Entry<String, String> entry:legisMap.entrySet()){
            favorLegisList.add(gson.fromJson(entry.getValue(), DetailBean.class));
        }
        Collections.sort(favorLegisList, new Comparator<DetailBean>(){
            public int compare(DetailBean o1, DetailBean o2){
                return o1.name.compareTo(o2.name);
            }
        });
        for(Map.Entry<String, String> entry:billsMap.entrySet()){
            favorBillsList.add(gson.fromJson(entry.getValue(), BillItem.class));
        }
        Collections.sort(favorBillsList, new Comparator<BillItem>(){
            public int compare(BillItem o1, BillItem o2){
                return 0-o1.introduced_on.compareTo(o2.introduced_on);
            }
        });
        for(Map.Entry<String, String> entry:comsMap.entrySet()){
            favorComList.add(gson.fromJson(entry.getValue(), CommitteeItem.class));
        }
        Collections.sort(favorComList, new Comparator<CommitteeItem>(){
            public int compare(CommitteeItem o1, CommitteeItem o2){
                return o1.name.compareTo(o2.name);
            }
        });
        DetailBeanJsonAdapter adapter1=new DetailBeanJsonAdapter(getActivity(), favorLegisList);
        BillJsonAdapter adapter2=new BillJsonAdapter(getActivity(),favorBillsList);
        ComJsonAdapter adapter3=new ComJsonAdapter(getActivity(), favorComList);
        lvFavorBills.setAdapter(adapter2);
        lvFavorLegis.setAdapter(adapter1);
        lvFavorComs.setAdapter(adapter3);
        getIndexList(favorLegisList);
        displayIndex();
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    public View createTabContent(String tag) {
        return null;
    }

    private void getIndexList(List<DetailBean> list) {
        mapIndex = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < list.size(); i++) {
            String lastName = list.get(i).name;
            String index = lastName.substring(0, 1);

            if (mapIndex.get(index) == null)
                mapIndex.put(index, i);
        }
    }

    private void displayIndex() {
        LinearLayout indexLayout;

        indexLayout = (LinearLayout) getView().findViewById(R.id.side_index);
        indexLayout.removeAllViews();
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

    @Override
    public void onClick(View v) {
        TextView selectedIndex = (TextView) v;
        lvFavorLegis.setSelection(mapIndex.get(selectedIndex.getText()));
    }
}
