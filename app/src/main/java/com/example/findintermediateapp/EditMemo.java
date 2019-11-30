package com.example.findintermediateapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;

public class EditMemo extends ChangeStateBar {

    TextView tv_editMemoLocation;
    TextView tv_editMemoAddress;
    EditText et_editMemoContent;
    GridView gv_editMemoImage;
    String[] str_imageArr;
    Uri[] uri_imageArr;
    Uri[] uri_newImageArr;
    EditImageAdapter editImageAdapter;
    int memoId;
    public static final int KITKAT_VALUE = 1002;
    private static Uri filePath;
    static Uri[] uri_addedUri = new Uri[100];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        tv_editMemoLocation = findViewById(R.id.edit_memo_location);
        tv_editMemoAddress = findViewById(R.id.edit_memo_address);
        et_editMemoContent = findViewById(R.id.edit_memo_content);

        Toolbar toolbar = findViewById(R.id.edit_memo_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_left_arrow);

        tv_editMemoLocation.setText(getIntent().getStringExtra("edit_memo_location"));
        tv_editMemoAddress.setText(getIntent().getStringExtra("edit_memo_address"));
        et_editMemoContent.setText(getIntent().getStringExtra("edit_memo_content"));
        str_imageArr = getIntent().getStringArrayExtra("edit_memo_imageArr");
        uri_imageArr = new Uri[str_imageArr.length];
        for(int i = 0; i < str_imageArr.length; i++)
        {
            uri_imageArr[i] = Uri.parse(str_imageArr[i]);
        }

        editImageAdapter = new EditImageAdapter(EditMemo.this, this, uri_imageArr);
        gv_editMemoImage = findViewById(R.id.edit_memo_gridview);
        gv_editMemoImage.setAdapter(editImageAdapter);
        editImageAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_memo_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_addMemo:

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateMemo(long id) {

    }

    public class EditImageAdapter extends BaseAdapter {
        private Context context;

        Activity mActiviy = null;
        Uri[] memo_image = null;
        Uri[] new_image = null;

        public EditImageAdapter(Activity activity, Context con, Uri[] work) {
            this.context = con;
            this.mActiviy = activity;
            this.memo_image = work;
            new_image = new Uri[memo_image.length+1];
            for(int i = 0; i < memo_image.length; i++)
            {
                new_image[i] = memo_image[i];
            }
        }
        public int getCount() {
            return (null != new_image) ? new_image.length : 0;
        }

        public Object getItem(int pos) {
            return (null != new_image) ?  new_image.length : 0;
        }

        public long getItemId(int pos) {
            return pos;
        }

        public View getView(int pos, View convertView, ViewGroup parent) {

            for(int i = 0; i < new_image.length; i++) {
                Log.d("memo_imageaaa", String.valueOf(new_image[i]));
            }
            Log.d("memo_image.length gg", String.valueOf(new_image.length));
            ImageView imageView;

            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int screenWidth = metrics.widthPixels;

            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(screenWidth / 3 - 10, screenWidth / 3 - 10));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(3, 3, 3, 3);
            } else {
                imageView = (ImageView) convertView;
            }

            if (pos == new_image.length - 1) {
                imageView.setImageResource(R.drawable.add_work_no2);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent;
                        if (Build.VERSION.SDK_INT < 19) {
                            intent = new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("*/*");
                            mActiviy.startActivityForResult(intent, KITKAT_VALUE);
                        } else  {
                            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("*/*");
                            startActivityForResult(intent, KITKAT_VALUE);
                        }
                    }
                });
            } else {
                Log.d("아니아니아니아니아니","ㅁ");
                Glide.with(context).load(new_image[pos]).into(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });
            }
            return imageView;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == KITKAT_VALUE) {
            if (resultCode == RESULT_OK) {
                try {
                    int addcount = uri_imageArr.length;
                    addcount++;
                    filePath = data.getData();
                    Uri imageUri = Uri.parse(String.valueOf(filePath));
                    uri_newImageArr = new Uri[addcount+2];
                    uri_addedUri[addcount -1] = imageUri;
                    Log.d("uri_addedUri[addcount-1", String.valueOf(uri_addedUri[addcount-1]));
                    for(int i = 0; i < uri_newImageArr.length; i++)
                    {
                        if(i == uri_newImageArr.length-2)
                        {
                            uri_newImageArr[i] = imageUri;
                        } else {
                            uri_newImageArr[i] = uri_imageArr[i];
                        }
                    }

                    editImageAdapter = new EditImageAdapter(this, EditMemo.this, uri_newImageArr);
                    gv_editMemoImage.setAdapter(editImageAdapter);

                } catch (Exception e) {

                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "사진 선택 취소", Toast.LENGTH_LONG).show();
            }
        }
    }
}
