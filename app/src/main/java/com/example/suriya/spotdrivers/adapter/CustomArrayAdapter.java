package com.example.suriya.spotdrivers.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.support.SupportConstant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Suriya on 19-07-2017.
 */

public class CustomArrayAdapter extends ArrayAdapter<String> {
    public List<String> driverDrivingKnownList  = new ArrayList<>();
    protected Context context;
    public static int checkAccumulator;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    private ViewHolder holder = new ViewHolder();
    public CustomArrayAdapter( Context context,String[] list) {
        super(context, R.layout.driver_driving_known_list, list);
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SupportConstant.PROFILE_SHARING, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        checkAccumulator = 0;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.driver_driving_known_list, parent, false);
        String stringelement = getItem(position);

        holder.checkbox = (CheckBox)customView.findViewById(R.id.driver_check_box);
        CompoundButton.OnCheckedChangeListener  checkedChangeListener = new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                countCheck(isChecked, position);
                Log.i("MAIN", checkAccumulator + "");
                Log.i("List", String.valueOf(driverDrivingKnownList.size()));
                Log.i("List", String.valueOf(driverDrivingKnownList));
                String driverKnowns = new Gson().toJson(driverDrivingKnownList);
                Log.i("json", driverKnowns);
                //editor.putString(SupportConstant.DRIVER_KNOWN, driverKnowns);
                editor.commit();

            }
        };

        holder.checkbox.setText(stringelement);
        holder.checkbox.setOnCheckedChangeListener(checkedChangeListener);
        return customView;
    }

    private void countCheck(boolean isChecked, int position) {
       // checkAccumulator += isChecked ? 1 : -1 ;
       // String g = getItem(position).toString();
        if (isChecked)
        {
            driverDrivingKnownList.add(getItem(position).toString());
            checkAccumulator++;
        }else {
            //Toast.makeText(context.getApplicationContext(), g, Toast.LENGTH_SHORT).show();
            driverDrivingKnownList.remove(getItem(position).toString());
            checkAccumulator--;
        }
    }
    static class ViewHolder {
        protected CheckBox checkbox;
    }
}
