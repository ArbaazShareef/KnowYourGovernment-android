package com.arbaaz.knowyourgovernment;

import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {
    private ImageView picView;
    private TextView Title,Name,LLocation;
    private String picurl;
    private ConstraintLayout pccs;
    Official official;
    private String Location;
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
        setContentView(R.layout.activity_photo_detail);
        Intent intent = getIntent();
        pccs = (ConstraintLayout)findViewById(R.id.picLayout);
         Location = intent.getStringExtra("Location");
        official = (Official) getIntent().getSerializableExtra("Official");
        picView = (ImageView)findViewById(R.id.PicView);
        LLocation = (TextView)findViewById(R.id.piclocation);
        LLocation.setText(Location);
        Title = (TextView)findViewById(R.id.title);
        Name = (TextView)findViewById(R.id.name);
        Title.setText(official.getTitle());
        Name.setText(official.getName());
        picurl = official.getPhotoURL().toString();

        if(official.getParty().equalsIgnoreCase("Republican")){
            pccs.setBackgroundColor(getResources().getColor(R.color.colorRed));
        }else if (official.getParty().equalsIgnoreCase("Democratic")){
            pccs.setBackgroundColor(getResources().getColor(R.color.colorBlue));
        }else{
            pccs.setBackgroundColor(getResources().getColor(R.color.colorBlack));
        }

        if(picurl != null && !picurl.isEmpty()){
          //  Toast.makeText(this, picurl, Toast.LENGTH_SHORT).show();
            Picasso picasso = new Picasso.Builder(this).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
// Here we try https if the http image attempt failed

                    final String changedUrl =picurl.replace("http:", "https:");
                    picasso.load(changedUrl)
                            .error(R.drawable.brokenimage)
                            .placeholder(R.drawable.placeholder)
                            .into(picView);
                }
            }).build();
            picasso.load(picurl)
                    .error(R.drawable.brokenimage)
                    .placeholder(R.drawable.placeholder)
                    .into(picView);
          //  Toast.makeText(this, "Pass", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Image Loading Please Wait for some time", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            picView.setImageDrawable(getResources().getDrawable(R.drawable.missingimage));
        }
    }
}
