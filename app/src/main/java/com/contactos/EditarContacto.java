package com.contactos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EditarContacto extends Activity implements LocationListener {

    // The minimum distance to change updates in metters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 metters
    // The minimum time beetwen updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

    final static int cameraData = 0;
    int idContacto;
    Contacto contacto;
    Button btadicionar;
    Button btTirarFoto;
    EditText edtNome;
    EditText edtEmail;
    EditText edtTelefone;
    ImageView iv;
    DBAdapter datasource;
    Location loc = null;
    LocationManager locationManager = null;
    String provider = "";

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getLayoutParams().width, v.getLayoutParams().height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getLayoutParams().width, v.getLayoutParams().height);
        v.draw(c);
        return b;
    }

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editarcontacto);

        datasource = new DBAdapter(this);
        edtNome = (EditText) findViewById(R.editarcontacto.ednome);
        edtEmail = (EditText) findViewById(R.editarcontacto.edEmail);
        edtTelefone = (EditText) findViewById(R.editarcontacto.edTelefone);
        btadicionar = (Button) findViewById(R.editarcontacto.btadicionar);
        btTirarFoto = (Button) findViewById(R.editarcontacto.tirarFoto);
        iv = (ImageView) findViewById(R.editarcontacto.ivReturnedPic);

        carregaDetalhesContacto();

        btTirarFoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(i, cameraData);
            }
        });
        Boolean gps = false, rede = false;
        loc = null;
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        rede = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        provider = "";
        if (gps) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            if (rede) {
                provider = LocationManager.NETWORK_PROVIDER;
            }
        }
        if (!provider.equals("")) {
            locationManager.requestLocationUpdates(
                    provider, MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        }

        btadicionar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                datasource.open();
                Contacto c = datasource.updateContacto(idContacto, edtNome.getText().toString(), edtEmail.getText().toString(), edtTelefone.getText().toString(), loadBitmapFromView(iv));
                datasource.close();

                if (locationManager != null) {
                    if (!provider.equals("")) {
                        System.out.println("LocationManager:" + locationManager);
                        loc = locationManager
                                .getLastKnownLocation(provider);
                    }
                }
                AlertDialog.Builder dialogo = new
                        AlertDialog.Builder(EditarContacto.this);
                dialogo.setTitle("Gravado");
                String mensagem = "Contacto:" + c.getNome();
                if (loc != null) {
                    mensagem += "\nloc: " + loc.getLatitude() + "," + loc.getLongitude();
                    Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
                    List<Address> addresses;
                    try {
                        addresses = gcd.getFromLocation(loc.getLatitude(),
                                loc.getLongitude(), 1);
                        if (addresses.size() > 0) {
                            System.out.println("##################################");
                            mensagem +=  "\nCidade:" + addresses.get(0).getLocality();
                            System.out.println(addresses.get(0).getLocality());
                            System.out.println(addresses.get(0).getFeatureName());
                            System.out.println(addresses.get(0).getThoroughfare());
                            System.out.println("##################################");
                        }
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                dialogo.setMessage(mensagem);
                dialogo.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                dialogo.show();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap bmp = (Bitmap) extras.get("data");
            iv.setImageBitmap(bmp);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void carregaDetalhesContacto() {
        idContacto = getIntent().getIntExtra("idContacto", 0);

        datasource = new DBAdapter(this);
        datasource.open();
        contacto = datasource.getContacto(idContacto);
        datasource.close();

        iv.setImageBitmap(contacto.getFoto());
        edtNome.setText(contacto.getNome());
        edtEmail.setText(contacto.getEmail());
        edtTelefone.setText(contacto.getTelefone());
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void onProviderDisabled(String provider) {
    }

    public void onProviderEnabled(String provider) {
    }

    public void onLocationChanged(Location location) {
    }
}


