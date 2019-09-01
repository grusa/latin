package ru.ai.latin;
/**
 *  * @author Sergey Grushin <sgrushin70@gmail.com>
 *  * @version     1.0
 *  * @since
 * <p>
 * MVC Model class
 * </p>
 */
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.UserDictionary;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public  class VocabularyModel {
    final private String TAG = "ML";
    public static final String DB_NAME = "vocdb";
    public static final int DB_VERSION = 1;
    SQLiteOpenHelper helper;
    SQLiteDatabase db;
    private ArrayList<String> booksAdd;
    private List<String> chapters;
    public List<Word> words;
    FirebaseFirestore dataFB;
    CollectionReference queryFB;
    FirebaseAuth firebaseAuth;
    Source source;
    private Random random;
    private Cursor c;

    public class Word {
        int id;
        String latin;
        String rus;
        String wrong1;
        String wrong2;
        String wrong3;

        public Word(int id, String latin, String rus, String wrong1, String wrong2, String wrong3) {
            this.id = id;
            this.latin = latin;
            this.rus = rus;
            this.wrong1 = wrong1;
            this.wrong2 = wrong2;
            this.wrong3 = wrong3;
        }
    }

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
        this.booksAdd = new ArrayList<String>();
        this.chapters = new ArrayList<String>();
        this.words = new ArrayList<Word>();
    }

    /*public Cursor getBooks() {
        final Cursor c = this.db.rawQuery("SELECT namerus FROM books;",null);

        return c;
    }*/
    public ArrayList<String> getBooks() {
        c = this.db.rawQuery("SELECT namerus FROM books;",null);
        booksAdd.clear();
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                booksAdd.add(c.getString(0));
                Log.d(TAG,c.getString(0));
                c.moveToNext();
            }
            } else {
                booksAdd.add("No data, please load dictionaries");
            }
        c.close();
        return booksAdd;
    }
    private List<VocabularyModel.Word> getWords(int id_book, int id_chapter) {
        int j;

        Cursor c = this.db.rawQuery("SELECT id,namelat,namerus FROM words where bookid =?" +
                "AND chapterid = ?;",new String[] {Integer.toString(id_book),Integer.toString(id_chapter)});
        Word w;
        List<String> wrong = new ArrayList<>();
        List<String> wrongSet;

        if (c != null) {
            c.moveToFirst();
            wrong.clear();
            words.clear();
            while (c.isAfterLast() == false) {
                w = new Word(c.getInt(0),c.getString(1),
                        c.getString(2),null,null,null);
                wrong.add(w.rus);
                words.add(w);
                c.moveToNext(); }
            for (int i=0;i<(words.size());i++){
                random = new Random();
                wrongSet =  new ArrayList<> (wrong);
                wrongSet.remove(i);
                w = new Word(words.get(i).id,words.get(i).latin,words.get(i).rus,
                        null,null,null);
                //set wrong1
                j = random.nextInt(wrongSet.size());
                w.wrong1 = wrongSet.get(j);
                wrongSet.remove(j);
                //set Wrong2
                j = random.nextInt(wrongSet.size());
                w.wrong2 = wrongSet.get(j);
                wrongSet.remove(j);
                //set wrong3
                j = random.nextInt(wrongSet.size());
                w.wrong3 = wrongSet.get(j);
                words.set(i,w);
            }
            c.close(); }
        return words;
    }
    public Word getWord(int id_book,int id_chapter) {

        Random random1 = new Random();
        List<Word> ws = this.getWords(id_book,id_chapter);
        Word w;
        w=ws.get(random1.nextInt(ws.size()));
        return w;
    }
    public int getBooksCount() {
        Cursor c = this.db.rawQuery("SELECT namerus FROM books;",null);
        int i = c.getColumnCount();
        c.close();
        return i;
    }
    /*public Cursor getChapter(int idbook) {
        idbook++;
        Cursor c = this.db.rawQuery("SELECT DISTINCT chapterid FROM words  WHERE " +
                "bookid = ? ORDER BY chapterid;",new String [] {Integer.toString(idbook)});
        return c;
    }*/
    public List<String> getChapters(int idbook) {
        chapters.clear();
        idbook = idbook+1;
        Cursor c = this.db.rawQuery("SELECT DISTINCT chapterid FROM words  WHERE " +
                "bookid = ? ORDER BY chapterid;",new String [] {Integer.toString(idbook)});
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                chapters.add("Глава "+c.getString(0));
                Log.d(TAG,c.getString(0));
                c.moveToNext(); }
            c.close();} else {chapters.add("Оглавление отсутствует");}
        return chapters;
    }
    public boolean putBook(int idlevel,String nameeng,String namerus) {
        try {
            db.beginTransaction();
            db.execSQL("INSERT INTO books(namerus,nameeng,level_id) VALUES(?,?,?);",
                    new String[]{namerus, nameeng, Integer.toString(idlevel)});
            Log.d(TAG,nameeng);
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
    /*public Cursor getWords(int id_book,int id_chapter) {
        id_book++;id_chapter++; //TODO position to book_id
        final Cursor c = this.db.rawQuery("SELECT id,namelat,namerus FROM words where bookid =?" +
                "AND chapterid = ?;",new String[] {Integer.toString(id_book),Integer.toString(id_chapter)});
        return  c;
    }*/
    public boolean putWord (String namelat,String nameeng,String namerus,int bookid, int chapterid) {
        try {
            db.beginTransaction();
            db.execSQL("INSERT INTO words (namelat,nameeng,namerus,bookid,chapterid) VALUES(?,?,?,?,?);",
                    new String[]{namelat, nameeng, namerus, Integer.toString(bookid), Integer.toString(chapterid)});
            Log.d(TAG,nameeng);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG,e.toString());
        } finally {
            db.endTransaction();
        }
        return true;
    }
    private void clearData(){
        db.execSQL("DELETE FROM books;");
        db.execSQL("DELETE FROM words;");
        Log.d(TAG,"Deleted books and words");
    }
    public void synchronizeDB (int bookid,final Context context) {
        Log.d(TAG, "Connect to FB");
        clearData();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(context,"Подключенно к базе данных",Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                            Toast.makeText(context,"Не могу подключится к базе данных",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        dataFB = FirebaseFirestore.getInstance();
        dataFB.collection("books")
//                .whereEqualTo("idlevel",1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Log.d(TAG, doc.getId() + " " + doc.getData() + " " +
                                        doc.getString("namerus"));
                                putBook(Math.toIntExact(doc.getLong("idlevel")),
                                        doc.getString("nameeng"),
                                        doc.getString("namerus"));
                                Toast.makeText(context,"Загруженны книги",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "No data found", task.getException());
                            Toast.makeText(context,"Невозможно загрузить книги",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        dataFB.collection("words")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                Log.d(TAG, doc.getId() + " " + doc.getData());
                                putWord(doc.getString("namelat"),
                                        doc.getString("nameeng"),
                                        doc.getString("namerus"),
                                        Math.toIntExact(doc.getLong("bookid")),
                                        Math.toIntExact(doc.getLong("chapterid")));
                                Toast.makeText(context,"Загруженны словари",Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context,"Словари не загруженны",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

