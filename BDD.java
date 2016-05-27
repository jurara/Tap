import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.provider.BaseColumns._ID;

/**
 * Created by jurara on 11/05/2016.
 */
public class BDD extends SQLiteOpenHelper {

    public BDD(Context c) {
        super(c, "BD", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String pres = "CREATE TABLE Prestados(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Nombre TEXT,Celular TEXT,Domicilio TEXT, Articulo TEXT, FechaPrestamo TEXT, FechaDevolucion TEXT, descripcion TEXT, Estado TEXT);";

        String art = "CREATE TABLE Articulos(" + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + "Articulo TEXT);";

        db.execSQL(art);

        art = "INSERT INTO articulos VALUES(1,'Seleccione Articulo');";
        db.execSQL(art);
        art = "INSERT INTO articulos VALUES(2,'Libro');";
        db.execSQL(art);
        art = "INSERT INTO articulos VALUES(3,'Dinero');";
        db.execSQL(art);
        db.execSQL(pres);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST Prestados");
        onCreate(db);
        db.execSQL("DROP TABLE IF EXIST Articulos");
        onCreate(db);
    }


    public void insertarconsulta(String Nombre, String Celular, String Domicilio, String Articulo, String FechaPrestamo, String FechaDevolucion, String descripcion) {
        ContentValues cv = new ContentValues();
        cv.put("Nombre", Nombre);
        cv.put("Celular", Celular);
        cv.put("Domicilio", Domicilio);
        cv.put("Articulo", Articulo);
        cv.put("FechaPrestamo", FechaPrestamo);
        cv.put("FechaDevolucion", FechaDevolucion);
        cv.put("descripcion", descripcion);
        cv.put("Estado", " No devuelto");

        this.getReadableDatabase().insert("Prestados", null, cv);
    }

    public void insertarart(String Articulo) {
        ContentValues cv = new ContentValues();
        cv.put("Articulo", Articulo);
        this.getWritableDatabase().insert("Articulos", null, cv);
    }

    public void eliminarArticulos(String Articulo) {
        this.getWritableDatabase().delete("articulos", "Articulo=" + "'" + Articulo + "'", null);
    }

    public String[] leer() {
        String cad[];
        String conjunto = "";
        String[] columnas = {_ID, "Nombre", "Celular", "Domicilio", "Articulo", "FechaPrestamo", "FechaDevolucion", "descripcion", "Estado"};
        Cursor cursor = this.getReadableDatabase().query("Prestados", columnas, null, null, null, null, null);
        cursor.moveToFirst();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            conjunto = conjunto +
                    "blitzkriegs" + cursor.getString(0) +
                    "blitzkriegs" + cursor.getString(1) +
                    "blitzkriegs" + cursor.getString(2) +
                    "blitzkriegs" + cursor.getString(3) +
                    "blitzkriegs" + cursor.getString(4) +
                    "blitzkriegs" + cursor.getString(5) +
                    "blitzkriegs" + cursor.getString(6) +
                    "blitzkriegs" + cursor.getString(7) +
                    "blitzkriegs" + cursor.getString(8) + "\n";
        }
        cad = conjunto.split("\n");
        return cad;

    }

    public void abrir() {
        this.getReadableDatabase();
    }

    public void cerrar() {
        this.close();
    }

    public String[] leerart() {
        String cad[];
        String cads = "";
        String[] columnas = {"Articulo"};
        Cursor cursor = this.getReadableDatabase().query("Articulos", columnas, null, null, null, null, null);
        cursor.moveToFirst();
        int arti = cursor.getColumnIndex("Articulo");
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            cads = cads + cursor.getString(arti) + "\n";
        }
        cad = cads.split("\n");
        return cad;

    }


    public void editar(int id, String fde) {
        SQLiteDatabase sqld = this.getWritableDatabase();
        ContentValues CV = new ContentValues();
        CV.put("Estado", " Devuelto");
        CV.put("FechaDevolucion", fde + "");
        sqld.update("Prestados", CV, "_ID=" + (id + 1), null);
    }


}
