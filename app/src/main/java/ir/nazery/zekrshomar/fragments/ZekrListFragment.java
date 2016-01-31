package ir.nazery.zekrshomar.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ir.nazery.zekrshomar.R;
import ir.nazery.zekrshomar.adapter.ZekrListAdapter;
import ir.nazery.zekrshomar.database.DataManager;

public class ZekrListFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
//    private OnClickListener itemClickListener;

    public ZekrListFragment() {
    }

    public interface OnClickListener {
        void onItemSelected(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list_zekr, container, false);
        try {
            initView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void initView(View view) throws Exception {
        recyclerView = (RecyclerView) view.findViewById(R.id.zekrList_recyclerView);
        FloatingActionButton addZekr_fab = (FloatingActionButton) view.findViewById(R.id.zekrList_floatButton_addZekr);

        recyclerView.setHasFixedSize(false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        RecyclerView.Adapter adapter = new ZekrListAdapter(new DataManager().getZekrs(getActivity()), this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        addZekr_fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        OnClickListener itemClickListener = (OnClickListener) getActivity();
        if (v.getId() == R.id.zekrList_floatButton_addZekr) {
            /*** FloatActionButton Click ***/
            itemClickListener.onItemSelected(-1);
        } else {
            /*** List Item Click ***/
            try {
                int position = recyclerView.getChildAdapterPosition(v);
                itemClickListener.onItemSelected(position);
            } catch (NullPointerException e) {
                Snackbar.make(getView(), R.string.zekr404, Snackbar.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            updateDisplay();
        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(getView(), R.string.errorInDB, Snackbar.LENGTH_LONG).show();
        }
    }

    private void updateDisplay() throws Exception {
//        recyclerView.notify();
        RecyclerView.Adapter adapter = new ZekrListAdapter(new DataManager().getZekrs(getActivity()), this);
        recyclerView.setAdapter(adapter);
//        recyclerView.getAdapter().notifyDataSetChanged();
    }
}
