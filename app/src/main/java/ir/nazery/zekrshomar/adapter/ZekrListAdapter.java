package ir.nazery.zekrshomar.adapter;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ir.nazery.zekrshomar.R;
import ir.nazery.zekrshomar.database.Zekr;

/***
 * Created by REZA on 1/25/2016
 ***/
public class ZekrListAdapter extends RecyclerView.Adapter<ZekrListAdapter.ViewHolder> {

    //    private View.OnClickListener clickListener;
    private List<Zekr> list;


    public ZekrListAdapter(List<Zekr> list) {
        this.list = list;
//        this.clickListener = clickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zekr_list_row, parent, false);
//        view.setOnClickListener(clickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Zekr zekr = list.get(position);
        holder.name_textView.setText(zekr.getZekrName());
        holder.family_tv.setText(zekr.getZekrCountAsString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

//    public void setData(List<Zekr> list) {
//        this.list = list;
//        notifyDataSetChanged();
//    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name_textView, family_tv;

        public ViewHolder(View view) {
            super(view);
            name_textView = (TextView) view.findViewById(R.id.list_name_textView);
            family_tv = (TextView) view.findViewById(R.id.list_family_textView);

            Typeface font = Typeface.createFromAsset(view.getContext().getAssets(), "font/b_yekan.ttf");
            name_textView.setTypeface(font);
            family_tv.setTypeface(font);
        }
    }
}
