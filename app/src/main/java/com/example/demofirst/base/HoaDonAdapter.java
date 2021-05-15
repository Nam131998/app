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

public class HoaDonAdapter extends ArrayAdapter<HoaDonThang> {
    private Context context;
    private int resource;
    private ArrayList<HoaDonThang> arrHoaDon;
    public HoaDonAdapter(Context context, int resource, ArrayList<HoaDonThang> arrDichVu) {
        super(context, resource, arrDichVu);
        this.context = context;
        this.resource = resource;
        this.arrHoaDon = arrDichVu;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dong_hoa_don, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvThang = (TextView) convertView.findViewById(R.id.tvThanghd);
            viewHolder.tvPhong = (TextView) convertView.findViewById(R.id.tvPhong);
            viewHolder.tvGia = (TextView) convertView.findViewById(R.id.tvGia);
            viewHolder.tvChiPhiKhac = (TextView) convertView.findViewById(R.id.tvChiPhiKhac);
            viewHolder.tvThanhTien = (TextView) convertView.findViewById(R.id.tvThanhTien);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HoaDonThang hoadon = arrHoaDon.get(position);
        PhongTro b=new PhongTro();
        Cursor a= MainActivity.db.Lay1PhongTro(hoadon.getPHONGID());
        if(a.getCount() == 0){
            viewHolder.tvPhong.setText("Đã xoá phòng");
        }
        else{
            while(a.moveToNext()){
                b.setID(a.getString(0));
                b.setTEN_PHONG(a.getString(1));
                b.setSUC_CHUA(a.getString(2));
                b.setDIEN_TICH(a.getString(3));
                b.setGIA_THUE(a.getString(4));
                b.setTHONG_TIN_KHAC(a.getString(5));
                b.setSO_DIEN(a.getString(6));
                b.setSO_NUOC(a.getString(7));
            }
        }
        viewHolder.tvPhong.setText(b.getTEN_PHONG());
        viewHolder.tvThang.setText("Tháng "+hoadon.getTHANG());
        NumberFormat formatter = new DecimalFormat("#,###");
        String gia=formatter.format(Double.parseDouble(b.getGIA_THUE()));
        String khac=Integer.parseInt(hoadon.getSO_DIEN())*MainActivity.db.LayGiaDien()+Integer.parseInt(hoadon.getSO_NUOC())*MainActivity.db.LayGiaNuoc()+Integer.parseInt(hoadon.getCHI_PHI_KHAC())+"";
        viewHolder.tvChiPhiKhac.setText("Khác( điện, nước,...):"+formatter.format(Double.parseDouble(khac)));
        viewHolder.tvThanhTien.setText("Thành tiền:"+formatter.format(Double.parseDouble(hoadon.getTHANH_TIEN())));
        viewHolder.tvGia.setText("Phòng: "+gia);
        return convertView;
    }

    public class ViewHolder {
        TextView tvPhong,tvChiPhiKhac, tvGia,tvThanhTien,tvThang;
    }

}
