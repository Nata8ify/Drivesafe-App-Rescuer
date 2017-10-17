package com.sitsenior.g40.weewhorescuer.cores;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.models.extra.Hospital;
import com.sitsenior.g40.weewhorescuer.models.extra.HospitalDistance;

import java.util.List;

/**
 * Created by PNattawut on 17-Oct-17.
 */

public class NearestHospitalAdapter extends ArrayAdapter<HospitalDistance> {

    private int layoutId;
    private List<HospitalDistance> hospitalDistances;
    private Context context;

    public NearestHospitalAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HospitalDistance> hospitalDistances) {
        super(context, resource, hospitalDistances);
        this.layoutId = resource;
        this.hospitalDistances = hospitalDistances;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(context).inflate(R.layout.row_nearest_hospital, parent, false);
        Hospital hospital = hospitalDistances.get(position).getHospital();
        double distance = hospitalDistances.get(position).getDistance();
        TextView txtName = (TextView)row.findViewById(R.id.txt_hospital_name);
        TextView txtDistance = (TextView)row.findViewById(R.id.txt_hospital_distance);
        txtName.setText(hospital.getName());
        txtDistance.setText(String.valueOf(distance).substring(0, String.valueOf(distance).indexOf(".")+3).concat(" ").concat(context.getString(R.string.kilometer)));
        return row;
    }
}
