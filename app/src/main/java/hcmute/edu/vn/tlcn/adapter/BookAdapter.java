package hcmute.edu.vn.tlcn.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.tlcn.Models.Book;
import hcmute.edu.vn.tlcn.Models.Ticket;
import hcmute.edu.vn.tlcn.R;
import hcmute.edu.vn.tlcn.my_interface.IClickItemBookListener;
import hcmute.edu.vn.tlcn.my_interface.IClickItemListener;

public class BookAdapter extends BaseAdapter {

    private Context context;
    private List<Book> mListBook;
    private IClickItemBookListener iClickItemBookListener;

    public BookAdapter(Context context, List<Book> mListBook) {
        this.context = context;
        this.mListBook = mListBook;
    }

    @Override
    public int getCount() {
        return mListBook.size();
    }

    @Override
    public Object getItem(int position) {
        return mListBook.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view ==null){
            view = LayoutInflater.from(context).inflate(R.layout.item_book, null);
        }
        LinearLayout linearLayout = view.findViewById(R.id.layout_item_book);
        TextView tvFrom = view.findViewById(R.id.tv_frombook);
        TextView tvTo = view.findViewById(R.id.tv_tobook);
        TextView tvTime = view.findViewById(R.id.tv_timebook);
        TextView tvType = view.findViewById(R.id.tv_typeboook);

        tvFrom.setText(mListBook.get(position).getStartpoint());
        tvTo.setText(mListBook.get(position).getEndpoint());
        tvTime.setText(mListBook.get(position).getTime());
        tvType.setText(mListBook.get(position).getType());

        return view;
    }
}
