package com.example.jumper;

import java.util.HashMap;
import java.util.Map;

import com.example.jumper.utils.impl.ContactsProviderImpl;
import com.example.jumper.utils.interfaces.ContactsProvider;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

public class ChooseGroup extends ListActivity {

	private static ContactsProvider m_contactsProvider;
	private static String[] m_contactNumbers;
	private static String[] m_contactNames;

	String[] city= {
			"Bangalore",
			"Chennai",
			"Mumbai",
			"Pune",
			"Delhi",
			"Jabalpur",
			"Indore",
			"Ranchi",
			"Hyderabad",
			"Ahmedabad",
			"Kolkata",
			"Bhopal"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_choose_group);

		/* OUR CODE BEGINS HERE */
		m_contactsProvider = new ContactsProviderImpl(this);

		listSetUp();
	}

	private void listSetUp() {
		// Display mode of the ListView
		ListView listview= getListView();
		listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);

		// text filtering
		listview.setTextFilterEnabled(true);

		Map<String, String> contacts = new HashMap<String, String>();;
		try {
			contacts = doChooseGroup();
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}

		m_contactNumbers = contacts.keySet().toArray(new String[contacts.keySet().size()]);
		m_contactNames = contacts.values().toArray(new String[contacts.values().size()]);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked,m_contactNames));
	}

	@Override
	public void onListItemClick(ListView parent, View v,int position,long id){
		CheckedTextView item = (CheckedTextView) v;
		Toast.makeText(this, m_contactNames[position] + " checked : " +
				item.isChecked(), Toast.LENGTH_SHORT).show();
	}

	public Map<String, String> doChooseGroup() {
		return m_contactsProvider.provideContacts();
	};
}
