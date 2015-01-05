package com.experimental.rxinteraction.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.experimental.rxinteraction.ArenaCard;
import com.experimental.rxinteraction.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Custom Array Adapter for showing the cards that the user has chosen inside a list view
 * <p/>
 * Currently nothing more than just a simple text description of the card
 */
public class ChosenCardAdapter extends ArrayAdapter<ArenaCard> {

    public ChosenCardAdapter(Context context, List<ArenaCard> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chosen_card_row, parent, false);
        }

        TextView cardDescription = ButterKnife.findById(convertView, R.id.card_description);

        ArenaCard card = getItem(position);

        String description = String.format("%d %s", card.getCost(), card.getName());
        cardDescription.setText(description);

        return convertView;
    }
}
