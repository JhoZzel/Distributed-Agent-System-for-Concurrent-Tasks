package com.concurrente.clienteraft;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Environment;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    EditText txt_cargar, txt_descargar;
    EditText edit_ip, edit_port;
    Button btn_cargar, btn_descargar;
    String ip;
    int port;
    int port_transferencia = 1111;
    Cliente client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        txt_cargar = findViewById(R.id.txt_cargar);
        txt_descargar = findViewById(R.id.txt_descargar);
        edit_ip = findViewById(R.id.edit_ip);
        btn_descargar = findViewById(R.id.btn_descargar);

        btn_descargar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                descargarArchivo();
            }
        });
    }

    private void descargarArchivo() {
        String texto = txt_cargar.getText().toString();
        String palabra = edit_ip.getText().toString();
        texto = "1\n"+texto+"1\n"+palabra;
        Cliente cliente = new Cliente(texto);
        cliente.run();
    }

}
