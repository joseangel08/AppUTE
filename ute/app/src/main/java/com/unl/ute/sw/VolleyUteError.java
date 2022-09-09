package com.unl.ute.sw;

import android.os.Parcelable;
import android.os.Parcel;

import com.android.volley.Request;
import com.android.volley.VolleyError;





/**
 * Muestra los errore al momenro de conectarse
 */

public class VolleyUteError implements Parcelable {
    public String errorCode = ERR_UNKNOWN;
    public String errorTitle = "";
    public String errorMessage = "";

    public String messageTitle = "";
    public String messageBody = "";
    public String messageMore = "";

    public int httpCode = -1;
    public long networkTimeMs = -1;

    //Server-defined API error codes
    static public final String ERR_ACCOUNT_DUPLICATE = "ERR_ACCOUNT_DUPLICATE";
    static public final String ERR_ACCOUNT_NOT_EXISTS = "ERR_ACCOUNT_NOT_EXISTS";

    static public final String ERR_ACCOUNT_DUPLICATE_FB_ACCOUNT_EXISTS = "ERR_ACCOUNT_DUPLICATE_FB_ACCOUNT_EXISTS";
    static public final String ERR_ACCOUNT_DUPLICATE_EMAIL_ACCOUNT_EXISTS = "ERR_ACCOUNT_DUPLICATE_EMAIL_ACCOUNT_EXISTS";

    static public final String ERR_ACCOUNT_PASSWORD_REQUEST_EXPIRED = "ERR_ACCOUNT_PASSWORD_REQUEST_EXPIRED ";
    static public final String ERR_ACCOUNT_PASSWORD_REQUEST_NOT_EXISTS = "ERR_ACCOUNT_PASSWORD_REQUEST_NOT_EXISTS";

    static public final String ERR_BAD_REQUEST_DATA = "ERR_BAD_REQUEST_DATA";
    static public final String ERR_UNKNOWN = "ERR_UNKNOWN";


    //Client-defined error codes
    static public final String ERROR_INTERNAL = "ERROR_INTERNAL";
    static public final String ERR_REQUEST_TIMEOUT = "ERR_REQUEST_TIMEOUT";
    static public final String ERR_NETWORK_CONNECTIVITY = "ERR_NETWORK_CONNECTIVITY";
    static public final String ERR_INVALID_RESPONSE = "ERR_INVALID_RESPONSE";

    /**
     * Error de conexion
     * @return VolleyFilmsError
     */
    public static VolleyUteError createVolleyErrorNoNetwork() {
        VolleyUteError error = new VolleyUteError();
        error.httpCode = 200;
        error.errorCode = ERR_NETWORK_CONNECTIVITY;

        return error;
    }

    /**
     * Error en la aplicacion
     * @param msgTitle Titulo del error
     * @param msgContent Mensaje de error
     * @return
     */
    public static VolleyUteError createVolleyError(String msgTitle, String msgContent) {
        VolleyUteError error = new VolleyUteError();
        error.httpCode = -1;
        error.errorCode = ERR_UNKNOWN;
        error.errorTitle = msgTitle;
        error.errorMessage = msgContent;

        return error;
    }

    public VolleyUteError() {

    }

    public VolleyUteError(VolleyError error) {
        this(error, null);
    }

    public VolleyUteError(VolleyError error, Request request) {
        VolleyUteError errors = VolleyResultProcessor.parseErrorResponse(error);
        errorCode = errors.errorCode;
        errorTitle = errors.errorTitle;
        errorMessage = errors.errorMessage;

        messageTitle = errors.messageTitle;
        if (Utilidades.isNotEmpty(messageTitle))
            messageTitle = errorTitle;
        messageBody = errors.messageBody;
        httpCode = errors.httpCode;
        networkTimeMs = errors.networkTimeMs;

        if (request != null) {
            messageBody = "RESULT ===================== ";
            messageBody += "\n- Error code: " + errorCode;
            messageBody += "\n- Http code: " + httpCode;
            messageBody += "\n- Error msg: " + errorMessage;
            //messageBody += "\n- Time: " + networkTimeMs + "ms";

            messageBody += "\n\nREQUEST ===================== ";

            if (request instanceof VolleyRequest) {
                messageBody += ((VolleyRequest) request).requestToString();
            } //else if (request instanceof GsonRequest) {
                //messageBody += new Gson().toJson(request);//((Gson) request).requestToString();
            //}
        }
    }

    protected VolleyUteError(Parcel in) {
        errorCode = in.readString();
        errorTitle = in.readString();
        errorMessage = in.readString();
        messageTitle = in.readString();
        messageBody = in.readString();
        messageMore = in.readString();
        httpCode = in.readInt();
        networkTimeMs = in.readLong();
    }

    public void setMessageMore(String messageMore) {
        this.messageMore = messageMore;
    }

    public static final Creator<VolleyUteError> CREATOR = new Creator<VolleyUteError>() {
        @Override
        public VolleyUteError createFromParcel(Parcel in) {
            return new VolleyUteError(in);
        }

        @Override
        public VolleyUteError[] newArray(int size) {
            return new VolleyUteError[size];
        }
    };

    @Override
    public String toString() {
        return "(" + errorCode + "," + httpCode + "," + /*networkTimeMs +"ms: " +*/ errorMessage + ")";
    }

    public boolean isGeneralError() {
        return ERR_UNKNOWN.equals(errorCode) ||
                ERROR_INTERNAL.equals(errorCode) ||
                ERR_REQUEST_TIMEOUT.equals(errorCode);
    }

    public boolean isErrorResetPasswordInvalid() {
        return ERR_ACCOUNT_PASSWORD_REQUEST_EXPIRED.equals(errorCode) ||
                ERR_BAD_REQUEST_DATA.equals(errorCode) ||
                ERR_ACCOUNT_NOT_EXISTS.equals(errorCode) ||
                ERR_ACCOUNT_PASSWORD_REQUEST_NOT_EXISTS.equals(errorCode);
    }

    public boolean isErrorRegisterAccountInvalid() {
        return ERR_ACCOUNT_DUPLICATE_EMAIL_ACCOUNT_EXISTS.equals(errorCode) ||
                ERR_ACCOUNT_DUPLICATE_FB_ACCOUNT_EXISTS.equals(errorCode) ||
                ERR_ACCOUNT_DUPLICATE.equals(errorCode);
    }

    public boolean isErrorNetworkConnection() {
        return ERR_NETWORK_CONNECTIVITY.equals(errorCode);
    }

    public boolean isErrorInvalidResponse() {
        return ERR_INVALID_RESPONSE.equals(errorCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(errorCode);
        dest.writeString(errorTitle);
        dest.writeString(errorMessage);
        dest.writeString(messageTitle);
        dest.writeString(messageBody);
        dest.writeString(messageMore);
        dest.writeInt(httpCode);
        dest.writeLong(networkTimeMs);
    }
}
