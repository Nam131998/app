package com.example.demofirst.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.demofirst.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class DichVuAdapter extends ArrayAdapter<DichVu> {
    private Context context;
    private int resource;
    private ArrayList<DichVu> arrDichVu;
    public DichVuAdapter(Context context, int resource, ArrayList<DichVu> arrDichVu) {
        super(context, resource, arrDichVu);
        this.context = context;
        this.resource = resource;
        this.arrDichVu = arrDichVu;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dong_dich_vu, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvDichVu = (TextView) convertView.findViewById(R.id.tvDichVu);
            viewHolder.tvGia = (TextView) convertView.findViewById(R.id.tvGia);
            viewHolder.tvstt = (TextView) convertView.findViewById(R.id.tvstt);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DichVu dichvu = arrDichVu.get(position);
        viewHolder.tvstt.setText(String.valueOf(position+1));
        NumberFormat formatter = new DecimalFormat("#,###");
        String gia=formatter.format(Double.parseDouble(dichvu.getDON_GIA()))+" VNĐ/"+dichvu.getQUY_CACH();
        viewHolder.tvDichVu.setText(dichvu.getTEN_CP());
        viewHolder.tvGia.setText(gia);
        return convertView;
    }

    public class ViewHolder {
        TextView tvstt,tvDichVu, tvGia;
    }
}
