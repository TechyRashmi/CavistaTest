package com.testleotech.cavistatest.Activity.activity.allClasses;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.testleotech.cavistatest.Activity.activity.Adapter.ImageListAdaper;
import com.testleotech.cavistatest.Activity.activity.other.CustomLoader;
import com.testleotech.cavistatest.Activity.activity.other.Utils;
import com.testleotech.cavistatest.Activity.activity.model.ImageModel;
import com.testleotech.cavistatest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager mGridLayoutManager;
    ImageListAdaper mAdapter;
    EditText etSearch;
    CustomLoader loader;

    ArrayList<ImageModel> imageArrayList;
    String search_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        mGridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mGridLayoutManager);
        imageArrayList = new ArrayList<>();

        //loader
        loader = new CustomLoader(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);

        etSearch = findViewById(R.id.etSearch);
        etSearch.setLongClickable(false);




        etSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_RIGHT = 2;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (etSearch.getRight() - etSearch.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        search_text = etSearch.getText().toString().trim().toLowerCase();

                        if (etSearch.getText().toString().equalsIgnoreCase("")) {
                            Toast.makeText(MainActivity.this, "search field can't be blank", Toast.LENGTH_LONG).show();
                        } else {
                            if (Utils.isNetworkConnected(MainActivity.this)) {
                                loader.show();
                                requestImageAPI(search_text);
                            } else {
                                Toast.makeText(MainActivity.this, "No Internet connection", Toast.LENGTH_LONG).show();
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });


    }

    public void requestImageAPI(String search_string) {
        RequestQueue queue = Volley.newRequestQueue(this);
        imageArrayList.clear();
        String url = "https://api.imgur.com/3/gallery/search/1/?q=" + search_string;
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        ImageModel model;

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("success").equalsIgnoreCase("true")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    JSONArray imageArray = object.getJSONArray("images");
                                    for (int j = 0; j < imageArray.length(); j++) {
                                        JSONObject imageObject = imageArray.getJSONObject(j);
                                        model = new ImageModel(imageObject.getString("id"), imageObject.getString("link"), object.getString("title"));
                                        imageArrayList.add(model);
                                        mAdapter = new ImageListAdaper(imageArrayList, MainActivity.this);
                                        recyclerView.setAdapter(mAdapter);
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Authentication Issue from server", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        loader.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                        Toast.makeText(MainActivity.this, "Authentication Issue from server", Toast.LENGTH_LONG).show();
                        loader.dismiss();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authentication", "Client-ID 137cda6b5008a7c");
                return params;
            }


        };
        queue.add(getRequest);

    }
}