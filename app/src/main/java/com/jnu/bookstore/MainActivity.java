package com.jnu.bookstore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
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

    public static final int RESULT_CODE_ADD_DATA = 996;
    public static final int REQUEST_CODE_ADD = 123;
    public static final int REQUEST_CODE_EDIT =REQUEST_CODE_ADD+1;

    private List<book> bookitems;
    private MyRecyclerViewAdapter recyclerViewAdapter;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQUEST_CODE_ADD) {
            if (resultCode == RESULT_CODE_ADD_DATA) {
                String name = data.getStringExtra("name");
                int position=data.getIntExtra("position",bookitems.size());
                bookitems.add(position,new book(name, R.drawable.book_no_name));
                recyclerViewAdapter.notifyItemInserted(position);

            }
        }
        if(requestCode==REQUEST_CODE_EDIT) {
            if (resultCode == RESULT_CODE_ADD_DATA) {
                String name = data.getStringExtra("name");
                int position = data.getIntExtra("position", bookitems.size());
                bookitems.get(position).setTitle(name);
                //bookitems.add(position,new book(name, R.drawable.book_no_name));
                recyclerViewAdapter.notifyItemChanged(position);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDate();

        RecyclerView mainRecycleView=findViewById(R.id.recycle_view_books);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mainRecycleView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new MyRecyclerViewAdapter(getbookitems());
        mainRecycleView.setAdapter(recyclerViewAdapter);
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
            public static final int CONTEXT_MENU_ID_ADD = 1;
            public static final int CONTEXT_MENU_ID_UPDATE = CONTEXT_MENU_ID_ADD+1;
            public static final int CONTEXT_MENU_ID_DELETE = CONTEXT_MENU_ID_ADD+2;
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
                MenuItem menuItemAdd=contextMenu.add(Menu.NONE,CONTEXT_MENU_ID_ADD,CONTEXT_MENU_ID_ADD,"Add"+position);
                MenuItem menuItemEdit=contextMenu.add(Menu.NONE,CONTEXT_MENU_ID_UPDATE,CONTEXT_MENU_ID_UPDATE,"Edit"+position);
                MenuItem menuItemelete=contextMenu.add(Menu.NONE,CONTEXT_MENU_ID_DELETE,CONTEXT_MENU_ID_DELETE,"Delete"+position);

                menuItemAdd.setOnMenuItemClickListener(this);
                menuItemEdit.setOnMenuItemClickListener(this);
                menuItemelete.setOnMenuItemClickListener(this);
            }

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int position= getAdapterPosition();
                Intent intent;
                switch(menuItem.getItemId()){
                    case CONTEXT_MENU_ID_ADD:
                        intent=new Intent(MainActivity.this,EditBookActivity.class);
                        intent.putExtra("position",position);
                        MainActivity.this.startActivityForResult(intent,REQUEST_CODE_ADD);

                        break;
                    case CONTEXT_MENU_ID_UPDATE:
                        intent=new Intent(MainActivity.this,EditBookActivity.class);
                        intent.putExtra("name",bookitems.get(position).getTitle());
                        intent.putExtra("position",position);

                        MainActivity.this.startActivityForResult(intent,REQUEST_CODE_EDIT);
                        break;
                    case CONTEXT_MENU_ID_DELETE:
                        bookitems.remove(position);
                        MyRecyclerViewAdapter.this.notifyItemRemoved(position);
                        break;
                }
                return false;
            }
        }
    }
}