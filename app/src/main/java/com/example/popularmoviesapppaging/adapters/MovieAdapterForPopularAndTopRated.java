package com.example.popularmoviesapppaging.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.popularmoviesapppaging.Movies;
import com.example.popularmoviesapppaging.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapterForPopularAndTopRated extends RecyclerView.Adapter<MovieAdapterForPopularAndTopRated.MovieViewHolder> {

    List<Movies> mMoviesData;
    OnItemClickListener clickListener;

    public interface OnItemClickListener{
        void onItemClick(List<Movies> mData, int position);
    }

    public MovieAdapterForPopularAndTopRated(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    @NonNull
    @Override
    public MovieAdapterForPopularAndTopRated.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.movie_item_list, parent, false);

        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterForPopularAndTopRated.MovieViewHolder holder, int position) {
        Movies popularMovies = mMoviesData.get(position);
        holder.bind(popularMovies);
    }

    @Override
    public int getItemCount() {
        if (mMoviesData == null) {
            return 0;
        } else {
            return mMoviesData.size();
        }
    }

    public void setMovies(List<Movies> data) {
        mMoviesData = data;
        notifyDataSetChanged();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView movieImageView;
        ProgressBar loadingBar;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImageView = itemView.findViewById(R.id.movie_image_view);
            //movieTitle = itemView.findViewById(R.id.movie_title);
            loadingBar = itemView.findViewById(R.id.loading_bar);
            itemView.setOnClickListener(this);
        }

        public void bind(Movies movies) {
            loadingBar.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(movies.getImage())
                    .into(movieImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            loadingBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            movieImageView.setBackgroundResource(R.drawable.no_image_available);
                        }
                    });
        }
        @Override
        public void onClick(View view) {
            int positionClicked = getAdapterPosition();
            List<Movies> movies = mMoviesData;
            clickListener.onItemClick(movies, positionClicked);
        }
    }
}
