package com.example.jurara.myapplication;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    String Nombre, Celular, Domicilio, Articulo, FechaPres, FechaDev, Descripcion;
    private final int Date_id = 0, Date_id2 = 1;
    EditText txtNom, FechaP, fechad, des, cel, dom;
    Spinner artics;
    Button Nuevo, pre, verpres;
    final BDD bdd = new BDD(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        bdd.abrir();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //nombre
        verpres = (Button) findViewById(R.id.btnVer);

        verpres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Lista.class);
                startActivity(i);
            }
        });



        txtNom = (EditText) findViewById(R.id.txtNombre);
        cel = (EditText) findViewById(R.id.txtCel);
        dom = (EditText) findViewById(R.id.txtDom);
        pre = (Button) findViewById(R.id.btnRegArt);
        //spinner
        artics = (Spinner) findViewById(R.id.spiner);
        //Creamos la lista




        //prestamo fecha
        FechaP = (EditText) findViewById(R.id.fecp);
        FechaP.setText(getDatePhone());
        FechaP.setInputType(InputType.TYPE_NULL);
        FechaP.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog(Date_id);
                return false;
            }
        });

        //devolucion
        fechad = (EditText) findViewById(R.id.fece);
        fechad.setInputType(InputType.TYPE_NULL);
        fechad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                showDialog(Date_id2);
                return false;
            }
        });


        des = (EditText) findViewById(R.id.txtDes);

        Nuevo = (Button) findViewById(R.id.btnRegArt);

        Nuevo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Nuevo.getContext());
                final EditText editText = new EditText(Nuevo.getContext());

                alertDialog.setTitle("Agregar Nuevo Articulo");
                alertDialog.setMessage("Agrega el nombre de tu nuevo articulo");
                alertDialog.setView(editText);

                alertDialog.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (editText.length() == 0) {
                            Toast.makeText(getApplicationContext(), "Articulo no valido", Toast.LENGTH_SHORT).show();
                        } else {

                            final String str[] = bdd.leerart();
                            for (int i = 0; i < str.length; i++) {
                                if (str[i].equalsIgnoreCase(editText.getText().toString())) {
                                    Toast.makeText(getApplicationContext(), "El artÃ­culo ya existe.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            bdd.insertarart(editText.getText().toString());
                            actualizar();
                            /// verificarArt();
                            Toast.makeText(getApplicationContext(), "ArtÃ­culo agregado.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Cancelado", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog ad = alertDialog.create();
                ad.show();
            }
        });
        //empieza manejo de la bdd
        String sep[] = bdd.leerart();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, sep);
        artics.setAdapter(adapter);


    }

    public void actualizar() {
        final String str[] = bdd.leerart();

        final ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, str);
        artics.setAdapter(adapter);
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy");
        String formateDate = df.format(date);
        return formateDate;
    }

    protected Dialog onCreateDialog(int id) {

        //Obtener calendario
        Calendar c = Calendar.getInstance();

        //Del calendario, obtener el aÃƒÂ±o, mes, dÃƒÂ­a
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        switch (id) {
            case Date_id:

                //Abrir diÃƒÂ¡logo de DatePicker
                return new DatePickerDialog(MainActivity.this, date_listener1, year, month, day);
            case Date_id2:
                return new DatePickerDialog(MainActivity.this, date_listener, year, month, day);
        }
        return null;
    }


    //DiÃƒÂ¡logo de DatePicker
    DatePickerDialog.OnDateSetListener date_listener1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //Almacenar los datos en un solo string y despuÃƒÂ©s se envian al campo de texto
            String date1 = String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
            FechaP = (EditText) findViewById(R.id.fecp);

            FechaP.setText(date1);

        }
    };


    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            //Almacenar los datos en un solo string y despuÃƒÂ©s se envian al campo de texto
            String date1 = String.valueOf(day) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
            fechad = (EditText) findViewById(R.id.fece);

            fechad.setText(date1);

        }
    };


    public void regPres(View view) {
        verificacion();

        if (error == 0) {
            Nombre = txtNom.getText().toString();
            Celular = cel.getText().toString();
            Domicilio = dom.getText().toString();
            Articulo = artics.getSelectedItem().toString();
            FechaPres = FechaP.getText().toString();
            FechaDev = fechad.getText().toString();
            Descripcion = des.getText().toString();
            bdd.insertarconsulta(Nombre, Celular, Domicilio, Articulo, FechaPres, FechaDev, Descripcion);
            vaciarCampos();
            Toast.makeText(MainActivity.this, "Registro guardado exitosamente", Toast.LENGTH_SHORT).show();
        }
    }

    // vaciar campos
    public void vaciarCampos() {
        txtNom.setText("");
        cel.setText("");
        dom.setText("");
        artics.setSelection(0);
        des.setText("");
        fechad.setText("");
    }


    //MÃ©todo de verificaciÃ³n de campos
    int error;

    private void verificacion() {
        error = 0;
        if (txtNom.length() == 0) {
            error = 1;
            Toast.makeText(MainActivity.this, "Falta agregar el Nombre de a quiÃ©n se le presta el artÃ­culo", Toast.LENGTH_LONG).show();
            txtNom.requestFocus();
        }

        if (error == 0) {
            if (cel.length() == 0) {
                if (error == 0) {
                    Toast.makeText(MainActivity.this, "Falta agregar Un Celular de la persona a quien se le prestara el articulo", Toast.LENGTH_SHORT).show();
                    cel.requestFocus();
                }
                error = 2;
            }
        }
        if (error == 0) {
            if (dom.length() == 0) {
                if (error == 0) {
                    Toast.makeText(MainActivity.this, "Falta agregar Un Domicilio de la persona a quien se le prestara el articulo", Toast.LENGTH_SHORT).show();
                    dom.requestFocus();
                }
                error = 3;
            }
        }

        if (error == 0) {
            if (fechad.length() == 0) {
                if (error == 0) {
                    Toast.makeText(MainActivity.this, "Falta agregar Una fecha de cuando te devolvera el articulo", Toast.LENGTH_SHORT).show();
                    fechad.requestFocus();
                }
                error = 3;
            }
        }

        final String fecha1, fecha2;
        fecha1 = FechaP.getText().toString();
        fecha2 = fechad.getText().toString();

        if (error == 0) {
            if (compararFechas(fecha1, fecha2) == false) {
                Toast.makeText(MainActivity.this, "Error, la fecha de devoluciÃ³n no puede ser inferior a la de prÃ©stamo", Toast.LENGTH_LONG).show();
                showDialog(Date_id);
                error = 4;
            }
        }

        if (error == 0) {
            if (des.length() == 0) {
                if (error == 0) {
                    Toast.makeText(MainActivity.this, "Falta agregar Una descripcion de lo que prestaste", Toast.LENGTH_SHORT).show();
                    des.requestFocus();
                }
                error = 5;
            }
        }

        if (error == 0) {
            if (artics.getSelectedItemPosition() == 0) {
                if (error == 0) {
                    Toast.makeText(MainActivity.this, "Falta agregar El articulo a prestar", Toast.LENGTH_SHORT).show();
                    artics.requestFocus();
                }
                error = 6;
            }
        }
    }

    public Boolean compararFechas(String fecha1, String fecha2) {
        Boolean tmp = true;
        String[] a = fecha1.split("/");
        String[] b = fecha2.split("/");

        int numDay1, numMonth1, numYear1,
                numDay2, numMonth2, numYear2;

        numDay1 = Integer.parseInt(a[0]);
        numMonth1 = Integer.parseInt(a[1]);
        numYear1 = Integer.parseInt(a[2]);

        numDay2 = Integer.parseInt(b[0]);
        numMonth2 = Integer.parseInt(b[1]);
        numYear2 = Integer.parseInt(b[2]);

        if ((numDay2 < numDay1) ||
                (numDay2 >= numDay1 && numMonth2 < numMonth1) ||
                (numDay2 >= numDay1 && numMonth2 >= numMonth1 && numYear2 < numYear1)) {
            tmp = false;
        }
        return tmp;
    }



}
