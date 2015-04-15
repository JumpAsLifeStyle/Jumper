package com.example.jumper.providers.impl;

import java.util.HashMap;
import java.util.Map;

import com.example.jumper.providers.interfaces.ContactsProvider;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

public class ContactsProviderImpl extends ContextWrapper implements ContactsProvider {

	public ContactsProviderImpl(Context base) {
		super(base);
	}

	@Override
	public Map<String, String> provideContacts() {
		Map<String, String> contacts = new HashMap<String, String>();

		Cursor c = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		String name, number = "";
		String id;
		c.moveToFirst();
		for (int i = 0; i < c.getCount(); i++) {
			name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));

			if (Integer.parseInt(c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
				Cursor pCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { id },
						null);
				while (pCur.moveToNext()) {
					number = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				}
			}
			Log.i("name ", name + " ");
			Log.i("number ", number + " ");


			if (number != null)
			{
				contacts.put(number, name);
			}

			c.moveToNext();
		}

		return contacts;
	}
}
