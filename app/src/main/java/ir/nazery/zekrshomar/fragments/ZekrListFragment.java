package ir.nazery.zekrshomar.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.ActiveAndroid;

import java.util.Collections;
import java.util.List;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import ir.nazery.zekrshomar.MainActivity;
import ir.nazery.zekrshomar.R;
import ir.nazery.zekrshomar.adapter.RecyclerItemClickListener;
import ir.nazery.zekrshomar.adapter.ZekrListAdapter;
import ir.nazery.zekrshomar.database.DataManager;
import ir.nazery.zekrshomar.model.Zekr;

public class ZekrListFragment extends Fragment implements RecyclerItemClickListener.OnItemClickListener {

    private static final String TAG = "aaaa";
    private TextView emptyList_textView;
    private ZekrListAdapter adapter;
    private DataManager dataManager;
    private List<Zekr> list;
//    private boolean rearranged = false;

    public ZekrListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_zekr, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        Log.d(TAG, "onViewCreated: called");
        try {
            final FragmentActivity context = getActivity();

            FloatingActionButton addZekr_fab = (FloatingActionButton) view.findViewById(R.id.zekrList_floatButton_addZekr);
            addZekr_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity mainActivity = (MainActivity) context;
                    Zekr zekr = new Zekr("", 0, list.size());

                    // here save it. so later just will update
                    zekr.save();
                    mainActivity.changeFragment(CounterFragment.newInstance(zekr));
                }
            });

            emptyList_textView = (TextView) view.findViewById(R.id.zekrList_textView_emptyMessage);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.zekrList_recyclerView);

            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            dataManager = new DataManager();
            list = dataManager.getZekrs();
            adapter = new ZekrListAdapter(list);
            recyclerView.setAdapter(adapter);
            recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, this));

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    int fromPosition = viewHolder.getAdapterPosition();
                    int toPosition = target.getAdapterPosition();
                    Zekr fromZekr = list.get(fromPosition);
                    Zekr toZekr = list.get(toPosition);

                    ActiveAndroid.beginTransaction();
                    fromZekr.position = toPosition;
                    toZekr.position = fromPosition;
                    fromZekr.save();
                    toZekr.save();
                    ActiveAndroid.setTransactionSuccessful();
                    ActiveAndroid.endTransaction();

                    Collections.swap(list, fromPosition, toPosition);
                    adapter.notifyItemMoved(fromPosition, toPosition);
//                    rearranged = true;
                    return true;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                    final int position = viewHolder.getAdapterPosition();
                    final Zekr removedZekr = list.remove(position);
                    removedZekr.delete();
                    adapter.notifyItemRemoved(position);
                    checkListIsEmpty();

                    Snackbar.make(view, String.format("ذکر %s حذف شد", removedZekr.getZekrName()), Snackbar.LENGTH_INDEFINITE)
                            .setAction("بازگرداندن", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    list.add(position, removedZekr);
                                    Zekr zekr = new Zekr(removedZekr);
                                    zekr.save();
                                    adapter.notifyItemInserted(position);
                                    checkListIsEmpty();
                                }
                            }).show();
                }
            });
            itemTouchHelper.attachToRecyclerView(recyclerView);

            checkListIsEmpty();

            int size = list.size();
            if (size == 0) {
                // show float button help
                new MaterialIntroView.Builder(getActivity())
                        .enableDotAnimation(true)
                        .enableIcon(true)
                        .setFocusGravity(FocusGravity.CENTER)
                        .setFocusType(Focus.ALL)
                        .setDelayMillis(700)
                        .enableFadeAnimation(true)
                        .performClick(true)
                        .setInfoText("برای ایجاد ذکر جدید این گزینه را انتخاب کنید")
                        .setTarget(addZekr_fab)
                        .setUsageId("fab") //THIS SHOULD BE UNIQUE ID
                        .show();
            } else if (size == 1){
                // show remove list item help
                new MaterialIntroView.Builder(getActivity())
                        .enableDotAnimation(true)
                        .enableIcon(true)
                        .setFocusGravity(FocusGravity.CENTER)
                        .setFocusType(Focus.MINIMUM)
                        .setDelayMillis(700)
                        .enableFadeAnimation(true)
                        .performClick(false)
                        .setInfoText("جهت حذف ذکر، آن را به سمت چپ یا راست بکشید")
                        .setTarget(recyclerView)
                        .setUsageId("remove_list") //THIS SHOULD BE UNIQUE ID
                        .show();
            } else if (size > 1){
                // show rearrange list item help
                new MaterialIntroView.Builder(getActivity())
                        .enableDotAnimation(true)
                        .enableIcon(true)
                        .setFocusGravity(FocusGravity.CENTER)
                        .setFocusType(Focus.MINIMUM)
                        .setDelayMillis(700)
                        .enableFadeAnimation(true)
                        .performClick(false)
                        .setInfoText("برای جابه جایی ذکرها روی ذکر لمس طولانی کنید و سپس آن را جا به جا کنید")
                        .setTarget(recyclerView)
                        .setUsageId("arrange_list") //THIS SHOULD BE UNIQUE ID
                        .show();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Snackbar.make(view, R.string.errorInDB, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        try {
            MainActivity mainActivity = (MainActivity) getActivity();
            Zekr zekr = list.get(position);
            mainActivity.changeFragment(CounterFragment.newInstance(zekr));
        } catch (NullPointerException e) {
            Snackbar.make(v, R.string.zekr404, Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void checkListIsEmpty() {
        if (emptyList_textView != null) {
            emptyList_textView.setVisibility(list.size() > 0 ? TextView.GONE : TextView.VISIBLE);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        try {
//            dataManager.setZekrs(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
