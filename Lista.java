package com.example.jurara.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Lista extends Activity {
    /* private List<String> listValues;
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_lista);
         listValues=new ArrayList<String>();
         listValues.add("abcd");
         listValues.add("efg");
         listValues.add("hij");
         listValues.add("klm");
         listValues.add("nop");
         listValues.add("qrs");

         ArrayAdapter<String> miadapter=new ArrayAdapter<String>(this,R.layout.listacompleta,R.id.listText,listValues);
         setListAdapter(miadapter);
     }
 */
    ArrayAdapter AA;


    final BDD BD = new BDD(this);
    ListView lista;
    String[] basefinal;
    int j;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        lista = (ListView) findViewById(R.id.abc);
        AA = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        try {
            ArrayAdapter a = actualizar();


            lista.setAdapter(a);
        } catch (java.lang.ArrayIndexOutOfBoundsException e) {
            Toast.makeText(Lista.this, "No hay articulos prestados", Toast.LENGTH_SHORT).show();
        }



        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {


                final EditText ET = new EditText(lista.getContext());
                final AlertDialog.Builder ADB = new AlertDialog.Builder(lista.getContext());
                lista.getSelectedItem();
                String e = AA.getItem(position).toString();
                String[] b = e.split("\n");

                String[] d = b[3].split(":");

                if (d[1].equalsIgnoreCase("Devuelto.")) {
                    Toast.makeText(Lista.this, "Ya se devolvio el artículo", Toast.LENGTH_LONG).show();
                    return;
                }

                ADB.setTitle("Estado");
                ADB.setMessage("Ya ha devuelto el Articulo?\n Fecha (DD/MM/AAAA)");

                ET.setText(getDatePhone());
                ADB.setView(ET);

                ADB.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String fdd = ET.getText() + "";

                        if (Fecha(ET.getText().toString()) == true) {
                            BD.editar(position, fdd);
                            lista.setAdapter(actualizar());
                            Toast.makeText(getApplicationContext(), "Articulo devuelto", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


                ADB.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Articulo aun no devuelto", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog AD = ADB.create();
                AD.show();

            }
        });
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formatteDate = df.format(date);
        return formatteDate;
    }


    int a, d, m;

    public Boolean Fecha(String f) {
        Boolean b = false;
        int Mes[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (f.length() == 0) {
            Toast.makeText(Lista.this, "Introduzca una fecha valida\n'DD/MM/AAAA'", Toast.LENGTH_SHORT).show();
            b = false;
            return b;
        } else if (f.length() != 10) {
            Toast.makeText(Lista.this, "Introduzca una fecha valida\n'DD/MM/AAAA'", Toast.LENGTH_SHORT).show();
            b = false;
            return b;
        } else {
            String A[] = f.split("/");
            if (A.length != 3) {
                Toast.makeText(Lista.this, "Introduzca una fecha valida\n'DD/MM/AAAA'", Toast.LENGTH_SHORT).show();
                b = false;
                return b;
            } else {

                try {
                    d = Integer.parseInt(A[0]);
                    m = Integer.parseInt(A[1]);
                    a = Integer.parseInt(A[2]);
                }/*try*/ catch (NumberFormatException e) {
                    Toast.makeText(Lista.this, "Introduzca una fecha valida\n'DD/MM/AAAA'\nSolo numeros", Toast.LENGTH_SHORT).show();
                    b = false;
                    return b;
                }

                if (a % 4 == 0) Mes[2]++;
                if (m < 1 || m > 12) {
                    Toast.makeText(Lista.this, "Mes invalido: 1-12", Toast.LENGTH_SHORT).show();
                    b = false;
                    return b;
                }
                if (d > Mes[m]) {
                    Toast.makeText(Lista.this, "el Mes: " + m + " del ciclo anual: " + a + " solo tiene: " + Mes[m] + " dias", Toast.LENGTH_SHORT).show();
                    b = false;
                    return b;
                }
                b = true;
            }/*else antes del try*/
        }/*String A[]=f.split("/");*/
        return b;
    }

    public ArrayAdapter actualizar() {
        BD.abrir();
        final String consultas[] = BD.leer();
        AA.clear();
        String consulta = "";
        for (int j = 0; j < consultas.length; j++) {
            consulta = consulta + consultas[j] + "";
        }

        final String[] celdas;
        celdas = consulta.split("blitzkriegs");

        j = 0;
        basefinal = new String[consultas.length];
        for (int i = 0; i < basefinal.length; i++) {

            basefinal[i] = "Registro: " + celdas[j + 1] + ".   \nNombre: " + celdas[j + 2] +
                    ".\nCelular: " + celdas[j + 3] +
                    ".\nDomicilio: " + celdas[j + 4] +
                    ".\nArticulo: " + celdas[j + 5] +

                    ".\nFecha Prestamo: " + celdas[j + 6] +
                    ".\nFecha Devolucion: " + celdas[j + 7] + "." +
                    ".\nDescripcion: " + celdas[j + 8] +
                    ".\nEstado:" + celdas[j + 9];
            j = j + 9;
        }

        for (int i = 0; i < basefinal.length; i++) {
            AA.add(basefinal[i]);
        }

        return AA;
    }

}
