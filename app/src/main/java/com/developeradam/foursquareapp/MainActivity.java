package com.developeradam.foursquareapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private TextView jsonTxt;
    private StringBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonTxt = (TextView) findViewById(R.id.textView2);
        String JsonURL = getResources().getString(R.string.jsonURL);
        builder = new StringBuilder();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(JsonURL)
                .build();

        Call call =client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.networkFailed),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String json = response.body().string();
                try {
                    getJsonFromString(json);
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            jsonTxt.setText(builder.toString());
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void getJsonFromString(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        for(int i=0;i<jsonArray.length();i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            DisplayJsonInformation(jsonObject);

        }
    }

    private void DisplayJsonInformation(JSONObject jsonObject) throws JSONException {
        final String name =jsonObject.getString("name");
        final String gender =jsonObject.getString("gender");
        final int age  = jsonObject.getInt("age");
        final String eyeColor  =jsonObject.getString("eyeColor");
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.append("Adı: "+name +
                        "\nCinsiyeti: " + gender +
                        "\nYaşı: " + age +
                        "\nGöz Rengi: " + eyeColor +
                        "\n\n");
            }
        });
    }
}
