package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kevin on 8/18/2016.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<AndroidFlavor.review> reviewList;

    public ReviewAdapter(List<AndroidFlavor.review> reviewList) {
        this.reviewList = reviewList;
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    @Override
    public void onBindViewHolder(final ReviewViewHolder reviewViewHolder, int i) {
        final AndroidFlavor.review review = reviewList.get(i);
        reviewViewHolder.author.setText(review.getAuthor());
        //I don't want to show all the text on the screen
        String contentHide;
        final String content;
        try {
            contentHide = review.getContent().substring(0, 100) + ".......";

        }catch (StringIndexOutOfBoundsException siobe){
            contentHide = review.getContent();
        }

        content = contentHide;

        reviewViewHolder.review.setText(contentHide);
        //When the person click the review, it will show all the text and click again hide the text
        reviewViewHolder.view.setOnClickListener(new View.OnClickListener() {
            boolean state = false;
            @Override
            public void onClick(View v) {
                if(state == false) {
                    reviewViewHolder.review.setText(review.getContent());
                    state = true;
                }else{
                    reviewViewHolder.review.setText(content);
                    state = false;
                }
            }
        });
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.list_item_review, viewGroup, false);

        return new ReviewViewHolder(itemView);
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        protected View view;
        protected TextView author;
        protected TextView review;

        public ReviewViewHolder(View v) {
            super(v);
            view = v;
            author = (TextView) v.findViewById(R.id.author);
            review = (TextView) v.findViewById(R.id.review);
        }
    }
}
