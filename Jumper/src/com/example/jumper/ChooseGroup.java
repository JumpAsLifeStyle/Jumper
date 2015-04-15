package com.example.jumper;

import java.util.Map;

import com.example.jumper.providers.common.ContactsGroup;
import com.example.jumper.providers.impl.ContactsProviderImpl;
import com.example.jumper.providers.interfaces.ContactsProvider;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

public class ChooseGroup extends ListActivity {
	private static ContactsGroup m_allContacts;
	private static ContactsGroup m_choosenContacts;
	private static ContactsProvider m_contactsProvider;

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
		listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

		// Enable text filtering
		listview.setTextFilterEnabled(true);

		m_choosenContacts = new ContactsGroup();
		m_allContacts = new ContactsGroup(doChooseGroup());

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, m_allContacts.getNames()));
	}

	@Override
	public void onListItemClick(ListView parent, View v,int position,long id){
		CheckedTextView item = (CheckedTextView) v;

		boolean isChecked = item.isChecked();

		String name = m_allContacts.getNames()[position];
		String number = m_allContacts.getNumbers()[position];

		if (isChecked)
		{
			m_choosenContacts.add(number, name);
		}
		else
		{
			m_choosenContacts.removeByNumber(number);
		}

		Toast.makeText(this, (isChecked ? " נוסף " : " הורד " ) + name, Toast.LENGTH_SHORT).show();
	}

	public Map<String, String> doChooseGroup() {
		return m_contactsProvider.provideContacts();
	};
}
