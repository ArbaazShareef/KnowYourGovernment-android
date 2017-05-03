package com.arbaaz.knowyourgovernment;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity implements View.OnClickListener {
    private Official official;
   private TextView LocationTV,TitleTV,NameTV,PartyTV,AddressTV,PhnoTV,WebsiteTV,EmailTV;
    ImageView Official,Youtube,Facebook,Twitter,Gplus;
    private  String picurl ;
    private ConstraintLayout cs;
    private  String Location;
    @Override
    public void onBackPressed() {
       /* Intent back = new Intent(this,MainActivity.class);
        back.putExtra("LOC",Location);
        this.startActivity(back);
        */
        super.onBackPressed();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent back = new Intent(this,MainActivity.class);
                back.putExtra("LOC",Location);
                this.startActivity(back);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_official);
        Intent intent = getIntent();
        Location = intent.getStringExtra("Location");
        cs = (ConstraintLayout)findViewById(R.id.OfficialBaground);
        official = (Official) getIntent().getSerializableExtra("Official");
        if(official.getParty().isEmpty()){
            official.setParty("Other");
        }
        LocationTV = (TextView)findViewById(R.id.LocationOfficialActivity);
        LocationTV.setText(Location);
        TitleTV = (TextView)findViewById(R.id.Title);
        TitleTV.setText(official.getTitle());
        NameTV = (TextView)findViewById(R.id.NameViewOfficialActivity);
        NameTV.setText(official.getName());
        EmailTV =(TextView)findViewById(R.id.EmailViewOfficialActivity);
        EmailTV.setText(official.getEmail());
        PartyTV = (TextView)findViewById(R.id.PartyViewOfficialActivity);
        PartyTV.setText(official.getParty());
        AddressTV = (TextView)findViewById(R.id.AddressViewOfficialActivity);
        AddressTV.setText(official.getAddress());
        PhnoTV = (TextView)findViewById(R.id.phnoViewOfficialActivity);
        PhnoTV.setText(official.getPhno());
        WebsiteTV =(TextView)findViewById(R.id.WebsiteViewOfficialActivity);
        WebsiteTV.setText(official.getWebsite());
        Official = (ImageView)findViewById(R.id.OfficialImage);
        Youtube =(ImageView)findViewById(R.id.youtube);
        Facebook = (ImageView)findViewById(R.id.facebook);
        Gplus = (ImageView)findViewById(R.id.gplus);
        Twitter = (ImageView)findViewById(R.id.twitter);
        AddressTV.setPaintFlags(AddressTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        PhnoTV.setPaintFlags(PhnoTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        EmailTV.setPaintFlags(EmailTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        WebsiteTV.setPaintFlags(WebsiteTV.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        if(official.getParty().equalsIgnoreCase("Republican")){
            cs.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }else if (official.getParty().equalsIgnoreCase("Democratic")){
            cs.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        }else{
            cs.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        }

        picurl = official.getPhotoURL();
        if ( official.getYoutubeUrl().isEmpty()){
            Youtube.setVisibility(View.INVISIBLE);

        }
        if (official.getFacebookURL().isEmpty()){
            Facebook.setVisibility(View.INVISIBLE);
        }
        if (official.getGooglePlusURL().isEmpty()){
            Gplus.setVisibility(View.INVISIBLE);
        }
        if (official.getTwitter().isEmpty()){
            Twitter.setVisibility(View.INVISIBLE);
        }

        if(picurl != null && !picurl.isEmpty()){
               Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                   @Override
                   public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
// Here we try https if the http image attempt failed

                       final String changedUrl =picurl.replace("http:", "https:");
                       picasso.load(changedUrl)
                               .error(R.drawable.brokenimage)
                               .placeholder(R.drawable.placeholder)
                               .into(Official);
                   }
               }).build();
               picasso.load(picurl)
                       .error(R.drawable.brokenimage)
                       .placeholder(R.drawable.placeholder)
                       .into(Official);

           }
            else {
          Official.setImageDrawable(getResources().getDrawable(R.drawable.missingimage));
        }

        Facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String FACEBOOK_URL = "https://www.facebook.com/" + official.getFacebookURL();
                String urlToUse;
                PackageManager packageManager = getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //newer versions of fb app
                        urlToUse = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    } else { //older versions of fb app
                        urlToUse = "fb://page/" + official.getFacebookURL();
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    urlToUse = FACEBOOK_URL; //normal web url
                }
                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                facebookIntent.setData(Uri.parse(urlToUse));
                startActivity(facebookIntent);
            }
        });

        Twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Tintent = null;
                String Tname = official.getTwitter();
                try {
                    // get the Twitter app if possible
                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                    Tintent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + Tname));
                    Tintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    // no Twitter app, revert to browser
                    Tintent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + Tname));
                }
                startActivity(Tintent);

            }
        });
        Gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Gname = official.getGooglePlusURL();
                Intent Gintent = null;
                try {
                    Gintent = new Intent(Intent.ACTION_VIEW);
                    Gintent.setClassName("com.google.android.apps.plus",
                            "com.google.android.apps.plus.phone.UrlGatewayActivity");
                    Gintent.putExtra("customAppUri", Gname);
                    startActivity(Gintent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://plus.google.com/" + Gname)));
                }
            }
        });

        Youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Yname =official.getYoutubeUrl();
                Intent Yintent = null;
                try {
                    Yintent = new Intent(Intent.ACTION_VIEW);
                    Yintent.setPackage("com.google.android.youtube");
                    Yintent.setData(Uri.parse("https://www.youtube.com/" + Yname));
                    startActivity(Yintent);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.youtube.com/" + Yname)));
                }
            }
        });
        AddressTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String address = AddressTV.getText().toString();

                Uri mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(address));

                Intent intent = new Intent(Intent.ACTION_VIEW, mapUri);
                intent.setPackage("com.google.android.apps.maps");

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    makeErrorAlert("No Application found that handles ACTION_VIEW (geo) intents");
                }
            }
        });

        PhnoTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = PhnoTV.getText().toString();

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));

                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    makeErrorAlert("No Application found that handles ACTION_DIAL (tel) intents");
                }
            }
        });
        EmailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] addresses = new String[]{EmailTV.getText().toString()};

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, addresses);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Email from Sample Implied Intent App");
                intent.putExtra(Intent.EXTRA_TEXT, "Email text body...");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    makeErrorAlert("No Application found that handles SENDTO (mailto) intents");
                }
            }
        });
        WebsiteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = WebsiteTV.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        Official.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               gotoPhoto();
            }
        });
    }

    private void gotoPhoto() {
        Intent run = new Intent(this,PhotoDetailActivity.class);
        run.putExtra("Official",official);
        run.putExtra("Location",LocationTV.getText());
        this.startActivity(run);
    }

    @Override
    public void onClick(View view) {

    }
    private void makeErrorAlert(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setTitle("No App Found");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
