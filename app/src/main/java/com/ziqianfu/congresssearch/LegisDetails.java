package com.ziqianfu.congresssearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2016/11/18.
 */

public class LegisDetails extends AppCompatActivity {
    private String jsonUrl;
    private DetailBean personInfo;
    private ImageView photo;
    private TextView name;
    private TextView party;
    private ImageView party_img;
    private TextView email;
    private TextView chamber;
    private TextView contact;
    private TextView start_term;
    private TextView end_term;
    private TextView office;
    private TextView state;
    private TextView birthday;
    private TextView fax;
    private TextView progressText;
    private ProgressBar termBar;
    private ImageView facebook;
    private ImageView twitter;
    private ImageView website;
    private ImageSwitcher favoriteBtn;
    private boolean saved=false;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.legis_details);
        Toolbar mtoolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mtoolbar);
        mtoolbar.setNavigationIcon(R.drawable.ic_arrow_back_white);
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Legislator Info");
        preferences=getSharedPreferences("favor_legis", MODE_APPEND);
        photo=(ImageView)findViewById(R.id.legis_photo);
        name=(TextView)findViewById(R.id.tv_name_data);
        party_img=(ImageView)findViewById(R.id.party_icon);
        party=(TextView)findViewById(R.id.party_name);
        email=(TextView)findViewById(R.id.tv_email_data);
        chamber=(TextView)findViewById(R.id.tv_chamber_data);
        contact=(TextView)findViewById(R.id.tv_contact_data);
        start_term=(TextView)findViewById(R.id.tv_startTerm_data);
        end_term=(TextView)findViewById(R.id.tv_endTerm_data);
        office=(TextView)findViewById(R.id.tv_office_data);
        state=(TextView)findViewById(R.id.tv_state_data);
        fax=(TextView)findViewById(R.id.tv_fax_data);
        birthday=(TextView)findViewById(R.id.tv_birthday_data);
        facebook=(ImageView)findViewById(R.id.facebook);
        twitter=(ImageView)findViewById(R.id.twitter);
        website=(ImageView)findViewById(R.id.website);
        progressText=(TextView)findViewById(R.id.progresstxt);

        termBar=(ProgressBar)findViewById(R.id.termProgress);
        favoriteBtn=(ImageSwitcher)findViewById(R.id.favorite_btn);


        Intent intent=getIntent();
        String bioguide_id=intent.getStringExtra("bioguide_id");
        jsonUrl="http://sample-env.5p7uahjtiv.us-west-2.elasticbeanstalk.com/csci571hw8/LoadPHP.php?key=legisDetail&bioguideId="+bioguide_id;
        new LegisAsyncTask(this).execute(jsonUrl);
        favoriteBtn.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                return new ImageView(LegisDetails.this);
            }
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(saved==false){
                    favoriteBtn.setImageResource(R.drawable.star_filled);
                    saved=true;
                    SharedPreferences.Editor editor=preferences.edit();
                    Gson gson=new Gson();
                    editor.putString(personInfo.bioId,gson.toJson(personInfo, DetailBean.class));
                    editor.commit();
                }
                else{
                    favoriteBtn.setImageResource(R.drawable.star_empty);
                    saved=false;
                    SharedPreferences.Editor editor=preferences.edit();
                    Gson gson=new Gson();
                    editor.remove(personInfo.bioId);
                    editor.commit();
                }
            }
        });

    }

    class LegisAsyncTask extends AsyncTask<String, Void, DetailBean> {
        private Context mContext;
        public LegisAsyncTask(Context context){
            mContext=context;
        }
        @Override
        protected DetailBean doInBackground(String... params) {
            return getJsonData(jsonUrl);
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(DetailBean result) {
            super.onPostExecute(result);
            //解析完毕后，进行适配器的数据设置填充
            Picasso.with(mContext).load("https://theunitedstates.io/images/congress/original/"+personInfo.bioId+".jpg").into(photo);
            name.setText(personInfo.rep+"."+personInfo.name);
            if(personInfo.email==null||personInfo.email.equals("null")){
                email.setText("N.A.");
            }
            else {
                email.setText(personInfo.email);
            }
            chamber.setText(WordUtils.capitalize(personInfo.chamber));
            contact.setText(personInfo.contact);
            office.setText(personInfo.office);
            state.setText(personInfo.shortState);
            if(personInfo.fax==null||personInfo.fax.equals("null")){
                fax.setText("N.A.");
            }
            else {
                fax.setText(personInfo.fax);
            }
            birthday.setText(personInfo.birthday);
            if(personInfo.party.equals("R")){
                party_img.setImageResource(R.drawable.r);
                party.setText("Republican");
            }
            else if(personInfo.party.equals("D")){
                party_img.setImageResource(R.drawable.d);
                party.setText("Democrat");
            }
            start_term.setText(personInfo.startTerm);
            end_term.setText(personInfo.endTerm);
            termBar.setProgress((int)(personInfo.termProgress*100));
            progressText.setText((int)(personInfo.termProgress*100)+"%");
            if(preferences.getString(personInfo.bioId, "0").equals("0")){
                saved=false;
                favoriteBtn.setImageResource(R.drawable.star_empty);
            }
            else {
                saved=true;
                favoriteBtn.setImageResource(R.drawable.star_filled);
            }
            facebook.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(personInfo.facebookId==null||personInfo.facebookId.equals("null"))) {
                        Intent intentF = new Intent();
                        intentF.setAction(Intent.ACTION_VIEW);
                        intentF.addCategory(Intent.CATEGORY_BROWSABLE);
                        intentF.setData(Uri.parse("https://www.facebook.com/"+personInfo.facebookId));
                        startActivity(intentF);
                    }
                    else{
                        Context context = getApplicationContext();
                        CharSequence text = "No facebook link!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            });
            twitter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(personInfo.twitterId==null||personInfo.twitterId.equals("null"))) {
                        Intent intentF = new Intent();
                        intentF.setAction(Intent.ACTION_VIEW);
                        intentF.addCategory(Intent.CATEGORY_BROWSABLE);
                        intentF.setData(Uri.parse("https://twitter.com/"+personInfo.twitterId));
                        startActivity(intentF);
                    }
                    else{
                        Context context = getApplicationContext();
                        CharSequence text = "No twitter link!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            });
            website.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!(personInfo.website==null||personInfo.website.equals("null"))) {
                        Intent intentF = new Intent();
                        intentF.setAction(Intent.ACTION_VIEW);
                        intentF.addCategory(Intent.CATEGORY_BROWSABLE);
                        intentF.setData(Uri.parse(personInfo.website));
                        startActivity(intentF);
                    }
                    else{
                        Context context = getApplicationContext();
                        CharSequence text = "No website link!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }
                }
            });
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
    public DetailBean getJsonData(String jsonUrl) {
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
            personInfo=new DetailBean();
            // 整体是一个jsonObject
            JSONObject jsonObject = new JSONObject(sb.toString());
            // 键是jsonArray数组
            JSONArray jsonArray = jsonObject.getJSONArray("results");

                //获取jsonArray中的每个对象
                JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                //创建本地的newsBean对象
//                DetailBean detailBean = new DetailBean();
                //为该对象进行属性值的设置操作
//				newsBean.imageViewUrl = jsonObject2
//						.getString("picSmall");
                personInfo.name = jsonObject2.getString("last_name")+", "+jsonObject2.getString("first_name");
                personInfo.state = jsonObject2.getString("state_name");
                personInfo.email=jsonObject2.getString("oc_email");
                personInfo.bioId=jsonObject2.getString("bioguide_id");
            personInfo.party=jsonObject2.getString("party");
            personInfo.chamber=jsonObject2.getString("chamber");
            personInfo.contact=jsonObject2.getString("phone");
            personInfo.startTerm=jsonObject2.getString("term_start");
            personInfo.shortState=jsonObject2.getString("state");
            personInfo.office=jsonObject2.getString("office");
            personInfo.birthday=jsonObject2.getString("birthday");
            personInfo.fax=jsonObject2.getString("fax");
            personInfo.rep=jsonObject2.getString("title");
            try {
                personInfo.facebookId = jsonObject2.getString("facebook_id");
                personInfo.twitterId = jsonObject2.getString("twitter_id");
                personInfo.website = jsonObject2.getString("website");
            }catch (Exception e){

            }
            FastDateFormat parser=FastDateFormat.getInstance("yyyy-MM-dd");
            Date startDay=parser.parse(personInfo.startTerm);
            FastDateFormat printer=FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
            personInfo.startTerm=printer.format(startDay);

            personInfo.endTerm=jsonObject2.getString("term_end");
            Date endDay=parser.parse(personInfo.endTerm);
            personInfo.endTerm=printer.format(endDay);
            Date now=new Date();
            personInfo.termProgress=(double) (now.getTime()-startDay.getTime())/(endDay.getTime()-startDay.getTime());
                //添加对象，组建集合
            Date birth=parser.parse(personInfo.birthday);
            personInfo.birthday=printer.format(birth);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return personInfo;
    }
}
