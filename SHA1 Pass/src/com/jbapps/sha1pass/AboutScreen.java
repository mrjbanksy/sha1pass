package com.jbapps.sha1pass;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

// -------------------------------------------------------------------------
/**
 * Displays information about the app
 *
 * @author Jeremy Banks
 * @version May 11, 2014
 */
public class AboutScreen
    extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_screen);
        TextView about = (TextView)findViewById(R.id.about);
        about
            .setText("This is an Android port of SHA1_Pass v1.7 by and copyright"
                + " 16 Systems®. The original can be found at"
                + " http://16s.us/sha1_pass.\nThis is a sentence based password"
                + " generation program. Enter a sentence and a word. Then click"
                + " an encoding button to generate a strong, secure password"
                + " based on your input. Use different sentences and words to"
                + " generate different passwords.\nOriginal idea, concept and"
                + " program Copyright © 2010-2012, 16 Systems® All rights"
                + " reserved.\nAndroid port Copyright © 2013, Jeremy Banks\n"
                + "Both versions distributed under the GPL. Source for the"
                + " Android version can be found at"
                + " https://github.com/mrjbanksy/sha1pass\nThe icon for this app"
                + " was created by the Crystal Project, and can be found at"
                + " http://openiconlibrary.sourceforge.net/gallery2/?./Icons/actions/document-encrypt.png."
                + " It is used under the LGPL 2.1.");
    }
}
