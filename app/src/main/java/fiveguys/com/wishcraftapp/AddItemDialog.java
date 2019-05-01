package fiveguys.com.wishcraftapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;

import faranjit.currency.edittext.CurrencyEditText;

public class AddItemDialog extends DialogFragment {

    private EditText editTextTitle;
    private EditText editTextLink;
    private CurrencyEditText editTextPrice;
    private AddItemDialogListener listener;

    private final static String NAME = "item_name";
    private final static String PRICE = "item_price";
    private final static String LINK = "item_link";
    private final static String IMAGE_URL = "item_image_url";
    private static final String DEBUG_TAG = "Ali";
    private final String apiAccessKey = "YNZBUIVFBSOPLNSO";
    private FirebaseAuth fbauth;
    private String userEmail;
    private String fbUserKey;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        fbauth = FirebaseAuth.getInstance();
        userEmail = fbauth.getCurrentUser().getEmail();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);

        builder.setView(view)
                .setTitle("Add Item to Wishlist")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //leave empty
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String itemName = editTextTitle.getText().toString();
                        String itemLink = editTextLink.getText().toString();
                        //String itemPriceString = editTextPrice.getText().toString();

                        double itemPrice = 0;
                        try {
                            if (!editTextPrice.getCurrencyText().isEmpty()) {
                                itemPrice = editTextPrice.getCurrencyDouble();
                            }
                            listener = new AddItemDialogListener() {
                                @Override
                                public void applyItemData(String itemName, Double itemPrice, String itemLink) {
                                    AliItem itemToAdd;

                                    try {
                                        itemToAdd = new AliItem(itemName, itemPrice, itemLink, "");
                                        addItemToDatabase(itemToAdd);
                                    } catch (Exception e) {
                                        Toast.makeText(AddItemDialog.this.getActivity(), "Can't add item with missing attributes!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            };
                            listener.applyItemData(itemName, itemPrice, itemLink);  //LISTENER NEEDS TO NOT BE NULL
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                });

        editTextTitle = view.findViewById(R.id.item_name_editText);
        editTextLink = view.findViewById(R.id.item_weblink);
        editTextPrice = view.findViewById(R.id.new_item_price);

        return builder.create();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (AddItemDialogListener) getTargetFragment();
            //listener = (AddItemDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement AddItemDialogListener");
        }
    }

    public interface AddItemDialogListener {
        void applyItemData(String itemName, Double itemPrice, String itemLink);
    }

    private void addItemToDatabase(final AliItem item) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference("userWishlist");
        database.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    fbUserKey = dataSnapshot.getChildren().iterator().next().getKey();
                    Log.d(DEBUG_TAG + ": Firebase", "User key to write to fb is: " + fbUserKey);
                    DatabaseReference newItem = database.child(fbUserKey + "/wishlist").push();
                    newItem.child(NAME).setValue(item.getItemName());
                    newItem.child(PRICE).setValue(item.getItemPrice());
                    newItem.child(LINK).setValue(item.getItemLink());
                    String imageUrl = item.getImageUrl();
                    newItem.child(IMAGE_URL).setValue(imageUrl);

                    Toast.makeText(AddItemDialog.this.getActivity(), item.getItemName() + " has been added to your list", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //needs to be empty to compile
            }
        });
    }
}