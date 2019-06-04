package gov.jussantiago.jmulki.autoconsulta.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

import gov.jussantiago.jmulki.autoconsulta.R;

public class ContactoActivity extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);

        setTitle(getString(R.string.app_name) + " - Contacto");
        textView = findViewById(R.id.textView);
        textView.setText("DUDAS, CONSULTAS, SUGERENCIAS...\n\n" +
                "Área Desarrollo de Sistemas\n" +
                "Prosecretaría de Información Jurídica\n" +
                "PODER JUDICIAL\n" +
                "DE SANTIAGO DEL ESTERO\n" +
                "\n" +
                "Tels.: (0385) 450-7785/34\n" +
                "Email: judiciales@jussantiago.gov.ar\n" +
                "\n" +
                "PALACIO DE TRIBUNALES\n" +
                "Subsuelo, antiguo edificio.\n" +
                "Pasillo Absalón Rojas.");
    }
}
