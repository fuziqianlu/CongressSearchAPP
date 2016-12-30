package com.ziqianfu.congresssearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/11/17.
 */

public class ComJsonAdapter extends BaseAdapter {
    List<CommitteeItem> data = new ArrayList<CommitteeItem>();
    LayoutInflater inflater;


    public ComJsonAdapter(Context context, List<CommitteeItem> data) {
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
            convertView = inflater.inflate(R.layout.listitem_committees, null);
            viewHolder.committee_id = (TextView) convertView
                    .findViewById(R.id.tv_com_id);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.tv_com_name);
			viewHolder.chamber = (TextView) convertView
					.findViewById(R.id.tv_com_chamber);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//		String imageViewUrl = data.get(position).imageViewUrl;
        //进行绑定--不会出现图片错位现象--因为viewholder是复用的，会显示复用的那个itme的图片
//		viewHolder.imageView.setTag(imageViewUrl);

        viewHolder.committee_id.setText(data.get(position).committee_id);
        viewHolder.name.setText(data.get(position).name);
        viewHolder.chamber.setText(WordUtils.capitalize(data.get(position).chamber));
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
        public TextView committee_id,name, chamber;
    }
}
