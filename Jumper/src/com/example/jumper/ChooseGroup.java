package com.example.jumper;

import java.io.IOException;
import java.util.Map;

import com.example.jumper.manager.impl.FileManagerImpl;
import com.example.jumper.manager.interfaces.FileManager;
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
import android.widget.TextView;
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

		String groupName = "";

		m_choosenContacts = new ContactsGroup(groupName);
		m_allContacts = new ContactsGroup(doChooseGroup());

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_checked, m_allContacts.getNames()));
	}

	@Override
	public void onListItemClick(ListView parent, View v,int position,long id){
		CheckedTextView item = (CheckedTextView) v;

		boolean isChecked = item.isChecked();

		String name = ((TextView)v).getText().toString();
		String number = m_allContacts.getNumber(name);

		if (isChecked)
		{
			m_choosenContacts.add(number, name);
		}
		else
		{
			m_choosenContacts.removeByNumber(number);
		}

		if (m_choosenContacts.getNames().length == 3)
		{
			FileManager fm = new FileManagerImpl();
			try {
				fm.write("pojo.txt", m_choosenContacts.getNumbersAsString(), false);

				fm.read("yolo.txt", "yolo!!!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Toast.makeText(this, (isChecked ? " נוסף " : " הורד " ) + name, Toast.LENGTH_SHORT).show();
	}

	public Map<String, String> doChooseGroup() {
		return m_contactsProvider.provideContacts();
	};
}
