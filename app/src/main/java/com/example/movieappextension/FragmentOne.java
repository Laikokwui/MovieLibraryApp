package com.example.movieappextension;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentOne extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private RecyclerView recyclerView;
    private MyAdapter.DbInteractionListener dbInteractionListener;
    private String Param1;

    public FragmentOne() {}

    public static FragmentOne newInstance(String param1) {
        FragmentOne fragmentOne = new FragmentOne();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragmentOne.setArguments(args);
        return fragmentOne;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Param1 = getArguments().getString(ARG_PARAM1);
            if (!Param1.equals("null")) { ReturnMovieList(); }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_one, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration divider = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);

        if (Param1.equals("null")) {
            MyAdapter myAdapter = new MyAdapter(null,dbInteractionListener);
            recyclerView.setAdapter(myAdapter);
        }
        return view;
    }

    public void SetDbInteractionListener(MyAdapter.DbInteractionListener dbInteractionListener){
        this.dbInteractionListener = dbInteractionListener;
    }

    private void ReturnMovieList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Param1, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    MyAdapter myAdapter = new MyAdapter(response.getJSONArray("results"), dbInteractionListener);
                    recyclerView.setAdapter(myAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) { }
        });

        final RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }
}