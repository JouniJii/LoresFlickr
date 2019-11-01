package com.example.loresflickr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ConnectivityManager cm;
    private Context context;
    private Boolean isNet;
    private final static int BUTTON_ID = 1234;
    private final static int EDITTEXT_ID = 5678;
    public RequestQueue queue = null;
    private Gson gson = new Gson();

    private LoremAdapter loremAdapter;
    private ArrayList<LoremObject> lorems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (context == null) {
            context = getApplicationContext();
        }

        isNet = isInternet(context);
        if(isNet == false) {
            Toast.makeText(context, "No network available.", Toast.LENGTH_SHORT).show();
        }
        buildToolbar();

        loremAdapter = new LoremAdapter(this, R.layout.listview_item, lorems);
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(loremAdapter);
    }

    public void buildToolbar () {
        LinearLayout toolbarLayout = findViewById(R.id.toolbar);

        final EditText editText = new EditText(this);
        editText.setId(EDITTEXT_ID);
        editText.setHint(R.string.editText_hint);
        final LinearLayout.LayoutParams editText_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        editText.setBackgroundColor(Color.LTGRAY);
        toolbarLayout.addView(editText, editText_params);

        Button button = new Button(this);
        button.setId(BUTTON_ID);
        button.setText(R.string.button_text);
        LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, 0.2f);
        toolbarLayout.addView(button, button_params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String keyword = editText.getText().toString();

                doJsonQuery(keyword);
            }
        });

    }

        private void doJsonQuery(String keyword) {
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }
        String url = "https://loremflickr.com/json/320/240/" + keyword.trim();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        LoremObject loremObject = gson.fromJson(response.toString(), LoremObject.class);
                        lorems.add(loremObject);
                        loremAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Error fetching data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    private boolean isInternet(Context context) {

        final Network[] allNetworks;
        cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        allNetworks = cm.getAllNetworks();
        return (allNetworks != null);
    }

}
