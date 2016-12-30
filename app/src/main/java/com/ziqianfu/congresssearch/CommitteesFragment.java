package com.ziqianfu.congresssearch;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lenovo on 2016/11/15.
 */

public class CommitteesFragment extends Fragment implements AdapterView.OnItemClickListener, TabHost.TabContentFactory, TabHost.OnTabChangeListener{
    private View committeeView;
    private TabHost tabHost;
    private ListView lstHouse;
    private ListView lstSenate;
    private ListView lstJoint;
    private List<CommitteeItem> comHouseList;
    private List<CommitteeItem> comSenateList;
    private List<CommitteeItem> comJointList;
    private List<CommitteeItem> comAllList;
    private String jsonUrl="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=allCommittee";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        committeeView = inflater.inflate(R.layout.committees_layout, container,false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Committees");
        tabHost=(TabHost)committeeView.findViewById(R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("House").setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Senate").setContent(R.id.tab2));
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Joint").setContent(R.id.tab3));
        tabHost.setCurrentTab(0);
        tabHost.setOnTabChangedListener(this);
        lstHouse=(ListView)committeeView.findViewById(R.id.committee_house);
        lstSenate=(ListView)committeeView.findViewById(R.id.committee_senate);
        lstJoint=(ListView)committeeView.findViewById(R.id.committee_joint);
        new CommitteeAsynsTask().execute(jsonUrl);
        final Gson gson=new Gson();
        lstHouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1=new Intent("com.ziqianfu.congresssearch.committees");
                CommitteeItem item=(CommitteeItem)parent.getItemAtPosition(position);
                intent1.putExtra("committee_detail", gson.toJson(item, CommitteeItem.class));
                startActivity(intent1);
            }
        });
        lstSenate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent2=new Intent("com.ziqianfu.congresssearch.committees");
                CommitteeItem item=(CommitteeItem)parent.getItemAtPosition(position);
                intent2.putExtra("committee_detail", gson.toJson(item, CommitteeItem.class));
                startActivity(intent2);
            }
        });
        lstJoint.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent3=new Intent("com.ziqianfu.congresssearch.committees");
                CommitteeItem item=(CommitteeItem)parent.getItemAtPosition(position);
                intent3.putExtra("committee_detail", gson.toJson(item, CommitteeItem.class));
                startActivity(intent3);
            }
        });
        return committeeView;
    }

    class CommitteeAsynsTask extends AsyncTask<String,Void, List<CommitteeItem>>{

        @Override
        protected List<CommitteeItem> doInBackground(String... params) {
            return getJsonData(jsonUrl);
        }

        @Override
        protected void onPostExecute(List<CommitteeItem> committeeItems) {
            super.onPostExecute(committeeItems);

            ComJsonAdapter adapter1=new ComJsonAdapter(getActivity(), comHouseList);
            lstHouse.setAdapter(adapter1);
            ComJsonAdapter adapter2=new ComJsonAdapter(getActivity(), comSenateList);
            lstSenate.setAdapter(adapter2);
            ComJsonAdapter adapter3=new ComJsonAdapter(getActivity(), comJointList);
            lstJoint.setAdapter(adapter3);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onTabChanged(String tabId) {

    }

    @Override
    public View createTabContent(String tag) {
        return null;
    }

    public List<CommitteeItem> getJsonData(String jsonUrl){
        try {
            URL httpUrl=new URL(jsonUrl);
            HttpURLConnection connection=(HttpURLConnection)httpUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(5000);

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb=new StringBuffer();
            String str="";
            while((str=bufferedReader.readLine())!=null){
                sb.append(str);
            }
            Log.e("TAG", ""+sb.toString());
            Gson gson=new Gson();
            comAllList=gson.fromJson(sb.toString(),CommitteeItemList.class).results;
            Collections.sort(comAllList, new Comparator<CommitteeItem>(){
                public int compare(CommitteeItem o1, CommitteeItem o2){
                    return o1.name.compareTo(o2.name);
                }
            });
            comHouseList=new ArrayList<CommitteeItem>();
            comSenateList=new ArrayList<CommitteeItem>();
            comJointList=new ArrayList<CommitteeItem>();
            for(CommitteeItem entry: comAllList){
                if(entry.chamber.equals("house")){
                    comHouseList.add(entry);
                }
                else if(entry.chamber.equals("senate")){
                    comSenateList.add(entry);
                }
                else{
                    comJointList.add(entry);
                }
            }

        } catch (Exception e){

        }
        return comAllList;
    }
}
