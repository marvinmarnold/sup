package com.google.android.apps.sup;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class NewsListActivity extends Activity {

	ArrayList<String> animalsNameList;

	public void onCreate(Bundle saveInstanceState) {
		super.onCreate(saveInstanceState);
		setContentView(R.layout.activity_news);

		// Get the reference of ListViewAnimals
		ListView animalList = (ListView) findViewById(R.id.listViewAnimals);

		animalsNameList = new ArrayList<String>();
		getAnimalNames();
		// Create The Adapter with passing ArrayList as 3rd parameter
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, animalsNameList);
		// Set The Adapter
		animalList.setAdapter(arrayAdapter);

		// register onClickListener to handle click events on each item
		animalList.setOnItemClickListener(new OnItemClickListener() {
			// argument position gives the index of item which is clicked
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {

				String selectedAnimal = animalsNameList.get(position);
				Toast.makeText(getApplicationContext(),
						"Animal Selected : " + selectedAnimal,
						Toast.LENGTH_LONG).show();
			}
		});
	}

	void getAnimalNames() {
		animalsNameList.add("DOG");
		animalsNameList.add("CAT");
		animalsNameList.add("HORSE");
		animalsNameList.add("ELEPHANT");
		animalsNameList.add("LION");
		animalsNameList.add("COW");
		animalsNameList.add("MONKEY");
		animalsNameList.add("DEER");
		animalsNameList.add("RABBIT");
		animalsNameList.add("BEER");
		animalsNameList.add("DONKEY");
		animalsNameList.add("LAMB");
		animalsNameList.add("GOAT");

	}
}
