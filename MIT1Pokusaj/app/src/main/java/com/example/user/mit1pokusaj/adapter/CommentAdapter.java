package com.example.user.mit1pokusaj.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.mit1pokusaj.CommentActivity;
import com.example.user.mit1pokusaj.DetailsActivity;
import com.example.user.mit1pokusaj.R;
import com.example.user.mit1pokusaj.models.Comment;
import com.example.user.mit1pokusaj.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;
import java.util.zip.Inflater;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private Context context;
    private List<Comment> mComments;
    private String productId;

    private Comment comm;
    private DatabaseReference reference;
    FirebaseUser mUser;

    public CommentAdapter(Context context, List<Comment> mComments,String productId) {
        this.context = context;
        this.mComments = mComments;
        this.productId=productId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_item,viewGroup,false);
        return new CommentAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        comm = mComments.get(position);

        holder.comment.setText(comm.getComment());

        //preuzimamo podatke od onog sto je kreirao komentar
        getUserInfo(holder.username,comm.getPublisher());

        holder.cvComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView username,comment;
        public CardView cvComment;

        public ViewHolder(View itemView)
        {
            super(itemView);
            username = (TextView)itemView.findViewById(R.id.username);
            comment=(TextView)itemView.findViewById(R.id.comment);
            cvComment=(CardView)itemView.findViewById(R.id.cvComment);

        }

    }

    private void getUserInfo(final TextView username, String userId){
        reference=FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                username.setText(user.getUserName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Error ocuured",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showPopup(View v, final int position){
        PopupMenu popup = new PopupMenu(context, v);
        popup.inflate(R.menu.comment_menu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_update:
                        showUpdateDialog(position);
                        return true;
                    case R.id.menu_delete:
                        deleteComment(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popup.show();
    }

    private void deleteComment(int position){
        comm=mComments.get(position);

        mUser=FirebaseAuth.getInstance().getCurrentUser();
        //ukoliko korisnik nije kreirao komentar ne moze ga ni obrisati
        if(mUser.getUid().equals(comm.getPublisher())) {
            reference = FirebaseDatabase.getInstance().getReference()
                    .child("comments").child(productId).child(comm.getCommentId());
            reference.removeValue();

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Toast.makeText(context,"Comment is deleted",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(context,"Error ocuured",Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(context,"You cannot delete this comment!",Toast.LENGTH_SHORT).show();
        }

    }

    //provera da li sme da se update i izbacivanje dijaloga
    private void showUpdateDialog(final int position){

        comm=mComments.get(position);
        mUser=FirebaseAuth.getInstance().getCurrentUser();
        if(mUser.getUid().equals(comm.getPublisher())) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            View view = LayoutInflater.from(context).inflate(R.layout.update_comment_dialog, null);
            dialogBuilder.setView(view);

            final EditText etNewComment = (EditText) view.findViewById(R.id.enter_new_comment);
            etNewComment.setText(comm.getComment());
            Button button = (Button) view.findViewById(R.id.btn_update);

            dialogBuilder.setTitle("Update a comment");

            final AlertDialog alertDialog = dialogBuilder.create();
            alertDialog.show();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etNewComment.getText().toString().equals("")) {
                        Toast.makeText(context, "You cannot have empty fields!", Toast.LENGTH_SHORT).show();
                    }else{
                        updateComment(position, etNewComment.getText().toString());
                        alertDialog.dismiss();
                    }

                }
            });
        }else{
            Toast.makeText(context,"You cannot update this comment!",Toast.LENGTH_SHORT).show();
        }
    }

    private void updateComment(int position, String newCommentText){

        comm=mComments.get(position);
        reference=FirebaseDatabase.getInstance().getReference()
                .child("comments").child(productId).child(comm.getCommentId());

        Comment newComment=new Comment(newCommentText,comm.getPublisher(),comm.getCommentId());
        reference.setValue(newComment);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Toast.makeText(context,"Comment is updated",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Error ocuured",Toast.LENGTH_SHORT).show();
            }
        });

    }

}
