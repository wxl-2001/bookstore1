package com.jnu.bookstore;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link bookFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class bookFragment extends Fragment {
    public static final int RESULT_CODE_ADD_DATA = 996;
    public static final int REQUEST_CODE_ADD = 123;
    public static final int REQUEST_CODE_EDIT =REQUEST_CODE_ADD+1;

    private List<book> bookitems;
    private bookFragment.MyRecyclerViewAdapter recyclerViewAdapter;

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


    public bookFragment() {
        // Required empty public constructor
    }


    public static bookFragment newInstance() {
        bookFragment fragment = new bookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_book,container,false);
        initDate();
        FloatingActionButton fabAdd=rootView.findViewById(R.id.floating_action_button_add);
        fabAdd.setOnClickListener(view -> {
            Intent intent=new Intent(this.getContext(),EditBookActivity.class);
            intent.putExtra("position",bookitems.size());
            launcherAdd.launch(intent);
        });

        RecyclerView mainRecycleView=rootView.findViewById(R.id.recycle_view_books);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getContext());
        mainRecycleView.setLayoutManager(layoutManager);

        recyclerViewAdapter = new bookFragment.MyRecyclerViewAdapter(getbookitems());
        mainRecycleView.setAdapter(recyclerViewAdapter);
        return rootView;
    }



    public void initDate(){
        dataBank = new DataBank(this.getContext());
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

            return new bookFragment.MyRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder Holder, int position) {
            bookFragment.MyRecyclerViewAdapter.ViewHolder holder=(bookFragment.MyRecyclerViewAdapter.ViewHolder) Holder;
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
                        intent=new Intent(bookFragment.this.getContext(),EditBookActivity.class);
                        intent.putExtra("position",position);
                        launcherAdd.launch(intent);
                        break;
                    case CONTEXT_MENU_ID_UPDATE:
                        intent=new Intent(bookFragment.this.getContext(),EditBookActivity.class);
                        intent.putExtra("name",bookitems.get(position).getTitle());
                        intent.putExtra("position",position);
                        launcherEdit.launch(intent);
                        break;
                    case CONTEXT_MENU_ID_DELETE:
                        AlertDialog.Builder alertDB = new AlertDialog.Builder(bookFragment.this.getContext());
                        alertDB.setPositiveButton(bookFragment.this.getContext().getResources().getString(R.string.string_confirmation), (dialogInterface, i) -> {
                            bookitems.remove(position);
                            dataBank.saveData();
                            bookFragment.MyRecyclerViewAdapter.this.notifyItemRemoved(position);
                        });
                        alertDB.setNegativeButton(bookFragment.this.getContext().getResources().getString(R.string.string_cancel), (dialogInterface, i) -> {

                        });
                        alertDB.setMessage(bookFragment.this.getContext().getResources().getString(R.string.string_confirm_delete) +bookitems.get(position).getTitle()+"？");
                        alertDB.setTitle(bookFragment.this.getContext().getResources().getString(R.string.hint)).show();
                        break;
                }
                return false;
            }
        }
    }
}