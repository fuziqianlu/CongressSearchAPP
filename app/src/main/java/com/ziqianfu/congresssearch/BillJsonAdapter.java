package com.ziqianfu.congresssearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2016/11/17.
 */

public class BillJsonAdapter extends BaseAdapter {
    List<BillItem> data = new ArrayList<BillItem>();
    LayoutInflater inflater;


    public BillJsonAdapter(Context context, List<BillItem> data) {
        super();
        this.data = data;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.listitem_bills, null);
            viewHolder.bill_id = (TextView) convertView
                    .findViewById(R.id.tv_billid);
            viewHolder.bill_title = (TextView) convertView
                    .findViewById(R.id.tv_billtitle);
			viewHolder.bill_date = (TextView) convertView
					.findViewById(R.id.tv_billdate);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//		String imageViewUrl = data.get(position).imageViewUrl;
        //进行绑定--不会出现图片错位现象--因为viewholder是复用的，会显示复用的那个itme的图片
//		viewHolder.imageView.setTag(imageViewUrl);

        viewHolder.bill_id.setText(data.get(position).bill_id);
        if(data.get(position).short_title!=null) {
            viewHolder.bill_title.setText(data.get(position).short_title);
        }
        else{
            viewHolder.bill_title.setText(data.get(position).official_title);
        }
        FastDateFormat parser=FastDateFormat.getInstance("yyyy-MM-dd");
        FastDateFormat printer=FastDateFormat.getDateInstance(FastDateFormat.MEDIUM);
        try {
            Date billDate=parser.parse(data.get(position).introduced_on);
            String introduce=printer.format(billDate);
            viewHolder.bill_date.setText(introduce);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //viewHolder.bill_date.setText(data.get(position).sponsor.first_name);

        /**
         * 这个方式是通过分线程进行图片下载
         */
//		new ImageLoaderThread().showImageByThread(viewHolder.imageView, data.get(position).imageViewUrl);
        /**
         * 这个方式是进行异步任务方式进行图片加载
         */
//		new ImageLoaderAsyncTask().showImageAsyncTask(viewHolder.imageView, data.get(position).imageViewUrl);
        return convertView;
    }
    class ViewHolder{
        public TextView bill_id,bill_title, bill_date;
    }
}
