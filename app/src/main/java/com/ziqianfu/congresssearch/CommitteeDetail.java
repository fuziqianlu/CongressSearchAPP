package com.ziqianfu.congresssearch;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by lenovo on 2016/11/20.
 */

public class CommitteeDetail extends AppCompatActivity {
    private TextView committee_id;
    private TextView committee_name;
    private TextView committee_chamber;
    private TextView committee_parent;
    private TextView committee_contact;
    private TextView committee_office;
    private ImageView chamber_img;

    private ImageSwitcher favor_btn;
    private CommitteeItem committee;
    private boolean saved;
    SharedPreferences preferences;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.committees_details);
        preferences=getSharedPreferences("favor_committees", MODE_APPEND);
        final SharedPreferences.Editor editor=preferences.edit();
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.tool_bar_bill);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getSupportActionBar().setTitle("Committee Info");
        favor_btn=(ImageSwitcher)findViewById(R.id.favorite_button);
        committee_id=(TextView)findViewById(R.id.com_id_data);
        committee_name=(TextView)findViewById(R.id.com_name_data);
        committee_chamber=(TextView)findViewById(R.id.com_chamber_data);
        committee_parent=(TextView)findViewById(R.id.com_parent_data);
        committee_office=(TextView)findViewById(R.id.com_office_data);
        committee_contact=(TextView)findViewById(R.id.com_contact_data);
        chamber_img=(ImageView)findViewById(R.id.chamber_image);


        Intent intent=getIntent();
        final Gson gson=new Gson();
         committee=gson.fromJson(intent.getStringExtra("committee_detail"), CommitteeItem.class);

        favor_btn.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(CommitteeDetail.this);
            }
        });
        favor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saved==false){
                    favor_btn.setImageResource(R.drawable.star_filled);
                    saved=true;
                    editor.putString(committee.committee_id, gson.toJson(committee, CommitteeItem.class));
                    editor.commit();
                }
                else {
                    favor_btn.setImageResource(R.drawable.star_empty);
                    saved=false;
                    editor.remove(committee.committee_id);
                    editor.commit();
                }
            }
        });
        if(preferences.getString(committee.committee_id, "0").equals("0")){
            saved=false;
            favor_btn.setImageResource(R.drawable.star_empty);
        }
        else {
            saved=true;
            favor_btn.setImageResource(R.drawable.star_filled);
        }
        committee_id.setText(committee.committee_id);
        committee_name.setText(committee.name);
        committee_chamber.setText(WordUtils.capitalize(committee.chamber));
        if(committee.chamber.equals("house")){
            chamber_img.setImageResource(R.drawable.h);
        }
        else{
            chamber_img.setImageResource(R.drawable.s);
        }
        if(committee.phone==null){
            committee_contact.setText("N.A.");
        }
        else {
            committee_contact.setText(committee.phone);
        }
        if(committee.parent_committee_id==null){
            committee_parent.setText("N.A.");
        }
        else {
            committee_parent.setText(committee.parent_committee_id);
        }
        if(committee.office==null){
            committee_office.setText("N.A.");
        }
        else {
            committee_office.setText(committee.office);
        }
    }
}
