package com.sitsenior.g40.weewhorescuer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.models.Accident;

import java.util.List;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class AccidentListAdapter extends ArrayAdapter<Accident>{

    private Context context;
    private int resource;
    private List<Accident> accidentList;
    private static ArrayAdapter<Accident> accidentListAdapter;

    public static ArrayAdapter<Accident> getInstance(ArrayAdapter<Accident> accidentListAdapter){
        if(AccidentListAdapter.accidentListAdapter == null)
            AccidentListAdapter.accidentListAdapter = accidentListAdapter;
        if(accidentListAdapter != null)
            AccidentListAdapter.accidentListAdapter = accidentListAdapter;

        return accidentListAdapter;
    }

    public AccidentListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Accident> accidentList) {
        super(context, resource, accidentList);
        this.context = context;
        this.resource = resource;
        this.accidentList = accidentList;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = LayoutInflater.from(this.context).inflate(this.resource, parent, false);
        Accident accident = accidentList.get(position);
        LinearLayout accCodeBarStatus = (LinearLayout) row.findViewById(R.id.linrlout_acccodecolor);
        ImageView accidentTypeImage = (ImageView) row.findViewById(R.id.img_acctype);
        TextView txtReportTime = (TextView)row.findViewById(R.id.txt_reporttime);
        TextView txtBriefAddress = (TextView)row.findViewById(R.id.txt_briefaddr);
        TextView txtEstimateDistance = (TextView)row.findViewById(R.id.txt_distanceest);
        txtReportTime.setText(accident.getTime());
        txtBriefAddress.setText(AddressFactory.getInstance(null).getBriefLocationAddress(new LatLng(accident.getLatitude(), accident.getLongitude())));
        if(LocationFactory.getInstance(null).isLocationActivated())
            txtEstimateDistance.setText(getEstimatedDistanceMessage(LocationFactory.getInstance(null).getLatLng(), new LatLng(accident.getLatitude(), accident.getLongitude())));
        accidentTypeImage.setImageResource(getAccidentTypeImage(accident.getAccType()));
        accCodeBarStatus.setBackgroundColor(Color.parseColor(getColorByAccidentCode(accident.getAccCode())));
        return row;
    }

    public int getAccidentTypeImage(byte accType){
        String imgName = null;
        switch (accType){
            case Accident.ACC_TYPE_TRAFFIC :
                imgName = "acctype_crash"; break;
            case Accident.ACC_TYPE_FIRE :
                imgName = "acctype_fire"; break;
            case Accident.ACC_TYPE_ANIMAL :
                imgName = "acctype_animal"; break;
            case Accident.ACC_TYPE_PATIENT :
                imgName = "acctype_patient"; break;
            case Accident.ACC_TYPE_BRAWL :
                imgName = "acctype_brawl"; break;
            case Accident.ACC_TYPE_OTHER :
                imgName = "acctype_other"; break;
            default : imgName = "acctype_other"; //TODO
        }
        return context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
    }

    public String getColorByAccidentCode(char accCode){
        switch (accCode){
            case Accident.ACC_CODE_A : return "#bc0029";
            case Accident.ACC_CODE_G : return "#ffb912";
            case Accident.ACC_CODE_R : return "#ffed80";
            case Accident.ACC_CODE_C : return "#8cff8c";
            case Accident.ACC_CODE_ERRU : return "#ddd";
            case Accident.ACC_CODE_ERRS : return "#ddd";
        }
        return "#fff";
    }

    public String getEstimatedDistanceMessage(LatLng current, LatLng des){
        if(current != null){
            return AddressFactory.getInstance(context).getEstimateDistanceFromCurrentPoint(current, des) + " Km(s)";
        } else {

        }

        return "unavaialbe";
    }

}
