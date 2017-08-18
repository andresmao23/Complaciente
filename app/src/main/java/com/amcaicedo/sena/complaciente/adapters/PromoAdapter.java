package com.amcaicedo.sena.complaciente.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amcaicedo.sena.complaciente.R;
import com.amcaicedo.sena.complaciente.models.Promocion;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.OnItemClick;

/**
 * Created by asus on 24/05/2017.
 */

public class PromoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    public interface OnItemClick{
        void onItemClick(int position);
    }


    Context context;
    List<Promocion> data;

    OnItemClick onItemClick;
    RecyclerView recyclerView;

    public PromoAdapter(Context context, List<Promocion> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        View v = View.inflate(context, R.layout.template_promocion, null);
        v.setOnClickListener(this);
        holder = new PromoViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Promocion promo = data.get(position);

        PromoViewHolder promoViewHolder = (PromoViewHolder) holder;
        promoViewHolder.title.setText(promo.getTitulo());
        promoViewHolder.bar.setText(promo.getBar());
        //promoViewHolder.content.setText(promo.getDescripcion());

        Uri uri = Uri.parse(promo.getImagen());
        Picasso.with(context).load(uri).into(promoViewHolder.img);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnItemClick(RecyclerView recyclerView, OnItemClick onItemClick){
        this.recyclerView = recyclerView;
        this.onItemClick = onItemClick;
    }

    @Override
    public void onClick(View v) {
        int position = recyclerView.getChildAdapterPosition(v);
        onItemClick.onItemClick(position);
    }

    class PromoViewHolder extends RecyclerView.ViewHolder{

        ImageView img;
        TextView bar;
        TextView title;
        TextView content;

        public PromoViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            bar = (TextView) itemView.findViewById(R.id.bar);
            title = (TextView) itemView.findViewById(R.id.title);
            //content = (TextView) itemView.findViewById(R.id.content);
        }
    }
}
