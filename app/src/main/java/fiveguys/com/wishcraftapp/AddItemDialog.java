package fiveguys.com.wishcraftapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import java.text.ParseException;

import faranjit.currency.edittext.CurrencyEditText;

public class AddItemDialog extends AppCompatDialogFragment {

    private EditText editTextTitle;
    private EditText editTextLink;
    private CurrencyEditText editTextPrice;
    private AddItemDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

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
                            listener.applyItemData(itemName, itemPrice, itemLink);
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
            listener = (AddItemDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement AddItemDialogListener");
        }
    }

    public interface AddItemDialogListener {
        void applyItemData(String itemName, Double itemPrice, String itemLink);
    }
}