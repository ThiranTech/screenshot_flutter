package com.thirantech.screenshotnative;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

import static java.lang.Thread.sleep;

public class MainActivity extends FlutterActivity  {
  private static final String CHANNEL = "team.native.io/screenshot";
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    StrictMode.VmPolicy.Builder builder = null;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      builder = new StrictMode.VmPolicy.Builder();
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
      StrictMode.setVmPolicy(builder.build());
    }
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);
    new MethodChannel(getFlutterView(), CHANNEL).setMethodCallHandler(
            new MethodCallHandler() {
              @Override
              public void onMethodCall(MethodCall call, Result result) {
                if (call.method.equals("takeScreenshot")) {
                    String fileUri =call.argument("filePath").toString();
                    Uri filePath=Uri.parse(fileUri);
                    //takeScreenshot();
                    sendMail(Uri.fromFile(new File(fileUri)));
                    result.success("Done");
                }else {
                    result.notImplemented();
                }

              }
            });
  }
  private void sendMail(Uri filePath){
    Intent intent = new Intent(Intent.ACTION_SEND);
    intent.setType("text/plain");
    intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
    intent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
    //Uri uri =Uri.parse(filePath);
    intent.putExtra(Intent.EXTRA_STREAM, filePath);
    startActivity(Intent.createChooser(intent, "Send email..."));
  }
  @TargetApi(Build.VERSION_CODES.CUPCAKE)
  private void takeScreenshot() {
    Date now = new Date();
    android.text.format.DateFormat.format("yyyy-MM-dd", now);

    try {
      // image naming and path  to include sd card  appending name you choose for file
      String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "Screeshot" + ".jpg";

      // create bitmap screen capture
      View v1 = getWindow().getDecorView().getRootView();
      v1.setDrawingCacheEnabled(true);
      v1.buildDrawingCache();
      sleep(5000);
      Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
      Rect frame = new Rect();
      getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
      int statusBarHeight = frame.top;

      //Find the screen dimensions to create bitmap in the same size.
      int width = this.getWindowManager().getDefaultDisplay().getWidth();
      int height = this.getWindowManager().getDefaultDisplay().getHeight();

      //Bitmap b = Bitmap.createBitmap(bitmap, 0, 0, width, height);

      File imageFile = new File(
              android.os.Environment.getExternalStorageDirectory(),"teamfiles");
      if(!imageFile.exists()) {
        imageFile.mkdirs();
      }

      String path =imageFile + "/screenshot.png";

      FileOutputStream outputStream = new FileOutputStream(path);
      int quality = 90;
      bitmap.compress(Bitmap.CompressFormat.PNG, quality, outputStream);
      outputStream.flush();
      outputStream.close();
      //openScreenshot(imageFile);
    } catch (Throwable e) {
      // Several error may come out with file handling or DOM
      e.printStackTrace();
    }
  }

}
