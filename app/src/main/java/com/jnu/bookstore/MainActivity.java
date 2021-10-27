package com.jnu.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<book> bookitems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDate();

        RecyclerView mainRecycleView=findViewById(R.id.recycle_view_books);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mainRecycleView.setLayoutManager(layoutManager);
        mainRecycleView.setAdapter(new MyRecyclerViewAdapter(getbookitems()));
    }
    public void initDate(){
        bookitems= new ArrayList<book>();
        bookitems.add(new book("软件项目管理案例教程（第4版）",R.drawable.book_2));
        bookitems.add(new book("创新工程实践",R.drawable.book_no_name));
        bookitems.add(new book("信息安全数学基础（第2版）",R.drawable.book_1));
    }
    public List<book> getbookitems() {
        return bookitems;
    }


    private class MyRecyclerViewAdapter extends RecyclerView.Adapter {
        private List<book> bookitems;

        public MyRecyclerViewAdapter(List<book> bookitems) {
            this.bookitems=bookitems;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_item_holder, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder Holder, int position) {
            ViewHolder holder=(ViewHolder) Holder;
            holder.getImageView().setImageResource(bookitems.get(position).getCoverResourceId());
            holder.getTextView().setText(bookitems.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return bookitems.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
            private final ImageView imageView;
            private final TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);

                this.imageView=itemView.findViewById(R.id.image_view_book_cover);
                this.textView=itemView.findViewById(R.id.text_view_book_title);

                itemView.setOnCreateContextMenuListener(this);
            }

            public ImageView getImageView() {
                return imageView;
            }

            public TextView getTextView() {
                return textView;
            }


            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                int position=getAdapterPosition();
                MenuItem menuItemAdd=contextMenu.add(Menu.NONE,1,1,"Add"+position);
                MenuItem menuItemelete=contextMenu.add(Menu.NONE,2,2,"Delete"+position);

                menuItemAdd.setOnMenuItemClickListener(this);
                menuItemelete.setOnMenuItemClickListener(this);
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int position= getAdapterPosition();
                switch(menuItem.getItemId()){
                    case 1:
                        View dialagueView= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialogue_input_item,null);
                        AlertDialog.Builder alertDialogBuiler = new AlertDialog.Builder(MainActivity.this);
                        alertDialogBuiler.setView(dialagueView);

                        alertDialogBuiler.setPositiveButton("确定",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                EditText editName=dialagueView.findViewById(R.id.edit_text_name);
                                bookitems.add(position,new book(editName.getText().toString(),R.drawable.book_2));
                                MyRecyclerViewAdapter.this.notifyItemInserted(position);
                            }
                        });
                        alertDialogBuiler.setCancelable(false).setNegativeButton ("取消",new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alertDialogBuiler.create().show();;

                        break;
                    case 2:
                        bookitems.remove(position);
                        MyRecyclerViewAdapter.this.notifyItemRemoved(position);
                        break;
                }
                return false;
            }
        }
    }
}