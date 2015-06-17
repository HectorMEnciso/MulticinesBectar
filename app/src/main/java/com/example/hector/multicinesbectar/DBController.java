package com.example.hector.multicinesbectar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Hector on 21/01/2015.
 */
public class DBController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;

    public DBController(Context applicationcontext) {
        super(applicationcontext, "DBMulticines.db", null, 1);
        Log.d(LOGCAT, "DBMulticines.db creada");
    }
    @Override
    public void onCreate(SQLiteDatabase database) {

        String queryProyecciones = "CREATE TABLE Proyecciones (IdProyeccion integer PRIMARY KEY,IdCine integer," +
        "IdPelicula integer,IdButaca integer,IdSala integer,Hora time,Dia date,ButacasDisponibles integer," +
        "IdCompra integer)";
        database.execSQL(queryProyecciones);
        Log.d(LOGCAT,"Proyecciones Created");

        String queryPeliculas = "CREATE TABLE Peliculas (IdPelicula integer PRIMARY KEY AUTOINCREMENT," +
        "ImgPelicula TEXT, Titulo TEXT, Director TEXT,Interpretes TEXT, Genero Text, Duracion TEXT, Anyo TEXT,Trailer TEXT,Sinopsis TEXT," +
        "FOREIGN KEY (IdPelicula) references Proyecciones(IdProyeccion))";
        database.execSQL(queryPeliculas);
        Log.d(LOGCAT,"Peliculas Created");

        String queryCines = "CREATE TABLE Cines (IdCine integer PRIMARY KEY,ImgCine TEXT, Direccion TEXT, NombreCine TEXT," +
        "FOREIGN KEY (IdCine) references Proyecciones (IdProyeccion))";
        database.execSQL(queryCines);
        Log.d(LOGCAT,"Cines Created");

        String querySalas = "CREATE TABLE Salas (IdSala integer PRIMARY KEY,NumeroSala integer,NumeroFilas integer,NumeroButacas integer," +
        "FOREIGN KEY (IdSala) references Proyecciones (IdProyeccion))";
        database.execSQL(querySalas);
        Log.d(LOGCAT,"Salas Created");

        String queryButacas= "CREATE TABLE Butacas (IdButaca integer primary key AUTOINCREMENT,NumeroButaca integer," +
        "NumeroFila integer, Estado TEXT, Tipo TEXT," +
        "FOREIGN KEY (IdButaca) references Proyecciones (IdProyeccion))";
        database.execSQL(queryButacas);
        Log.d(LOGCAT,"Butacas Created");

        String queryUsuarios = "CREATE TABLE Usuarios (IdUsuario integer primary key AUTOINCREMENT,DNI TEXT,ImgUsuario TEXT," +
                "Nombre TEXT, Apellidos TEXT, Email TEXT, UserName TEXT, Pass TEXT, T_Credito integer)";
        database.execSQL(queryUsuarios);
        Log.d(LOGCAT,"Usuarios Created");

        String queryCompras= "CREATE TABLE Compras (IdCompra integer primary key AUTOINCREMENT,NumeroEntradas integer,PrecioTotal REAl," +
        "IdUsuario integer,IdEntrada integer,FOREIGN KEY (IdCompra) references Proyecciones (IdProyeccion),FOREIGN KEY (IdUsuario) references Usuarios (IdUsuario))";
        database.execSQL(queryCompras);
        Log.d(LOGCAT,"Compras Created");

        String queryEntradas = "CREATE TABLE Entradas (IdEntrada integer primary key AUTOINCREMENT,IdCompra integer,NombrePelicula TEXT," +
        "Fecha TEXT,PrecioEntrada REAL, NumeroSala integer,NumeroButaca integer,FOREIGN KEY (IdCompra) references Compras (IdCompra))";
        database.execSQL(queryEntradas);
        Log.d(LOGCAT,"Entradas Created");

        String queryValoraciones= "CREATE TABLE Valoraciones (IdValoracion integer primary key AUTOINCREMENT,IdUsuario integer,IdPelicula integer,UserName TEXT," +
        "TextoValoracion TEXT,ValorRatingBar REAL,FOREIGN KEY (IdUsuario) references Usuarios (IdUsuario),FOREIGN KEY (IdPelicula) references Peliculas (IdPelicula))";
        database.execSQL(queryValoraciones);
        Log.d(LOGCAT,"Valoraciones Created");

    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String queryProyecciones;
        queryProyecciones = "DROP TABLE IF EXISTS Proyecciones";
        database.execSQL(queryProyecciones);

        String queryPeliculas;
        queryPeliculas = "DROP TABLE IF EXISTS Peliculas";
        database.execSQL(queryPeliculas);

        String queryCines;
        queryCines = "DROP TABLE IF EXISTS Cines";
        database.execSQL(queryCines);

        String querySalas;
        querySalas = "DROP TABLE IF EXISTS Salas";
        database.execSQL(querySalas);

        String queryButacas;
        queryButacas = "DROP TABLE IF EXISTS Butacas";
        database.execSQL(queryButacas);

        String queryCompras;
        queryCompras = "DROP TABLE IF EXISTS Compras";
        database.execSQL(queryCompras);

        String queryEntradas;
        queryEntradas = "DROP TABLE IF EXISTS Entradas";
        database.execSQL(queryEntradas);

        String queryUsuarios;
        queryUsuarios = "DROP TABLE IF EXISTS Usuarios";
        database.execSQL(queryUsuarios);


        onCreate(database);
    }

    public boolean existeValoracion (int id){
        boolean existe=false;
        String selectQuery = "SELECT * FROM Valoraciones where IdValoracion='" + id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            existe=true;
        }
        else {
            existe = false;
        }
        return existe;
    }


    public ArrayList<HashMap<String, String>> getValoracionesByIdPelicula(String IdPelicula){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select IdValoracion,TextoValoracion,ValorRatingBar,Valoraciones.UserName from Valoraciones,Peliculas  where Valoraciones.IdPelicula=Peliculas.IdPelicula " +
                "and Peliculas.IdPelicula='"+IdPelicula+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("IdValoracion", cursor.getString(0));
                map.put("TextoValoracion", cursor.getString(1));
                map.put("ValorRatingBar", cursor.getString(2));
                map.put("UserName", cursor.getString(3));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }


    public ArrayList<HashMap<String, String>> getAllCines() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Cines";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("IdCine", cursor.getString(0));
                map.put("ImgCine", cursor.getString(1));
                map.put("Direccion", cursor.getString(2));
                map.put("NombreCine", cursor.getString(3));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }



    public void insertValoracion(HashMap<String, String> queryValues ) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdValoracion", queryValues.get("IdValoracion"));
        values.put("IdUsuario", queryValues.get("IdUsuario"));
        values.put("IdPelicula", queryValues.get("IdPelicula"));
        values.put("UserName", queryValues.get("UserName"));
        values.put("TextoValoracion", queryValues.get("TextoValoracion"));
        values.put("ValorRatingBar", queryValues.get("ValorRatingBar"));
        database.insert("Valoraciones", null, values);
        database.close();
    }


    public void insertCine(HashMap<String, String> queryValues ) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdCine", queryValues.get("IdCine"));
        values.put("ImgCine", queryValues.get("ImgCine"));
        values.put("Direccion", queryValues.get("Direccion"));
        values.put("NombreCine", queryValues.get("NombreCine"));
        database.insert("Cines", null, values);
        database.close();
    }

    public boolean existeCine (String nombre){
        boolean existe=false;
        String selectQuery = "SELECT * FROM Cines where NombreCine='" + nombre+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            existe=true;
        }
        else {
            existe = false;
        }
        return existe;
    }

    public ArrayList<HashMap<String, String>> getAllPeliculas() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select distinct Peliculas.IdPelicula,ImgPelicula,Titulo, " +
        "Genero,Duracion from Peliculas,Proyecciones where Proyecciones.IdPelicula=Peliculas.IdPelicula;";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("IdPelicula", cursor.getString(0));
                map.put("ImgPelicula", cursor.getString(1));
                map.put("Titulo", cursor.getString(2));
                map.put("Genero", cursor.getString(3));
                map.put("Duracion", cursor.getString(4));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getAllPeliculasByCineId(String IdCine){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select distinct Peliculas.IdPelicula,ImgPelicula,Titulo, " +
                "Genero,Duracion,NombreCine from Peliculas,Proyecciones,Cines where Proyecciones.IdPelicula=Peliculas.IdPelicula and Proyecciones.IdCine=Cines.IdCine and Cines.IdCine='"+IdCine+"';";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("IdPelicula", cursor.getString(0));
                map.put("ImgPelicula", cursor.getString(1));
                map.put("Titulo", cursor.getString(2));
                map.put("Genero", cursor.getString(3));
                map.put("Duracion", cursor.getString(4));
                map.put("NombreCine", cursor.getString(5));
                //map.put("Sinopsis", cursor.getString(6));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public ArrayList<HashMap<String, String>> getIrYa(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select distinct Peliculas.IdPelicula,ImgPelicula,Titulo,Director,Interpretes," +
        "Genero,Duracion,Anyo,NombreCine,Hora from Peliculas,Proyecciones,Cines where Proyecciones.IdCine = Cines.IdCine " +
        "and Proyecciones.IdPelicula=Peliculas.IdPelicula and Date('now')=Proyecciones.Dia and Proyecciones.Hora>=time('now','localtime');";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("IdPelicula", cursor.getString(0));
                map.put("ImgPelicula", cursor.getString(1));
                map.put("Titulo", cursor.getString(2));
                map.put("Director", cursor.getString(3));
                map.put("Interpretes", cursor.getString(4));
                map.put("Genero", cursor.getString(5));
                map.put("Duracion", cursor.getString(6));
                map.put("Anyo", cursor.getString(7));
                map.put("NombreCine", cursor.getString(8));
                map.put("Hora", cursor.getString(9));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public void insertPelicula(HashMap<String, String> queryValues ) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdPelicula", queryValues.get("IdPelicula"));
        values.put("ImgPelicula", queryValues.get("ImgPelicula"));
        values.put("Titulo", queryValues.get("Titulo"));
        values.put("Director", queryValues.get("Director"));
        values.put("Interpretes", queryValues.get("Interpretes"));
        values.put("Genero", queryValues.get("Genero"));
        values.put("Duracion", queryValues.get("Duracion"));
        values.put("Anyo", queryValues.get("Anyo"));
        values.put("Trailer", queryValues.get("Trailer"));
        values.put("Sinopsis", queryValues.get("Sinopsis"));
        database.insert("Peliculas", null, values);
        database.close();
    }

    public boolean existePelicula(String Titulo){
        boolean existe=false;
        String selectQuery = "SELECT * FROM Peliculas where Titulo='" + Titulo+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            existe=true;
        }
        else {
            existe = false;
        }
        return existe;
    }

    public void insertProyeccion(HashMap<String, String> queryValues ) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdProyeccion", queryValues.get("IdProyeccion"));
        values.put("IdCine", queryValues.get("IdCine"));
        values.put("IdPelicula", queryValues.get("IdPelicula"));
        values.put("IdButaca", queryValues.get("IdButaca"));
        values.put("IdSala", queryValues.get("IdSala"));
        values.put("Hora", queryValues.get("Hora"));
        values.put("Dia", queryValues.get("Dia"));
        values.put("ButacasDisponibles", queryValues.get("ButacasDisponibles"));
        values.put("IdCompra", queryValues.get("IdCompra"));
        database.insert("Proyecciones", null, values);
        database.close();
    }

    public boolean existeProyeccion(int IdProyeccion){
        boolean existe=false;
        String selectQuery = "SELECT * FROM Proyecciones where IdProyeccion='" + IdProyeccion+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            existe=true;
        }
        else {
            existe = false;
        }
        return existe;
    }

    public void deleteAll() {
        Log.d(LOGCAT, "deleteAll");
        SQLiteDatabase database = this.getWritableDatabase();

        String deleteProyecciones = "DELETE FROM Proyecciones";
        Log.d("deleteProyecciones", deleteProyecciones);
        database.execSQL(deleteProyecciones);

        String deletePeliculas = "DELETE FROM Peliculas";
        Log.d("deletePeliculas", deletePeliculas);
        database.execSQL(deletePeliculas);

        String deleteCines = "DELETE FROM Cines";
        Log.d("deleteCines", deleteCines);
        database.execSQL(deleteCines);

        String deleteSalas = "DELETE FROM Salas";
        Log.d("deleteSalas", deleteSalas);
        database.execSQL(deleteSalas);

        String deleteButacas = "DELETE FROM Butacas";
        Log.d("deleteButacas", deleteButacas);
        database.execSQL(deleteButacas);

        String deleteEntradas = "DELETE FROM Entradas";
        Log.d("deleteEntradas", deleteEntradas);
        database.execSQL(deleteEntradas);

        String deleteUsuarios = "DELETE FROM Usuarios";
        Log.d("deleteUsuarios", deleteUsuarios);
        database.execSQL(deleteUsuarios);

        String deleteCompras = "DELETE FROM Compras";
        Log.d("deleteCompras", deleteCompras);
        database.execSQL(deleteCompras);

    }

    public ArrayList<HashMap<String, String>> getPeliculainfo(String id) {
        ArrayList<HashMap<String, String>> wordList = new  ArrayList<HashMap<String, String>>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "select Peliculas.IdPelicula,ImgPelicula,Titulo,Director,Interpretes," +
        "Genero,Duracion,Anyo,NombreCine,Hora,Proyecciones.IdProyeccion,Salas.NumeroSala,Trailer,Sinopsis,Dia from Peliculas,Proyecciones,Cines,Salas where Proyecciones.IdCine = Cines.IdCine " +
         "and Proyecciones.IdPelicula=Peliculas.IdPelicula and Proyecciones.IdSala=Salas.IdSala and Peliculas.IdPelicula='"+id+"' order by NombreCine";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("IdPelicula", cursor.getString(0));
                map.put("ImgPelicula", cursor.getString(1));
                map.put("Titulo", cursor.getString(2));
                map.put("Director", cursor.getString(3));
                map.put("Interpretes", cursor.getString(4));
                map.put("Genero", cursor.getString(5));
                map.put("Duracion", cursor.getString(6));
                map.put("Anyo", cursor.getString(7));
                map.put("NombreCine", cursor.getString(8));
                map.put("Hora", cursor.getString(9));
                map.put("IdProyeccion", cursor.getString(10));
                map.put("NumeroSala", cursor.getString(11));
                map.put("Trailer", cursor.getString(12));
                map.put("Sinopsis", cursor.getString(13));
                map.put("Dia", cursor.getString(14));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }


    public ArrayList<HashMap<String, String>> getAllSalas() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT * FROM Salas";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("IdSala", cursor.getString(0));
                map.put("NumeroSala", cursor.getString(1));
                map.put("NumeroFilas", cursor.getString(2));
                map.put("NumeroButacas", cursor.getString(3));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
    }



    public void insertSala(HashMap<String, String> queryValues ) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("IdSala", queryValues.get("IdSala"));
        values.put("NumeroSala", queryValues.get("NumeroSala"));
        values.put("NumeroFilas", queryValues.get("NumeroFilas"));
        values.put("NumeroButacas", queryValues.get("NumeroButacas"));
        database.insert("Salas", null, values);
        database.close();
    }

    public boolean existeSala(int id){
        boolean existe=false;
        String selectQuery = "SELECT * FROM Salas where IdSala='" + id+"'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()){
            existe=true;
        }
        else {
            existe = false;
        }
        return existe;
    }
}
