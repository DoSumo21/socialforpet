package com.example.socialforpet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;



public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder>{

    EditText edt_TenPet;
    Button btn_upload;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user;
    FirebaseFirestore db;

    private Context mContext;
    private List<OJUser> mListUser;

    IOnclick iOnclick;
    public UserAdapter(Context mContext, IOnclick iOnclick ) {

        this.mContext = mContext;
        this.iOnclick = iOnclick;
    }

    public void setData(List<OJUser> list){
        this.mListUser = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        OJUser ojUser = mListUser.get(position);
        if(ojUser == null){
            return;
        }
        holder.imageUser.setImageResource(ojUser.getResourceId());
        holder.tvName.setText(ojUser.getName());
        holder.cardView.setOnClickListener(v -> iOnclick.onclick(ojUser));

    }

    @Override
    public int getItemCount() {
        if(mListUser != null) {
            return mListUser.size();
        }
        return 0;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageUser;
        private TextView tvName;

        CardView cardView;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            imageUser = itemView.findViewById(R.id.img_user);
            tvName = itemView.findViewById(R.id.txt_name);
            cardView = itemView.findViewById(R.id.rcv_View);
        }
    }
}
