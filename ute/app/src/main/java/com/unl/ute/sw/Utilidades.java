package com.unl.ute.sw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.text.SimpleDateFormat;

import android.location.Location;
import android.os.Build;
import android.os.PatternMatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.webkit.URLUtil;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


/**
 * Clase Utilidades de la aplicacion
 */

public class Utilidades extends StringUtils {

    private static final String HTML_BOLD_START = "<b>";
    private static final String HTML_BOLD_END = "</b>";
    private static Locale defaultLocale;

    public static Bitmap get_imagen(String url) {
        Bitmap bm = null;
        URL imageUrl = null;
        try {
            imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            //conn.connect();
            bm = BitmapFactory.decodeStream(conn.getInputStream());
            //imageView.setImageBitmap(loadedImage);
        } catch (IOException e) {
            //Toast.makeText(getApplicationContext(), "Error cargando la imagen: "+e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return bm;
    }

    /**
     * Formato de fecha
     * @param date Un objeto Date
     * @return La fecha en anio, mes y dia hora, minuto y segundo
     */
    public static String formatoFecha(Date date) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strFecha = "";
        Date fechaDate = null;
        try {
            strFecha = formato.format(date);
            //System.out.println(fechaDate.toString());
            return strFecha;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    /**
     * Permite obtener los paises
     * @return lista de paises
     */
    public static String[] locales() {

        String[] localesAux = Locale.getISOCountries();
        String [] locales = new String[localesAux.length];
        int cont = 0;
        for (String countryCode : localesAux) {
            Locale obj = new Locale("", countryCode);
            locales[cont] = obj.getDisplayCountry()+" (" +  obj.getCountry() + ")";
            cont++;
        }

        return locales;
    }

    public static String wrapInHtmlBold(final String text) {
        return HTML_BOLD_START + text + HTML_BOLD_END;
    }

    public static JsonObject getJSONError(String texto) {
        JsonParser parser = new JsonParser();
        JsonElement obj =  parser.parse(texto);
        return obj.getAsJsonObject();

    }



    public static double calcularDistancias(double lon, double lat, double lon1, double lat1) {
        Location locationA = new Location("punto A");

        locationA.setLatitude(lat);
        locationA.setLongitude(lon);

        Location locationB = new Location("punto B");

        locationB.setLatitude(lat1);
        locationB.setLongitude(lon1);

        float distance = locationA.distanceTo(locationB)/ 1000;
        return dosdecimales(Double.parseDouble(String.valueOf(distance)));
    }

    public static double dosdecimales(double number) {
        number = Math.round(number * 100);
        number = number/100;
        return number;
    }

    public static double getDistance(double lat1,double lon1, double lat2, double lon2){
        double Radius = 6.371;//6371000; //Radio de la tierra
        /*double lat1 = lat_a / 1E6;
        double lat2 = lat_b / 1E6;
        double lon1 = lng_a / 1E6;
        double lon2 = lon_b / 1E6;*/
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon /2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return  (Radius * c);
    }




    /**
     * Permite cambiar idioma de la aplicacion
     * @param lenguaje Codigo de lenguaje
     * @param pais Codigo de pais
     */
    public static void cambiarIdioma(String lenguaje, String pais, Context context, Intent intent) {

        Locale locale;
       // Sessions session = new Sessions(context);
        //Log.e("Lan",session.getLanguage());
        locale = new Locale(lenguaje, pais);
        Resources resources = context.getResources();
        //Configuration config = new Configuration(resources.getConfiguration());
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLocale(locale);
        } else{
            configuration.locale=locale;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            context.getApplicationContext().createConfigurationContext(configuration);
            context.getResources().updateConfiguration(configuration, resources.getDisplayMetrics());
        } else {
            context.getResources().updateConfiguration(configuration, resources.getDisplayMetrics());
        }
        //Intent intent =;
        Intent i = intent;
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(i);

        //context.startActivity(intent);
        /*Locale.setDefault(locale);
        config.setLocale(locale);

        resources.updateConfiguration(config,
                resources.getDisplayMetrics());*/

        //cambiarIdioma("es", "ES");
        //cambiarIdioma("en", "EN");
        //Locale localizacion = new Locale(lenguaje, pais);
        //LogsApp.i("se cambio el idioma a "+pais);
        //Locale.setDefault(localizacion);
        //Configuration config = new Configuration();
        //config.locale = localizacion;
        //config.setLocale(localizacion);
        //this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        //Log.e("BaseACtivitySinMenu", config.toString());
        //Intent refrescar = new Intent(this, BaseActivitySinMenu.class);
        //startActivity(refrescar);
        //finish();
    }

    public static String split_cadena(String cadena, String delimitador, int posicion) {
        String [] arreglo = cadena.split(delimitador);
        if((arreglo.length -1) >= posicion) {
            return arreglo[posicion];
        } else {
            return "";
        }
    }

    /**
     * Para ver el nodo anterior
     * @param size tamano de la lista
     * @param pos la posicion actual
     * @return
     */
    public static int posAnt(int size, int pos) {
        pos = pos -1;
        if(size == 0)
            return 0;
        else
            return (pos <= -1) ? size - 1 : pos;
    }

    /**
     * Para ver el nodo siguiente
     * @param size tamano de la lista
     * @param pos la posicion actual
     * @return
     */
    public static int posSig(int size, int pos) {
        pos = pos + 1;
        if(size == 0)
            return 0;
        else
            return (pos >= size) ? 0 : pos;
    }



    public static String transformarKmtoM(double valor) {
        if(valor >= 1)
            return String.valueOf(valor) + " KM";
        else
            return String.valueOf(dosdecimales(valor*1000))+" M";
    }

    public static String formatearMinutosAHoraMinuto(int tsegundos) {
        String FORMAT = "%02d:%02d:%02d";
        String  myTime =  String.format(FORMAT,
                //Hours
                TimeUnit.MILLISECONDS.toHours(tsegundos) -                        TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(tsegundos)),
                //Minutes
                TimeUnit.MILLISECONDS.toMinutes(tsegundos) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(tsegundos)),
                //Seconds
                TimeUnit.MILLISECONDS.toSeconds(tsegundos) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(tsegundos)));
        return myTime;
    }

    public static boolean verificarUUID4(String dato) {

        String patter = "/^[0-9A-F]{8}-[0-9A-F]{4}-[4][0-9A-F]{3}-[89AB][0-9A-F]{3}-[0-9A-F]{12}$/i";
        Log.e("UTILIdADES", dato+"  "+dato.matches(patter));
        Pattern aux = Pattern.compile(patter);
        return aux.matcher(dato).matches();
    }
    public static String[] trozarCadena(String dato) {
        return dato.split("/");
    }

    public static boolean IsValidUrl(String urlString) {
        try {
            URL url = new URL(urlString);
            URI uri = url.toURI();
            //Log.e("IsValidUrl", "otro "+urlString+"   "+Patterns.WEB_URL);
            return Patterns.WEB_URL.matcher(urlString).matches();
        } catch (MalformedURLException | URISyntaxException ignored) {
            Log.e("IsValidUrl", ignored.toString());
        }
        return false;
    }


}
