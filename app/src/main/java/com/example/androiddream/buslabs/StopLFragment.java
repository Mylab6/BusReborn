package com.example.androiddream.buslabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.androiddream.buslabs.dummy.DummyContent;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class StopLFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public List<BusRecord> BusesReturned ;
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static StopLFragment newInstance(String param1, String param2) {
        StopLFragment fragment = new StopLFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StopLFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content


        mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1);//, DummyContent.ITEMS);



    }
//public  String GpsSize  = "0.2";


    void AddPoint (List<BusRecord> buses, GoogleMap mp){

        mp.clear();

        for( BusRecord bus  : buses ) {

       // mp.set
            mp.addMarker(

                    new MarkerOptions()

                            .position(new LatLng(Double.parseDouble(bus.Lat), Double.parseDouble(bus.Lon)))
                            .title(bus.StopName + ":" + bus.route)

                            .flat(false)
                            .snippet(bus.stopId )


            )
            ;


            mp.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
               //     mListener.RefreshRecord(marker.getSnippet()) ;
                        mListener.MapStopIdLook(marker.getSnippet(), marker.getTitle() ) ;
                    return false;
                }
            });

        }


    }

    public  void GetStops(Double lat, Double lon, String GpsSize, final GoogleMap mp) {

        BasicGet GetViaGps = new BasicGet() ;
        // lat , lon
        GetViaGps.GetStops(String.valueOf(lat), String.valueOf(lon), GpsSize , getActivity().getApplicationContext(), new DataCallback() {
            @Override
            public void ParsedJSON(List<BusCurrent> buses) {



            }

            @Override
            public void SavedBuses(final List<BusRecord> buses) {
                BusesReturned = buses ;

                final ArrayAdapter<BusRecord> Colors = new ArrayAdapter<BusRecord>(getActivity(),
                        android.R.layout.simple_list_item_1, buses);

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                           AddPoint(buses, mp);
                           // mp
                        mListView.setAdapter(Colors);

                    }
                });

            }

            @Override
            public void GetStops(List<BusStop> stops) {

            }

            @Override
            public void HttpStatus(final String status) {


             final   Context context = getActivity();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        CharSequence text = status;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                    }
                });


            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopl, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setBackgroundColor(Color.LTGRAY);
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);


         //   GetStops("0.02");


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public  void  BusRecordGet(final DataCallback callback){

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        callback.SavedBuses( Select.from(BusRecord.class).list()  );
                    }}).start() ;

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.


            mListener.RefreshRecord(BusesReturned.get(position));




            //  mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void RefreshRecord(BusRecord id);
        public  void MapStopIdLook(String id, String title) ;
    }

}
