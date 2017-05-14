package company.luongchung.camnangamthuc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import company.luongchung.adapter.adapter;
import company.luongchung.model.LoaiMonAn;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoaiFragment extends Fragment {
    ListView lvLoaiMonAn;
    adapter adapterLoai;
    ArrayList<LoaiMonAn> listLoaiMonAn;
    public LoaiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loai, container, false);
        lvLoaiMonAn= (ListView) view.findViewById(R.id.lvLoaiMonAn);
        listLoaiMonAn=new ArrayList<>();
        addListMonAn();
        adapterLoai = new adapter(getActivity(), R.layout.item,listLoaiMonAn);
        lvLoaiMonAn.setAdapter(adapterLoai);
        return view;
    }
    private void addListMonAn() {

        listLoaiMonAn.add (new LoaiMonAn("Món Chay","http://www.doivi.net/mon-chay/"));
        listLoaiMonAn.add (new LoaiMonAn("Tráng Miệng - Thức Uống","http://www.doivi.net/trang-mieng-thuc-uong/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Ăn Kèm","http://www.doivi.net/mon-an-kem/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Ăn Hàng Ngày","http://www.doivi.net/mon-an-hang-ngay/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Ăn Cuối Tuần","http://www.doivi.net/mon-an-cuoi-tuan/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Ăn Tốt Cho Sức Khỏe","http://www.doivi.net/mon-an-tot-cho-suc-khoe/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Ăn Nước Ngoài","http://www.doivi.net/mon-an-nuoc-ngoai/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Ăn Vùng Miền","http://www.doivi.net/mon-an-vung-mien/"));
        listLoaiMonAn.add (new LoaiMonAn("Mẹo Nấu Ăn","http://www.doivi.net/meo-nau-an/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Tết","http://www.doivi.net/mon-tet/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Tết Miền Bắc","http://www.doivi.net/mon-tet-mien-bac/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Tết Miền Trung","http://www.doivi.net/mon-tet-mien-trung/"));
        listLoaiMonAn.add (new LoaiMonAn("Món Tết Miền Nam","http://www.doivi.net/mon-tet-mien-nam/"));
        listLoaiMonAn.add (new LoaiMonAn("Ẩm Thực Cho Bà Bầu","http://www.doivi.net/am-thuc-cho-ba-bau/"));
        listLoaiMonAn.add (new LoaiMonAn("Ẩm Thực Người Cao Tuổi","http://www.doivi.net/am-thuc-nguoi-cao-tuoi/"));
        listLoaiMonAn.add (new LoaiMonAn("Ẩm Thực Giảm Cân","http://www.doivi.net/am-thuc-giam-can/"));
        listLoaiMonAn.add (new LoaiMonAn("Ẩm Thực Hè Phố","http://www.doivi.net/am-thuc-he-pho/"));
        listLoaiMonAn.add (new LoaiMonAn("Ẩm Thực Làm Đẹp","http://www.doivi.net/am-thuc-lam-dep/"));
        listLoaiMonAn.add (new LoaiMonAn("Ẩm Thực Cho Doanh Nhân","http://www.doivi.net/am-thuc-cho-doanh-nhan/"));
        listLoaiMonAn.add (new LoaiMonAn("Thực Phẩm Chức Năng","http://www.doivi.net/thuc-pham-chuc-nang/"));
        listLoaiMonAn.add (new LoaiMonAn("Tư Vấn - Kinh Nghiệm","http://www.doivi.net/tu-van-kinh-nghiem/"));
        listLoaiMonAn.add (new LoaiMonAn("Góc Cho Bé","http://www.doivi.net/goc-cho-be/"));
        listLoaiMonAn.add (new LoaiMonAn("Thực Phẩm Dinh Dưỡng","http://www.doivi.net/thuc-pham-dinh-duong/"));

    }

}
