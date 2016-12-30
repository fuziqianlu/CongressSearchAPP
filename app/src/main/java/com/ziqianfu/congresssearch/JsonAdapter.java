package com.ziqianfu.congresssearch;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.ziqianfu.congresssearch.R.id.imageView;

/**
 * Created by lenovo on 2016/11/17.
 */

public class JsonAdapter extends BaseAdapter {
    List<ItemBean> data = new ArrayList<ItemBean>();
    LayoutInflater inflater;


    public JsonAdapter(Context context, List<ItemBean> data) {
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
            convertView = inflater.inflate(R.layout.listitem_legis, null);
            viewHolder.name = (TextView) convertView
                    .findViewById(R.id.tv_name);
            viewHolder.content = (TextView) convertView
                    .findViewById(R.id.tv_content);
			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.legisimage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
//		String imageViewUrl = data.get(position).imageViewUrl;
        //进行绑定--不会出现图片错位现象--因为viewholder是复用的，会显示复用的那个itme的图片
//		viewHolder.imageView.setTag(imageViewUrl);

        viewHolder.name.setText(data.get(position).name);
        String district="";
        if(data.get(position).district.equals("null")){
            district="0";
        }
        else{
            district=data.get(position).district;
        }
        viewHolder.content.setText("("+data.get(position).party+") "+data.get(position).state+" - District "+district);
        Picasso.with(convertView.getContext()).load("https://theunitedstates.io/images/congress/original/"+data.get(position).bioId+".jpg").into(viewHolder.imageView);
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
        public TextView name,content;
		public ImageView imageView;
    }
}
