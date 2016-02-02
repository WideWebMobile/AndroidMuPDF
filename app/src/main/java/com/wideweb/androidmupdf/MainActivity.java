package com.wideweb.androidmupdf;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.artifex.mupdfdemo.FilePicker;
import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private MuPDFCore core;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout mContentView = (RelativeLayout) findViewById(R.id.content_view);

        try {
            InputStream ins = getResources().getAssets().open("MuPdf.pdf");
            FileOutputStream fos = new FileOutputStream(new File(getExternalCacheDir()+"/1.pdf"));
            byte[] b = new byte[1024];
            while(ins.read(b)!= -1){
                fos.write(b);
            }
            fos.flush();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Uri uri = Uri.parse(getExternalCacheDir()+"/1.pdf");
        core = openFile(uri.getPath());
        if (core != null && core.countPages() == 0) {
            core = null;
        }
        if (core == null || core.countPages() == 0 || core.countPages() == -1) {
            Log.e(TAG, "Document Not Opening");
        }
        if (core != null) {
            MuPDFReaderView mDocView = new MuPDFReaderView(this) {
                @Override
                protected void onMoveToChild(int i) {
                    if (core == null)
                        return;
                    super.onMoveToChild(i);
                }

            };
            mDocView.setAdapter(new MuPDFPageAdapter(this, new FilePicker.FilePickerSupport() {
                @Override
                public void performPickFor(FilePicker picker) {

                }
            }, core));
            mContentView.addView(mDocView);
        }
    }

    @Override
    public void onDestroy() {
        if (core != null)
            core.onDestroy();
        core = null;
        super.onDestroy();
    }

    private MuPDFCore openFile(String path) {
        try {
            core = new MuPDFCore(this, path);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return core;
    }
}
