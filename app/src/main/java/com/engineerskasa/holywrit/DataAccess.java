package com.engineerskasa.holywrit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataAccess {
    private SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private static DataAccess instance;


    private DataAccess(Context context){
        this.mSQLiteOpenHelper = new DatabaseHelper(context);
    }

    public static DataAccess getInstance(Context context){
        if(instance == null){
            instance = new DataAccess(context);
        }
        return instance;
    }

    public void open(){
        this.mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
    }

    public void close(){
        if(mSQLiteDatabase != null)
            this.mSQLiteDatabase.close();
    }

    public Bible getBibleInfo(){
        Bible bibleInfo = new Bible();
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * from bible", null);
        if(cursor.moveToFirst()) {
            bibleInfo.setIndex(cursor.getInt(0));
            bibleInfo.setName(cursor.getString(1));
            bibleInfo.setEdition(cursor.getString(2));
            bibleInfo.setBooks_no(cursor.getInt(3));
        }
        cursor.close();
        return  bibleInfo;
    }

    public List<Book> getAllBooks(){
        List<Book> bookList = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * from book", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Book book = new Book();
            book.setIndex(cursor.getInt(0));
            book.setOrder(cursor.getInt(1));
            book.setCaption(cursor.getString(2));
            book.setName(cursor.getString(3));
            book.setAuthor(cursor.getString(4));
            book.setTestament(cursor.getString(5));
            book.setGenre(cursor.getString(6));
            book.setChapters_no(cursor.getInt(7));
            book.setBible_FK(cursor.getInt(8));
            bookList.add(book);
            cursor.moveToNext();
        }

        cursor.close();
        return bookList;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<ArrayList<String>> refForOneVerse(String s){

        Map<String, Integer> bookOrders = new HashMap<String, Integer>();
        ArrayList<String> splitInput = new ArrayList<String>(Arrays.asList(s.split(";")));
        ArrayList<String> requestedVerses = new ArrayList<>();
        ArrayList<String> reqVerses = new ArrayList<>();
        ArrayList<ArrayList<String>> returnVerses = new ArrayList<>();
        String bookNameGlobal = "";
        String bookNameStored = "";
        int book = 0;
        int chapter = 0;
        int start_verse = 0;
        int end_verse = 0;
        int current_verse = 0;
        ArrayList<Integer> otherVerses = new ArrayList<>();
        String verse = "";
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * from book", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String bookName =  cursor.getString(3);
            int bookOrder = cursor.getInt(1);
            bookOrders.put(bookName, bookOrder);
            cursor.moveToNext();
        }
        cursor.close();
        for (String subs:splitInput) {
            boolean bookHasNumberBeforeName = Character.isDigit(subs.charAt(0));
            if(!bookHasNumberBeforeName){
                bookNameGlobal = subs.substring(0,3);
                for(Map.Entry<String, Integer> pair : bookOrders.entrySet()){
                    if(bookNameGlobal.toLowerCase().equals(pair.getKey().substring(0,3).toLowerCase())){
                        book = pair.getValue();
                        bookNameStored = pair.getKey();
                    }
                }
            }else{
                bookNameGlobal = subs.substring(0,4);
                for(Map.Entry<String, Integer> pair : bookOrders.entrySet()){
                   if(Character.isDigit(pair.getKey().charAt(0))){
                    if(bookNameGlobal.toLowerCase().equals(pair.getKey().substring(0,5).replaceAll("\\s","").toLowerCase())){
                        book = pair.getValue();
                        bookNameStored = pair.getKey();
                    }
                   }
                }
            }
                String minus = subs.replace(bookNameGlobal,"");
                ArrayList<String> chapterAndVerse = new ArrayList<String>(Arrays.asList(minus.trim().split(",")));

                ArrayList<String> splitChapterAndVerse = new ArrayList<String>(Arrays.asList(chapterAndVerse.get(0).split(":")));
                chapter = Integer.parseInt(splitChapterAndVerse.get(0));

                if(splitChapterAndVerse.size() > 1) {
                    ArrayList<String> splitVerses = new ArrayList<String>(Arrays.asList(splitChapterAndVerse.get(1).split("-")));
                    start_verse = Integer.parseInt(splitVerses.get(0));
                    if (splitVerses.size() > 1)
                        end_verse = Integer.parseInt(splitVerses.get(1));
                    if (chapterAndVerse.size() > 1)
                        otherVerses.add(Integer.valueOf(chapterAndVerse.get(1)));
                }

                Cursor cursorChapter = mSQLiteDatabase.rawQuery("SELECT * from chapter", null);
                cursorChapter.moveToFirst();
                while(!cursorChapter.isAfterLast()){
                    if((cursorChapter.getInt(1) == chapter) && (cursorChapter.getInt(3) == book)){
                        Cursor cursorGetVerses = mSQLiteDatabase.rawQuery("SELECT * from verse", null);
                        cursorGetVerses.moveToFirst();
                        while(!cursorGetVerses.isAfterLast()){
                            if(cursorGetVerses.getInt(4) == cursorChapter.getInt(0)) {
                                if(splitChapterAndVerse.size() == 1){
                                    reqVerses.add(bookNameStored + " " + chapter + ":" + cursorGetVerses.getInt(1));
                                    requestedVerses.add(cursorGetVerses.getString(2));
                                }else {
                                    if (end_verse != 0) {
                                        if ((cursorGetVerses.getInt(1) >= start_verse) && (cursorGetVerses.getInt(1) <= end_verse)) {
                                            reqVerses.add(bookNameStored + " " + chapter + ":" + cursorGetVerses.getInt(1));
                                            requestedVerses.add(cursorGetVerses.getString(2));
                                        }
                                    } else {
                                        if (cursorGetVerses.getInt(1) == start_verse) {
                                            reqVerses.add(bookNameStored + " " + chapter + ":" + cursorGetVerses.getInt(1));
                                            requestedVerses.add(cursorGetVerses.getString(2));
                                        }
                                    }
                                }
                            }
                            cursorGetVerses.moveToNext();
                        }

                        if(otherVerses.size() > 0) {
                            cursorGetVerses.moveToFirst();
                        while(!cursorGetVerses.isAfterLast()){
                            if(cursorGetVerses.getInt(4) == cursorChapter.getInt(0)) {
                                if (cursorGetVerses.getInt(1) == otherVerses.get(0)) {
                                    reqVerses.add(bookNameStored + " " + chapter + ":" + cursorGetVerses.getInt(1));
                                    requestedVerses.add(cursorGetVerses.getString(2));
                                    }
                                }
                            cursorGetVerses.moveToNext();
                            }
                        cursorGetVerses.close();
                        }
                    }
                    cursorChapter.moveToNext();
                }
                cursorChapter.close();

        }
        returnVerses.add(reqVerses);
        returnVerses.add(requestedVerses);
        return returnVerses;
    }

    public ArrayList<BookInfo> fetchBookInfo(){
        ArrayList<BookInfo> bookInfos = new ArrayList<>();
        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * from book", null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                BookInfo bookInfo = new BookInfo();
                bookInfo.setName(cursor.getString(3));
                bookInfo.setTestament(cursor.getString(5));
                bookInfo.setAuthor(cursor.getString(4));
                bookInfo.setCaption(cursor.getString(2));
                bookInfo.setChapters_no(cursor.getInt(7));
                bookInfo.setGenre(cursor.getString(6));
                bookInfo.setId(cursor.getInt(0));
                bookInfos.add(bookInfo);

                cursor.moveToNext();
            }
            cursor.close();
        }
        return bookInfos;
    }

    public ArrayList<BookInfo> searchInfo(String s){

        ArrayList<BookInfo> bookInfos = new ArrayList<>();
        if(fetchBookInfo().size() > 0){
            for(BookInfo BI: fetchBookInfo()){
                if(BI.getAuthor().toLowerCase().contains(s.toLowerCase()) || BI.getCaption().toLowerCase().contains(s.toLowerCase()) || BI.getGenre().toLowerCase().contains(s.toLowerCase()) || BI.getName().toLowerCase().contains(s.toLowerCase()) || BI.getTestament().toLowerCase().contains(s.toLowerCase())){
                    bookInfos.add(BI);
                }
            }
        }
        return bookInfos;
    }



    public ArrayList<ArrayList<String>> textModeSearch(String s){

        ArrayList<String> verses = new ArrayList<>();
        ArrayList<String> ref = new ArrayList<>();
        ArrayList<ArrayList<String>> returnList = new ArrayList<>();
        int verse = 0;
        int chapter = 0;
        String book = "";
        Map<String, Integer> bookOrders = new HashMap<String, Integer>();


        Cursor cursor = mSQLiteDatabase.rawQuery("SELECT * from book", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            String bookName =  cursor.getString(3);
            int bookOrder = cursor.getInt(1);
            bookOrders.put(bookName, bookOrder);
            cursor.moveToNext();
        }

        Cursor cursorVerse = mSQLiteDatabase.rawQuery("SELECT * from verse", null);
        cursorVerse.moveToFirst();
        while(!cursorVerse.isAfterLast()) {
            if((cursorVerse.getString(2).toLowerCase().contains(s.toLowerCase()))){
                verses.add(cursorVerse.getString(2));
                verse = cursorVerse.getInt(1);

                Cursor cursorChapter = mSQLiteDatabase.rawQuery("SELECT * from chapter", null);
                cursorChapter.moveToFirst();
                while(!cursorChapter.isAfterLast()) {
                if(cursorVerse.getInt(4) == cursorChapter.getInt(0)){
                    chapter = cursorChapter.getInt(1);

                    for(Map.Entry<String, Integer> pair : bookOrders.entrySet()){
                        if(pair.getValue() ==  cursorChapter.getInt(3))
                            book = pair.getKey();
                    }
                    ref.add(book + " " + chapter + ":" + verse);
                }
                    cursorChapter.moveToNext();
                }
            }
            verse = 0;
            chapter = 0;
            cursorVerse.moveToNext();
        }

        returnList.add(ref);
        returnList.add(verses);

        return returnList;
    }



}
