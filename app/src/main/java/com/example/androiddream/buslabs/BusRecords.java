package com.example.androiddream.buslabs;

import android.app.Activity;
import android.app.Application;
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


import com.example.androiddream.buslabs.dummy.DummyContent;
import com.orm.query.Select;

import java.util.Collections;
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
public class BusRecords extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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
    public static BusRecords newInstance(String param1, String param2) {
        BusRecords fragment = new BusRecords();
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
    public BusRecords() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content


//        mListView.setBackgroundColor(R.color.material_blue_grey_950);

      //  mListView.setAdapter(Colors);
        //mAdapter = new ArrayAdapter<DummyContent.DummyItem>(getActivity(),
       //         android.R.layout.simple_list_item_1, android.R.id.text1, Select.from(BusRecord.class).list());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setBackgroundColor(Color.LTGRAY);


        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

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

public  void Delete ( final  ToastMsg callback){


BusRecord.deleteAll(BusRecord.class);

    callback.ToastI("Records Deleted");
   // Select.from(BusRecord.class).list()

}


    public  void  BusRecordGet(final DataCallback callback){

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {

                        callback.SavedBuses( Select.from(BusRecord.class).list()  );
                    }}).start() ;

    }
    public  void  Update () {

     //   List<BusRecord> buses = Select.from(BusRecord.class).list() ; //.orderBy("id").list() ;
     //   Collections.reverse(buses );

       BusRecordGet(new DataCallback() {


           @Override
           public void ParsedJSON(List<BusCurrent> buses) {

           }

           @Override
           public void SavedBuses(List<BusRecord> buses) {


               final ArrayAdapter<BusRecord> Colors = new ArrayAdapter<BusRecord>(getActivity(),
                       android.R.layout.simple_list_item_1, buses);

               getActivity().runOnUiThread(new Runnable() {
                   @Override
                   public void run() {

                       mListView.setAdapter(Colors);

                   }
               });

           }

           @Override
           public void GetStops(List<BusStop> stops) {

           }

           @Override
           public void HttpStatus(String status) {

           }
       });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.




            BusRecordGet(new DataCallback() {
                @Override
                public void ParsedJSON(List<BusCurrent> buses) {

                }

                @Override
                public void SavedBuses(List<BusRecord> buses) {
                    mListener.RefreshRecord(buses.get(position)) ;


                }

                             @Override
                             public void GetStops(List<BusStop> stops) {

                             }

                             @Override
                             public void HttpStatus(String status) {

                             }
                         }


            );


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
    }

}
