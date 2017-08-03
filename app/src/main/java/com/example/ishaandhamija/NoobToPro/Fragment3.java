package com.example.ishaandhamija.NoobToPro;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * Created by ishaandhamija on 22/04/17.
 */

class Fragment3 extends Fragment {

    public Fragment3() {
        // Required empty public constructor
    }

    static TextView tv1, tv2, tv3, tv4, tv5;
    int r1=0,r2=0,r3=0,r4=0,r5=0;
    public static ArrayList<String> sosContacts;
    Context context = getContext();
    FloatingActionButton fl;
    Integer RQS_PICK_CONTACT = 121;
    EditText editText = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment3, container, false);
        tv1 = (TextView) rootview.findViewById(R.id.tv1);
        tv2 = (TextView) rootview.findViewById(R.id.tv2);
        tv3 = (TextView) rootview.findViewById(R.id.tv3);
        tv4 = (TextView) rootview.findViewById(R.id.tv4);
        tv5 = (TextView) rootview.findViewById(R.id.tv5);

        fl = (FloatingActionButton) rootview.findViewById(R.id.fab);

        sosContacts = new ArrayList<>();
        sosContacts.add(new String(tv1.getText().toString()));
        sosContacts.add(new String(tv2.getText().toString()));
        sosContacts.add(new String(tv3.getText().toString()));
        sosContacts.add(new String(tv4.getText().toString()));
        sosContacts.add(new String(tv5.getText().toString()));

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(v.getContext(),tv1);
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(v.getContext(),tv2);
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(v.getContext(),tv3);
            }
        });

        tv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(v.getContext(),tv4);
            }
        });

        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInputDialog(v.getContext(),tv5);
            }
        });

        return rootview;
    }

    protected void showInputDialog(final Context c, final TextView et) {

        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View promptView = layoutInflater.inflate(R.layout.custom_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);
        alertDialogBuilder.setView(promptView);

//        final EditText editText = (EditText) promptView.findViewById(R.id.et);
//        ImageView contact = (ImageView) promptView.findViewById(R.id.imagebtn);

        editText = (EditText) promptView.findViewById(R.id.et);
        ImageView contact = (ImageView) promptView.findViewById(R.id.imagebtn);

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                pickContact();
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, RQS_PICK_CONTACT);
            }
        });

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        et.setText(editText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public boolean sendMessage(Context c,String phno, String msg) {
        try {
            if (phno == null) {
                return false;
            } else {
                SmsManager smsmanager = SmsManager.getDefault();
                smsmanager.sendTextMessage(phno, null, msg, null, null);
                Toast.makeText(c, "Message Sent to " + phno, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        catch (Exception e)
        {
            Toast.makeText(c, "MessgAct Ecxp", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RQS_PICK_CONTACT) {
            if (resultCode == RESULT_OK) {
                String namee = "";
                String phone = "";
                Uri contact = data.getData();
                ContentResolver cr = getContext().getContentResolver();

//                Cursor c = managedQuery(contact, null, null, null, null);
                Cursor c = cr.query(contact, null, null, null, null);

//                      c.moveToFirst();

                try {
                    while (c.moveToNext()) {
                        String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

                         namee = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                            while (pCur.moveToNext()) {
                                 phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                            tv.setText(namee+" Added " + phone);
//                            name.setText(namee.toString());
                                Toast.makeText(context, namee.toString(), Toast.LENGTH_SHORT).show();
//                            number.setText(phone.toString());
                                editText.setText("1290");

                            }
                        }

                    }
                }
                catch (SecurityException e){

                }
                editText.setText(phone.toString());
            }
        }
    }
}
