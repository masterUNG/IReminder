package com.example.hi.ireminder;


import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class All extends Fragment {
    DBAdapter db;
    RecyclerView recycle;
    MyCustomAdapter adapter;
    ArrayList<Reminder> list;
    public All() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list=new ArrayList<Reminder>();
        db=new DBAdapter(this.getActivity());
        getList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v;
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
        int isEmpty      = app_preferences.getInt("isempty", 0);

        if(isEmpty!=0)
        {
            v= inflater.inflate(R.layout.fragment_all, container, false);


            recycle= (RecyclerView) v.findViewById(R.id.recycleview);
            recycle.setLayoutManager(new LinearLayoutManager(this.getActivity()));

            adapter=new MyCustomAdapter(getActivity(),list);
            recycle.setAdapter(adapter);


        }
        else
            v= inflater.inflate(R.layout.nodata, container, false);

        return v;
    }


    public void getList() {
        db.open();
        Cursor c = db.getAllReminders();
        if(c!=null) {
            if (c.moveToFirst()) {
                do {
                    Reminder ob = new Reminder();
                    ob.setId(c.getInt(0));
                    ob.setName(c.getString(1));
                    ob.setDay(c.getInt(2));
                    ob.setMonth(c.getInt(3));
                    ob.setYear(c.getInt(4));
                    ob.setHour(c.getInt(5));
                    ob.setMinute(c.getInt(6));
                    ob.setDetails(c.getString(7));

                    list.add(ob);

                } while (c.moveToNext());
            }
            db.close();
        }
        if(list.size()==0) {
            SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            SharedPreferences.Editor editor = app_preferences.edit();
            editor.putInt("isempty", 0);
            editor.commit();
        }
    }
}
