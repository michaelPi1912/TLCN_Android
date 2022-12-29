package hcmute.edu.vn.tlcn.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.tlcn.Models.Notice;
import hcmute.edu.vn.tlcn.R;
import hcmute.edu.vn.tlcn.my_interface.IClickItemListener;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    //private final List<Notice> mListNotice;
    private Context mContext;
    private Cursor mCursor;
    private IClickItemListener iClickItemListener;


    public NoticeAdapter(Context context,Cursor cursor) {
        /*this.mListNotice = mListNotice;*/
        this.mCursor = cursor;
        this.mContext = context;
    }
    @NonNull
    @Override
    public NoticeAdapter.NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
        return new NoticeAdapter.NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeAdapter.NoticeViewHolder holder, int position) {

        /*final Notice notice = mListNotice.get(position);
        if(notice == null){
            return;
        }
        holder.tvToday.setText(notice.getDay()+" "+notice.getTime());*/
        if(!mCursor.moveToPosition(position)){
            return;
        }
        holder.tvToday.setText(mCursor.getString(3)+" "+ mCursor.getString(4));
    }

    @Override
    public int getItemCount() {
        /*if(mListNotice!=null){
            return mListNotice.size();
        }
        return 0;*/
        if(mCursor !=null){
            return mCursor.getCount();
        }
        return 0;
    }

    public void swapCursor(Cursor newCursor){
        if(mCursor !=null){
            mCursor.close();
        }
        mCursor = newCursor;
        if(newCursor !=null){
            notifyDataSetChanged();
        }else {
            Toast.makeText(mContext, "sai o day", Toast.LENGTH_SHORT).show();
        }
    }

    public static class NoticeViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvToday;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);

            tvToday = itemView.findViewById(R.id.tv_today);
        }
    }
}
