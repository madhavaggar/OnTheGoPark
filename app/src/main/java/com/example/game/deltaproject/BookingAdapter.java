package com.example.game.deltaproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.MyViewHolder> {
    List<Bookingclass> booklist;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public View mview;
        TextView status;
        TextView info;
        TextView gotoMap;
        public MyViewHolder(View itemView){
            super(itemView);
            mview=itemView;
            status=mview.findViewById(R.id.status);
            info=mview.findViewById(R.id.info);
            gotoMap=mview.findViewById(R.id.gotomap);
        }
    }

    public BookingAdapter(Context context, List<Bookingclass> booklist){
        this.context=context;
        this.booklist=booklist;

    }

    @Override
    public BookingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_rowbooking, parent, false);
        return new BookingAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position){
        holder.status.setText(booklist.get(position).getStatus());
        holder.info.setText("Latitude: " + booklist.get(position).getLatitude()+"\nLongitude: " + booklist.get(position).getLongitude() +
                "\nBooking Time: " + booklist.get(position).getBooktime() + "\nEntry Time: " + booklist.get(position).getIntime()
                + "\nExit Time: " + booklist.get(position).getOuttime());
        holder.gotoMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(booklist.get(position).getStatus()==0 || booklist.get(position).getStatus()==1) {
                    Intent intent = new Intent(context, MapEngine.class);
                    intent.putExtra("Username", booklist.get(position).getUsername());
                    context.startActivity(intent);
                }
                else{
                    Toast.makeText(context,"The booking has been completed, cannot view",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount(){
        return booklist.size();
    }

}
