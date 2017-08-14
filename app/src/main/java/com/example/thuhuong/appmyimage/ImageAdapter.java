package com.example.thuhuong.appmyimage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ThuHuong on 21/05/2017.
 */

public class ImageAdapter extends BaseAdapter{
    Context myContext;
    int myLayout;
    ArrayList<Image> arrayImage ;

    public ImageAdapter(Context myContext, int myLayout, ArrayList<Image> arrayImage) {
        this.myContext = myContext;
        this.myLayout = myLayout;
        this.arrayImage = arrayImage;
    }

    public ImageAdapter() {
    }

    @Override
    public int getCount() {
        return arrayImage.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayImage.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder{
        ImageView imgImage;
        TextView tvNameImg;
        TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = convertView;
        ViewHolder holder = new ViewHolder();

        if(rowView == null){
            rowView = inflater.inflate(myLayout, null);
            holder.tvNameImg = (TextView) rowView.findViewById(R.id.tvNameImg);
            holder.imgImage = (ImageView) rowView.findViewById(R.id.imgImage);
            holder.time = (TextView) rowView.findViewById(R.id.tvTime);
            rowView.setTag(holder);
        }else {
            holder = (ViewHolder) rowView.getTag();
        }

        holder.tvNameImg.setText(arrayImage.get(position).getName());
        holder.time.setText(arrayImage.get(position).getTimeCreate());
        Picasso.with(myContext).load(arrayImage.get(position).getUrl()).into(holder.imgImage);
        return rowView;
    }
}
