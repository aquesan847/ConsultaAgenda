package com.aqs.ad.agenda;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.UserDictionary;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private final int CONTACTS_PERMISSION = 1;
    private final String TAG = "xyzyx";

    private Button btSearch; // = findViewById(R.id.btSearch);
    private EditText etPhone;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v(TAG,"onCreate"); //v = verbose
        initialize();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v(TAG,"onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(TAG,"onPause");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v(TAG,"onRequestPermissionsResult");
        switch (requestCode){
            case CONTACTS_PERMISSION:
                if(grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Permiso concecido
                    search();
                } else {
                    //Permiso revocado
                    
                }
                break;
        }
        /*
         *  requestCode
         *  permissions
         *  grantResults
         */
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v(TAG,"onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v(TAG,"onResume");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v(TAG,"onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v(TAG,"onStop");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void explain() {

        showRationaleDialog(getString(R.string.title), getString(R.string.message),
                Manifest.permission.READ_CONTACTS, CONTACTS_PERMISSION);

    }

    //Para tener un código mas limpio hacemos este método
    private void initialize() {

        btSearch = findViewById(R.id.btSearch);
        etPhone = findViewById(R.id.etPhone);
        tvResult = findViewById(R.id.tvResult);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchIfPermitted();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {

        requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, CONTACTS_PERMISSION);

    }

    private void search() {

        /*
         * buscar entre los contactos
         * ContentProvider Proveedor de contenidos
         * ContentResolver Consultor de contenidos
         * Queries the user dictionary and returns results
         * url: https://ieszaidinvergeles.org/carpeta/carpeta2/pagina.html?dato=1
         * uri: protocolo://direccion/ruta/recurso
         */

        /*Cursor cursor = getContentResolver().query(
                UserDictionary.Words.CONTENT_URI,   // Then content URI of the words table
                new String[] {"projection"},        // The columns to return for each row
                "selectionClause",                  // Selection criteria
                new String[] {"selectionArgs"},    // Selection criteria
                "sortOrder");            */ // The sort order for the returned rows

        /*Uri uri = ContactsContract.Contacts.CONTENT_URI;
        String proyeccion[] = new String[]{ContactsContract.Contacts.DISPLAY_NAME};
        String seleccion = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = ? and " +
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER + "= ?";
        String argumentos[] = new String[]{"1","1"};
        //seleccion = null;     Con los null muestran todos, tengan o no números
        //argumentos = null;
        String orden = ContactsContract.Contacts.DISPLAY_NAME + " collate localized asc";
        Cursor cursor = getContentResolver().query(uri, proyeccion, seleccion, argumentos, orden);
        String[] columnas = cursor.getColumnNames();
        for (String s : columnas) {
            Log.v(TAG,s);
        }
        String displayName;
        int columna = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        while (cursor.moveToNext()){
            displayName = cursor.getString(columna);
            Log.v(TAG, displayName);
        }*/

        Uri uri2 = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String proyeccion2[] = new  String[] {ContactsContract.CommonDataKinds.Phone.NUMBER,
                                            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME};
        String seleccion2 = ContactsContract.CommonDataKinds.Phone.NUMBER + " like ?";
        String argumentos2[] = new String[]{etPhone.getText().toString()+"%"};
        String orden2 = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
        Cursor cursor2 = getContentResolver().query(uri2, proyeccion2, seleccion2, argumentos2, orden2);
        String[] columnas2 = cursor2.getColumnNames();
        for (String s : columnas2) {
            Log.v(TAG,s);
        }
        int columnaNombre = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        int columnaNumero = cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
        String nombre,numero;

        while (cursor2.moveToNext()){
            nombre = cursor2.getString(columnaNombre);
            numero = cursor2.getString(columnaNumero);
            Log.v(TAG, nombre + ": "+numero);
            tvResult.append("Nombre: "+nombre+"\n"+"Numero: "+numero+"\n"+"\n");
        }

    }


    private void searchIfPermitted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //La version de android es posterior a la 6.0 (incluida)
            if (ContextCompat.checkSelfPermission(

                    this, Manifest.permission.READ_CONTACTS) ==
                    PackageManager.PERMISSION_GRANTED) {
                //Ya tengo el permiso
                search();

            } else if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
                explain();
            } else {
                requestPermission();
            }
        } else {

            //La version de android es anterior a la 6.0
            //Ya tengo el permiso
            search();
        }

    }

    private void showRationaleDialog(String title, String message, String permission, int requestCode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(title)
                .setMessage(message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //nada
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission();
                    }
                });

        builder.create().show();

    }

}