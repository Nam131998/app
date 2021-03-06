package com.example.demofirst;

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
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demofirst.base.DichVu;
import com.example.demofirst.base.DichVuAdapter;
import com.example.demofirst.base.PhongTro;

import java.util.ArrayList;

public class DichVuActivity extends AppCompatActivity {

    public ListView lvdichvu;
    ArrayList<DichVu> arrDichVu = new ArrayList<>();
    DichVuAdapter customAdaper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dich_vu);
        lvdichvu = (ListView) findViewById(R.id.lvdichvu);
        showAll();
        customAdaper = new DichVuAdapter(this,R.layout.ptrow,arrDichVu);
        lvdichvu.setAdapter(customAdaper);
        registerForContextMenu(lvdichvu);
        actionBar();
        Menu();

    }

    public void showAll(){
        Cursor a=MainActivity.db.getAllDichVu();
        if(a.getCount() == 0){
        }
        else{
            arrDichVu.clear();
            while(a.moveToNext()){
                DichVu b=new DichVu();
                b.setMaCP(a.getString(0));
                b.setTEN_CP(a.getString(1));
                b.setQUY_CACH(a.getString(2));
                b.setDON_GIA(a.getString(3));
                arrDichVu.add(b);
            }
        }
    }

    public void Xoapt(){
        lvdichvu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int pos, long id) {
                final DichVu pt = (DichVu)arrDichVu.get(pos);
                if(pos==0 || pos==1)
                    Toast.makeText(DichVuActivity.this,"Kh??ng ???????c xo?? d??ng n??y",Toast.LENGTH_LONG).show();
                else{
                    new AlertDialog.Builder(DichVuActivity.this)
                            .setMessage("B???n mu???n xo?? d???ch v??? "+pt.getTEN_CP()+"?")
                            .setCancelable(false)
                            .setPositiveButton("Xo??", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    xoaPT(pt.getMaCP());
                                    arrDichVu.remove(pt);
                                    customAdaper.notifyDataSetChanged();
                                }
                            })
                            .setNegativeButton("Kh??ng", null)
                            .show();
                }
                return false;
            }
        });
    }

    public void ThemDichVu(View view){
        final Dialog dialog=new Dialog(DichVuActivity.this);
        dialog.setTitle("Th??m m???i d???ch v???");
        dialog.setContentView(R.layout.them_dich_vu);
        Button themdichvu=(Button)dialog.findViewById(R.id.btnThemDichVu);
        Button huy=(Button)dialog.findViewById(R.id.btnHuy);
        final EditText tendv=(EditText)dialog.findViewById(R.id.edTenDV);
        final EditText quicach=(EditText)dialog.findViewById(R.id.edQuicach);
        final EditText gia=(EditText)dialog.findViewById(R.id.edGia);
        themdichvu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tendv.getText().toString().equals("")){
                    tendv.setError("Ph???i nh???p t??n d???ch v???");
                    return;
                }
                else if(quicach.getText().toString().equals("")){
                    quicach.setError("Ph???i nh???p qui c??ch");
                    return;
                }
                else if(gia.getText().toString().equals("")){
                    gia.setText("0");
                }
                boolean them=MainActivity.db.themDichVu(tendv.getText().toString(),quicach.getText().toString(),gia.getText().toString());
                if(them==true)
                    Toast.makeText(DichVuActivity.this,"Th??m d???ch v??? th??nh c??ng",Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(DichVuActivity.this,"Kh??ng th??m ???????c",Toast.LENGTH_LONG).show();
                showAll();
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

    public void xoaPT(String id) {
        Integer d=MainActivity.db.xoaDichVu(id);
        if(d>0)
            Toast.makeText(this,"Xo?? d???ch v??? th??nh c??ng",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this,"Xo?? d???ch v??? th???t b???i",Toast.LENGTH_LONG).show();
    }

    public void Menu(){
        lvdichvu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v,
                                           int pos, long id) {
                final DichVu pt = (DichVu)arrDichVu.get(pos);
                final Dialog dialog=new Dialog(DichVuActivity.this);
                dialog.setTitle("Chi ti???t");
                dialog.setContentView(R.layout.them_dich_vu);
                Button themdichvu=(Button)dialog.findViewById(R.id.btnThemDichVu);
                Button huy=(Button)dialog.findViewById(R.id.btnHuy);
                TextView tvtitle=(TextView)dialog.findViewById(R.id.tvtitle);
                final EditText tendv=(EditText)dialog.findViewById(R.id.edTenDV);
                final EditText quicach=(EditText)dialog.findViewById(R.id.edQuicach);
                final EditText gia=(EditText)dialog.findViewById(R.id.edGia);
                tvtitle.setText("Chi ti???t d???ch v???");
                tendv.setText(pt.getTEN_CP());
                quicach.setText(pt.getQUY_CACH());
                gia.setText(pt.getDON_GIA());
                if(pos==0||pos==1){
                    huy.setEnabled(false);
                    tendv.setEnabled(false);
                    quicach.setEnabled(false);
                }
                themdichvu.setText("S???a");
                huy.setText("Xo??");
                themdichvu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(tendv.getText().toString().equals("")){
                            tendv.setError("Ph???i nh???p t??n d???ch v???");
                            return;
                        }
                        else if(quicach.getText().toString().equals("")){
                            quicach.setError("Ph???i nh???p qui c??ch");
                            return;
                        }
                        else if(gia.getText().toString().equals("")){
                            gia.setText("0");
                        }
                        boolean them=MainActivity.db.capnhatDichVu(pt.getMaCP(),tendv.getText().toString(),quicach.getText().toString(),gia.getText().toString());
                        if(them==true)
                            Toast.makeText(DichVuActivity.this,"S???a d???ch v??? th??nh c??ng",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(DichVuActivity.this,"Kh??ng s???a ???????c",Toast.LENGTH_LONG).show();
                        showAll();
                        customAdaper.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        new AlertDialog.Builder(DichVuActivity.this)
                                .setMessage("B???n mu???n xo?? d???ch v??? "+pt.getTEN_CP()+"?")
                                .setCancelable(false)
                                .setPositiveButton("Xo??", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        xoaPT(pt.getMaCP());
                                        arrDichVu.remove(pt);
                                        customAdaper.notifyDataSetChanged();
                                    }
                                })
                                .setNegativeButton("Kh??ng", null)
                                .show();
                    }
                });
                dialog.show();
                return false;
            }
        });
    }

    //ActionBar
    public void actionBar(){
        //C??i ?????t ti??u ?????, icon cho actionbar
        getSupportActionBar().setTitle("D???CH V???");
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
