package com.unl.ute.sw;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

//import com.arte.caminart.controlador.utilidades.Utilidades;
import com.google.gson.Gson;



import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Permite hacer una peticion a un servicio web
 */

public class VolleyRequest<T> extends Request<T> {
    private static final String PROTOCOL_CHARSET = "utf-8";
    //private static final String CONTENT_TYPE = "application/vnd.mido.api-v1+json";
    private static final String CONTENT_TYPE = "application/json";
    private String postData = null;
    private byte[] rawData = null;
    private String typeRawData = "";
    private Map<String, String> headers = new HashMap<>();
    private final Gson gson = new Gson();//GsonFactory.getGson();
    private Context context;
    private Listener<T> listener;
    private Class<T> clazz;
    private int mHttpCode = Integer.MIN_VALUE;

    public VolleyRequest(Context context, int method, String url, Listener<T> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        this.context = context;
        initialHeader("");
        this.listener = listener;
    }

    public VolleyRequest(Context context, int method, String url, JSONObject param, Class<T> clazz, Listener<T> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        this.context = context;
        this.listener = listener;
        this.clazz = clazz;
        initialHeader("");
        setPostData(param);
    }

    public VolleyRequest(Context context, int method, String url, String param, Class<T> clazz, Listener<T> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.context = context;
        this.clazz = clazz;
        initialHeader("");
        setPostData(param);
    }

    public VolleyRequest(Context context, int method, String url, Object postObject, Class objectType, Class<T> clazz, Listener<T> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        this.context = context;
        this.listener = listener;
        this.clazz = clazz;
        initialHeader("");
        setPostData(postObject, objectType);
        Log.e("setPostData", headers.toString() + "  "+postData);
    }

    public VolleyRequest(Context context, int method, String url, Object postObject, Class objectType, Class<T> clazz, String authorization, Listener<T> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        this.context = context;
        this.listener = listener;
        this.clazz = clazz;
        initialHeader(authorization);
        setPostData(postObject, objectType);
    }

    public VolleyRequest(Context context, int method, String url, JSONObject paramJson, Class<T> clazz, String authorization, Listener<T> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        this.context = context;
        this.clazz = clazz;
        initialHeader(authorization);
        setPostData(paramJson);
    }

    public VolleyRequest(Context context, int method, String url, String paramString, Class<T> clazz, String authorization, Listener<T> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        this.context = context;
        this.listener = listener;
        this.clazz = clazz;
        initialHeader(authorization);
        setPostData(paramString);
    }

    public VolleyRequest(Context context, int method, String url, Class<T> clazz, String authorization, Listener<T> listener,
                         ErrorListener errorListener) {
        super(method, url, errorListener);
        this.context = context;
        this.listener = listener;
        this.clazz = clazz;
        initialHeader(authorization);
        setPostData("");
    }

    public void initialHeader(String authorization) {
        setTag(getUrl());
        setRetryPolicy(new DefaultRetryPolicy(30000,
                /*DefaultRetryPolicy.DEFAULT_MAX_RETRIES*/ 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        headers.put("Connection", "Keep-Alive");
        headers.put("Accept-Encoding", "UTF-8");
        //headers.put("Launch-Id", BuildConfig.APPLICATION_ID);
        headers.put("Content-Type", getContentType());
        /*GeocodeLocation geocodeLocation = MidoSharedPreferences.getGeocodeLocation(context);
        if (geocodeLocation != null) {
            headers.put("Geo-Position", geocodeLocation.lat + "," + geocodeLocation.lng);
            if (!TextUtils.isEmpty(geocodeLocation.countryCode)) {
                headers.put("Geo-Country", geocodeLocation.countryCode);
            }
            if (!TextUtils.isEmpty(geocodeLocation.state)) {
                headers.put("Geo-State", geocodeLocation.state);
            }
        }*/

        setAuthorization(authorization);
    }

    public String getContentType() {
        return CONTENT_TYPE;
    }

    public String getMethodName() {
        int method = getMethod();
        if (method == Method.DELETE) return "DELETE";
        if (method == Method.GET) return "GET";
        if (method == Method.POST) return "POST";
        if (method == Method.PUT) return "PUT";
        if (method == Method.PATCH) return "PATCH";
        return "Unknown";
    }

    public void logUrlAndSpecialHeaders() {
        String log = "VolleyRequest of " + getMethodName() + ": " + getUrl() + "\n";
        String strGeo = headers.get("Geo-Position");
        if (Utilidades.isEmpty(strGeo)) {
            log += "... No Geo-XXX headers\n";
        } else {
            log += "... Geo-Position: " + headers.get("Geo-Position") + "\n";
            log += "... Geo-Country: " + headers.get("Geo-Country") + "\n";
            log += "... Geo-State: " + headers.get("Geo-State") + "\n";
        }
        log += "... postData: " + postData + "\n";
        //Constants.ticketSelectionEncryptParam = log;
        //ALog.d(this, log);
    }

    public VolleyRequest setResponseClass(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    public VolleyRequest setAuthorization(String authorization) {
        if (Utilidades.isNotEmpty(authorization))
            headers.put("Authorization", authorization);
        return this;
    }

    public VolleyRequest setPostData(String postData) {
        this.postData = postData;
        return this;
    }

    public VolleyRequest setPostData(Object postObject, Class inputType) {
        postData = gson.toJson(postObject, inputType);
        return this;
    }

    public VolleyRequest setPostData(Object postObject, Type inputType) {
        postData = gson.toJson(postObject, inputType);
        //ALog.d("DucNM", "post Data: " + postData);
        return this;
    }

    public VolleyRequest setRawData(byte[] rawData, String type) {
        this.rawData = rawData;
        this.typeRawData = type;
        headers.put("Content-Type", type);
        return this;
    }

    public VolleyRequest setPostData(JSONObject jsonObject) {
        if (jsonObject != null) {
            postData = jsonObject.toString();
        }
        return this;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (rawData != null) {
            return rawData;
        } else {
            if (postData != null) {
                try {
                    return postData.getBytes(PROTOCOL_CHARSET);
                } catch (UnsupportedEncodingException e) {
                    return postData.getBytes();
                }
            }
        }
        return super.getBody();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        mHttpCode = response == null ? -1 : response.statusCode;
        return VolleyResultProcessor.parseNetworkResponse(response, clazz);
    }

    @Override
    protected void deliverResponse(T response) {
        //ALog.d(this, "deliverError(" + mHttpCode + ") of " + getUrl());
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mHttpCode = error == null ? -2 :
                error.networkResponse == null ? -3 : error.networkResponse.statusCode;
        super.deliverError(error);
    }

    @Override
    public String getBodyContentType() {
        if (rawData != null) {
            return typeRawData;
        } else {
            if (getMethod() != Method.PATCH)
                return "application/json";
            else
                return " ";
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    public String requestToString() {
        String str = "\n>Method: " + getMethodName();
        str += "\n\n>Url: " + getUrl();
        str += "\n\n>Headers: ";
        try {
            Map<String, String> headers = getHeaders();
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                str += "\n-" + entry.getKey() + ": " + entry.getValue();
            }
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        str += "\n\n>Payload: " + postData;

        return str;
    }

}
