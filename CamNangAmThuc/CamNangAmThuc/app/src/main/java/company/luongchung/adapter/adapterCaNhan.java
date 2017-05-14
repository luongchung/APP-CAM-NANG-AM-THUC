package company.luongchung.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayOutputStream;
import java.util.List;
import company.luongchung.camnangamthuc.R;
import company.luongchung.camnangamthuc.SuaMonAn;
import company.luongchung.model.MonAnCaNhan;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by LUONGCHUNG on 4/12/2017.
 */
public class adapterCaNhan extends ArrayAdapter<MonAnCaNhan> {
    Activity context;
    int resource;
    List<MonAnCaNhan> objects;
    ImageView iv_Anh;
    Button btn_Xoa,btn_Sua;
    TextView txt_TenMonAn;
    SQLiteDatabase sqLiteDatabase = null;
    String DATABASE_NAME = "dbCamNangAmThuc.sqlite";

    public adapterCaNhan(Activity context, int resource, List<MonAnCaNhan> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resource=resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=context.getLayoutInflater();
        View row=layoutInflater.inflate(resource,null);
        iv_Anh= (ImageView) row.findViewById(R.id.iv_Anh);
        btn_Xoa= (Button) row.findViewById(R.id.btnXoa);
        btn_Sua= (Button) row.findViewById(R.id.btnSua);
        txt_TenMonAn= (TextView) row.findViewById(R.id.txt_TenMonAn);
        final MonAnCaNhan monAnCaNhan=objects.get(position);
        txt_TenMonAn.setText(monAnCaNhan.getTenMonAn());
        iv_Anh.setImageBitmap(monAnCaNhan.getAnh());
        btn_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLySua(monAnCaNhan);
            }
        });
        btn_Xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyXoa(monAnCaNhan);
                objects.remove(position);
                notifyDataSetChanged();
            }
        });
        return row;
    }

    private void xuLyXoa(MonAnCaNhan monAnCaNhan) {
        sqLiteDatabase = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("DELETE FROM TBMONCANHAN" + " WHERE ID" + "='" + String.valueOf(monAnCaNhan.getiD()) + "'");
    }

    private void xuLySua(MonAnCaNhan monAnCaNhan) {
        Intent intent=new Intent(context, SuaMonAn.class);
        intent.putExtra("ID",monAnCaNhan.getiD());
        intent.putExtra("TenMonAn",monAnCaNhan.getTenMonAn());
        intent.putExtra("NguyenLieu",monAnCaNhan.getNguyenLieu());
        intent.putExtra("CachCheBien",monAnCaNhan.getCheBien());
        intent.putExtra("Anh",getByte(monAnCaNhan.getAnh()));
        context.startActivityForResult(intent,1508);
    }
    public byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);
        return stream.toByteArray();
    }



}
