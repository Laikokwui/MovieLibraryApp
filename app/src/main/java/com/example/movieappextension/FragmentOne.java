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
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
    
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(divider);
    
        if (Param1.equals("null")) {
            MyAdapter myAdapter = new MyAdapter(null, dbInteractionListener);
            recyclerView.setAdapter(myAdapter);
        }
        return view;
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null); // Clear the adapter to avoid leaks
        dbInteractionListener = null;  // Clear listener reference
    }

    public void SetDbInteractionListener(MyAdapter.DbInteractionListener dbInteractionListener){
        this.dbInteractionListener = dbInteractionListener;
    }

    private void ReturnMovieList() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, Param1, null,
            response -> {
                try {
                    MyAdapter myAdapter = new MyAdapter(response.getJSONArray("results"), dbInteractionListener);
                    recyclerView.setAdapter(myAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, 
            error -> {
                // Handle error
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(), "Network Timeout. Please check your connection.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof AuthFailureError) {
                    Toast.makeText(getContext(), "Authentication error.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(getContext(), "Server error. Please try again later.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(getContext(), "Network error. Please check your internet connection.", Toast.LENGTH_SHORT).show();
                } else if (error instanceof ParseError) {
                    Toast.makeText(getContext(), "Error parsing server response.", Toast.LENGTH_SHORT).show();
                } else {
                    // General error
                    Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
                }
                // Optionally, log the error details for debugging
                Log.e("VolleyError", error.toString());
            });
    
        Singleton.getInstance(getContext()).getRequestQueue().add(jsonObjectRequest);
    }
}
