package com.example.demofirst;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
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

import com.example.demofirst.base.KhachAdapter;
import com.example.demofirst.base.KhachThue;
import com.example.demofirst.base.PhongTro;
import com.example.demofirst.base.PhongTroAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class PhongTroActivity extends AppCompatActivity {
    EditText edTenPhong,edSucchua,edDienTich,edGiaThue,edSoDien,edSoNuoc,edTTKhac;
    TextView tvTenPhongTro,tvSoNguoiO,tvDs;
    PhongTro p;
    private ListView lvnguoithue;
    ArrayList<KhachThue> arrKhach = new ArrayList<>();
    KhachAdapter customAdaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phong_tro);
        if (getIntent().getExtras() != null) {
            p = (PhongTro) getIntent().getSerializableExtra("EDIT");
        }
        Anhxa();
        tvTenPhongTro.setText(p.getTEN_PHONG());
        showKhach();
        actionBar();
        lvnguoithue=(ListView) findViewById(R.id.lvnguoithue);
        customAdaper = new KhachAdapter(this,R.layout.dong_khach,arrKhach);
        lvnguoithue.setAdapter(customAdaper);
        MenuKhach();
    }

    //ActionBar
    public void actionBar(){
        //Lấy chiều cao của ActionBar
        TypedArray styledAttributes =
                getTheme().obtainStyledAttributes(new int[] { android.R.attr.actionBarSize });
        int actionBarSize = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        //Tạo Drawable mới bằng cách thu/phóng
        Drawable drawable= getResources().getDrawable(R.drawable.phongtroicon);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        Drawable newdrawable = new BitmapDrawable(getResources(),
                Bitmap.createScaledBitmap(bitmap, actionBarSize,  actionBarSize, true));

        //Cài đặt tiêu đề, icon cho actionbar
        getSupportActionBar().setTitle("PHÒNG TRỌ");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(newdrawable);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_phu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.Them:
                ThemKhachHang();
                return true;
            default:break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Anhxa(){
        edTenPhong=(EditText)findViewById(R.id.edTenPhong);
        edSucchua=(EditText)findViewById(R.id.edSucchua);
        edDienTich=(EditText)findViewById(R.id.edDienTich);
        edGiaThue=(EditText)findViewById(R.id.edGiaThue);
        edSoDien=(EditText)findViewById(R.id.edSoDien);
        edSoNuoc=(EditText)findViewById(R.id.edSoNuoc);
        edTTKhac=(EditText)findViewById(R.id.edTTKhac);
        tvTenPhongTro=(TextView) findViewById(R.id.tvTenPhongTro);
        tvSoNguoiO=(TextView) findViewById(R.id.tvSoNguoiO);
        tvDs=(TextView) findViewById(R.id.tvDs);
    }

    public void showKhach(){
        Cursor a=MainActivity.db.getAllKhachP(p.getID());
        if(a.getCount() == 0){
            tvSoNguoiO.setText("Phòng trống");
            tvDs.setText("Chưa có khách thuê phòng");
        }
        else{
            arrKhach.clear();
            tvSoNguoiO.setText(a.getCount()+" người ở trọ");
            while(a.moveToNext()){
                KhachThue b=new KhachThue();
                b.setID(a.getString(0));
                b.setTEN_KH(a.getString(1));
                b.setGIOI_TINH(a.getString(2));
                b.setNAM_SINH(a.getString(3));
                b.setCMND(a.getString(4));
                b.setNGAY_CAP(a.getString(5));
                b.setSDT(a.getString(6));
                b.setPHONG_O(a.getString(7));
                b.setHINH_ANH(a.getString(8));
                arrKhach.add(b);
            }
        }
    }

    public void ThemKhachHang(){
        final Dialog dialog=new Dialog(PhongTroActivity.this);
        dialog.setTitle("Thêm mới dịch vụ");
        dialog.setContentView(R.layout.them_khach);
        Button btThemKhach=(Button)dialog.findViewById(R.id.btThemKhach);
        Button huy=(Button)dialog.findViewById(R.id.btHuy);
        RadioButton rdNam=(RadioButton)dialog.findViewById(R.id.rdNam);
        RadioButton rdNu=(RadioButton)dialog.findViewById(R.id.rdNu);
        final EditText edTenKhach=(EditText)dialog.findViewById(R.id.edTenKhach);
        final EditText edNamSinh=(EditText)dialog.findViewById(R.id.edNamSinh);
        final EditText edCMND=(EditText)dialog.findViewById(R.id.edCMND);
        final EditText edNgay=(EditText)dialog.findViewById(R.id.edNgay);
        final EditText edThang=(EditText)dialog.findViewById(R.id.edThang);
        final EditText edNam=(EditText)dialog.findViewById(R.id.edNam);
        final EditText edSDT=(EditText)dialog.findViewById(R.id.edSDT);
        final RadioGroup rdgGioiTinh=(RadioGroup)dialog.findViewById(R.id.rdgGioitinh) ;
        btThemKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int a=c.get(Calendar.YEAR);
                if(edTenKhach.getText().toString().equals("")){
                    edTenKhach.setError("Tên khách không thể trống");
                    return;
                }
                else if(edNamSinh.getText().toString().equals("")){
                    edNamSinh.setError("Năm sinh không thể trống");
                    return;
                }
                else if(edSDT.getText().toString().equals("")){
                    edSoNuoc.setError("Số điện thoại không thể trống");
                    return;
                }
                String gt="";
                int checkedRadioId = rdgGioiTinh.getCheckedRadioButtonId();
                if(checkedRadioId== R.id.rdNam) {
                    gt="Nam";
                } else if(checkedRadioId== R.id.rdNu ) {
                    gt="Nữ";
                }
                String NgayCap=edNgay.getText().toString()+"/"+edThang.getText().toString()+"/"+edNam.getText().toString();
                boolean them=MainActivity.db.themKhach(edTenKhach.getText().toString(),gt,edNamSinh.getText().toString(),
                        edCMND.getText().toString(),NgayCap,edSDT.getText().toString(),p.getID(),"");
                if(them==true){
                    Toast.makeText(PhongTroActivity.this,"Thêm khách thành công",Toast.LENGTH_LONG).show();
                    tvDs.setText("Danh sách khách thuê");
                }
                else
                    Toast.makeText(PhongTroActivity.this,"Không thêm được",Toast.LENGTH_LONG).show();
                showKhach();
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

    public void xoaKhach(String id) {
        Integer d=MainActivity.db.xoaKhach(id);
        if(d>0)
            Toast.makeText(this,"Xoá khách hàng thành công",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Xoá khách hàng thất bại",Toast.LENGTH_LONG).show();
    }


    public void MenuKhach(){
        lvnguoithue.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int pos, long id) {
                final KhachThue pt = (KhachThue) arrKhach.get(pos);
                final Dialog dialog=new Dialog(PhongTroActivity.this);
                dialog.setTitle("Chi tiết");
                dialog.setContentView(R.layout.them_khach);
                Button btThemKhach=(Button)dialog.findViewById(R.id.btThemKhach);
                Button huy=(Button)dialog.findViewById(R.id.btHuy);
                RadioButton rdNam=(RadioButton)dialog.findViewById(R.id.rdNam);
                RadioButton rdNu=(RadioButton)dialog.findViewById(R.id.rdNu);
                final EditText edTenKhach=(EditText)dialog.findViewById(R.id.edTenKhach);
                final EditText edNamSinh=(EditText)dialog.findViewById(R.id.edNamSinh);
                final EditText edCMND=(EditText)dialog.findViewById(R.id.edCMND);
                final EditText edNgay=(EditText)dialog.findViewById(R.id.edNgay);
                final EditText edThang=(EditText)dialog.findViewById(R.id.edThang);
                final EditText edNam=(EditText)dialog.findViewById(R.id.edNam);
                final EditText edSDT=(EditText)dialog.findViewById(R.id.edSDT);
                TextView tvtitle=(TextView)dialog.findViewById(R.id.tvtitle);
                final RadioGroup rdgGioiTinh=(RadioGroup)dialog.findViewById(R.id.rdgGioitinh) ;
                tvtitle.setText("Chi tiết khách thuê");
                edTenKhach.setText(pt.getTEN_KH());
                edNamSinh.setText(pt.getNAM_SINH());
                edCMND.setText(pt.getCMND());
                if(pt.getNGAY_CAP().equals("")){
                }
                else {
                    String[] Ngaycap = pt.getNGAY_CAP().split("/");
                    edNgay.setText(Ngaycap[0]);
                    edThang.setText(Ngaycap[1]);
                    edNam.setText(Ngaycap[2]);
                }
                edSDT.setText(pt.getSDT());
                btThemKhach.setText("Sửa");
                huy.setText("Xoá");
                if(pt.getGIOI_TINH().equals("Nam"))
                    rdNam.setChecked(true);
                else
                    rdNu.setChecked(true);
                btThemKhach.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String gt="";
                        int checkedRadioId = rdgGioiTinh.getCheckedRadioButtonId();
                        if(checkedRadioId== R.id.rdNam) {
                            gt="Nam";
                        } else if(checkedRadioId== R.id.rdNu ) {
                            gt="Nữ";
                        }
                        Calendar c = Calendar.getInstance();
                        int a=c.get(Calendar.YEAR);
                        if(edTenKhach.getText().toString().equals("")){
                            edTenKhach.setError("Tên khách không thể trống");
                            return;
                        }
                        else if(edNamSinh.getText().toString().equals("")){
                            edNamSinh.setError("Năm sinh không thể trống");
                            return;
                        }
                        else if(edSDT.getText().toString().equals("")){
                            edSoNuoc.setError("Số điện thoại không thể trống");
                            return;
                        }
                        String NgayCap="";
                        if(edNgay.getText().toString().equals("")||edThang.getText().toString().equals("")||edNam.getText().toString().equals("")){

                        }
                        else {
                            NgayCap=edNgay.getText().toString()+"/"+edThang.getText().toString()+"/"+edNam.getText().toString();
                        }
                        boolean them=MainActivity.db.capnhatKhach(pt.getID(),edTenKhach.getText().toString(),gt,edNamSinh.getText().toString(),
                                edCMND.getText().toString(),NgayCap,edSDT.getText().toString(),p.getID(),"");
                        if(them==true)
                            Toast.makeText(PhongTroActivity.this,"Sửa thông tin khách thành công",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(PhongTroActivity.this,"Không sửa được",Toast.LENGTH_LONG).show();
                        showKhach();
                        customAdaper.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        new AlertDialog.Builder(PhongTroActivity.this)
                                .setMessage("Bạn muốn xoá "+pt.getTEN_KH()+"?")
                                .setCancelable(false)
                                .setPositiveButton("Xoá", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        xoaKhach(pt.getID());
                                        arrKhach.remove(pt);
                                        if(arrKhach.size()==0){
                                            tvSoNguoiO.setText("Phòng trống");
                                            tvDs.setText("Chưa có khách thuê phòng");
                                        }
                                        else
                                            tvSoNguoiO.setText(arrKhach.size()+" người ở trọ");
                                        customAdaper.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Không", null)
                                .show();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }
}
