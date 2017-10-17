package com.sitsenior.g40.weewhorescuer.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;
import com.sitsenior.g40.weewhorescuer.CloseIncidentActivity;
import com.sitsenior.g40.weewhorescuer.MainActivity;
import com.sitsenior.g40.weewhorescuer.R;
import com.sitsenior.g40.weewhorescuer.cores.AccidentFactory;
import com.sitsenior.g40.weewhorescuer.cores.AddressFactory;
import com.sitsenior.g40.weewhorescuer.cores.LocationFactory;
import com.sitsenior.g40.weewhorescuer.cores.NearestHospitalAdapter;
import com.sitsenior.g40.weewhorescuer.cores.ViewReportUserInfoAsyncTask;
import com.sitsenior.g40.weewhorescuer.cores.Weeworh;
import com.sitsenior.g40.weewhorescuer.models.Accident;
import com.sitsenior.g40.weewhorescuer.models.Profile;
import com.sitsenior.g40.weewhorescuer.models.extra.HospitalDistance;
import com.sitsenior.g40.weewhorescuer.models.extra.ReporterProfile;
import com.sitsenior.g40.weewhorescuer.services.GoingService;
import com.sitsenior.g40.weewhorescuer.utils.WeeworhRestService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    private Button btnFindNearestHospital;
    private static final String N8IFY_GOOGLE_MAPS_API_KEY = "AIzaSyBz4yyNYqj3KNAl_cn2DpbIEne_45J9KTQ";
    private static final String N8IFY_GOOGLE_MAPS_DIRECTION_KEY = "AIzaSyAUyVikwoN9vvsV8vHvqj98g-Nxq0WtDAg";

    private AlertDialog leftButUnClosedAlertDialog;
    private AlertDialog nearestHospitalAlertDialog;
    private AlertDialog.Builder alreadyTakeCaseAlertDialog;
    private com.sitsenior.g40.weewhorescuer.models.extra.Profile responsibleCaseRescuer;
    Retrofit retrofit;
    WeeworhRestService weeworh;

    private double cuurentLat;
    private double cuurentLng;
    private List<HospitalDistance> hospitalDistances ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        context = getContext();
        retrofit = new Retrofit.Builder()
                .baseUrl(Weeworh.Url.HOST)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setDateFormat("yyyy-MM-dd").create()))
                .build();
        weeworh = retrofit.create(WeeworhRestService.class);
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
        btnFindNearestHospital = (Button) inflateNavigatorView.findViewById(R.id.btn_nearest_hospital);
        navMapView = (MapView) inflateNavigatorView.findViewById(R.id.map_navmap);
        navMapView.onCreate(savedInstanceState);
        navMapView.onResume();

        leftButUnClosedAlertDialog = new AlertDialog.Builder(context)
                .setTitle(getString(R.string.warning))
                .setCancelable(false)
                .setMessage(getString(R.string.warn_in_responsible_found))
                .setPositiveButton(getString(R.string.mainnav_continue_rescue), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //todo
                    }
                })
                .setNegativeButton(getString(R.string.mainnav_continue_to_close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent closeIntent = new Intent(context, CloseIncidentActivity.class);
                        closeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        closeIntent.putExtra(GoingService.RESPONSIBLE_INCIDENT_KEY, AccidentFactory.getResponsibleAccident().getAccidentId());
                        closeIntent.setAction(GoingService.CLOSE_INCIDENT);
                        startActivity(closeIntent);
                    }
                })
                .create();
        alreadyTakeCaseAlertDialog = new AlertDialog.Builder(context)
                .setTitle(getString(R.string.warning))
                .setPositiveButton(context.getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
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
        btnFindNearestHospital.setOnClickListener(this);
        /* On.. stuffs */
        MainActivity.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                        }
                    })
                    .setCancelable(false)
                    .create();

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                /*if (AccidentFactory.getInstance(null).getSelectAccident() == null) {
                    return;
                }
                if (position == OverviewFragment.OVERVIEW_PAGE && AccidentFactory.getInstance(null).getSelectAccident().getAccCode() == Accident.ACC_CODE_G) {
                    if (!onGoingDialog.isShowing()) {
                        onGoingDialog.show();
                    }
                }*/

            }

            @Override
            public void onPageScrollStateChanged(int state) {

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


                if (AccidentFactory.getResponsibleAccident() != null) {
                    MainActivity.mainViewPager.setCurrentItem(2);
                    AccidentFactory.setSelectAccident(AccidentFactory.getResponsibleAccident());
                    viewAccidentDataandLocation(AccidentFactory.getResponsibleAccident());
                } else {
                    weeworh.getInResposibleIncidetByRescuerId(Profile.getInsatance().getUserId()).enqueue(new Callback<Accident>() {
                        @Override
                        public void onResponse(Call<Accident> call, Response<Accident> response) {
                            if (response.body() != null) {
                                AccidentFactory.setResponsibleAccident(response.body()); // used in non-close activity.
                                AccidentFactory.setSelectAccident(AccidentFactory.getResponsibleAccident());
                                ReporterProfile.setInstance(Weeworh.with(context).getReportUserInformation(AccidentFactory.getSelectAccident().getUserId()));
                                getActivity().startService(new Intent(context, GoingService.class));
                                //->>Switch to 3rd page
                                MainActivity.mainViewPager.setCurrentItem(2);
                                viewAccidentDataandLocation(AccidentFactory.getResponsibleAccident());
                                leftButUnClosedAlertDialog.show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Accident> call, Throwable t) {
                            Log.d("$$!F", t.toString() + "");
                        }
                    });
                }

            }
        });
    }

    public void viewAccidentDataandLocationFromNoti(final Accident accident) {
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
        googleMap.addMarker(new MarkerOptions().draggable(false).position(des).title(desBriefAddress));
        cameraPosition = new CameraPosition.Builder().target(des).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
                            if (estimatedDistance >= 100) {
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
                            }
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
        imgAcctype.setImageResource((int) accTypeProperties[1]);
        txtAccidentType.setText((String) accTypeProperties[0]);
        txtDestinationDescription.setText(AddressFactory.getInstance(null).getBriefLocationAddress(des));
        txtNavigatorEstimatedDistance.setText(String.valueOf(estimatedDistance).concat(" ").concat(getString(R.string.kms)).concat(" ").concat(getString(R.string.mainnav_from_curposition)));
        btnImGoing.setVisibility(View.VISIBLE);
        if (accident.getAccCode() != Accident.ACC_CODE_A) {
            btnImGoing.setVisibility(View.GONE);
        }

    }

    /* Listener will be here. */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_going:
                if (AccidentFactory.getResponsibleAccident() != null) {
                    makeToastText(getString(R.string.mainnav_already_got_case));
                    return;
                }

                //Weeworh.with(context).setGoingCode(AccidentFactory.getSelectAccident().getAccidentId());
                weeworh.setGoing(Profile.getInsatance().getUserId(), AccidentFactory.getSelectAccident().getAccidentId()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        Log.d("$$!", response.raw().toString());
                        if (response.body()) {
                            ReporterProfile.setInstance(Weeworh.with(context).getReportUserInformation(AccidentFactory.getSelectAccident().getUserId()));
                            AccidentFactory.setResponsibleAccident(AccidentFactory.getSelectAccident()); // used in non-close activity.
                            btnImGoing.setVisibility(View.GONE);
                            getActivity().startService(new Intent(context, GoingService.class));
                        } else {
                            weeworh.getRescuerProfileByIncidetById(AccidentFactory.getSelectAccident().getAccidentId()).enqueue(new Callback<com.sitsenior.g40.weewhorescuer.models.extra.Profile>() {
                                @Override
                                public void onResponse(Call<com.sitsenior.g40.weewhorescuer.models.extra.Profile> call, Response<com.sitsenior.g40.weewhorescuer.models.extra.Profile> response) {
                                    if (response.body() == null) {
                                        return;
                                    }
                                    responsibleCaseRescuer = response.body();
                                    alreadyTakeCaseAlertDialog.setMessage(getString(R.string.mrservice_already_accepted).concat(" \n".concat(getString(R.string.mrservice_responsbler))).concat(" : ").concat(responsibleCaseRescuer.getFirstName() + " " + responsibleCaseRescuer.getLastName()));
                                    alreadyTakeCaseAlertDialog.setNegativeButton(context.getResources().getString(R.string.call), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                                            callIntent.setData(Uri.parse("tel:".concat(responsibleCaseRescuer.getPhoneNumber())));
                                            startActivity(callIntent);
                                        }
                                    });
                                    alreadyTakeCaseAlertDialog.create().show();
                                }

                                @Override
                                public void onFailure(Call<com.sitsenior.g40.weewhorescuer.models.extra.Profile> call, Throwable t) {

                                }
                            });
                            makeToastText(getString(R.string.mrservice_already_accepted));
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });

                break;
            case R.id.btn_userdetail:
                new ViewReportUserInfoAsyncTask(context).execute(AccidentFactory.getSelectAccident().getUserId());
                break;
            case R.id.btn_nearest_hospital:
                cuurentLat = LocationFactory.getInstance(null).getLatLng().latitude;
                cuurentLng = LocationFactory.getInstance(null).getLatLng().longitude;
                //hospitalDistances = Weeworh.with(context).getNearestHospital(cuurentLat, cuurentLng);
                weeworh.getNearestHospitals(cuurentLat, cuurentLng).enqueue(new Callback<List<HospitalDistance>>() {
                    @Override
                    public void onResponse(Call<List<HospitalDistance>> call, Response<List<HospitalDistance>> response) {
                        hospitalDistances = response.body();
                        if (hospitalDistances == null) {
                            makeToastText(getString(R.string.warn_no_network));
                            return;
                        }
                        if (hospitalDistances.isEmpty()) {
                            makeToastText(getString(R.string.mainnav_no_result_nearest_hospital_tiitle));
                            return;
                        }

                        nearestHospitalAlertDialog = new AlertDialog.Builder(context)
                                .setTitle(getString(R.string.mainnav_result_nearest_hospital_tiitle))
                                .setAdapter(new NearestHospitalAdapter(context, R.layout.row_nearest_hospital, hospitalDistances), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String uri = "http://maps.google.com/maps?saddr=" + cuurentLat + "," + cuurentLng + "&daddr=" + hospitalDistances.get(which).getHospital().getLatitude() + "," + hospitalDistances.get(which).getHospital().getLongitude();
                                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                                        startActivity(intent);
                                    }
                                })
                                .create();
                        nearestHospitalAlertDialog.show();
                    }

                    @Override
                    public void onFailure(Call<List<HospitalDistance>> call, Throwable t) {

                    }
                });

                break;
        }
    }


    /* Useful */
    public Object[] getFullAccidentTypeProperties(byte accType) {
        Object[] accidentProps = new Object[2];
        switch (accType) {
            case Accident.ACC_TYPE_TRAFFIC:
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_traffic);
                accidentProps[1] = getResources().getIdentifier("acctype_crash", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_FIRE:
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_fires);
                accidentProps[1] = getResources().getIdentifier("acctype_fire", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_PATIENT:
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_patient);
                accidentProps[1] = getResources().getIdentifier("acctype_patient", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_ANIMAL:
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_animal);
                accidentProps[1] = getResources().getIdentifier("acctype_animal", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_BRAWL:
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_brawl);
                accidentProps[1] = getResources().getIdentifier("acctype_brawl", "drawable", context.getPackageName());
                break;
            case Accident.ACC_TYPE_OTHER:
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_other);
                accidentProps[1] = getResources().getIdentifier("acctype_other", "drawable", context.getPackageName());
                break;
            default:
                accidentProps[0] = getResources().getString(R.string.mainnav_acctype_unknown);
                accidentProps[1] = getResources().getIdentifier("acctype_other", "drawable", context.getPackageName());
        }
        return accidentProps;
    }

    private void makeToastText(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static final int NAVIGATOR_PAGE = 1;
}
