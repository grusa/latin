package ru.ai.latin;
/**
 *  * @author Sergey Grushin <sgrushin70@gmail.com>
 *  * @version     1.0
 *  * @since
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * MVC Model class
 * <p>
 *
 * </p>
 */
final class VocabularyModel {
    final private String TAG = "ML";
    public static final String DB_NAME = "vocdb";
    public static final int DB_VERSION = 1;
    SQLiteOpenHelper helper;
    SQLiteDatabase db;

    public VocabularyModel(final Context ctx) {
        this.helper = new SQLiteOpenHelper(ctx, VocabularyModel.DB_NAME, null,
                VocabularyModel.DB_VERSION) {
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL("DROP TABLE IF EXISTS levels;");
                db.execSQL("CREATE TABLE levels (_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + "namerus TEXT NOT NULL,\n"
                        + "nameeng TEXT NOT NULL);");
                        //"route_id INTEGER(4),\n"
                        //"\tFOREIGN KEY (route_id) REFERENCES routes(id));");

                db.execSQL("INSERT INTO levels(namerus,nameeng) VALUES (?,?);",new String [] {"Простой","Easy"}); //Простой,Средний,Сложный
                db.execSQL("INSERT INTO levels(namerus,nameeng) VALUES (?,?);",new String [] {"Средний","Middle"});
                db.execSQL("INSERT INTO levels(namerus,nameeng) VALUES (?,?);",new String [] {"Сложный","Complicated"});
                db.execSQL("DROP TABLE IF EXISTS books;");
                db.execSQL("CREATE TABLE books (_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + "namerus TEXT NOT NULL,\n"
                        + "nameeng TEXT NOT NULL,\n"
                        + "level_id INTEGER NOT NULL);");
                db.execSQL("DROP TABLE IF EXISTS users;");
                db.execSQL("CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + "mail TEXT);");
                db.execSQL("DROP TABLE IF EXISTS words;");
                db.execSQL("CREATE TABLE words (id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + "namelat TEXT NOT NULL,\n"
                        + "namerus TEXT NOT NULL,\n"
                        + "nameeng TEXT,\n"
                        + "namedu TEXT,\n"
                        + "bookid integer,\n"
                        + "chapterid integer);");
                db.execSQL("DROP TABLE IF EXISTS ub;");
                db.execSQL("CREATE TABLE ub (id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                        + "book_id INTEGER NOT NULL,\n"
                        + "user_id INTEGER NOT NULL);\n");
                //TODO indexes
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            }
            @Override
            public synchronized void close() {
                super.close();
            }
        };
        this.db = this.helper.getReadableDatabase();
    }

    public Cursor getBooks() {
        Cursor c = this.db.rawQuery("SELECT namerus FROM books;",null);
        return c;
    }
    public Cursor getChapter(int idbook) {
        idbook++;
        Cursor c = this.db.rawQuery("SELECT DISTINCT chapterid FROM words  WHERE " +
                "bookid = ? ORDER BY chapterid;",new String [] {Integer.toString(idbook)});
        return c;
    }
    public boolean putBook(int idlevel,String nameeng,String namerus) {
        try {
            db.beginTransaction();
            db.execSQL("INSERT INTO books(namerus,nameeng,level_id) VALUES(?,?,?);",
                    new String[]{namerus, nameeng, Integer.toString(idlevel)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG,e.toString());
        } finally {
            db.endTransaction();
        }
        return true;
    }
    public boolean stop(){
        this.helper.close();
        return true;
    }
    public Cursor getWords(int id_book,int id_chapter) {
        id_book++;id_chapter++; //TODO position to book_id
        final Cursor c = this.db.rawQuery("SELECT id,namelat,namerus FROM words where bookid =?" +
                "AND chapterid = ?;",new String[] {Integer.toString(id_book),Integer.toString(id_chapter)});
        return  c;
    }
    public boolean putWord (String namelat,String nameeng,String namerus,int bookid, int chapterid) {
        try {
            db.beginTransaction();
            db.execSQL("INSERT INTO words (namelat,nameeng,namerus,bookid,chapterid) VALUES(?,?,?,?,?);",
                    new String[]{namelat, nameeng, namerus, Integer.toString(bookid), Integer.toString(chapterid)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG,e.toString());
        } finally {
            db.endTransaction();
        }
        return true;
    }

}

