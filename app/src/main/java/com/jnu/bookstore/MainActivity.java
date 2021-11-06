package com.jnu.bookstore;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jnu.bookstore.data.DataBank;
import com.jnu.bookstore.data.book;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int RESULT_CODE_ADD_DATA = 996;
    public static final int REQUEST_CODE_ADD = 123;
    public static final int REQUEST_CODE_EDIT =REQUEST_CODE_ADD+1;

    private List<book> bookitems;
    private MyRecyclerViewAdapter recyclerViewAdapter;

    ActivityResultLauncher<Intent> launcherAdd = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            int resultCode = result.getResultCode();
            if(resultCode== RESULT_CODE_ADD_DATA){
                if(null==data)return;
                String name = data.getStringExtra("name");
                int position=data.getIntExtra("position",bookitems.size());
                bookitems.add(position,new book(name, R.drawable.book_no_name));
                dataBank.saveData();
                recyclerViewAdapter.notifyItemInserted(position);

            }
        }
    });
    ActivityResultLauncher<Intent>  launcherEdit = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Intent data = result.getData();
            int resultCode = result.getResultCode();
            if(resultCode== RESULT_CODE_ADD_DATA) {
                if(null==data)return;
                String name = data.getStringExtra("name");
                int position = data.getIntExtra("position", bookitems.size());
                bookitems.get(position).setTitle(name);
                dataBank.saveData();
                recyclerViewAdapter.notifyItemChanged(position);
            }
        }
    });

    private DataBank dataBank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDate();

        FloatingActionButton fabAdd=findViewById(R.id.floating_action_button_add);
        fabAdd.setOnClickListener(view -> {
            Intent intent=new Intent(MainActivity.this,EditBookActivity.class);
            intent.putExtra("position",bookitems.size());
            launcherAdd.launch(intent);
        });

        RecyclerView mainRecycleView=findViewById(R.id.recycle_view_books);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        mainRecycleView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new MyRecyclerViewAdapter(getbookitems());
        mainRecycleView.setAdapter(recyclerViewAdapter);
    }
    public void initDate(){
        dataBank = new DataBank(MainActivity.this);
        bookitems= dataBank.loadData();

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
                        launcherAdd.launch(intent);
                        break;
                    case CONTEXT_MENU_ID_UPDATE:
                        intent=new Intent(MainActivity.this,EditBookActivity.class);
                        intent.putExtra("name",bookitems.get(position).getTitle());
                        intent.putExtra("position",position);
                        launcherEdit.launch(intent);
                        break;
                    case CONTEXT_MENU_ID_DELETE:
                        AlertDialog.Builder alertDB = new AlertDialog.Builder(MainActivity.this);
                        alertDB.setPositiveButton(MainActivity.this.getResources().getString(R.string.string_confirmation), (dialogInterface, i) -> {
                            bookitems.remove(position);
                            dataBank.saveData();
                            MyRecyclerViewAdapter.this.notifyItemRemoved(position);
                        });
                        alertDB.setNegativeButton(MainActivity.this.getResources().getString(R.string.string_cancel), (dialogInterface, i) -> {

                        });
                        alertDB.setMessage(MainActivity.this.getResources().getString(R.string.string_confirm_delete) +bookitems.get(position).getTitle()+"？");
                        alertDB.setTitle(MainActivity.this.getResources().getString(R.string.hint)).show();
                        break;
                }
                return false;
            }
        }
    }
}