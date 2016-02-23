package com.blogspot.e_kanivets.moneytracker.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.blogspot.e_kanivets.moneytracker.DbHelper;
import com.blogspot.e_kanivets.moneytracker.R;
import com.blogspot.e_kanivets.moneytracker.activity.AddAccountActivity;
import com.blogspot.e_kanivets.moneytracker.activity.NavDrawerActivity;
import com.blogspot.e_kanivets.moneytracker.adapter.ExchangeRateAdapter;
import com.blogspot.e_kanivets.moneytracker.controller.ExchangeRateController;
import com.blogspot.e_kanivets.moneytracker.repo.ExchangeRateRepo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeRatesFragment extends Fragment {
    public static final String TAG = "ExchangeRatesFragment";

    private static final int REQUEST_ADD_EXCHANGE_RATE = 1;

    @Bind(R.id.list_view)
    ListView listView;

    private ExchangeRateController rateController;

    public static ExchangeRatesFragment newInstance() {
        ExchangeRatesFragment fragment = new ExchangeRatesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ExchangeRatesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rateController = new ExchangeRateController(new ExchangeRateRepo(new DbHelper(getActivity())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_exchange_rates, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        initActionBar();

        ((NavDrawerActivity) activity).onSectionAttached(TAG);
    }

    @OnClick(R.id.btn_add_exchange_rate)
    public void addAccount() {
        Intent intent = new Intent(getActivity(), AddAccountActivity.class);
        startActivityForResult(intent, REQUEST_ADD_EXCHANGE_RATE);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_exchange_rate, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.delete:
                rateController.delete(rateController.readAll().get(info.position));
                update();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void update() {
        listView.setAdapter(new ExchangeRateAdapter(getActivity(), rateController.readAll()));
        ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
    }

    private void initViews(View rootView) {
        if (rootView != null) {
            ButterKnife.bind(this, rootView);

            listView.setAdapter(new ExchangeRateAdapter(getActivity(), rateController.readAll()));
            ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
            registerForContextMenu(listView);

            ((NavDrawerActivity) getActivity()).onSectionAttached(TAG);
        }
    }

    private void initActionBar() {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) actionBar.setCustomView(null);
    }
}