package eugene.petsshelter.ui.adapter;

import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import eugene.petsshelter.R;
import eugene.petsshelter.data.models.Comment;
import eugene.petsshelter.databinding.CommentListItemBinding;


/**
 * Created by Eugene on 3/5/2018.
 */

public class CommentsRecyclerAdapter extends RecyclerView.Adapter<CommentsRecyclerAdapter.CommentViewHolder>{

    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;
    private List<String> mCommentIds = new ArrayList<>();
    private List<Comment> mComments = new ArrayList<>();

    public CommentsRecyclerAdapter(@NonNull DatabaseReference databaseReference) {

        mDatabaseReference = databaseReference;
        mChildEventListener = getChildEventListener();
        mDatabaseReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommentListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.comment_list_item,parent,false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        bind(holder.binding, mComments.get(position));
        holder.binding.executePendingBindings();
    }

    private void bind(CommentListItemBinding binding, Comment comment) {
        binding.setComment(comment);
    }

    @Override
    public int getItemCount() {return mComments == null ? 0 : mComments.size();}

    public ChildEventListener getChildEventListener() {

        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                mCommentIds.add(dataSnapshot.getKey());
                mComments.add(comment);
                notifyItemInserted(mComments.size() - 1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {

                Comment newComment = dataSnapshot.getValue(Comment.class);

                int commentIndex = mCommentIds.indexOf(dataSnapshot.getKey());
                if (commentIndex > -1) {
                    mComments.set(commentIndex, newComment);
                    notifyItemChanged(commentIndex);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                int commentIndex = mCommentIds.indexOf(dataSnapshot.getKey());
                if (commentIndex > -1) {
                    mCommentIds.remove(commentIndex);
                    mComments.remove(commentIndex);
                    notifyItemRemoved(commentIndex);
                }
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
    }

    public void cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{

        public final CommentListItemBinding binding;

        public CommentViewHolder(CommentListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
