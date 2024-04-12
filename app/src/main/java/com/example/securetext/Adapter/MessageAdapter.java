package com.example.securetext.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.securetext.Database.RoomDB;
import com.example.securetext.Model.Message;
import com.example.securetext.R;
import com.example.securetext.Utility.BDUtility;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.myViewHolder> {


    List<Message> messageList;
    LayoutInflater layoutInflater;
    RoomDB database;

    public MessageAdapter(List<Message> messageList, Context context, RoomDB database) {
        this.messageList = messageList;
        this.layoutInflater = LayoutInflater.from(context);
        this.database = database;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_message, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        holder.txtPlain.setText(messageList.get(position).getPtext());
        holder.txtEncrypted.setText(messageList.get(position).getEtext());
        holder.txtDate.setText(messageList.get(position).getCreationTime().toString());


        holder.btnCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BDUtility.setClipBoard(v.getContext(), messageList.get(position).getEtext());
                Toast.makeText(v.getContext(), "Copies to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Delete!!")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                database.mainDao().delete(messageList.get(position));
                                messageList.remove(messageList.get(position));
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(R.drawable.ic_delete)
                        .show();

            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View inflater) {
                // I gave name inflater form myself before this it was just v
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, messageList.get(position).getEtext());
                intent.setType("text/plain");

                if (intent.resolveActivity(inflater.getContext().getPackageManager()) != null) {
                    inflater.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {

        TextView txtPlain, txtEncrypted, txtDate;
        ImageButton btnShare, btnCopyToClipboard, btnDelete;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            txtPlain = itemView.findViewById(R.id.txtPlainText);
            txtEncrypted = itemView.findViewById(R.id.txtEncryptedText);
            txtDate = itemView.findViewById(R.id.txtCreationTime);

            btnShare = itemView.findViewById(R.id.btnShare);
            btnCopyToClipboard = itemView.findViewById(R.id.btnCopyToClipboard);
            btnDelete = itemView.findViewById(R.id.btnDelete);


        }
    }

}
