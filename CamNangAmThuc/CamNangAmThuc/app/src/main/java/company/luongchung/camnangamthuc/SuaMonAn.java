package company.luongchung.camnangamthuc;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class SuaMonAn extends AppCompatActivity {
    private int CODE_CAMERA=1998;
    private int CODE_SELECT_IMAGE=1999;
    SQLiteDatabase sqLiteDatabase=null;
    String DATABASE_NAME="dbCamNangAmThuc.sqlite";
    Button btn_ChonAnh,btn_ChupAnh,btnSuaMonAn;
    int _ID;
    EditText txtTenMonAn,txtNguyenLieu,txtCachCheBien;
    ImageView iv_MonAn;
    Bitmap bm_AnhMonAn;
    byte[] _Anh;
    String _txtTenMonAn,_txtNguyenLieu,_txtCachCheBien;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sua_mon_an);
        addControls();
        addEvents();
    }
    private boolean checknull() {
        if (txtTenMonAn.getText().toString().equals(""))
        {
            Toast.makeText(this, "Bạn chưa nhập tên món ăn.", Toast.LENGTH_LONG).show();
            return true;
        }
        if (txtNguyenLieu.getText().toString().equals(""))
        {
            Toast.makeText(this,"Bạn chưa nhập nguyên liệu.", Toast.LENGTH_LONG).show();
            return true;
        }
        if (txtCachCheBien.getText().toString().equals(""))
        {
            Toast.makeText(this, "Bạn chưa nhập cách chế biến.", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    private void addEvents() {
        btn_ChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenChonAnh = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intenChonAnh, CODE_SELECT_IMAGE);
            }
        });
        btn_ChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CODE_CAMERA);
            }
        });
        btnSuaMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLySuaMon();
            }
        });
    }

    private void xuLySuaMon() {
        if(!checknull())
        {
            sqLiteDatabase=openOrCreateDatabase(DATABASE_NAME,MODE_PRIVATE,null);
            sqLiteDatabase.execSQL("DELETE FROM TBMONCANHAN" + " WHERE ID" + "='" + String.valueOf(_ID) + "'");

            ContentValues row;
            row = new ContentValues();
            row.put("TenMonAn", txtTenMonAn.getText().toString());
            row.put("NguyenLieu", txtNguyenLieu.getText().toString());
            row.put("CheBien", txtCachCheBien.getText().toString());
            row.put("Anh", getByte(bm_AnhMonAn));
            sqLiteDatabase.insert("tbMonCaNhan", null, row);
            Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_CAMERA){
            bm_AnhMonAn = (Bitmap)data.getExtras().get("data");
            iv_MonAn.setImageBitmap(bm_AnhMonAn);
        }
        if (requestCode == CODE_SELECT_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            try {
                bm_AnhMonAn = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
            iv_MonAn.setImageBitmap(bm_AnhMonAn);
            iv_MonAn.buildDrawingCache();
            bm_AnhMonAn = iv_MonAn.getDrawingCache();
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    private void addControls() {

        Intent intent=getIntent();
        _ID=intent.getIntExtra("ID",-1);
        _txtTenMonAn=intent.getStringExtra("TenMonAn");
        _txtNguyenLieu=intent.getStringExtra("NguyenLieu");
        _txtCachCheBien=intent.getStringExtra("CachCheBien");
        _Anh=intent.getByteArrayExtra("Anh");


        btn_ChonAnh= (Button) findViewById(R.id.btn_ChonAnh);
        btn_ChupAnh= (Button) findViewById(R.id.btn_ChupAnh);
        iv_MonAn= (ImageView) findViewById(R.id.iv_MonAN);
        btnSuaMonAn= (Button) findViewById(R.id.btnSuaMonAn);
        txtTenMonAn= (EditText) findViewById(R.id.txtTenMonAn);
        txtCachCheBien= (EditText) findViewById(R.id.txtCachCheBien);
        txtNguyenLieu= (EditText) findViewById(R.id.txtNguyenLieu);

        txtTenMonAn.setText(_txtTenMonAn);
        txtNguyenLieu.setText(_txtNguyenLieu);
        txtCachCheBien.setText(_txtCachCheBien);
        bm_AnhMonAn=getBitMap(_Anh);
        iv_MonAn.setImageBitmap(bm_AnhMonAn);

    }
    public Bitmap getBitMap(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
    public byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);
        return stream.toByteArray();
    }
}
