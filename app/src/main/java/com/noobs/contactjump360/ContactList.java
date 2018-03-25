package com.noobs.contactjump360;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by harsh on 24-03-2018.
 */

public class ContactList extends ArrayAdapter<ContactInfo> {

    private Activity context;
    List<ContactInfo> contactInfos;

    public ContactList(Activity context, List<ContactInfo> contactInfos) {
        super(context,R.layout.contact_adapter,contactInfos);
        this.context=context;
        this.contactInfos=contactInfos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater=context.getLayoutInflater();
        View listViewItem=inflater.inflate(R.layout.contact_adapter,null,true);

        TextView txtName=listViewItem.findViewById(R.id.txtName);
        TextView txtNum=listViewItem.findViewById(R.id.txtNum);

        ContactInfo contactInfo=contactInfos.get(position);
        txtName.setText(contactInfo.name);
        txtNum.setText(String.valueOf(contactInfo.number));

        return listViewItem;
    }
}
