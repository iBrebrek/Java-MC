package hr.fer.android.jmbag0036472037.myapplication;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity that allows user to pick
 * operation (plus, minus, divide or multiply)
 * and calculate result of inputted two numbers.
 * <br>
 * When calculation is done, result (or message
 * with failure) is sent to activity
 * {@link DisplayActivity}.
 *
 * Created by Ivica Brebrek on 4.7.2016.
 */
public class CalculusActivity extends AppCompatActivity {
    /** Code used as a request code. */
    private static final int MY_CODE = 1337;

    /** Operator currently selected in GUI. */
    private Operator currentlyUsed = Operator.PLUS;
    /** Input for first number. */
    private EditText firstInput;
    /** Input for second number. */
    private EditText secondInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculus);

        firstInput = (EditText)findViewById(R.id.et_left);
        secondInput = (EditText)findViewById(R.id.et_right);

        createDropDown();

        findViewById(R.id.btn_calculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first = firstInput.getText().toString();
                String second = secondInput.getText().toString();
                String result = currentlyUsed.calculate(first, second);

                String display;
                if(result == null) {
                    display = String.format(
                            "Prilikom obavljanja operacije [%s] nad unosima " +
                                    "[%s] i [%s] došlo je do sljedeće greške: \"%s\".",
                            currentlyUsed.toString(), first, second,
                            currentlyUsed.getErrorMessage()
                    );
                } else {
                    display = String.format(
                            "Rezultat operacije [%s] je [%s].",
                            currentlyUsed.getLastCalculation(),
                            result
                    );
                }

                Intent intent = new Intent(CalculusActivity.this, DisplayActivity.class);
                intent.putExtra("display", display);
                startActivityForResult(intent, MY_CODE);
            }
        });
    }

    /**
     * Creates drop down menu to select operator.
     */
    private void createDropDown() {
        final List<Operator> supportedOperators = new ArrayList<>();
        supportedOperators.add(Operator.PLUS);
        supportedOperators.add(Operator.MINUS);
        supportedOperators.add(Operator.MULTIPLY);
        supportedOperators.add(Operator.DIVIDE);

        Spinner dropdown = (Spinner)findViewById(R.id.spn_sign);
        ArrayAdapter<Operator> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                supportedOperators
        );
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                currentlyUsed = supportedOperators.get(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == MY_CODE && resultCode == RESULT_OK) {
            firstInput.setText("");
            secondInput.setText("");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
