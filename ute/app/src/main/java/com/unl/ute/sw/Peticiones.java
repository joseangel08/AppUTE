package com.unl.ute.sw;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.unl.ute.sw.modelos.ListPersonaJS;

public class Peticiones {

    public static String API_URL = "http://192.168.1.48:3500/api/v1/";

    public static VolleyRequest<ListPersonaJS> getListaPersonas(
            @NonNull final Context context,
            @NonNull Response.Listener<ListPersonaJS> response,
            @NonNull Response.ErrorListener errorListener
    ){
        String url = API_URL + "persona";
        VolleyRequest request = new VolleyRequest(
                context,
                Request.Method.GET,
                url,
                response, errorListener
        );
        request.setResponseClass(ListPersonaJS.class);
        return request;
    }
}
