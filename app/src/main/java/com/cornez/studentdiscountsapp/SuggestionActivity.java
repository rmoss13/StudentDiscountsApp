package com.cornez.studentdiscountsapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SuggestionActivity extends AppCompatActivity {

    private String[] stateCodes;
    private String[] stateNames;
    private EditText business,address,city,discount;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        business = (EditText) findViewById(R.id.businessEdit);
        address = (EditText) findViewById(R.id.addressEdit);
        city = (EditText) findViewById(R.id.cityEdit);
        discount = (EditText) findViewById(R.id.discountEdit);

        submit = (Button) findViewById(R.id.submit_button);
        createSpinner();

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle b=new Bundle();
                b.putStringArray("data", new String[]{
                        business.getText().toString(),
                        address.getText().toString(),
                        city.getText().toString(),
                        discount.getText().toString()});
                Intent intent = new Intent(SuggestionActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("Source", "From Suggestion");
                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }

    private void createSpinner(){
        stateCodes = new String[52];  // 50 states + DC + blank
        stateNames = new String[52];

        // set first item to blank
        int stateCount = 0;
        stateCodes[stateCount ] = "";
        stateNames[stateCount ++] = "";

        // now add all US states
        stateCodes[stateCount]= "AL";
        stateNames[stateCount++] = "Alabama";
        stateCodes[stateCount]= "AK";
        stateNames[stateCount++] = "Alaska";
        stateCodes[stateCount]= "AZ";
        stateNames[stateCount++] = "Arizona";
        stateCodes[stateCount]= "AR";
        stateNames[stateCount++] = "Arkansas";
        stateCodes[stateCount]= "CA";
        stateNames[stateCount++] = "California";
        stateCodes[stateCount]= "CO";
        stateNames[stateCount++] = "Colorado";
        stateCodes[stateCount]= "CT";
        stateNames[stateCount++] = "Connecticut";
        stateCodes[stateCount]= "DE";
        stateNames[stateCount++] = "Delaware";
        stateCodes[stateCount]= "FL";
        stateNames[stateCount++] = "Florida";
        stateCodes[stateCount]= "GA";
        stateNames[stateCount++] = "Georgia";
        stateCodes[stateCount]= "HI";
        stateNames[stateCount++] = "Hawaii";
        stateCodes[stateCount]= "ID";
        stateNames[stateCount++] = "Idaho";
        stateCodes[stateCount]= "IA";
        stateNames[stateCount++] = "Iowa";
        stateCodes[stateCount]= "IL";
        stateNames[stateCount++] = "Illinois";
        stateCodes[stateCount]= "IN";
        stateNames[stateCount++] = "Indiana";
        stateCodes[stateCount]= "KS";
        stateNames[stateCount++] = "Kansas";
        stateCodes[stateCount]= "KY";
        stateNames[stateCount++] = "Kentucky";
        stateCodes[stateCount]= "LA";
        stateNames[stateCount++] = "Louisiana";
        stateCodes[stateCount]= "ME";
        stateNames[stateCount++] = "Maine";
        stateCodes[stateCount]= "MD";
        stateNames[stateCount++] = "Maryland";
        stateCodes[stateCount]= "MA";
        stateNames[stateCount++] = "Massachusetts";
        stateCodes[stateCount]= "MI";
        stateNames[stateCount++] = "Michigan";
        stateCodes[stateCount]= "MN";
        stateNames[stateCount++] = "Minnesota";
        stateCodes[stateCount]= "MS";
        stateNames[stateCount++] = "Mississippi";
        stateCodes[stateCount]= "MO";
        stateNames[stateCount++] = "Missouri";
        stateCodes[stateCount]= "MT";
        stateNames[stateCount++] = "Montana";
        stateCodes[stateCount]= "NE";
        stateNames[stateCount++] = "Nebraska";
        stateCodes[stateCount]= "NV";
        stateNames[stateCount++] = "Nevada";
        stateCodes[stateCount]= "NH";
        stateNames[stateCount++] = "New Hampshire";
        stateCodes[stateCount]= "NJ";
        stateNames[stateCount++] = "New Jersey";
        stateCodes[stateCount]= "NM";
        stateNames[stateCount++] = "New Mexico";
        stateCodes[stateCount]= "NY";
        stateNames[stateCount++] = "New York";
        stateCodes[stateCount]= "NC";
        stateNames[stateCount++] = "North Carolina";
        stateCodes[stateCount]= "ND";
        stateNames[stateCount++] = "North Dakota";
        stateCodes[stateCount]= "OH";
        stateNames[stateCount++] = "Ohio";
        stateCodes[stateCount]= "OK";
        stateNames[stateCount++] = "Oklahoma";
        stateCodes[stateCount]= "OR";
        stateNames[stateCount++] = "Oregon";
        stateCodes[stateCount]= "PA";
        stateNames[stateCount++] = "Pennsylvania";
        stateCodes[stateCount]= "RI";
        stateNames[stateCount++] = "Rhode Island";
        stateCodes[stateCount]= "SC";
        stateNames[stateCount++] = "South Carolina";
        stateCodes[stateCount]= "SD";
        stateNames[stateCount++] = "South Dakota";
        stateCodes[stateCount]= "TN";
        stateNames[stateCount++] = "Tennessee";
        stateCodes[stateCount]= "TX";
        stateNames[stateCount++] = "Texas";
        stateCodes[stateCount]= "UT";
        stateNames[stateCount++] = "Utah";
        stateCodes[stateCount]= "VT";
        stateNames[stateCount++] = "Vermont";
        stateCodes[stateCount]= "VA";
        stateNames[stateCount++] = "Virginia";
        stateCodes[stateCount]= "WA";
        stateNames[stateCount++] = "Washington";
        stateCodes[stateCount]= "DC";
        stateNames[stateCount++] = "Washington DC";
        stateCodes[stateCount]= "WV";
        stateNames[stateCount++] = "West Virgina";
        stateCodes[stateCount]= "WI";
        stateNames[stateCount++] = "Wisconsin";
        stateCodes[stateCount]= "WY";
        stateNames[stateCount++] = "Wyoming";

        Spinner dropdown = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stateNames);
        dropdown.setAdapter(adapter);
    }
}
