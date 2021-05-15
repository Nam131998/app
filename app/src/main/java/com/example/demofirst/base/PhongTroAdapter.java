package com.example.demofirst.base;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.demofirst.MainActivity;
import com.example.demofirst.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

public class PhongTroAdapter extends ArrayAdapter<PhongTro> {
    private Context context;
    private int resource;
    private ArrayList<PhongTro> arrPhongTro;

    public PhongTroAdapter(Context context, int resource, ArrayList<PhongTro> arrPhongTro) {
        super(context, resource, arrPhongTro);
        this.context = context;
        this.resource = resource;
        this.arrPhongTro = arrPhongTro;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.ptrow, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTenPHong = (TextView) convertView.findViewById(R.id.tvTenPHong);
            viewHolder.tvGiaThue = (TextView) convertView.findViewById(R.id.tvGiaThue);
            viewHolder.tvSoNguoi = (TextView) convertView.findViewById(R.id.tvSoNguoi);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        PhongTro phontro = arrPhongTro.get(position);
        NumberFormat formatter = new DecimalFormat("#,###");
        viewHolder.tvTenPHong.setText(phontro.getTEN_PHONG());
        viewHolder.tvGiaThue.setText(formatter.format(Double.parseDouble(phontro.getGIA_THUE())));
        Cursor a= MainActivity.db.getAllKhachP(phontro.getID());
        if(a.getCount() == 0){
            viewHolder.tvSoNguoi.setText("Phòng trống");
        }
        else{
            viewHolder.tvSoNguoi.setText(a.getCount()+" người ở");
        }
        return convertView;
    }

    public class ViewHolder {
        TextView tvTenPHong, tvGiaThue,tvSoNguoi;
    }
}
