package com.solstice.evaluation.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.solstice.evaluation.R;
import com.solstice.evaluation.VolleySingleton;
import com.solstice.evaluation.model.Contact;

import java.util.List;

/**
 * Created by Nacho on 22/10/2015.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {

    static class ViewHolder {
        TextView contactName;
        TextView contactPhone;
        ImageView mImageView;
    }

    public ContactsAdapter(Context context) {
        super(context, R.layout.contact_list_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // reuse views
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, parent, false);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.contactPhone = (TextView) convertView.findViewById(R.id.contact_phone);
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.contact_image);
            convertView.setTag(viewHolder);
        }

        // fill data
        ViewHolder holder = (ViewHolder) convertView.getTag();
        Contact contact = getItem(position);
        Glide.with(getContext()).load(contact.getSmallImageUrl()).centerCrop().into(holder.mImageView);
        holder.contactName.setText(contact.getName());
        holder.contactPhone.setText(contact.getPhones().get("home"));

        return convertView;
    }

    public void swapContactRecords(List<Contact> contacts) {
            clear();

            for (Contact contact : contacts) {
                add(contact);
            }

            notifyDataSetChanged();
    }
}
