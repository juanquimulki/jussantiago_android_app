package gov.jussantiago.jmulki.autoconsulta.classes;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import gov.jussantiago.jmulki.autoconsulta.R;

/**
 * Created by jmulki on 10/05/2019.
 */

public class ObjetoVolley {

    private Context context;
    private String url;
    private Map<String,String> params;
    private Integer method;
    private String authorization;

    public ObjetoVolley() {}

    public ObjetoVolley(Context context, String url, Map<String, String> params, Integer method, String authorization) {
        this.context = context;
        this.url = url;
        this.params = params;
        this.method = method;
        this.authorization = authorization;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setMethod(Integer method) {
        this.method = method;
    }

    public void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result);
    }

    public void execute(final VolleyCallback callback) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);

        switch (method) {
            case Request.Method.GET:
                String parametros = getParams();
                this.url += "?" + parametros;
                break;
            case Request.Method.POST:
                break;
        }

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(method, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String str = response;
                        try {
                            JSONObject objeto = new JSONObject(str);
                            callback.onSuccessResponse(objeto);
                        } catch (JSONException e) {
                            Toast.makeText(context, context.getString(R.string.error) + " (Cód: 01)", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context, context.getString(R.string.sin_conexion), Toast.LENGTH_SHORT).show();
                        } else {
                            if ((error.networkResponse.statusCode)==400 || (error.networkResponse.statusCode==401)) {
                                try {
                                    String body = new String(error.networkResponse.data,"UTF-8");
                                    JSONObject objeto = new JSONObject(body);
                                    String mensaje = objeto.getString("message");
                                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show();
                                } catch (UnsupportedEncodingException e) {
                                    Toast.makeText(context, context.getString(R.string.error) + " (Cód: 02)", Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    Toast.makeText(context, context.getString(R.string.error) + " (Cód: 03)", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(context, context.getString(R.string.error) + " (Cód: 04) - "+String.valueOf(error.networkResponse.statusCode), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", authorization);
                return params;
            }
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private String getParams() {
        String cadena = "";
        for (Map.Entry<String, String> entry : this.params.entrySet()) {
            cadena += entry.getKey() + "=" + entry.getValue() + "&";
        }
        return cadena;
    }
}
