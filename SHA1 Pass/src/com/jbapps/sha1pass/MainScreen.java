package com.jbapps.sha1pass;

import android.widget.TextView;
import android.util.Base64;
import java.security.InvalidKeyException;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Mac;
import android.content.Context;
import android.content.ClipData;
import android.content.ClipboardManager;
import java.io.UnsupportedEncodingException;
import android.util.Log;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import android.content.Intent;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

// -------------------------------------------------------------------------
/**
 * The main screen and all code generation for the app
 * 
 * @author Jeremy Banks
 * @version July 4, 2014
 */
public class MainScreen extends Activity implements View.OnClickListener {
	private CheckBox viewCheckBox;
	private CheckBox secureCheckBox;
	private CheckBox hmacCheckBox;
	private CheckBox complexCheckBox;
	private Button hexButton;
	private Button hexHalfButton;
	private Button b64Button;
	private Button b64HalfButton;
	private Button aboutButton;
	private EditText sentenceField;
	private EditText wordField;
	private TextView peek;
	private final static String hmacMessage = "SHA1_Pass";

	/**
	 * Set up all view objects in the interface
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_screen);

		viewCheckBox = (CheckBox) findViewById(R.id.viewCheckBox);
		viewCheckBox.setOnClickListener(this);

		secureCheckBox = (CheckBox) findViewById(R.id.secureCheckBox);
		secureCheckBox.setOnClickListener(this);

		hmacCheckBox = (CheckBox) findViewById(R.id.hmacCheckBox);
		hmacCheckBox.setOnClickListener(this);

		complexCheckBox = (CheckBox) findViewById(R.id.complexCheckBox);
		complexCheckBox.setOnClickListener(this);

		hexButton = (Button) findViewById(R.id.hex);
		hexButton.setOnClickListener(this);

		hexHalfButton = (Button) findViewById(R.id.hexHalf);
		hexHalfButton.setOnClickListener(this);

		b64Button = (Button) findViewById(R.id.b64);
		b64Button.setOnClickListener(this);

		b64HalfButton = (Button) findViewById(R.id.b64Half);
		b64HalfButton.setOnClickListener(this);

		aboutButton = (Button) findViewById(R.id.about);
		aboutButton.setOnClickListener(this);

		sentenceField = (EditText) findViewById(R.id.sentenceField);

		wordField = (EditText) findViewById(R.id.wordField);

		peek = (TextView) findViewById(R.id.peek);
		peek.setText("Peek: ");
	}

	/**
	 * Make the layout fairly standard
	 */
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout);
		int layoutWidth = layout.getWidth();

		wordField.setWidth(layoutWidth / 2);
		peek.setWidth(layoutWidth / 2);
		viewCheckBox.setWidth(layoutWidth / 2);
		secureCheckBox.setWidth(layoutWidth / 2);
		hmacCheckBox.setWidth(layoutWidth / 2);
		complexCheckBox.setWidth(layoutWidth / 2);
		hexButton.setWidth(layoutWidth / 2);
		hexHalfButton.setWidth(layoutWidth / 2);
		b64Button.setWidth(layoutWidth / 2);
		b64HalfButton.setWidth(layoutWidth / 2);
	}

	/**
	 * Actions taken when a button or checkbox is clicked
	 */
	public void onClick(View v) {
		if (v.getClass() == Button.class) {
			/*
			 * Generate the hex password for current sentence + word
			 */
			if (((Button) v).getText().equals("Hex")) {
				String hexResult = null;
				byte[] hashData = generateHash();

				/*
				 * If HMAC mode is selected, generate HMAC, otherwise use SHA-1
				 * hash
				 */
				if (hmacCheckBox.isChecked()) {
					byte[] hmacData = generateHMAC(hashData);
					hexResult = generateHex(hmacData);
				} else {
					hexResult = generateHex(hashData);
				}

				/*
				 * Append .H0k if complex mode is selected.
				 */
				if (complexCheckBox.isChecked()) {
					hexResult += ".H0k";
				}

				/*
				 * Copy result to clipboard, set peek to first four characters,
				 * and make a toast of a success message
				 */
				copyToClipboard(hexResult);
				peek.setText("Peek: " + hexResult.substring(0, 4));
				Toast.makeText(getApplicationContext(), "Hex password copied.",
						Toast.LENGTH_SHORT).show();
				System.out.println(hexResult);
			}

			/*
			 * Generate the hex half-length password for current sentence + word
			 */
			if (((Button) v).getText().equals("Hex Half")) {
				String hexHalfResult = null;
				byte[] hashData = generateHash();

				/*
				 * If HMAC mode is selected, generate HMAC, otherwise use SHA-1
				 * hash
				 */
				if (hmacCheckBox.isChecked()) {
					byte[] hmacData = generateHMAC(hashData);
					hexHalfResult = generateHex(hmacData).substring(0, 20);
				} else {
					hexHalfResult = generateHex(hashData).substring(0, 20);
				}

				/*
				 * Append .H0k if complex mode is selected.
				 */
				if (complexCheckBox.isChecked()) {
					hexHalfResult += ".H0k";
				}

				/*
				 * Copy result to clipboard, set peek to first four characters,
				 * and make a toast of a success message
				 */
				copyToClipboard(hexHalfResult);
				peek.setText("Peek: " + hexHalfResult.substring(0, 4));
				Toast.makeText(getApplicationContext(),
						"Hex half password copied.", Toast.LENGTH_SHORT).show();
				System.out.println(hexHalfResult);
			}

			/*
			 * Generate the Base64 password for current sentence + word
			 */
			if (((Button) v).getText().equals("B64")) {
				String b64Result = null;
				byte[] hashData = generateHash();

				/*
				 * If HMAC mode is selected, generate HMAC, otherwise use SHA-1
				 * hash
				 */
				if (hmacCheckBox.isChecked()) {
					byte[] hmacData = generateHMAC(hashData);
					// substring is to fix some weirdness with new line
					b64Result = generateB64(hmacData).substring(0, 28);
				} else {
					b64Result = generateB64(hashData);
				}

				/*
				 * Append .H0k if complex mode is selected.
				 */
				if (complexCheckBox.isChecked()) {
					b64Result += ".H0k";
				}

				/*
				 * Copy result to clipboard, set peek to first four characters,
				 * and make a toast of a success message
				 */
				copyToClipboard(b64Result);
				peek.setText("Peek: " + b64Result.substring(0, 4));
				Toast.makeText(getApplicationContext(), "B64 password copied.",
						Toast.LENGTH_SHORT).show();
				System.out.println(b64Result);
			}

			/*
			 * Generate the Base64 half-length password for current sentence +
			 * word
			 */
			if (((Button) v).getText().equals("B64 Half")) {
				String b64HalfResult = null;
				byte[] hashData = generateHash();

				/*
				 * If HMAC mode is selected, generate HMAC, otherwise use SHA-1
				 * hash
				 */
				if (hmacCheckBox.isChecked()) {
					byte[] hmacData = generateHMAC(hashData);
					b64HalfResult = generateB64(hmacData).substring(0, 14);
				} else {
					b64HalfResult = generateB64(hashData).substring(0, 14);
				}

				/*
				 * Append .H0k if complex mode is selected.
				 */
				if (complexCheckBox.isChecked()) {
					b64HalfResult += ".H0k";
				}

				/*
				 * Copy result to clipboard, set peek to first four characters,
				 * and make a toast of a success message
				 */
				copyToClipboard(b64HalfResult);
				peek.setText("Peek: " + b64HalfResult.substring(0, 4));
				Toast.makeText(getApplicationContext(),
						"B64 half password copied.", Toast.LENGTH_SHORT).show();
				System.out.println(b64HalfResult);
			}

			/*
			 * Show about screen
			 */
			if (((Button) v).getText().equals("About")) {
				Intent intent = new Intent(this, AboutScreen.class);
				startActivity(intent);
			}
		} else {
			/*
			 * Take actions when view mode is toggled. Displays sentence and
			 * word in clear text when active.
			 */
			if (((CheckBox) v).getText().equals("View")) {
				if (viewCheckBox.isChecked()) {
					sentenceField.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					wordField.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

					Toast.makeText(getApplicationContext(), "View mode on.",
							Toast.LENGTH_SHORT).show();
					if (secureCheckBox.isChecked()) {
						secureCheckBox.setChecked(false);
					}
				} else {
					sentenceField.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);
					wordField.setInputType(InputType.TYPE_CLASS_TEXT
							| InputType.TYPE_TEXT_VARIATION_PASSWORD);

					Toast.makeText(getApplicationContext(), "View mode off.",
							Toast.LENGTH_SHORT).show();
				}
			}

			/*
			 * Take actions when secure mode is toggled. When active, disables
			 * view mode. When deactivated, clears sentence, word and peek.
			 */
			if (((CheckBox) v).getText().equals("Secure")) {
				if (secureCheckBox.isChecked()) {
					if (viewCheckBox.isChecked()) {
						viewCheckBox.setChecked(false);
						sentenceField.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						wordField.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_VARIATION_PASSWORD);
						Toast.makeText(getApplicationContext(),
								"Secure mode on.", Toast.LENGTH_SHORT).show();
					}
					viewCheckBox.setClickable(false);
				} else {
					sentenceField.setText("");
					wordField.setText("");
					copyToClipboard(null);
					peek.setText("Peek: ");
					viewCheckBox.setClickable(true);
					Toast.makeText(getApplicationContext(), "Secure mode off.",
							Toast.LENGTH_SHORT).show();
				}
			}

			/*
			 * Takes actions when HMAC mode is toggled. When active, creates a
			 * HMAC using the sentence + word as the key and "SHA1_Pass" as the
			 * message.
			 */
			if (((CheckBox) v).getText().equals("HMAC")) {
				if (hmacCheckBox.isChecked()) {
					Toast.makeText(getApplicationContext(), "HMAC mode on.",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(), "HMAC mode off.",
							Toast.LENGTH_SHORT).show();
				}
			}

			/*
			 * Appends ".H0k" to the end of the password string to meet
			 * complexity requirements
			 */
			if (((CheckBox) v).getText().equals("Complex")) {
				if (complexCheckBox.isChecked()) {
					Toast.makeText(getApplicationContext(), "Complex mode on.",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getApplicationContext(),
							"Complex mode off.", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}

	/**
	 * Generates the SHA-1 hash of the sentence + word
	 * 
	 * @return the SHA-1 hash data
	 */
	private byte[] generateHash() {
		String input = sentenceField.getText().toString()
				+ wordField.getText().toString();
		MessageDigest sha1 = null;
		try {
			sha1 = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			Log.e("sha1pass", "Error creating SHA1 hash.");
		}
		try {
			sha1.update(input.getBytes("ASCII"));
		} catch (UnsupportedEncodingException e) {
			Log.e("sha1pass", "Error hashing input");
		}
		byte[] data = sha1.digest();
		return data;
	}

	/**
	 * Generates the HMAC of the sentence + word
	 * 
	 * @return the HMAC data
	 */
	private byte[] generateHMAC(byte[] hashData) {
		Mac mac = null;
		String input = sentenceField.getText().toString()
				+ wordField.getText().toString();
		try {
			mac = Mac.getInstance("HmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			Log.e("sha1pass", "Error creating HMAC");
		}
		SecretKeySpec secret;
		if (input == "") {
			byte[] emptyByteArray = new byte[]{0};
			secret = new SecretKeySpec(emptyByteArray, mac.getAlgorithm());
		} else {
			secret = new SecretKeySpec(input.getBytes(), mac.getAlgorithm());
		}
		try {
			mac.init(secret);
		} catch (InvalidKeyException e) {
			Log.e("sha1pass", "Error initializing the secret key");
		}
		byte[] data = mac.doFinal(hmacMessage.getBytes());
		return data;
	}

	/**
	 * Generate the hex version of the password hash
	 * 
	 * @return the hex string
	 */
	private String generateHex(byte[] data) {
		// Can't find built in support for generating a hex version of the
		// hash, will use method found at http://preview.tinyurl.com/lrkdp7h.
		char[] hexArray = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		char[] hexChars = new char[data.length * 2];
		int v;
		for (int j = 0; j < data.length; j++) {
			v = data[j] & 0xFF;
			hexChars[j * 2] = hexArray[v / 16];
			hexChars[j * 2 + 1] = hexArray[v % 16];
		}
		return new String(hexChars);
	}

	/**
	 * Generate the Base64 version of the password hash
	 * 
	 * @return the Base64 string
	 */
	private String generateB64(byte[] data) {
		return Base64.encodeToString(data, Base64.DEFAULT);
	}

	/**
	 * This copies the password string to the clipboard. Depending on the
	 * version of android used, needs different method.
	 * 
	 * @param toCopy
	 *            the password string
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	private void copyToClipboard(String toCopy) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(toCopy);
		} else {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("SHA1PassResult", toCopy);
			clipboard.setPrimaryClip(clip);
		}
	}

}
