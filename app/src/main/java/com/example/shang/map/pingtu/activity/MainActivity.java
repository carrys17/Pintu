package com.example.shang.map.pingtu.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.shang.map.pingtu.R;
import com.example.shang.map.pingtu.adapter.GridPicListAdapter;
import com.example.shang.map.pingtu.utils.ScreenUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {

    private GridView mGvPicList;
    private List<Bitmap> mPicList;
    private int[] mResPicId;

    // 显示Type
    private TextView mTvPuzzleMainTypeSelected;
    private PopupWindow mPopupWindow;
    private View mPopupView;
    private static final int RESULT_IMAGME = 100;
    private static final String IMAGE_TYPE = "image/*";
    public static String TEMP_IMAGE_PATH;
    private static final int RESULT_CAMERA =200;

    private int mType = 2;
    private String[] mCustomItems = new String[]{"本地图册", "相机拍照"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //要在2之前
        setContentView(R.layout.activity_main); // 2
        mPicList = new ArrayList<>(); //记得初始化
        TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/temp.png";

        initView();
        //为表格view设置适配器
        mGvPicList.setAdapter(new GridPicListAdapter(MainActivity.this,mPicList));
        //表格view的监听
        mGvPicList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mResPicId.length -1){ //选到最后的自定义
                    showDialogCustom();
                }else {    // 选择默认图片
                    Intent intent = new Intent(MainActivity.this,PuzzleMain.class);
                    intent.putExtra("picSelectedID",mResPicId[position]);
                    intent.putExtra("mType",mType);
                    startActivity(intent);
                }
            }
        });

        //显示难度Type
        mTvPuzzleMainTypeSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupShow(v);
            }
        });
    }

    // 显示选择系统图库 相机对话框
    private void showDialogCustom() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("选择：")
                .setItems(mCustomItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ( 0 == which){ //本地图册
                            Intent intent = new Intent(Intent.ACTION_PICK,null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_TYPE); // IMAGE_TYPE= "image/*"
                            startActivityForResult(intent,RESULT_IMAGME);
                        }
                        else if( 1 == which ){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            Uri photoUri  = Uri.fromFile(new File(TEMP_IMAGE_PATH));//TEMP_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/temp.png";
                            intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                            startActivityForResult(intent,RESULT_CAMERA);
                        }
                    }
                })
                .create().show();
    }

    // 获取选中的图片、拍照的图片
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){ // -1
            if (requestCode == RESULT_IMAGME && data != null){
                //相册
                Cursor cursor = this.getContentResolver().query(data.getData(),null,null,null,null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(cursor.getColumnIndex("_data"));
                    Intent intent = new Intent(MainActivity.this,PuzzleMain.class);
                    intent.putExtra("mPicPath",imagePath);
                    intent.putExtra("mType",mType);
                    cursor.close();
                    startActivity(intent);
                }
            }
            else if (requestCode == RESULT_CAMERA ){
                //相机
                Intent intent = new Intent(MainActivity.this,PuzzleMain.class);
                intent.putExtra("mPicPath",TEMP_IMAGE_PATH);
                intent.putExtra("mType",mType);
                startActivity(intent);
            }
        }
    }

    //初始化 view
    private void initView() {
        mGvPicList = (GridView) findViewById(R.id.gv_xpuzzle_main_pic_list);
        mResPicId = new int[]{
                R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,R.drawable.pic4,
                R.drawable.pic5,R.drawable.pic6,R.drawable.pic7,R.drawable.pic8,
                R.drawable.pic9,R.drawable.pic10,R.drawable.pic11,R.drawable.pic12,
                R.drawable.pic13,R.drawable.pic14,R.drawable.pic15,R.drawable.plus
        };
        Bitmap[] bitmaps = new Bitmap[mResPicId.length];
        for (int i = 0;i<bitmaps.length;i++){
            bitmaps[i] = BitmapFactory.decodeResource(getResources(),mResPicId[i]);
            mPicList.add(bitmaps[i]);
        }

        //显示Type
        mTvPuzzleMainTypeSelected = (TextView) findViewById(R.id.tv_puzzle_main_type_selected);
        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        mPopupView = mLayoutInflater.inflate(R.layout.xpuzzle_main_type_selected,null);
        TextView mTvType2 = (TextView) mPopupView.findViewById(R.id.tv_main_type_2);
        TextView mTvType3 = (TextView) mPopupView.findViewById(R.id.tv_main_type_3);
        TextView mTvType4 = (TextView) mPopupView.findViewById(R.id.tv_main_type_4);
        mTvType2.setOnClickListener(this);
        mTvType3.setOnClickListener(this);
        mTvType4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_main_type_2:
                mType = 2;
                mTvPuzzleMainTypeSelected.setText("2 X 2");
                break;
            case R.id.tv_main_type_3:
                mType = 3;
                mTvPuzzleMainTypeSelected.setText("3 X 3");
                break;
            case R.id.tv_main_type_4:
                mType = 4;
                mTvPuzzleMainTypeSelected.setText("4 X 4");
                break;
        }
        mPopupWindow.dismiss();
    }

    public void popupShow(View view){
        int density = (int) ScreenUtil.getDeviceDensity(this);
        mPopupWindow = new PopupWindow(mPopupView,200*density,50*density);//view,width,height
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true); //这个方法时设置popupWindow以外的区域可以相应触摸事件

        Drawable transparent = new ColorDrawable(Color.TRANSPARENT);// 设置透明背景，不设的话可能会导致一些比较奇怪的问题，
        mPopupWindow.setBackgroundDrawable(transparent);  // 不设置背景就不能响应返回键和点击外部消失的

        int[]location = new int[2];
        view.getLocationOnScreen(location);
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY,location[0],location[1]+30*density);
    }


}
