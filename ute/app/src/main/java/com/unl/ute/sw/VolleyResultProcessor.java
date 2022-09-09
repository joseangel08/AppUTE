package com.unl.ute.sw;

/**
 * Permite procesar los datos
 */

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;



import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Utility to process Volley result (NetworkResponse and VolleyError)
 */
public final class VolleyResultProcessor<T> {

    /**
     * Convert a VoleyError object into a more meaningful VolleyFilmsError
     *
     * @param error VolleyError
     * @return VolleyFilmsError
     */
    @NonNull
    public static VolleyUteError parseErrorResponse(@Nullable VolleyError error) {
        final VolleyUteError midoError = new VolleyUteError();
        if (error == null) {
            midoError.errorCode = VolleyUteError.ERR_UNKNOWN;
            midoError.errorMessage = "Unknown error";
            midoError.errorTitle = "Error";

        } else {
            int httpCode = error.networkResponse == null ? -2 : error.networkResponse.statusCode;
            midoError.networkTimeMs = error.getNetworkTimeMs();
            midoError.httpCode = httpCode;

            if (error instanceof ParseError) {
                midoError.errorCode = VolleyUteError.ERR_INVALID_RESPONSE;
                midoError.errorMessage = "Parser error: cannot parse network response";
                midoError.errorTitle = "Parser error";

            } else if (error instanceof TimeoutError || httpCode == 504) {
                midoError.errorCode = VolleyUteError.ERR_REQUEST_TIMEOUT;
                midoError.errorMessage = "Request timeout, hence network response is null";
                midoError.errorTitle = "Request timeout";

            } else if (error instanceof NoConnectionError) {
                midoError.errorCode = VolleyUteError.ERR_NETWORK_CONNECTIVITY;
                Throwable cause = error.getCause();
                if (cause != null && Utilidades.isNotEmpty(cause.getMessage()))
                    midoError.errorMessage = cause.getMessage();
                else
                    midoError.errorMessage = "Internet has problem. Mobile can't access server";
                midoError.errorTitle = "No connection";

            } else {
                if (error.networkResponse == null) {
                    String errMsg = error.getMessage();
                    midoError.errorCode = VolleyUteError.ERR_UNKNOWN;
                    midoError.errorMessage = Utilidades.isNotEmpty(errMsg) ? errMsg :
                            "Error of Volley library when network response is null";
                    midoError.errorTitle = "VolleyError is null";

                } else {
                    try {
                        String data = new String(error.networkResponse.data, HttpHeaderParser.parseCharset(error.networkResponse.headers));
                        if (data.contains("Welcome\r\n")) {
                            midoError.errorCode = VolleyUteError.ERR_UNKNOWN;
                            midoError.errorMessage = "Wrong link API";
                            midoError.errorTitle = "Wrong link API";

                        } else {
                            VolleyUteError tmp = new Gson().fromJson(data, VolleyUteError.class);
                            midoError.errorCode = tmp.errorCode;
                            midoError.errorMessage = tmp.errorMessage;
                            midoError.errorTitle = tmp.errorTitle;
                            midoError.messageTitle = tmp.messageTitle;
                            midoError.messageBody = tmp.messageBody;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        midoError.errorCode = VolleyUteError.ERR_UNKNOWN;
                        midoError.errorMessage = "Don't know error";
                        midoError.errorTitle = "Don't know error";
                    }
                }

            }

        }

        return midoError;
    }

    public static <T> Response<T> parseNetworkResponse(NetworkResponse response, Class<T> clazz) {
        String json = "";

        try {
            String datos = new String(response.data);
            if(datos.contains("{\"Search\":")) {
                datos = datos.replace("{\"Search\":","");
                String[] aux = datos.split("],");
                datos = aux[0] + "]";

            }
            Log.i("Respuesta sw ", new String(datos.getBytes()));
            json = new String(
                    datos.getBytes(),
                    HttpHeaderParser.parseCharset(response.headers));
            /*json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));*/
            if (clazz == String.class) {
                return (Response<T>) Response.success(
                        json,
                        HttpHeaderParser.parseCacheHeaders(response));
            } else if (clazz == JSONObject.class) {
                try {
                    return (Response<T>) Response.success(
                            new JSONObject(json),
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            } else if (clazz == NetworkResponse.class) {
                return (Response<T>) Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
            } else {

                return Response.success(
                        new Gson().fromJson(json, clazz),
                        HttpHeaderParser.parseCacheHeaders(response));
            }

        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }
}