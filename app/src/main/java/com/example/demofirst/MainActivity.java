package com.example.demofirst;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.demofirst.base.HoaDonThang;
import com.example.demofirst.base.PhongTro;
import com.example.demofirst.base.PhongTroAdapter;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public static DatabaseQLPT db;
    private ListView lvphongtro;
    ArrayList<PhongTro> arrPhongTro = new ArrayList<>();
    PhongTroAdapter customAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=new DatabaseQLPT(this);
        lvphongtro = (ListView) findViewById(R.id.lvphongtro);
        DichVuMacDinh();
        XoaHDCu();
        show();
        customAdaper = new PhongTroAdapter(this,R.layout.ptrow,arrPhongTro);
        lvphongtro.setAdapter(customAdaper);
        click();
        registerForContextMenu(lvphongtro);

    }

    public void show(){
        Cursor a=db.getAllPhongTro();
        if(a.getCount() == 0){
        }
        else{
            arrPhongTro.clear();
            while(a.moveToNext()){
                PhongTro b=new PhongTro();
                b.setID(a.getString(0));
                b.setTEN_PHONG(a.getString(1));
                b.setSUC_CHUA(a.getString(2));
                b.setDIEN_TICH(a.getString(3));
                b.setGIA_THUE(a.getString(4));
                b.setTHONG_TIN_KHAC(a.getString(5));
                b.setSO_DIEN(a.getString(6));
                b.setSO_NUOC(a.getString(7));
                arrPhongTro.add(b);
            }
        }
    }

    //Xoá phòng trọ theo id
    public void xoaPT(String id) {
        Integer d=db.xoaPhongTro(id);
        Integer p=MainActivity.db.xoaAllKhachP(id);
        Integer h=MainActivity.db.xoaHoaDonP(id);
        if(d>0 && p>0 &&h>0)
            Toast.makeText(this,"Xoá phòng trọ, khách,hoá đơn thành công",Toast.LENGTH_LONG).show();
        else if(d>0 && p>0)
            Toast.makeText(this,"Xoá phòng trọ, khách thành công",Toast.LENGTH_LONG).show();
        else if(d>0)
            Toast.makeText(this,"Xoá phòng trọ thành công",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Phòng trọ không tồn tại",Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        show();
        customAdaper.notifyDataSetChanged();
    }

    //Sự kiện click listview
    public void click(){
        lvphongtro.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, PhongTroActivity.class);
                intent.putExtra("EDIT", (Serializable) customAdaper.getItem(i));
                startActivityForResult(intent, 1);
            }
        });

    }

    //Tạo 2 dịch vụ mặc định ( điện và nước)
    public void DichVuMacDinh(){
        SQLiteDatabase sdb=db.getWritableDatabase();
        if(db.getAllDichVu().getCount()==0){
            ContentValues contentValues=new ContentValues();
            contentValues.put("TEN_CP","Điện");
            contentValues.put("QUI_CACH","kWh");
            contentValues.put("DON_GIA","4000");
            sdb.insert("DICHVU",null,contentValues);
            contentValues=new ContentValues();
            contentValues.put("TEN_CP","Nước");
            contentValues.put("QUI_CACH","m³");
            contentValues.put("DON_GIA","3500");
            sdb.insert("DICHVU",null,contentValues);
        }
    }

    public void XoaHDCu(){
        Calendar c = Calendar.getInstance();
        String thang=(c.get(Calendar.MONTH)+1)+"";
        Integer d=db.xoaHoaDonT(thang);
    }

    public void ThemPhongTro(){
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setTitle("Thêm mới phòng trọ");
        dialog.setContentView(R.layout.them_phong_tro);
        Button huy=(Button)dialog.findViewById(R.id.btHuy);
        Button lammoi=(Button)dialog.findViewById(R.id.btThemPT);
        final EditText edTenPhong=(EditText)dialog.findViewById(R.id.edTenPhong);
        final EditText edSucchua=(EditText)dialog.findViewById(R.id.edSucchua);
        final EditText edDienTich=(EditText)dialog.findViewById(R.id.edDienTich);
        final EditText edGiaThue=(EditText)dialog.findViewById(R.id.edGiaThue);
        final EditText edSoDien=(EditText)dialog.findViewById(R.id.edSoDien);
        final EditText edSoNuoc=(EditText)dialog.findViewById(R.id.edSoNuoc);
        final EditText edTTKhac=(EditText)dialog.findViewById(R.id.edTTKhac);
        lammoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edTenPhong.getText().toString().equals("")){
                    edTenPhong.setError("Tên phòng không thể trống");
                    return;
                }
                else if(edGiaThue.getText().toString().equals("")){
                    edGiaThue.setError("Giá thuê không thể trống");
                    return;
                }
                else if(edSoDien.getText().toString().equals("")){
                    edSoDien.setError("Số điện không thể trống");
                    return;
                }
                else if(edSoNuoc.getText().toString().equals("")){
                    edSoNuoc.setError("Số nước không thể trống");
                    return;
                }
                boolean them=MainActivity.db.themPhongTro(edTenPhong.getText().toString(),
                        edSucchua.getText().toString(),
                        edDienTich.getText().toString(),
                        edGiaThue.getText().toString(),
                        edTTKhac.getText().toString(),
                        edSoDien.getText().toString(),
                        edSoNuoc.getText().toString());
                if(them==true)
                    Toast.makeText(MainActivity.this,"Them phong tro thanh cong",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this,"Them phong tro that bai",Toast.LENGTH_LONG).show();
                show();
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
    }

    //Các chức năng của context menu
    public void XemChiTiet(final PhongTro p){
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setTitle("Chi tiết");
        dialog.setContentView(R.layout.them_phong_tro);
        Button themphongtro=(Button)dialog.findViewById(R.id.btThemPT);
        Button huy=(Button)dialog.findViewById(R.id.btHuy);
        final TextView tvtitle=(TextView)dialog.findViewById(R.id.tvtitle);
        final EditText edTenPhong=(EditText)dialog.findViewById(R.id.edTenPhong);
        final EditText edSucchua=(EditText)dialog.findViewById(R.id.edSucchua);
        final EditText edDienTich=(EditText)dialog.findViewById(R.id.edDienTich);
        final EditText edGiaThue=(EditText)dialog.findViewById(R.id.edGiaThue);
        final EditText edSoDien=(EditText)dialog.findViewById(R.id.edSoDien);
        final EditText edSoNuoc=(EditText)dialog.findViewById(R.id.edSoNuoc);
        final EditText edTTKhac=(EditText)dialog.findViewById(R.id.edTTKhac);
        tvtitle.setText("Chi tiết phòng trọ");
        edTenPhong.setText(p.getTEN_PHONG().toString());
        edTenPhong.setEnabled(false);
        edSucchua.setText(p.getSUC_CHUA().toString());
        edSucchua.setHint("");
        edSucchua.setEnabled(false);
        edDienTich.setText(p.getDIEN_TICH().toString());
        edDienTich.setEnabled(false);
        edDienTich.setHint("");
        edGiaThue.setText(p.getGIA_THUE().toString());
        edGiaThue.setEnabled(false);
        edSoDien.setText(p.getSO_DIEN().toString());
        edSoDien.setEnabled(false);
        edSoNuoc.setText(p.getSO_NUOC().toString());
        edSoNuoc.setEnabled(false);
        edTTKhac.setText(p.getTHONG_TIN_KHAC().toString());
        edTTKhac.setEnabled(false);
        edTTKhac.setHint("");
        themphongtro.setVisibility(View.GONE);
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void Updatept(final PhongTro p){
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setTitle("Chi tiết");
        dialog.setContentView(R.layout.them_phong_tro);
        Button suapt=(Button)dialog.findViewById(R.id.btThemPT);
        Button xoapt=(Button)dialog.findViewById(R.id.btHuy);
        final TextView tvtitle=(TextView)dialog.findViewById(R.id.tvtitle);
        final EditText edTenPhong=(EditText)dialog.findViewById(R.id.edTenPhong);
        final EditText edSucchua=(EditText)dialog.findViewById(R.id.edSucchua);
        final EditText edDienTich=(EditText)dialog.findViewById(R.id.edDienTich);
        final EditText edGiaThue=(EditText)dialog.findViewById(R.id.edGiaThue);
        final EditText edSoDien=(EditText)dialog.findViewById(R.id.edSoDien);
        final EditText edSoNuoc=(EditText)dialog.findViewById(R.id.edSoNuoc);
        final EditText edTTKhac=(EditText)dialog.findViewById(R.id.edTTKhac);
        tvtitle.setText("Chi tiết phòng trọ");
        edTenPhong.setText(p.getTEN_PHONG().toString());
        edSucchua.setText(p.getSUC_CHUA().toString());
        edDienTich.setText(p.getDIEN_TICH().toString());
        edGiaThue.setText(p.getGIA_THUE().toString());
        edSoDien.setText(p.getSO_DIEN().toString());
        edSoNuoc.setText(p.getSO_NUOC().toString());
        edTTKhac.setText(p.getTHONG_TIN_KHAC().toString());
        suapt.setText("Sửa");
        xoapt.setText("Xoá");
        suapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edTenPhong.getText().toString().equals("")){
                    edTenPhong.setError("Tên phòng không thể trống");
                    return;
                }
                else if(edGiaThue.getText().toString().equals("")){
                    edGiaThue.setError("Giá thuê không thể trống");
                    return;
                }
                else if(edSoDien.getText().toString().equals("")){
                    edSoDien.setError("Số điện không thể trống");
                    return;
                }
                else if(edSoNuoc.getText().toString().equals("")){
                    edSoNuoc.setError("Số nước không thể trống");
                    return;
                }
                boolean them=MainActivity.db.capnhatPhongTro(p.getID(),edTenPhong.getText().toString(),
                        edSucchua.getText().toString(),
                        edDienTich.getText().toString(),
                        edGiaThue.getText().toString(),
                        edTTKhac.getText().toString(),
                        edSoDien.getText().toString(),
                        edSoNuoc.getText().toString());
                if(them==true)
                    Toast.makeText(MainActivity.this,"Cập nhật phòng trọ thành công",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(MainActivity.this,"Không thành công",Toast.LENGTH_LONG).show();
                show();
                customAdaper.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        xoapt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Bạn muốn xoá phòng "+p.getTEN_PHONG()+"?")
                        .setCancelable(false)
                        .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                xoaPT(p.getID());
                                arrPhongTro.remove(p);
                                customAdaper.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });
        dialog.show();
    }

    public void ThemHoaDon(final PhongTro p){
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setTitle("Lập hoá đơn");
        dialog.setContentView(R.layout.them_hoa_don);
        Button btThemHoaDon=(Button)dialog.findViewById(R.id.btThemHoaDon);
        Button huy=(Button)dialog.findViewById(R.id.btHuy);
        final TextView tvtitle=(TextView)dialog.findViewById(R.id.tvtitle);
        final EditText edThang=(EditText)dialog.findViewById(R.id.edThang);
        final TextView tvNgay=(TextView)dialog.findViewById(R.id.tvNgay);
        final EditText edSoDien=(EditText)dialog.findViewById(R.id.edSoDien);
        final EditText edSoNuoc=(EditText)dialog.findViewById(R.id.edSoNuoc);
        final EditText edchiphikhac=(EditText)dialog.findViewById(R.id.edChiKhac);
        final int tiendien;
        final int tiennuoc;
        edSoDien.setHint("Số điện > "+p.getSO_DIEN());
        edSoNuoc.setHint("Số điện > "+p.getSO_NUOC());
        tiendien=MainActivity.db.LayGiaDien();
        tiennuoc=MainActivity.db.LayGiaNuoc();
        Calendar c = Calendar.getInstance();
        final String phong=p.getID();
        final int giathue=Integer.parseInt(p.getGIA_THUE());
        int ngay=c.get(Calendar.DATE);
        int thang=c.get(Calendar.MONTH);
        final int nam=c.get(Calendar.YEAR);
        tvtitle.setText("Lập hoá đơn phòng "+p.getTEN_PHONG());
        edThang.setText((thang+1)+"");
        tvNgay.setText("Ngày lập: "+ngay+"/"+(thang+1)+"/"+nam);
        final String Thang=(thang+1)+"";
        final String day=ngay+"/"+(thang+1)+"/"+nam;
        btThemHoaDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor kt = db.DaLapHoaDon(phong, edThang.getText().toString());
                if(edThang.getText().toString().equals("")||Integer.parseInt(edThang.getText().toString())<1||Integer.parseInt(edThang.getText().toString())>12){
                    edThang.setError("Tháng phải từ 1-12");
                    return;
                }
                else if(Integer.parseInt(Thang)==1){
                    if(!(Integer.parseInt(edThang.getText().toString())==12||Integer.parseInt(edThang.getText().toString())==1)){
                        edThang.setError("Tháng phải nhỏ hơn hoặc bằng tháng hiện tại");
                        return;
                    }
                }
                else if(Integer.parseInt(Thang)-Integer.parseInt(edThang.getText().toString())>1||Integer.parseInt(Thang)-Integer.parseInt(edThang.getText().toString())<0){
                    edThang.setError("Tháng phải nhỏ hơn hoặc bằng tháng hiện tại");
                    return;
                }
                else if(kt.getCount()>0){
                    while(kt.moveToNext()) {
                        String[] Ngaycap = kt.getString(7).split("/");
                        if (Ngaycap[Ngaycap.length - 1].equals(nam+"")){
                            if (kt.getString(8).equals("Đã thanh toán")) {
                                Toast.makeText(MainActivity.this, "Hoá đơn đã lập và thanh toán", Toast.LENGTH_LONG).show();
                                return;
                            } else {
                                Toast.makeText(MainActivity.this, "Hoá đơn đã lập và chưa thanh toán", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }
                    }
                }
                else if(edSoDien.getText().toString().equals("")){
                    edSoDien.setError("Phải nhập số điện");
                    return;
                }
                else if(Integer.parseInt(edSoDien.getText().toString())<Integer.parseInt(p.getSO_DIEN())){
                    edSoDien.setError("Số điện không được nhỏ hơn "+p.getSO_DIEN());
                    return;
                }
                else if(edSoNuoc.getText().toString().equals("")){
                    edSoNuoc.setError("Phải nhập số nước");
                    return;
                }
                else if(Integer.parseInt(edSoNuoc.getText().toString())<Integer.parseInt(p.getSO_NUOC())){
                    edSoNuoc.setError("Số nước không được nhỏ hơn "+p.getSO_NUOC());
                    return;
                }
                else if(edchiphikhac.getText().toString().equals("")){
                    edchiphikhac.setText("0");
                }
                int SoDien=(Integer.parseInt(edSoDien.getText().toString())-Integer.parseInt(p.getSO_DIEN()));
                int SoNuoc=(Integer.parseInt(edSoNuoc.getText().toString())-Integer.parseInt(p.getSO_NUOC()));
                int cpk=Integer.parseInt(edchiphikhac.getText().toString());
                String thanhtien=SoDien*tiendien+SoNuoc*tiennuoc+cpk+giathue+"";
                boolean them=MainActivity.db.themHoaDon(edThang.getText().toString(),phong,SoDien+"",
                        SoNuoc+"",edchiphikhac.getText().toString(),thanhtien,day,"Chưa thanh toán");
                if(them==true){
                    dialog.dismiss();
                    Showhd(edThang.getText().toString(),day,SoDien,SoNuoc,edchiphikhac.getText().toString(),thanhtien,p);
                    Toast.makeText(MainActivity.this,"Thêm hoá đơn thành công",Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(MainActivity.this,"Thêm hoá đơn thất bại",Toast.LENGTH_LONG).show();
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
    }

    public void Showhd(String thang,String ngay,int sodien,int sonuoc,String cpk,String tt,final PhongTro p){
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setTitle("Chi tiết hoá đơn");
        dialog.setContentView(R.layout.chi_tiet_hoa_don);
        Button btSuahoadon=(Button)dialog.findViewById(R.id.btSuahoadon);
        Button huy=(Button)dialog.findViewById(R.id.btHuy);
        final TextView tvthanhtoan=(TextView)dialog.findViewById(R.id.tvthanhtoan);
        final TextView tvtitle=(TextView)dialog.findViewById(R.id.tvtitle);
        final TextView tvThang=(TextView)dialog.findViewById(R.id.tvThang);
        final TextView tvNgay=(TextView)dialog.findViewById(R.id.tvNgay);
        final TextView tvGiaPhong=(TextView)dialog.findViewById(R.id.tvGiaPhong);
        final TextView tvSoDien=(TextView)dialog.findViewById(R.id.tvSoDien);
        final TextView tvSoNuoc=(TextView)dialog.findViewById(R.id.tvSoNuoc);
        final TextView tvchiphikhac=(TextView)dialog.findViewById(R.id.tvchiphikhac);
        final TextView tvThanhTien=(TextView)dialog.findViewById(R.id.tvThanhTien);
        final RadioGroup rdgThanhtoan=(RadioGroup)dialog.findViewById(R.id.rdgThanhtoan);
        btSuahoadon.setVisibility(View.GONE);
        tvthanhtoan.setVisibility(View.GONE);
        rdgThanhtoan.setVisibility(View.GONE);
        tvtitle.setText("Hoá đơn "+p.getTEN_PHONG());
        tvThang.setText("Tháng: "+thang);
        tvNgay.setText("Ngày lập "+ngay);
        NumberFormat formatter = new DecimalFormat("#,###");
        tvGiaPhong.setText(formatter.format(Double.parseDouble(p.getGIA_THUE())));
        int tiendien=MainActivity.db.LayGiaDien();
        int tiennuoc=MainActivity.db.LayGiaNuoc();
        String Dien=sodien+"x"+ formatter.format(Double.parseDouble(tiendien+""))+"="+
                formatter.format(Double.parseDouble((sodien*tiendien)+""));
        String Nuoc=sonuoc+"x"+ formatter.format(Double.parseDouble(tiennuoc+""))+"="+
                formatter.format(Double.parseDouble((sonuoc*tiennuoc)+""));
        tvSoDien.setText(Dien);
        tvSoNuoc.setText(Nuoc);
        tvchiphikhac.setText(formatter.format(Double.parseDouble(cpk)));
        tvThanhTien.setText(formatter.format(Double.parseDouble(tt)));
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //Tạo menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        Intent intent;
        final int result=1;
        switch (item.getItemId()) {
            case R.id.DVid:
                intent = new Intent(this,DichVuActivity.class);
                startActivityForResult(intent,result);
                break;
            case R.id.HDid:
                intent = new Intent(this,HoaDonActivity.class);
                startActivityForResult(intent,result);
                break;
            case R.id.Themid:
                ThemPhongTro();
                break;
        }
        return true;
    }

    //Context Menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_phong, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int pos = info.position;
        final PhongTro p = arrPhongTro.get(pos);
        switch (item.getItemId())
        {
            case R.id.menuChitiet:
                XemChiTiet(p);
                return true;

            case R.id.menuupdel:
                Updatept(p);
                return true;

            case R.id.menutraphong:
                Cursor a= MainActivity.db.getAllKhachP(p.getID());
                if(a.getCount()!=0){
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Trả phòng "+p.getTEN_PHONG()+"?")
                            .setCancelable(false)
                            .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Integer d= db.xoaAllKhachP(p.getID());
                                    if(d>0)
                                        Toast.makeText(MainActivity.this,"Đã trả phòng",Toast.LENGTH_LONG).show();
                                    else
                                        Toast.makeText(MainActivity.this,"Lỗi, vui lòng thao tác lại",Toast.LENGTH_LONG).show();
                                    customAdaper.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Không", null)
                            .show();
                }
                else
                    Toast.makeText(this,"Chưa có khách thuê phòng",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menulaphd:
                Cursor b= MainActivity.db.getAllKhachP(p.getID());
                if(b.getCount() == 0){
                    Toast.makeText(this,"Thao tác thất bại, phòng không có khách thuê",Toast.LENGTH_LONG).show();
                }
                else{
                    ThemHoaDon(p);
                }
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
