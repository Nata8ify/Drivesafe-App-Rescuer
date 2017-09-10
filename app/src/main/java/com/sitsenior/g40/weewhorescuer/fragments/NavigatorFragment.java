package com.sitsenior.g40.weewhorescuer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.ViewReportUserInfoAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.models.extra.AccidentBrief;

import io.realm.Realm;

/**
 * Created by PNattawut on 01-Aug-17.
 */

public class NavigatorFragment extends Fragment implements View.OnClickListener {

    private Context context;

    private MapView navMapView;
    private GoogleMap googleMap;
    private CameraPosition cameraPosition;

    private TextView txtNavigatorTitle;
    private TextView txtNavigatorDescription;

    private RelativeLayout currentPositionDetailRelativeLayout;
    private RelativeLayout destinationDetailRelativeLayout;
    private RelativeLayout actionDetailRelativeLayout;
    private ImageView imgAcctype;
    private TextView txtDestinationDescription;
    private TextView txtAccidentType;
    private TextView txtNavigatorEstimatedDistance;
    private Button btnImGoing;
    private Button btnReportUserInfo;
    private static final String N8IFY_GOOGLE_MAPS_API_KEY = "AIzaSyBz4yyNYqj3KNAl_cn2DpbIEne_45J9KTQ";
    private static final String N8IFY_GOOGLE_MAPS_DIRECTION_KEY = "AIzaSyAUyVikwoN9vvsV8vHvqj98g-Nxq0WtDAg";

    private Handler navigationHandler;
    private Runnable onGoingRunnable;
    public static boolean isOnGoing;

