package com.example.demofirst;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demofirst.base.DichVu;
import com.example.demofirst.base.HoaDonAdapter;
import com.example.demofirst.base.HoaDonThang;
import com.example.demofirst.base.PhongTro;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class HoaDonActivity extends AppCompatActivity {

    TextView tvDs;
    RadioButton rdDa,rdChua;
    RadioGroup rdgLoai;
    public ListView lvhoadon;
    public int tiendien,tiennuoc;
    Calendar c = Calendar.getInstance();
    int thang;
    ArrayList<HoaDonThang> arrHoaDonDa = new ArrayList<>();
    HoaDonAdapter customAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoa_don);
        Anhxa();
        showHoaDon(1);
        TienDien();
        TienNuoc();
        customAdaper = new HoaDonAdapter(this,R.layout.dong_hoa_don,arrHoaDonDa);
        lvhoadon.setAdapter(customAdaper);
        LoaiHoaDon();
        SuaHoaDon();
        actionBar();
    }

    public void Anhxa(){
        tvDs=(TextView) findViewById(R.id.tvDs);
        rdDa=(RadioButton)findViewById(R.id.rdDa);
        rdChua=(RadioButton)findViewById(R.id.rdChua);
        rdgLoai=(RadioGroup)findViewById(R.id.rdgLoai);
        lvhoadon = (ListView) findViewById(R.id.lvhoadon);
    }

    public void showHoaDon(int loai){
        Cursor a=MainActivity.db.getAllHoaDon();
        if(a.getCount() == 0){
        }
        else{
            arrHoaDonDa.clear();
            while(a.moveToNext()){
                HoaDonThang b=new HoaDonThang();
                b.setID(a.getString(0));
                b.setTHANG(a.getString(1));
                b.setPHONGID(a.getString(2));
                b.setSO_DIEN(a.getString(3));
                b.setSO_NUOC(a.getString(4));
                b.setCHI_PHI_KHAC(a.getString(5));
                b.setTHANH_TIEN(a.getString(6));
                b.setNGAY_LAP(a.getString(7));
                b.setTINH_TRANG(a.getString(8));
                if(loai==0&& a.getString(8).equals("Đã thanh toán"))
                    arrHoaDonDa.add(b);
                else if(loai==1&&a.getString(8).equals("Chưa thanh toán"))
                    arrHoaDonDa.add(b);
            }
        }
    }

    public void LoaiHoaDon(){
        rdgLoai.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int checkedRadioId = rdgLoai.getCheckedRadioButtonId();
                if(checkedRadioId== R.id.rdChua) {
                    showHoaDon(1);
                    customAdaper.notifyDataSetChanged();
                } else if(checkedRadioId== R.id.rdDa ) {
                    showHoaDon(0);
                    customAdaper.notifyDataSetChanged();
                }
            }
        });
    }

    public void TienDien(){
        DichVu b=new DichVu();
        Cursor a= MainActivity.db.get1DichVu("1");
        if(a.getCount() == 0){
            tiendien=1;
        }
        else{
            while(a.moveToNext()){
                b.setMaCP(a.getString(0));
                b.setTEN_CP(a.getString(1));
                b.setQUY_CACH(a.getString(2));
                b.setDON_GIA(a.getString(3));
            }
            tiendien=Integer.parseInt(b.getDON_GIA());
        }
    }

    public void TienNuoc(){
        DichVu b=new DichVu();
        Cursor a= MainActivity.db.get1DichVu("2");
        if(a.getCount() == 0){
            tiennuoc=1;
        }
        else{
            while(a.moveToNext()){
                b.setMaCP(a.getString(0));
                b.setTEN_CP(a.getString(1));
                b.setQUY_CACH(a.getString(2));
                b.setDON_GIA(a.getString(3));
            }
            tiennuoc=Integer.parseInt(b.getDON_GIA());
        }
    }

    public void SuaHoaDon(){
        lvhoadon.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int pos, long id) {
                final HoaDonThang p;
                p = (HoaDonThang) arrHoaDonDa.get(pos);
                final Dialog dialog=new Dialog(HoaDonActivity.this);
                dialog.setTitle("Chi tiết hoá đơn");
                dialog.setContentView(R.layout.chi_tiet_hoa_don);
                Button btSuahoadon=(Button)dialog.findViewById(R.id.btSuahoadon);
                Button huy=(Button)dialog.findViewById(R.id.btHuy);
                final TextView tvtitle=(TextView)dialog.findViewById(R.id.tvtitle);
                final TextView tvThang=(TextView)dialog.findViewById(R.id.tvThang);
                final TextView tvNgay=(TextView)dialog.findViewById(R.id.tvNgay);
                final TextView tvGiaPhong=(TextView)dialog.findViewById(R.id.tvGiaPhong);
                final TextView tvSoDien=(TextView)dialog.findViewById(R.id.tvSoDien);
                final TextView tvSoNuoc=(TextView)dialog.findViewById(R.id.tvSoNuoc);
                final TextView tvchiphikhac=(TextView)dialog.findViewById(R.id.tvchiphikhac);
                final TextView tvThanhTien=(TextView)dialog.findViewById(R.id.tvThanhTien);
                final RadioGroup rdgThanhtoan=(RadioGroup)dialog.findViewById(R.id.rdgThanhtoan);
                RadioButton rdRoi=(RadioButton)dialog.findViewById(R.id.rdRoi);
                RadioButton rdChua=(RadioButton)dialog.findViewById(R.id.rdChua);
                RadioButton rdHuy=(RadioButton)dialog.findViewById(R.id.rdHuy);
                PhongTro b=new PhongTro();
                Cursor a= MainActivity.db.Lay1PhongTro(p.getPHONGID());
                if(a.getCount() == 0){
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
                    tvtitle.setText("Hoá đơn "+b.getTEN_PHONG());
                }
                final PhongTro c=b;
                tvThang.setText("Tháng: "+p.getTHANG());
                tvNgay.setText("Ngày lập "+p.getNGAY_LAP());
                NumberFormat formatter = new DecimalFormat("#,###");
                tvGiaPhong.setText(formatter.format(Double.parseDouble(b.getGIA_THUE())));
                String Dien=formatter.format(Double.parseDouble(p.getSO_DIEN()))+"x"+
                        formatter.format(Double.parseDouble(tiendien+""))+"="+
                        formatter.format(Double.parseDouble((Integer.parseInt(p.getSO_DIEN())*tiendien)+""));
                String Nuoc=formatter.format(Double.parseDouble(p.getSO_NUOC()))+"x"+
                        formatter.format(Double.parseDouble(tiennuoc+""))+"="+
                        formatter.format(Double.parseDouble((Integer.parseInt(p.getSO_NUOC())*tiennuoc)+""));
                tvSoDien.setText(Dien);
                tvSoNuoc.setText(Nuoc);
                tvchiphikhac.setText(formatter.format(Double.parseDouble(p.getCHI_PHI_KHAC())));
                if(p.getTINH_TRANG().equals("Chưa thanh toán"))
                    rdChua.setChecked(true);
                else if(p.getTINH_TRANG().equals("Đã thanh toán")){
                    rdRoi.setChecked(true);
                    rdRoi.setEnabled(false);
                    rdChua.setVisibility(View.GONE);
                    rdHuy.setVisibility(View.GONE);
                    btSuahoadon.setVisibility(View.GONE);
                }
                tvThanhTien.setText(p.getTHANH_TIEN());
                btSuahoadon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tt="";
                        int checkedRadioId = rdgThanhtoan.getCheckedRadioButtonId();
                        if(checkedRadioId== R.id.rdChua) {
                            tt="Chưa thanh toán";
                            return;
                        } else if(checkedRadioId== R.id.rdRoi ) {
                            tt="Đã thanh toán";
                            boolean them=MainActivity.db.capnhatHoaDon(p.getID(),p.getTHANG(),p.getPHONGID(),p.getSO_DIEN(),
                                    p.getSO_NUOC(),p.getCHI_PHI_KHAC(),p.getTHANH_TIEN(),p.getNGAY_LAP(),tt);
                            if(them==true){
                                String dien=Integer.parseInt(c.getSO_DIEN())+Integer.parseInt(p.getSO_DIEN())+"";
                                String nuoc=Integer.parseInt(c.getSO_NUOC())+Integer.parseInt(p.getSO_NUOC())+"";
                                MainActivity.db.capnhatDienNuoc(c,dien,nuoc);
                                Toast.makeText(HoaDonActivity.this,"Cập nhật hoá đơn thành công",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(HoaDonActivity.this,"Không thành công",Toast.LENGTH_LONG).show();
                        } else if(checkedRadioId== R.id.rdHuy ){
                            Integer them=MainActivity.db.xoaHoaDon(p.getID());
                            if(them>0){
                                Toast.makeText(HoaDonActivity.this,"Đã xoá hoá đơn",Toast.LENGTH_LONG).show();
                            }
                            else
                                Toast.makeText(HoaDonActivity.this,"Lỗi, vui lòng thao tác lại",Toast.LENGTH_LONG).show();
                        }
                        showHoaDon(1);
                        customAdaper.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    //ActionBar
    public void actionBar(){
        //Lấy chiều cao của ActionBar
        TypedArray styledAttributes =
                getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        //Tạo Drawable mới bằng cách thu/phóng
        Drawable drawable= getResources().getDrawable(R.drawable.icon_hoa_don);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(bitmap, actionBarSize,  actionBarSize, true));

        //Cài đặt tiêu đề, icon cho actionbar
        getSupportActionBar().setTitle("HOÁ ĐƠN");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(newdrawable);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:break;
        }

        return super.onOptionsItemSelected(item);
    }
}
