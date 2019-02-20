package com.heyao216.react_native_installapk;

import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Binder;
import android.support.v4.content.FileProvider;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;

/**
 * Created by heyao on 2016/11/4.
 */
public class InstallApkModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext _context = null;

    public InstallApkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        _context = reactContext;
    }

    @Override
    public String getName() {
        return "InstallApk";
    }

    @ReactMethod
    public void install(String path) {
        String cmd = "chmod 777 " +path;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File file = new File(path);
        
        if (Build.VERSION.SDK_INT >= 24) {
          String packageName = _context.getPackageManager().getNameForUid(Binder.getCallingUid());

          Uri fileUri = FileProvider.getUriForFile(_context, packageName + ".provider", file);
          Intent intent = new Intent(Intent.ACTION_INSTALL_PACKAGE);

          intent.setData(fileUri);
          intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
          _context.startActivity(intent);
        } else {
          Uri fileUri = Uri.fromFile(file);
          Intent intent = new Intent(Intent.ACTION_VIEW);
          intent.setDataAndType(fileUri, "application/vnd.android.package-achive");
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

          _context.startActivity(intent);
        }
    }
}
