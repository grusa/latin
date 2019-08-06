package ru.ai.latin;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

/**
 * @author Sergey Grushin <sgrushin70@gmail.com>
 * @Version 1.0
 * MCV Controller
 */

public class VocabularyController {
    private List<String> books;
    private List<String> chapters;
    private List<Word> words;
    private Random random;
    FirebaseFirestore dataFB;
    CollectionReference queryFB;
    FirebaseAuth firebaseAuth;
    Source source;
    final String  TAG = "CON";//"VocabularyController";

    private VocabularyModel model;
    public class Word {
        int id;
        String latin;
        String rus;
        String wrong1;
        String wrong2;
        String wrong3;

        public Word(int id, String latin, String rus, String wrong1, String wrong2, String wrong3) {
            this.id = id;
            this.latin=latin;
            this.rus = rus;
            this.wrong1=wrong1;
            this.wrong2 = wrong2;
            this.wrong3=wrong3;
        }
    }
    public class Book {
        int idlevel;
        String nameeng;
        String namerus;
        public Book(){}
        public Book (int idlevel,String nameeng,String namerus) {
            this.idlevel=idlevel;
            this.nameeng=nameeng;
            this.namerus=namerus;
        }
    }
    public VocabularyController (Context ctx) {
        books = new ArrayList<String>();
        chapters = new ArrayList<String>();
        words = new ArrayList<Word>();
        model = new VocabularyModel(ctx);
    }

    /**
     * Return Names of books
     * <p>Return Names of Books for the MainActivities ListView </p>
     * @return list of russian name of books
     */
    //TODO eng names
    public List<String> getBooks() {
        Cursor c = model.getBooks();
        books.clear();
        if (c != null) {
          c.moveToFirst();
          while (c.isAfterLast() == false) {
          books.add(c.getString(0));
          Log.d(TAG,c.getString(0));
          c.moveToNext(); }
          c.close(); }
        return books;
    }

    public List<String> getChapters(int id_book) {
        chapters.clear();
        Cursor c = model.getChapter(id_book);
        if (c != null) {
            c.moveToFirst();
            while (c.isAfterLast() == false) {
                chapters.add("Глава "+c.getString(0));
                Log.d(TAG,c.getString(0));
                c.moveToNext(); }
            c.close();} else {chapters.add("Оглавление отсутствует");}
        return chapters;
    }

    private List<Word> getWords(int id_book,int id_chapter) {
        int j;
        Cursor c = model.getWords(id_book,id_chapter);
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

    public boolean synchronizeDB (int bookid){
        Log.v(TAG,"Connect to FB");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInAnonymously()
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInAnonymously:failure", task.getException());
                        }

                        // ...
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
                            for (QueryDocumentSnapshot doc :task.getResult()) {
                                Log.d(TAG, doc.getId() + " " + doc.getData() + " " +
                                        doc.getString("namerus"));
                                model.putBook(Math.toIntExact(doc.getLong("idlevel")),doc.getString("nameeng"),
                                        doc.getString("namerus"));
                            }
                        } else {
                            Log.d(TAG,"No data found",task.getException());
                        }
                    }
                });
        dataFB.collection("words")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc:task.getResult()) {
                                Log.d(TAG,doc.getId() + " " + doc.getData());
                                model.putWord(doc.getString("namelat"),
                                        doc.getString("nameeng"),
                                        doc.getString("namerus"),
                                        Math.toIntExact(doc.getLong("bookid")),
                                        Math.toIntExact(doc.getLong("chapterid")));
                            }
                        }
                    }
                });
       return true;
    }

    public boolean close() {
        if (model.stop()) return true;
        else return false;
    }
}

