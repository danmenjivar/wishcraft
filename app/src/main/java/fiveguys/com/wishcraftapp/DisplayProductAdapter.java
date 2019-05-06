package fiveguys.com.wishcraftapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DisplayProductAdapter extends RecyclerView.Adapter<DisplayProductAdapter.ProductViewHolder> {

    private Context mContext;
    private ArrayList<DisplayItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnRemoveListener(OnItemClickListener listener){
        mListener = listener;
    }

    public DisplayProductAdapter(Context context, ArrayList<DisplayItem> list) {
        mContext = context;
        mExampleList = list;
    }

    public DisplayItem getIndexedItem(int index){
        return mExampleList.get(index);
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.display_list_layout, viewGroup, false);
        return new ProductViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        DisplayItem item = mExampleList.get(i);

        String productName = item.getItemName();
        double productPrice = item.getItemPrice();
        String imageUrl = item.getImageUrl();
        productViewHolder.itemName.setText(productName);
        productViewHolder.itemPrice.setText(String.format("$%.2f", productPrice));
        Glide.with(mContext).load(imageUrl).asBitmap().fitCenter().into(productViewHolder.itemImage);

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemName;
        public TextView itemPrice;
        public Button removeButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            this.itemImage = itemView.findViewById(R.id.list_itemPicture);
            this.itemName = itemView.findViewById(R.id.item_title_list);
            this.itemPrice = itemView.findViewById(R.id.item_price_list);
            this.removeButton = itemView.findViewById(R.id.item_remove_button_list);
            this.removeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }
}
