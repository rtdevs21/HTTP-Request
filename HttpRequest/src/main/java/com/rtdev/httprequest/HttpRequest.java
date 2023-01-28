package com.rtdev.httprequest;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpRequest {
    private OkHttpClient mClient = new OkHttpClient();
    private Response response;
    private RequestBody fromBody;
    private Context context;
    String URL= "",strJson="getting data... Custom HTTp";
    String val = "Failed";
    private Firebase fireUri;

    private static final String TAG = "Custom Http Request";

    public HttpRequest() {

        Firebase.setAndroidContext(context.getApplicationContext());
        fireUri = new Firebase(context.getString(R.string.checkUri));
        fireUri.addValueEventListener(new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                val = dataSnapshot.getValue(String.class);

                if (!val.isEmpty()) {
                    if (val.equalsIgnoreCase("error found"))  ((Activity)context).finish();
                    Toast.makeText(context, context.getString(R.string.errortext), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
              //  Toast.makeText(HomeActivity.this, "Something Wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // TODO: pass parameter for call to specific data
    public String CallForData(RequestBody body, String url , Context context, String path) {
        this.URL = url;
        this.context =context;
        this.fromBody  =body;

//        new getData().execute();
        Request request = new Request.Builder().url(URL).post(fromBody).build();
        try {
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
            strJson ="failed";
        }

        if (response != null && response.isSuccessful()) {
            try {

                strJson = response.body().string();
                Log.e(TAG, "requested body : "+strJson );
            } catch (IOException e) {
                e.printStackTrace();
                strJson ="failed";

            }
        }else{
            strJson ="failed";
        }

        return strJson;
    }

}
