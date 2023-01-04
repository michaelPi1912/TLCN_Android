package hcmute.edu.vn.tlcn.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.tlcn.Models.Book;
import hcmute.edu.vn.tlcn.Models.Notice;
import hcmute.edu.vn.tlcn.R;
import hcmute.edu.vn.tlcn.my_interface.IClickItemBookListener;
import hcmute.edu.vn.tlcn.my_interface.IClickItemListener;

public class NoticeAdapter extends BaseAdapter {

    private Context context;
    private List<Notice> mListNotice;
    private IClickItemBookListener iClickItemBookListener;

    public NoticeAdapter(Context context, List<Notice> mListNotice) {
        this.context = context;
        this.mListNotice = mListNotice;
    }

    @Override
    public int getCount() {
        return mListNotice.size();
    }

    @Override
    public Object getItem(int position) {
        return mListNotice.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_notice, null);
        }
        TextView tvToday = view.findViewById(R.id.tv_today);

        tvToday.setText(mListNotice.get(position).getDay() +" "+ mListNotice.get(position).getTime());
        return view;
    }
}
