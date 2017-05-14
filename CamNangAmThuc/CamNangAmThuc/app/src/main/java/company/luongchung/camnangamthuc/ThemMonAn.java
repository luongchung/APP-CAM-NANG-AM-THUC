package company.luongchung.camnangamthuc;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThemMonAn extends AppCompatActivity {
    private int CODE_CAMERA=1996;
    private int CODE_SELECT_IMAGE=1997;
    Button btn_ChonAnh,btn_ChupAnh,btnThemMonAn;
    EditText txtTenMonAn,txtNguyenLieu,txtCachCheBien;
    ImageView iv_MonAn;
    Bitmap bm_AnhMonAn=null;
    SQLiteDatabase sqLiteDatabase=null;
    String DATABASE_NAME="dbCamNangAmThuc.sqlite";
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_mon_an);
        addControls();
        addEvent();
    }

    private void addEvent() {
        btn_ChupAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 xuLyChupAnh();
            }
        });
        btn_ChonAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyChonAnh();
            }
        });
        btnThemMonAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xuLyThemMon();
            }
        });

    }

    private void xuLyThemMon() {
        if(!checknull())
        {
            sqLiteDatabase = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
            ContentValues row;
            row = new ContentValues();
            row.put("TenMonAn", txtTenMonAn.getText().toString());
            row.put("NguyenLieu", txtNguyenLieu.getText().toString());
            row.put("CheBien", txtCachCheBien.getText().toString());
            row.put("Anh", getByte(bm_AnhMonAn));
            sqLiteDatabase.insert("tbMonCaNhan", null, row);
            Toast.makeText(this, "Thêm thành công!", Toast.LENGTH_LONG).show();
            finish();
        }
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

    private void xuLyChonAnh() {
        Intent intenChonAnh = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intenChonAnh, CODE_SELECT_IMAGE);
    }

    private void xuLyChupAnh() {
        if(checkAndRequestPermissions()) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CODE_CAMERA);
        }
    }

    private void addControls() {
        Drawable drawable = getResources().getDrawable(R.drawable.image_null);
        bm_AnhMonAn = ((BitmapDrawable) drawable).getBitmap();
        btn_ChonAnh= (Button) findViewById(R.id.btn_ChonAnh);
        btn_ChupAnh= (Button) findViewById(R.id.btn_ChupAnh);
        iv_MonAn= (ImageView) findViewById(R.id.iv_MonAN);
        btnThemMonAn= (Button) findViewById(R.id.btnThemMonAn);
        txtTenMonAn= (EditText) findViewById(R.id.txtTenMonAn);
        txtCachCheBien= (EditText) findViewById(R.id.txtCachCheBien);
        txtNguyenLieu= (EditText) findViewById(R.id.txtNguyenLieu);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CODE_CAMERA && data!=null){
            bm_AnhMonAn = (Bitmap)data.getExtras().get("data");
            iv_MonAn.setImageBitmap(bm_AnhMonAn);
        }
        if (requestCode == CODE_SELECT_IMAGE && null != data) {
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
    public byte[] getByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, stream);
        return stream.toByteArray();
    }
    private  boolean checkAndRequestPermissions() {
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
}
