package com.example.popularmoviesapppaging.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapppaging.R;
import com.example.popularmoviesapppaging.Reviews;

import java.util.List;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder> {

    List<Reviews> reviewsData;
    private Context context;

    public ReviewsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewsAdapter.ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.reviews_list,parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.ReviewsViewHolder holder, int position) {

        Reviews singleReview = reviewsData.get(position);
            holder.bindData(singleReview);
    }

    @Override
    public int getItemCount() {
        if (reviewsData == null) {
            return 0;
        } else {
            return reviewsData.size();
        }

    }

    public void setReviews(List<Reviews> reviews){
        reviewsData = reviews;
        notifyDataSetChanged();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {

        TextView reviewContent;
        TextView dateAndAuthor;

        public ReviewsViewHolder(@NonNull View itemView) {
            super(itemView);

            reviewContent = itemView.findViewById(R.id.reviewsTextView);
            dateAndAuthor = itemView.findViewById(R.id.authorTextView);
        }

        public void bindData(Reviews singleReview) {
            String[] date = singleReview.getDate().split("T");
            String author = "Written by " + singleReview.getAuthor()
                    + " on " + date[0];

            reviewContent.setText(singleReview.getContent());
            dateAndAuthor.setText(author);
        }
    }
}
