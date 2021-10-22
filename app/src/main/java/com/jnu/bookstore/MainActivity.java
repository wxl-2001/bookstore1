package com.jnu.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        private class ViewHolder extends RecyclerView.ViewHolder {
            private final ImageView imageView;
            private final TextView textView;

            public ViewHolder(View view) {
                super(view);

                this.imageView=view.findViewById(R.id.image_view_book_cover);
                this.textView=view.findViewById(R.id.text_view_book_title);
            }

            public ImageView getImageView() {
                return imageView;
            }

            public TextView getTextView() {
                return textView;
            }
        }
    }
}