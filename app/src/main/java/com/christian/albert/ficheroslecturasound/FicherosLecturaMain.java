package com.christian.albert.ficheroslecturasound;

import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.speech.tts.Voice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Locale;

public class FicherosLecturaMain extends AppCompatActivity implements TextToSpeech.OnInitListener{

    private EditText editTextWri;
    private Button botSave;
    private Button botRead;
    private TextView tvRead;

    private TextToSpeech textToSpeech;
    //private Context context = getApplicationContext();
    private String auxiliar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ficheros_lectura_main);

        editTextWri = (EditText) findViewById(R.id.editTextWrite);
        botRead = (Button) findViewById(R.id.buttonRead);
        botSave = (Button) findViewById(R.id.buttonSave);
        tvRead = (TextView) findViewById(R.id.labelTextRead);

        textToSpeech = new TextToSpeech(this, this);
        //creamos el directorio donde va a ir el fichero a leer
        crearDirectorioPal();
        //guardamos lo que hay en el EditText
        botSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auxiliar = editTextWri.getText().toString();
                Log.d("TAG", auxiliar);
                if (!auxiliar.equals("")){
                    escribirFichero(auxiliar);
                }
            }
        });

        //leemos lo que hay en el archivo a la vex que lo cargamos en el textView
        botRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String datos = leerFichero("archivo");
                tvRead.setText(datos);
                convertirTextoSpeech();
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(new Locale("es"));
            //float pitch = (float) -100;
            //textToSpeech.setPitch(0f);
            //textToSpeech.setSpeechRate(0.1f);

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("error", "This Language is not supported");
            } else {
                convertirTextoSpeech();
            }

        } else {
            Log.e("error", "Initilization Failed!");
        }
    }

    private void crearDirectorioPal(){

        //crear file
        File file = new File (Environment.getExternalStorageDirectory().getAbsolutePath() + "/SonidosLeidos");
        if (!file.exists()){
            file.mkdir();
        }


    }

    private String leerFichero(String fichero) {

        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath() + "/SonidosLeidos/", fichero);
        String datos = "";
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader archivo = new InputStreamReader(fileInputStream);

            BufferedReader bufferedReader = new BufferedReader(archivo);
            String linea = bufferedReader.readLine();

            while (linea != null){
                datos = datos + linea;
                linea = bufferedReader.readLine();
            }
            bufferedReader.close();
            archivo.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {

        }

        return datos;
    }

    private void escribirFichero(String datos) {
        try {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath()+"/SonidosLeidos/", "archivo");
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(datos);
            outputStreamWriter.flush();
            outputStreamWriter.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex){

        }
    }
    @Override
    public void onDestroy() {
        textToSpeech.shutdown();
    }

    @SuppressWarnings("deprecation")
    private void convertirTextoSpeech () {
        String text = tvRead.getText().toString();
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, text);

        if (null == text || "".equals(text)) {
            text = "Please give some input.";
        }
        //textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, map);

    }


}



