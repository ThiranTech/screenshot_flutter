package com.thirantech.screenshotnative;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;

import java.io.File;
import io.flutter.app.FlutterActivity;
import io.flutter.plugins.GeneratedPluginRegistrant;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;


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

}
