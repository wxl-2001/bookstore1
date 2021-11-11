package com.jnu.bookstore.data;

import android.content.Context;

import com.jnu.bookstore.MainActivity;
import com.jnu.bookstore.R;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DataBank {
    public static final String DATA_FILE_NAME = "data";
    private final Context context;
    List<book> bookList;

    public DataBank(Context context) {
        this.context=context;
    }

    public List<book> loadData() {
        bookList= new ArrayList<book>();
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(context.openFileInput(DATA_FILE_NAME));
            bookList = (ArrayList<book>) objectInputStream.readObject();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return bookList;
    }

    public void saveData() {
        ObjectOutputStream objectOutputStream=null;
        try{
            objectOutputStream = new ObjectOutputStream(context.openFileOutput(DATA_FILE_NAME, Context.MODE_PRIVATE));
            objectOutputStream.writeObject(bookList);
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (objectOutputStream != null) {
                    objectOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
