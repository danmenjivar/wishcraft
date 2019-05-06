//package fiveguys.com.wishcraftapp;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//
//public class DisplayProfileSearchAdapter  extends RecyclerView.Adapter<DisplayProfileSearchAdapter.ProfileViewHolder>{
//    private Context mContext;
//    private ArrayList<DisplayItem> mExampleList;
//    private DisplayProductAdapter.OnItemClickListener mListener;
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setOnRemoveListener(DisplayProductAdapter.OnItemClickListener listener){
//        mListener = listener;
//    }
//
//    public DisplayProfileSearchAdapter(Context context, ArrayList<> list) {
//        mContext = context;
//        mExampleList = list;
//    }
//
//    public DisplayItem getIndexedItem(int index){
//        return mExampleList.get(index);
//    }
//
//
//    @NonNull
//    @Override
//    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View v = LayoutInflater.from(mContext).inflate(R.layout., viewGroup, false);
//        return new ProfileViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull DisplayProductAdapter.ProductViewHolder productViewHolder, int i) {
//        DisplayItem item = mExampleList.get(i);
//
//        String productName = item.getItemName();
//        double productPrice = item.getItemPrice();
//        String imageUrl = item.getImageUrl();
//        productViewHolder.itemName.setText(productName);
//        productViewHolder.itemPrice.setText(String.format("$%.2f", productPrice));
//        Glide.with(mContext).load(imageUrl).asBitmap().fitCenter().into(productViewHolder.itemImage);
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return mExampleList.size();
//    }
//
//    public class ProfileViewHolder extends RecyclerView.ViewHolder {
//
//        public ImageView profileImage;
//        public TextView profileName;
//
//        public ProfileViewHolder(View itemView) {
//            super(itemView);
//            this.profileImage = itemView.findViewById(R.id.list_itemPicture);
//            this.profileName = itemView.findViewById(R.id.item_title_list);
//        }
//    }
//}
