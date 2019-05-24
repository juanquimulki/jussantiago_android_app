package gov.jussantiago.jmulki.autoconsulta;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jmulki on 15/05/2019.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        try {
            // Get updated InstanceID token.
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.d("Firebase", "Refreshed token " + refreshedToken);

            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            Integer codigo = sharedPref.getInt("codigo",0);
            String numSerie = sharedPref.getString("numSerie",null);
            String token = sharedPref.getString("token",null);

            if (codigo>0) {
                sendRegistrationToServer(refreshedToken, codigo, numSerie, token);
            }
            else {
                Log.i("proceso token","sin codigo");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRegistrationToServer(final String refreshedToken, Integer codigo, String numSerie, String token) {
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        Map<String, String> params = new HashMap<String, String>();
        params.put("codigo", codigo.toString());
        params.put("numserie", numSerie);
        params.put("refreshedToken", refreshedToken);

        ObjetoVolley volley = new ObjetoVolley(
                MyFirebaseInstanceIDService.this,
                getString(R.string.url_acttoken),
                params,
                Request.Method.POST,
                token
        );

        volley.execute(new ObjetoVolley.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject objeto) {
                if (objeto != null) {
                    try {
                        String mensaje = objeto.getString("message");
                        Log.i("proceso token",mensaje);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("refreshedToken",refreshedToken).commit();
                    } catch (Exception e) {
                        Toast.makeText(MyFirebaseInstanceIDService.this, getString(R.string.error) + " (Cód: Token01)", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MyFirebaseInstanceIDService.this, getString(R.string.respuesta_null), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
