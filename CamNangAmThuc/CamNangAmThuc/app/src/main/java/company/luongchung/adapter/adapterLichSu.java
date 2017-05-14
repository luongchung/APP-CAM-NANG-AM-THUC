package company.luongchung.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import company.luongchung.camnangamthuc.R;
import company.luongchung.camnangamthuc.chitiet;
import company.luongchung.camnangamthuc.yeuthich;
import company.luongchung.model.LichSuMonAn;
import company.luongchung.model.MonAn;

import static android.content.Context.MODE_PRIVATE;

public class adapterLichSu extends ArrayAdapter<LichSuMonAn> {

    Activity context;
    int resource;
    List<LichSuMonAn> objects;
    SQLiteDatabase sqLiteDatabase = null;
    String DATABASE_NAME = "dbCamNangAmThuc.sqlite";

    public adapterLichSu(Activity context, int resource, List<LichSuMonAn> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View row = inflater.inflate(resource, null);
        TextView txtTenMonAn = (TextView) row.findViewById(R.id.txtTenMon);
        TextView txtTime = (TextView) row.findViewById(R.id.txtTime);
        TextView txtTitle = (TextView) row.findViewById(R.id.txtTitle);
        ImageView ivICon = (ImageView) row.findViewById(R.id.ivIcon);
        Button btnXoaLichSu = (Button) row.findViewById(R.id.btnXoaLichSu);
        final LichSuMonAn lichSuMonAn = objects.get(position);
        txtTenMonAn.setText(lichSuMonAn.getTxtTenMon());
        txtTitle.setText(lichSuMonAn.getTitle());
        String URL = lichSuMonAn.getLinkImg();
        txtTime.setText(lichSuMonAn.getTime());
        Picasso.with(context).load(URL).into(ivICon);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyClickItem(lichSuMonAn);
            }
        });
        btnXoaLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyXoaLichSu(lichSuMonAn);
                objects.remove(position);
                notifyDataSetChanged();
            }
        });
        return row;
    }

    private void xuLyXoaLichSu(LichSuMonAn lichSuMonAn) {
        sqLiteDatabase = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("DELETE FROM TBLICHSU" + " WHERE TENMONAN" + "='" + lichSuMonAn.getTxtTenMon() + "'");
        sqLiteDatabase.close();
    }

    private void xuLyClickItem(LichSuMonAn lichSuMonAn) {
        Intent intent = new Intent(context, chitiet.class);
        intent.putExtra("TenMonAn", lichSuMonAn.getTxtTenMon());
        intent.putExtra("Title", lichSuMonAn.getTitle());
        intent.putExtra("LinkURL", lichSuMonAn.getLinkURL());
        intent.putExtra("LinkImage", lichSuMonAn.getLinkImg());
        context.startActivityForResult(intent, 90);
    }
}


