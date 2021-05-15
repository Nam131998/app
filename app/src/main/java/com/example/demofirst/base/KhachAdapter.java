package com.example.demofirst.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.demofirst.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class KhachAdapter extends ArrayAdapter<KhachThue> {
    private Context context;
    private int resource;
    private ArrayList<KhachThue> arrKhach;
    public KhachAdapter(Context context, int resource, ArrayList<KhachThue> arrDichVu) {
        super(context, resource, arrDichVu);
        this.context = context;
        this.resource = resource;
        this.arrKhach = arrDichVu;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dong_khach, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTenKhach = (TextView) convertView.findViewById(R.id.tvTenKhach);
            viewHolder.tvTuoi = (TextView) convertView.findViewById(R.id.tvTuoi);
            viewHolder.tvSDT = (TextView) convertView.findViewById(R.id.tvSDT);
            viewHolder.tvAvatar = (ImageView)convertView.findViewById(R.id.tvAvatar);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        KhachThue k = arrKhach.get(position);
        viewHolder.tvTenKhach.setText(k.getTEN_KH());
        Calendar c = Calendar.getInstance();
        int a=c.get(Calendar.YEAR);
        String tuoi = (a-Integer.parseInt(k.getNAM_SINH()))+"";
        if(k.getGIOI_TINH().equals("Nam"))
            viewHolder.tvAvatar.setImageResource(R.drawable.male_avatar);
        else
            viewHolder.tvAvatar.setImageResource(R.drawable.female_avatar);
        viewHolder.tvTuoi.setText(tuoi+" tuổi");
        viewHolder.tvSDT.setText(k.getSDT());
        return convertView;
    }

    public class ViewHolder {
        TextView tvTenKhach,tvTuoi, tvSDT;
        ImageView tvAvatar;
    }
}
