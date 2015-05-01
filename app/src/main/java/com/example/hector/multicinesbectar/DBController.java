package com.example.hector.multicinesbectar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
        "ImgPelicula TEXT, Titulo TEXT, Director TEXT,Interpretes TEXT, Genero Text, Duracion TEXT, Anyo TEXT," +
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

        String queryCompras= "CREATE TABLE Compras (IdCompra integer primary key AUTOINCREMENT,NumeroEntradas integer,PrecioTotal REAl," +
        "IdUsuario integer,IdEntrada integer,FOREIGN KEY (IdCompra) references Proyecciones (IdProyeccion))";
        database.execSQL(queryCompras);
        Log.d(LOGCAT,"Compras Created");

        String queryEntradas = "CREATE TABLE Entradas (IdEntrada integer primary key AUTOINCREMENT,IdCompra integer,NombrePelicula TEXT," +
        "Fecha TEXT,PrecioEntrada REAL, NumeroSala integer,NumeroButaca integer,FOREIGN KEY (IdCompra) references Compras (IdCompra))";
        database.execSQL(queryEntradas);
        Log.d(LOGCAT,"Entradas Created");

        String queryUsuarios = "CREATE TABLE Usuarios (IdUsuario integer primary key AUTOINCREMENT,DNI TEXT,ImgUsuario TEXT," +
        "Nombre TEXT, Apellidos TEXT, Email TEXT, UserName TEXT, Pass TEXT, T_Credito integer,FOREIGN KEY (IdUsuario) references Compras (IdCompra))";
        database.execSQL(queryUsuarios);
        Log.d(LOGCAT,"Usuarios Created");

    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Coches";
        database.execSQL(query);
        onCreate(database);
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
        String selectQuery = "select Peliculas.IdPelicula,ImgPelicula,Titulo,Director,Interpretes," +
        "Genero,Duracion,Anyo,NombreCine,Hora from Peliculas,Proyecciones,Cines where Proyecciones.IdCine = Cines.IdCine " +
        "and Proyecciones.IdPelicula=Peliculas.IdPelicula;";
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

    public ArrayList<HashMap<String, String>> getIrYa(){
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "select Peliculas.IdPelicula,ImgPelicula,Titulo,Director,Interpretes," +
        "Genero,Duracion,Anyo,NombreCine,Hora from Peliculas,Proyecciones,Cines where Proyecciones.IdCine = Cines.IdCine " +
        "and Proyecciones.IdPelicula=Peliculas.IdPelicula and Date('now')=Proyecciones.Dia and Proyecciones.Hora>=time('now');";
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

    public int updateCoche(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idfoto", queryValues.get("idfoto"));
        values.put("matricula", queryValues.get("matricula"));
        values.put("marca", queryValues.get("marca"));
        values.put("modelo", queryValues.get("modelo"));
        values.put("motorizacion", queryValues.get("motorizacion"));
        values.put("cilindrada", queryValues.get("cilindrada"));
        values.put("fechaCompra", queryValues.get("fechaCompra"));
        return database.update("Coches", values, "id" + " = ?", new String[] { queryValues.get("id") });
    }

    public void deleteAllCoches() {
        Log.d(LOGCAT,"deleteAll");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Coches";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }

    public void deleteCoche(String id) {
        Log.d(LOGCAT,"delete");
        SQLiteDatabase database = this.getWritableDatabase();
        String deleteQuery = "DELETE FROM Coches where id='"+ id +"'";
        Log.d("query",deleteQuery);
        database.execSQL(deleteQuery);
    }


    public HashMap<String, String> getCocheInfo(String id) {
        HashMap<String, String> wordList = new HashMap<String, String>();
        SQLiteDatabase database = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM Coches where id='"+id+"'";
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                wordList.put("idfoto", cursor.getString(1));
                wordList.put("matricula", cursor.getString(2));
                wordList.put("marca", cursor.getString(3));
                wordList.put("modelo", cursor.getString(4));
                wordList.put("motorizacion", cursor.getString(5));
                wordList.put("cilindrada", cursor.getString(6));
                wordList.put("fechaCompra", cursor.getString(7));
            } while (cursor.moveToNext());
        }
        return wordList;
    }

    public void GenerarXMl(ArrayList<HashMap<String, String>> map){
        int i=0;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();

            doc.setXmlVersion("1.0");
            Element raiz = doc.createElement("Coches");
            doc.appendChild(raiz);

            while(i<map.size()) {
            Element coche = doc.createElement("coche");

                Element idfoto = doc.createElement("idfoto");
                Text textidfoto = doc.createTextNode(map.get(i).get("idfoto"));
                idfoto.appendChild(textidfoto);
                coche.appendChild(idfoto);

                Element matricula = doc.createElement("matricula");
                Text textmatricula = doc.createTextNode(map.get(i).get("matricula"));
                matricula.appendChild(textmatricula);
                coche.appendChild(matricula);

                Element marca = doc.createElement("marca");
                Text textmarca = doc.createTextNode(map.get(i).get("marca"));
                marca.appendChild(textmarca);
                coche.appendChild(marca);

                Element modelo = doc.createElement("modelo");
                Text textmodelo = doc.createTextNode(map.get(i).get("modelo"));
                modelo.appendChild(textmodelo);
                coche.appendChild(modelo);

                Element motorizacion = doc.createElement("motorizacion");
                Text textmotorizacion = doc.createTextNode(map.get(i).get("motorizacion"));
                motorizacion.appendChild(textmotorizacion);
                coche.appendChild(motorizacion);

                Element cilindrada = doc.createElement("cilindrada");
                Text textcilindrada = doc.createTextNode(map.get(i).get("cilindrada"));
                cilindrada.appendChild(textcilindrada);
                coche.appendChild(cilindrada);

                Element fechaCompra = doc.createElement("fechaCompra");
                Text textfechaCompra = doc.createTextNode(map.get(i).get("fechaCompra"));
                fechaCompra.appendChild(textfechaCompra);
                coche.appendChild(fechaCompra);

                doc.getDocumentElement().appendChild(coche);
                i++;
            }
            Source source = new DOMSource(doc);
            Result result = new StreamResult(new File("/data/data/com.example.hector.crud/files/CochesGenerate.xml"));

            // Transformación del Document al fichero
            Transformer trans = TransformerFactory.newInstance().newTransformer();
            trans.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
