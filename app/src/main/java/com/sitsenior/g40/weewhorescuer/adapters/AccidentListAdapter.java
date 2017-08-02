package com.sitsenior.g40.weewhorescuer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.models.Accident;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class AccidentListAdapter extends ArrayAdapter<Accident>{

    private Context context;
    private int resource;
    private List<Accident> accidentList;

    public AccidentListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Accident> accidentList) {
        super(context, resource, accidentList);
        this.context = context;
        this.resource = resource;
        this.accidentList = accidentList;
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
        txtBriefAddress.setText(AddressFactory.getInstance(context).getBriefLocationAddress(accident.getLatitude(), accident.getLongitude()));
        txtEstimateDistance.setText("99 Kms From Now");
        accidentTypeImage.setImageResource(getAccidentTypeImage(accident.getAccType()));
        accCodeBarStatus.setBackgroundColor(Color.parseColor(getColorByAccidentCode(accident.getAccCode())));
        return row;
    }

    public int getAccidentTypeImage(byte accType){
        String imgName = null;
        Log.d("accType", accType+" ");
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
            default : //TODO
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

}
