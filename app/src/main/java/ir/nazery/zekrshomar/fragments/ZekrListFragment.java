package ir.nazery.zekrshomar.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import ir.nazery.zekrshomar.R;
import ir.nazery.zekrshomar.adapter.RecyclerItemClickListener;
import ir.nazery.zekrshomar.adapter.ZekrListAdapter;
import ir.nazery.zekrshomar.database.DataManager;
import ir.nazery.zekrshomar.database.Zekr;

public class ZekrListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    private static final String TAG = "aksjdfoiuqwer";
    private TextView emptyView;
    private RecyclerView recyclerView;
    private OnZekrClickListener itemClickListener;
    private ZekrListAdapter adapter;
    private DataManager dataManager;
    private List<Zekr> list = new LinkedList<>();

    public ZekrListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_zekr, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated: called");
        try {
            FloatingActionButton addZekr_fab = (FloatingActionButton) view.findViewById(R.id.zekrList_floatButton_addZekr);
            addZekr_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemClickListener.onZekrSelected(-1);
                }
            });

            emptyView = (TextView) view.findViewById(R.id.zekrList_textView_emptyMessage);
            recyclerView = (RecyclerView) view.findViewById(R.id.zekrList_recyclerView);

            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
            dataManager = new DataManager();
            adapter = new ZekrListAdapter(list);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(), this));

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    Collections.swap(list, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    final Zekr removedZekr = list.remove(position);
                    adapter.notifyItemRemoved(position);
                    checkListIsEmpty();

                    Snackbar.make(view, MessageFormat.format("ذکر '{0}' حذف شد", removedZekr.getZekrName()), Snackbar.LENGTH_LONG)
                            .setAction("بازگرداندن", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    list.add(position, removedZekr);
                                    adapter.notifyItemInserted(position);
                                    checkListIsEmpty();
                                }
                            }).show();
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);

            updateDisplay();

        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(view, R.string.errorInDB, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        try {
            itemClickListener.onZekrSelected(position);
        } catch (NullPointerException e) {
            Snackbar.make(v, R.string.zekr404, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void checkListIsEmpty() {
        if (emptyView != null) {
            emptyView.setVisibility(list.size() > 0 ? TextView.GONE : TextView.VISIBLE);
        }
    }

    private void updateDisplay() throws Exception {
        list.clear();
        list.addAll(dataManager.getZekrs());
        adapter.notifyDataSetChanged();

        checkListIsEmpty();
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            dataManager.setZekrs(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnZekrClickListener) {
            itemClickListener = (OnZekrClickListener) getActivity();
        }
    }

    public interface OnZekrClickListener {
        void onZekrSelected(int position);
    }
}
