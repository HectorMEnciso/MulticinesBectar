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
        String queryCines = "CREATE TABLE Cines (IdCine integer PRIMARY KEY AUTOINCREMENT,Direccion TEXT, NombreCine TEXT)";
        database.execSQL(queryCines);
        Log.d(LOGCAT,"Cines Created");

        String queryPeliculas = "CREATE TABLE Peliculas (IdPelicula integer PRIMARY KEY AUTOINCREMENT,Titulo TEXT, Director TEXT,Interpretes TEXT, Genero Text, Duracion TEXT, Anyo TEXT)";
        database.execSQL(queryPeliculas);
        Log.d(LOGCAT,"Peliculas Created");
    }
    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS Coches";
        database.execSQL(query);
        onCreate(database);
    }

    public void insertCoche(HashMap<String, String> queryValues ) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("idfoto", queryValues.get("idfoto"));
        values.put("matricula", queryValues.get("matricula"));
        values.put("marca", queryValues.get("marca"));
        values.put("modelo", queryValues.get("modelo"));
        values.put("motorizacion", queryValues.get("motorizacion"));
        values.put("cilindrada", queryValues.get("cilindrada"));
        values.put("fechaCompra", queryValues.get("fechaCompra"));
        database.insert("Coches", null, values);
        database.close();
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

    public ArrayList<HashMap<String, String>> getAllCoches() {
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM Coches";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("idfoto", cursor.getString(1));
                map.put("matricula", cursor.getString(2));
                map.put("marca", cursor.getString(3));
                map.put("modelo", cursor.getString(4));
                map.put("motorizacion", cursor.getString(5));
                map.put("cilindrada", cursor.getString(6));
                map.put("fechaCompra", cursor.getString(7));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        return wordList;
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
    public boolean existeCoche (String matricula){
        boolean existe=false;
        String selectQuery = "SELECT * FROM Coches where matricula='" + matricula+"'";
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
