package com.solstice.evaluation.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.solstice.evaluation.R;
import com.solstice.evaluation.VolleySingleton;
import com.solstice.evaluation.model.Contact;
import com.solstice.evaluation.model.ContactDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a single Contact detail screen.
 * This fragment is either contained in a {@link ContactListActivity}
 * in two-pane mode (on tablets) or a {@link ContactDetailActivity}
 * on handsets.
 */
public class ContactDetailFragment extends Fragment {

    private static final String TAG = ContactDetailFragment.class.getSimpleName();

    /**
     * The fragment argument representing the index of the selected item that this fragment
     * presents.
     */
    public static final String ARG_ITEM_INDEX = "item_index";

    /**
     * The item details content this fragment is presenting.
     */
    private ContactDetails mItemDetails;

    /**
     * The item main content this fragment is presenting.
     */
    private Contact mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_INDEX)) {
            // Load the contact details specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ContactListFragment.mAdapter.getItem(getArguments().getInt(ARG_ITEM_INDEX));
            fetchContactDetails(mItem.getDetailsUrl());

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        final FrameLayout about = (FrameLayout) rootView.findViewById(R.id.about);

        // Show the
        if (mItem != null) {
            ((TextView) about.findViewById(R.id.value)).setText("+54 249 461 4459");//setText(mItem.details);
        }

        return rootView;
    }

    private void fetchContactDetails(String detailsUrl) {
        JsonObjectRequest request = new JsonObjectRequest(
                detailsUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            Log.d(TAG, jsonObject.toString());
                            mItemDetails = parseContactDetails(jsonObject);

                            //mAdapter.swapImageRecords(contacts);
                        } catch (JSONException e) {
                            Toast.makeText(getActivity(), "Unable to parse data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), "Unable to fetch data: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(this.getActivity().getApplicationContext()).addToRequestQueue(request);
    }

    private ContactDetails parseContactDetails(JSONObject jsonObject) throws JSONException {

            ContactDetails contactDetails = new ContactDetails();
            contactDetails.setEmployeeId(jsonObject.getInt("employeeId"));
            contactDetails.setFavorite(jsonObject.getBoolean("favorite"));
            contactDetails.setLargeImageURL(jsonObject.getString("largeImageURL"));
            contactDetails.setEmail(jsonObject.getString("email"));
            contactDetails.setWebsite(jsonObject.getString("website"));

            JSONObject address = jsonObject.getJSONObject("address");
            if (address != null) {
                Iterator<String> addressIter = address.keys();
                Map<String, String> addressMap = new HashMap<String, String>();
                while (phonesIter.hasNext()) {
                    String phoneType = phonesIter.next();
                    phones.put(phoneType, phone.getString(phoneType));
                }
                contactDetails.setAddress(phones);

        }

        return contactDetails;
    }
}