    private Realm realm;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        navigationHandler = new Handler();
        onGoingRunnable = new Runnable() {
            private final double CLOSEST_DISTANCE = 0.01; // 10 Meters
            @Override
            public void run() {
                if(AddressFactory.getInstance(null).getEstimateDistanceFromCurrentPoint(LocationFactory.getInstance(null).getLatLng(), new LatLng(AccidentFactory.getInstance(null).getSelectAccident().getLatitude(), AccidentFactory.getInstance(null).getSelectAccident().getLongitude())) <= CLOSEST_DISTANCE){
                    navigationHandler.removeCallbacks(this);
                    if(Weeworh.with(context).setRescuingCode(AccidentFactory.getSelectAccident().getAccidentId())){
                        Toast.makeText(context, getString(R.string.mainnav_incident_is_near), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("NOT CLOESTE","NOT");
                    navigationHandler.postDelayed(this, 10000L);
                }
            }
        };
        context = getContext();
        Realm.init(context);
        realm = Realm.getDefaultInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflateNavigatorView = inflater.inflate(R.layout.fragment_navigator, container, false);
        currentPositionDetailRelativeLayout = (RelativeLayout) inflateNavigatorView.findViewById(R.id.reltvlout_curposition_details);
        destinationDetailRelativeLayout = (RelativeLayout) inflateNavigatorView.findViewById(R.id.reltvlout_desposition_details);
        actionDetailRelativeLayout = (RelativeLayout) inflateNavigatorView.findViewById(R.id.reltvlout_action);
        imgAcctype = (ImageView) inflateNavigatorView.findViewById(R.id.img_acctype);
        txtDestinationDescription = (TextView) inflateNavigatorView.findViewById(R.id.txt_desnavdesc);
        txtAccidentType = (TextView) inflateNavigatorView.findViewById(R.id.txt_acctype);
        txtNavigatorTitle = (TextView) inflateNavigatorView.findViewById(R.id.txt_curnavtitle);
        txtNavigatorDescription = (TextView) inflateNavigatorView.findViewById(R.id.txt_curnavdesc);
        txtNavigatorEstimatedDistance = (TextView) inflateNavigatorView.findViewById(R.id.txt_estdistance);
        btnImGoing = (Button) inflateNavigatorView.findViewById(R.id.btn_going);
        btnReportUserInfo = (Button) inflateNavigatorView.findViewById(R.id.btn_userdetail);
        navMapView = (MapView) inflateNavigatorView.findViewById(R.id.map_navmap);
        navMapView.onCreate(savedInstanceState);
        navMapView.onResume();
        return inflateNavigatorView;
    }

    @Override
    public void onStart() {
        LatLng current = LocationFactory.getInstance(null).getLatLng();
        txtNavigatorDescription.setText(AddressFactory.getInstance(null).getBriefLocationAddress(current));
        /* Google Map and Map View Setting */
        MapsInitializer.initialize(this.getActivity());
        initialMap(navMapView, googleMap);

        /* Onlick Overrid Stuffs */
        btnImGoing.setOnClickListener(this);
        btnReportUserInfo.setOnClickListener(this);

        /* On.. stuffs */
        MainActivity.mainViewPager.setOnTouchListener(new View.OnTouchListener() {
            private final AlertDialog onGoingDialog = new AlertDialog.Builder(context)
                    .setMessage(getString(R.string.mainnav_leave_nav))

                    .setPositiveButton(getString(R.string.mainnav_stay_here), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.mainViewPager.setCurrentItem(NAVIGATOR_PAGE, true);
                        }
                    })
                    .setNegativeButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.mainViewPager.setCurrentItem(OverviewFragment.OVERVIEW_PAGE);
                        }
                    })
                    .setCancelable(false)
                    .create();
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(AccidentFactory.getInstance(null).getSelectAccident() == null){ return false;}
                if(MainActivity.mainViewPager.getCurrentItem() == NAVIGATOR_PAGE && AccidentFactory.getInstance(null).getSelectAccident().getAccCode() == Accident.ACC_CODE_G){
                    if(!onGoingDialog.isShowing()){
                        onGoingDialog.show();}
                    return false;
                } else {
                    return false;
                }

            }
        });
        Log.d("nav onStart", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Log.d("nav onResume", "onResume");
        super.onResume();
        navMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("nav on pause", "D");
        navMapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navMapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        navMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navMapView.onLowMemory();
    }

    /**
     * Initialize Google Map
     */
    public void initialMap(MapView mapView, GoogleMap googleMap) {
        navMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                NavigatorFragment.this.googleMap = googleMap;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                NavigatorFragment.this.googleMap.setMyLocationEnabled(true);
                NavigatorFragment.this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        txtNavigatorDescription.setText(AddressFactory.getInstance(null).getBriefLocationAddress(LocationFactory.getInstance(null).getLatLng()));
                        currentPositionDetailRelativeLayout.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
                // For dropping a marker at a point on the Map
                LatLng current = new LatLng(LocationFactory.getInstance(null).getLatLng().latitude, LocationFactory.getInstance(null).getLatLng().longitude);
                googleMap.addMarker(new MarkerOptions().draggable(false).position(current).title("Current Place").snippet("Your Current Place"));

                // For zooming automatically to the location of the marker
                cameraPosition = new CameraPosition.Builder().target(current).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
        });
    }

    public void viewAccidentDataandLocationFromNoti(final Accident accident){
        navMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                NavigatorFragment.this.googleMap = googleMap;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                NavigatorFragment.this.googleMap.setMyLocationEnabled(true);
                NavigatorFragment.this.googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        txtNavigatorDescription.setText(AddressFactory.getInstance(null).getBriefLocationAddress(LocationFactory.getInstance(null).getLatLng()));
                        currentPositionDetailRelativeLayout.setVisibility(View.VISIBLE);
                        return false;
                    }
                });
                // For dropping a marker at a point on the Map
                LatLng current = new LatLng(LocationFactory.getInstance(null).getLatLng().latitude, LocationFactory.getInstance(null).getLatLng().longitude);
                googleMap.addMarker(new MarkerOptions().draggable(false).position(current).title("Current Place").snippet("Your Current Place"));

                // For zooming automatically to the location of the marker
                cameraPosition = new CameraPosition.Builder().target(current).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                viewAccidentDataandLocation(accident);
            }
        });
    }

    public void viewAccidentDataandLocation(Accident accident) {
        /* Map and Location */
        googleMap.clear();
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        final LatLng current = new LatLng(LocationFactory.getInstance(null).getLatLng().latitude, LocationFactory.getInstance(null).getLatLng().longitude);
        final LatLng des = new LatLng(accident.getLatitude(), accident.getLongitude());
        final double estimatedDistance = AddressFactory.getInstance(null).getEstimateDistanceFromCurrentPoint(current, des);
        //googleMap.addMarker(new MarkerOptions().draggable(false).position(current).title(getString(R.string.mainnav_marker_curposition_title)));
        String desBriefAddress = AddressFactory.getInstance(null).getBriefLocationAddress(des);
        googleMap.addMarker(new MarkerOptions().draggable(false).position(des).title(desBriefAddress.substring(0, desBriefAddress.indexOf(","))).snippet(desBriefAddress.substring(desBriefAddress.indexOf(",")+1)));
        /*GoogleDirection.withServerKey(N8IFY_GOOGLE_MAPS_DIRECTION_KEY)
                .from(LocationFactory.getInstance(null).getLatLng())
                .to(new LatLng(accident.getLatitude(), accident.getLongitude()))
                .transportMode(TransportMode.TRANSIT)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if (direction.isOK()) {
                            //googleMap.addPolyline(DirectionConverter.createPolyline(context, direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint(), 8, Color.RED));
                            //double midLat = ((current.latitude + des.latitude) / 2d);
                            //double midLng = ((current.longitude + des.longitude) / 2d);
                            float zoom = 17;
                            *//*if (estimatedDistance >= 100) {
                                zoom = 5;
                            } else if (estimatedDistance >= 42) {
                                zoom = 7;
                            } else if (estimatedDistance >= 27) {
                                zoom = 9;
                            } else if (estimatedDistance >= 9) {
                                zoom = 11;
                            } else if (estimatedDistance >= 3) {
                                zoom = 13;
                            } else {
                                zoom = 15;
                            }*//*
                            cameraPosition = new CameraPosition.Builder().target(des).zoom(zoom).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        //Location might be not existed.
                    }
                });*/

        currentPositionDetailRelativeLayout.setVisibility(View.GONE);
        destinationDetailRelativeLayout.setVisibility(View.VISIBLE);
        actionDetailRelativeLayout.setVisibility(View.VISIBLE);
        Object[] accTypeProperties = getFullAccidentTypeProperties(accident.getAccType());
        imgAcctype.setImageResource((int)accTypeProperties[1]);
        txtAccidentType.setText((String)accTypeProperties[0]);
        txtDestinationDescription.setText(AddressFactory.getInstance(null).getBriefLocationAddress(des));
        txtNavigatorEstimatedDistance.setText(String.valueOf(estimatedDistance).concat(" ").concat(getString(R.string.kms)).concat(" ").concat(getString(R.string.mainnav_from_curposition)));
        if(Profile.getInsatance().getUserId() != accident.getResponsibleRescr() && accident.getResponsibleRescr() != 0){
            btnImGoing.setVisibility(View.GONE);
        } else {
            btnImGoing.setText(getString(R.string.mainnav_btn_going));
        }
        if(Profile.getInsatance().getUserId() == accident.getResponsibleRescr() && accident.getAccCode() != Accident.ACC_CODE_A){
            btnImGoing.setText(getString(R.string.mainnav_btn_close));
            isOnGoing=true;
        }
    }

    /* Listener will be here. */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_going:
                if(isOnGoing){
                    if(Weeworh.with(context).setRescuedCode(AccidentFactory.getInstance(null).getSelectAccident().getAccidentId())) {
                        navigationHandler.removeCallbacks(onGoingRunnable);
                        btnImGoing.setVisibility(View.GONE);
                        isOnGoing = false;
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.delete(AccidentBrief.class);
                            }
                        });
                    }
                    return;
                }
                Weeworh.with(context).setGoingCode(AccidentFactory.getInstance(null).getSelectAccident().getAccidentId());
                isOnGoing = true;
                navigationHandler.post(onGoingRunnable);
                ((TextView) view).setText(getString(R.string.mainnav_btn_close));
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.insert(new AccidentBrief(AccidentFactory.getSelectAccident()));
                    }
                });
                break;
            case R.id.btn_userdetail:
                new ViewReportUserInfoAsyncTask(context).execute(AccidentFactory.getInstance(null).getSelectAccident().getUserId());
                break;
        }
    }



    /* Useful */
    public Object[] getFullAccidentTypeProperties(byte accType){
        Object[] accidentProps = new Object[2];
        switch (accType){
            case Accident.ACC_TYPE_TRAFFIC :
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_traffic);
                accidentProps[1] = getResources().getIdentifier("acctype_crash", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_FIRE :
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_fires);
                accidentProps[1] = getResources().getIdentifier("acctype_fire", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_PATIENT :
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_patient);
                accidentProps[1] = getResources().getIdentifier("acctype_patient", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_ANIMAL :
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_animal);
                accidentProps[1] = getResources().getIdentifier("acctype_animal", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_BRAWL :
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_brawl);
                accidentProps[1] = getResources().getIdentifier("acctype_brawl", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_OTHER :
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_other);
                accidentProps[1] = getResources().getIdentifier("acctype_other", "drawable", context.getPackageName());
                break;
            default:
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_unknown);
                accidentProps[1] = getResources().getIdentifier("acctype_other", "drawable", context.getPackageName());
        }
        return accidentProps;
    }

    public static final int NAVIGATOR_PAGE = 2;
}
