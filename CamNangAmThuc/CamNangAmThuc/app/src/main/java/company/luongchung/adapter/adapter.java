package company.luongchung.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import company.luongchung.camnangamthuc.R;
import company.luongchung.camnangamthuc.item_loai;
import company.luongchung.model.LoaiMonAn;

/**
 * Created by LUONG CHUNG on 3/6/2017.
 */

public class adapter extends ArrayAdapter<LoaiMonAn> {
    Activity context;
    int resource;
    ArrayList<LoaiMonAn> objects;
    Typeface font;
    public adapter(Activity context, int resource, ArrayList<LoaiMonAn> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=this.context.getLayoutInflater();
        View row=layoutInflater.inflate(R.layout.item,null);
        font = Typeface.createFromAsset(context.getAssets(),"fonts/font3.ttf");
        final LoaiMonAn loaiMonAn=objects.get(position);
        TextView txtTenLoai= (TextView) row.findViewById(R.id.txtTenLoai);
        txtTenLoai.setText(loaiMonAn.getTenLoai());
        txtTenLoai.setTypeface(font);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyClickItem(loaiMonAn);
            }
        });
        return row;
    }

    private void xuLyClickItem(LoaiMonAn loaiMonAn) {
        Intent intent=new Intent(this.context,item_loai.class);
        intent.putExtra("TenLoai",loaiMonAn.getTenLoai());
        intent.putExtra("LinkURL",loaiMonAn.getLinkURL());
        context.startActivityForResult(intent,14);
    }

}
