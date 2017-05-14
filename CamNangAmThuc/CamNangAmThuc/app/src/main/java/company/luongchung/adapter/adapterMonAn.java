package company.luongchung.adapter;

import android.app.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import java.util.List;
import company.luongchung.camnangamthuc.R;
import company.luongchung.camnangamthuc.chitiet;
import company.luongchung.model.MonAn;
public class adapterMonAn extends ArrayAdapter<MonAn> {
    Activity context;
    int resource;
    List<MonAn> objects;
    public adapterMonAn(Activity context, int resource, List<MonAn> objects) {
        super(context, resource, objects);
        this.context=context;
        this.objects=objects;
        this.resource=resource;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = this.context.getLayoutInflater();
        View row = layoutInflater.inflate(this.resource, null);
        TextView txtTenMon = (TextView) row.findViewById(R.id.txtTenMon);
        TextView txtTitle = (TextView) row.findViewById(R.id.txtTitle);
        ImageView ivIcon = (ImageView) row.findViewById(R.id.ivIcon);
        final MonAn monAn = objects.get(position);
        txtTenMon.setText(monAn.getTxtTenMon());
        txtTitle.setText(monAn.getTitle());
        String URL = monAn.getLinkImg();
        Picasso.with(context).load(URL).into(ivIcon);
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyClickItem(monAn);
            }
        });
        return row;
    }
    private void xuLyClickItem(MonAn monAn) {
        Intent intent=new Intent(context,chitiet.class);
        intent.putExtra("TenMonAn",monAn.getTxtTenMon());
        intent.putExtra("Title",monAn.getTitle());
        intent.putExtra("LinkURL",monAn.getLinkURL());
        intent.putExtra("LinkImage",monAn.getLinkImg());
        context.startActivityForResult(intent,13);
    }
}


