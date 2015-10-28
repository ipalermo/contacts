package com.solstice.evaluation.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.solstice.evaluation.R;
import com.solstice.evaluation.VolleySingleton;
import com.solstice.evaluation.model.Contact;

import java.util.List;

/**
 * Created by Nacho on 22/10/2015.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {

    private ImageLoader mImageLoader;

    public ContactsAdapter(Context context) {
        super(context, R.layout.contact_list_item);

        mImageLoader = VolleySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
        }

        NetworkImageView mNetworkImageView = (NetworkImageView) convertView.findViewById(R.id.contact_image);
        TextView contactName = (TextView) convertView.findViewById(R.id.contact_name);
        TextView contactPhone = (TextView) convertView.findViewById(R.id.contact_phone);

        Contact contact = getItem(position);

        mNetworkImageView.setImageUrl(contact.getSmallImageUrl(), mImageLoader);
        contactName.setText(contact.getName());
        contactPhone.setText(contact.getPhones().get("home"));

        return convertView;
    }

    public void swapImageRecords(List<Contact> contacts) {
        clear();

        for (Contact contact : contacts) {
            add(contact);
        }

        notifyDataSetChanged();
    }
}
