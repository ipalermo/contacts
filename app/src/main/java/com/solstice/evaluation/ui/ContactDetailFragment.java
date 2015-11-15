package com.solstice.evaluation.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.TintImageView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.solstice.evaluation.R;
import com.solstice.evaluation.VolleySingleton;
import com.solstice.evaluation.model.Contact;
import com.solstice.evaluation.model.ContactDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

    private View fragmentView;

    private LayoutInflater inflater;

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
            mItem = ContactListFragment.mContactListAdapter.getItem(getArguments().getInt(ARG_ITEM_INDEX));
            fetchContactDetails(mItem.getDetailsUrl());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null)
            appBarLayout.setTitle(mItem.getName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        this.inflater = inflater;
        return fragmentView;
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

                            loadContactImage();
                            setDetails();
                            //mContactDetailsAdapter.swapRecords(mItemDetails);

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

    private void loadContactImage() {
        ImageView avatarImage = (ImageView) getActivity().findViewById(R.id.avatar);
        Glide.with(this).load(mItemDetails.getLargeImageURL()).centerCrop().into(avatarImage);
    }

    private ContactDetails parseContactDetails(JSONObject jsonContactDetails) throws JSONException {

        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setEmployeeId(jsonContactDetails.getInt("employeeId"));
        String fav = jsonContactDetails.getString("favorite");
        if (fav == "true" || fav == "1")
            contactDetails.setFavorite(true);
        else
            contactDetails.setFavorite(false);
        contactDetails.setLargeImageURL(jsonContactDetails.getString("largeImageURL"));
        contactDetails.setEmail(jsonContactDetails.getString("email"));
        contactDetails.setWebsite(jsonContactDetails.getString("website"));

        JSONObject jsonAddress = jsonContactDetails.getJSONObject("address");
        if (jsonAddress != null) {
            ContactDetails.Address address = contactDetails.new Address();
            address.setStreet(jsonAddress.getString("street"));
            address.setCity(jsonAddress.getString("city"));
            address.setState(jsonAddress.getString("state"));
            address.setCountry(jsonAddress.getString("country"));
            address.setZip(jsonAddress.getString("zip"));
            address.setLatitude(jsonAddress.getDouble("latitude"));
            address.setLongitude(jsonAddress.getDouble("longitude"));

            contactDetails.setAddress(address);
        }

        return contactDetails;
    }

    private void setDetails() {

        setPhones();

        if (mItem != null && mItemDetails != null) {
            if (mItemDetails.isFavorite())
                ((FloatingActionButton)getActivity().findViewById(R.id.fab)).setColorFilter(R.color.star_favorited, PorterDuff.Mode.SRC_ATOP);
            final FrameLayout company = (FrameLayout) fragmentView.findViewById(R.id.company);
            final FrameLayout address = (FrameLayout) fragmentView.findViewById(R.id.address);
            final FrameLayout birthday = (FrameLayout) fragmentView.findViewById(R.id.birthday);
            final FrameLayout email = (FrameLayout) fragmentView.findViewById(R.id.email);
            final FrameLayout website = (FrameLayout) fragmentView.findViewById(R.id.website);

            ((TextView) company.findViewById(R.id.value)).setText(mItem.getCompany());
            ((TextView) company.findViewById(R.id.title)).setText("Company");

            ((TextView) address.findViewById(R.id.value)).setText(mItemDetails.getAddress().getStreet()+"\n"+
                    mItemDetails.getAddress().getCity()+ ", "+
                    mItemDetails.getAddress().getCountry()+ " "+
                    mItemDetails.getAddress().getZip());
            ((TextView) address.findViewById(R.id.title)).setText("Address");

            ((TextView) birthday.findViewById(R.id.value)).setText(new SimpleDateFormat("MMMM dd, yyyy", Locale.US).format(mItem.getBirthDate()));
            ((TextView) birthday.findViewById(R.id.title)).setText("Birthday");

            ((TextView) email.findViewById(R.id.value)).setText(mItemDetails.getEmail());
            ((TextView) email.findViewById(R.id.title)).setText("Email");

            ((TextView) website.findViewById(R.id.value)).setText(mItemDetails.getWebsite());
            ((TextView) website.findViewById(R.id.title)).setText("Website");
        }

    }

    private void setPhones() {
        LinearLayout phonesContainer = (LinearLayout) fragmentView.findViewById(R.id.phoneContainer);
        View phonesDivider = fragmentView.findViewById(R.id.phoneDivider);
        if (mItem.getPhones().size() == 0) {
            phonesContainer.setVisibility(View.GONE);
            phonesDivider.setVisibility(View.GONE);
        } else {
            phonesContainer.setVisibility(View.VISIBLE);
            phonesDivider.setVisibility(View.VISIBLE);
            List<Map.Entry<String,String>> phones = new ArrayList<>(mItem.getPhones().entrySet());
            for (int i = 0; i < phones.size(); i++) {
                final Map.Entry<String,String> record = phones.get(i);
                if (record.getValue().isEmpty())
                    continue;
                View recordView = inflater.inflate(R.layout.contact_detail_record, phonesContainer, false);
                TintImageView tintImageView = (TintImageView) recordView.findViewById(R.id.recordIcon);
                if (i == 0) {
                    tintImageView.setImageResource(R.drawable.ic_call_white_36dp);
                    tintImageView.setVisibility(View.VISIBLE);
                } else {
                    tintImageView.setVisibility(View.INVISIBLE);
                }
                if (i != phones.size() - 1) {
                    recordView.findViewById(R.id.divider).setVisibility(View.GONE);
                } else {
                    recordView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
                }

                ((TextView) recordView.findViewById(R.id.value)).setText(record.getValue());
                ((TextView) recordView.findViewById(R.id.title)).setText(record.getKey());
                phonesContainer.addView(recordView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));

//                recordView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        new AlertDialog.Builder(getActivity())
//                                .setItems(new CharSequence[]{
//                                        getString(R.string.phone_menu_call).replace("{0}", phoneNumber),
//                                        getString(R.string.phone_menu_sms).replace("{0}", phoneNumber),
//                                        getString(R.string.phone_menu_share).replace("{0}", phoneNumber),
//                                        getString(R.string.phone_menu_copy)
//                                }, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (which == 0) {
//                                            startActivity(new Intent(Intent.ACTION_DIAL)
//                                                    .setData(Uri.parse("tel:+" + record.getPhone())));
//                                            // messenger().startCall(uid);
//                                            // startActivity(new Intent(getActivity(), CallActivity.class));
//                                        } else if (which == 1) {
//                                            startActivity(new Intent(Intent.ACTION_VIEW)
//                                                    .setData(Uri.parse("sms:+" + record.getPhone())));
//                                        } else if (which == 2) {
//                                            startActivity(new Intent(Intent.ACTION_SEND)
//                                                    .setType("text/plain")
//                                                    .putExtra(Intent.EXTRA_TEXT, getString(R.string.settings_share_text)
//                                                            .replace("{0}", phoneNumber)
//                                                            .replace("{1}", user.getName().get())));
//                                        } else if (which == 3) {
//                                            ClipboardManager clipboard =
//                                                    (ClipboardManager) getActivity()
//                                                            .getSystemService(Context.CLIPBOARD_SERVICE);
//                                            ClipData clip = ClipData.newPlainText("Phone number", phoneNumber);
//                                            clipboard.setPrimaryClip(clip);
//                                            Toast.makeText(getActivity(), R.string.toast_phone_copied, Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                })
//                                .show()
//                                .setCanceledOnTouchOutside(true);
//                    }
//                });
//                recordView.setOnLongClickListener(new View.OnLongClickListener() {
//                    @Override
//                    public boolean onLongClick(View v) {
//                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText("Phone number", "+" + record.getPhone());
//                        clipboard.setPrimaryClip(clip);
//                        Toast.makeText(getActivity(), R.string.toast_phone_copied, Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//                });
            }
        }
    }
}
