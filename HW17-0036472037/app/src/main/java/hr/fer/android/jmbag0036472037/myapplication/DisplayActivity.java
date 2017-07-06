package hr.fer.android.jmbag0036472037.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This activity displays given message.
 * When button OK is clicked activity finishes with RESULT_OK.
 * Activity offer button that will send
 * given message to email ana.baotic@infinum.hr.
 *
 * Created by Ivica Brebrek on 4.7.2016.
 */
public class DisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        String message = getIntent().getStringExtra("display");
        TextView screen = (TextView)findViewById(R.id.tv_display_message);
        screen.setText(message);

        initOKButton();
        initSendButton(message);
    }

    /**
     * Initializes OK button which
     * finishes activity with RESULT_OK.
     */
    private void initOKButton() {
        findViewById(R.id.btn_display_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    /**
     * On click sends email with given message
     * to email ana.baotic@infinum.hr.
     *
     * @param message   message sent in email.
     */
    private void initSendButton(final String message) {
        findViewById(R.id.btn_display_send_report).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setData(Uri.parse("mailto:"));
                email.setType("text/plain");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"ana.baotic@infinum.hr"});
                email.putExtra(Intent.EXTRA_SUBJECT, "0036472037: dz report");
                email.putExtra(Intent.EXTRA_TEXT, message);
                try {
                    startActivity(Intent.createChooser(email, "Send mail..."));
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DisplayActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
