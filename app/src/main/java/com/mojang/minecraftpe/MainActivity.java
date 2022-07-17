//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.mojang.minecraftpe;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.NativeActivity;
import android.app.ActivityManager.MemoryInfo;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.SystemClock;
import android.os.Vibrator;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnKeyListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import androidx.annotation.Keep;

import com.appboy.Appboy;
import com.mojang.android.StringValue;
import com.mojang.minecraftpe.TextInputProxyEditTextbox.MCPEKeyWatcher;
import com.mojang.minecraftpe.input.InputDeviceManager;
import com.mojang.minecraftpe.platforms.Platform;
import com.mojang.minecraftpe.python.PythonPackageLoader;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.fmod.FMOD;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressWarnings("JavaJniMissingFunction")
public class MainActivity extends NativeActivity implements OnKeyListener, FilePickerManagerHandler {
    private static final String TAG = MainActivity.class.getCanonicalName();
    public static int RESULT_PICK_IMAGE;
    private static boolean _isPowerVr;
    private static boolean mHasStoragePermission;
    @Keep
    public static int RESULT_GOOGLEPLAY_PURCHASE;
    public static MainActivity mInstance;
    private final DateFormat DateFormat = new SimpleDateFormat();
    private BrazeMessageManagerListener _brazeMessageManagerListener;
    private boolean _fromOnCreate;
    private int _userInputStatus = -1;
    private String[] _userInputText;
    private ArrayList<StringValue> _userInputValues = new ArrayList<>();
    private ClipboardManager clipboardManager;
    private Bundle data;
    private InputDeviceManager deviceManager;
    private Locale initialUserLocale;
    private boolean isRunningInAppCenter = false;
    List<ActivityListener> mActivityListeners = new ArrayList();
    private BatteryMonitor mBatteryMonitor;
    MainActivity.MessageConnectionStatus mBound = MainActivity.MessageConnectionStatus.NOTSET;
    MemoryInfo mCachedMemoryInfo = new MemoryInfo();
    long mCachedMemoryInfoUpdateTime;
    long mCachedUsedMemory;
    long mCachedUsedMemoryUpdateTime;
    private long mCallback;
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName var1, IBinder var2) {
            MainActivity.this.mService = new Messenger(var2);
            MainActivity.this.mBound = MainActivity.MessageConnectionStatus.CONNECTED;
            Message var4 = Message.obtain((Handler) null, 672, 0, 0);
            var4.replyTo = MainActivity.this.mMessenger;

            try {
                MainActivity.this.mService.send(var4);
            } catch (RemoteException var3) {
            }

        }

        public void onServiceDisconnected(ComponentName var1) {
            MainActivity.this.mService = null;
            MainActivity.this.mBound = MainActivity.MessageConnectionStatus.DISCONNECTED;
        }
    };
    private boolean mCursorLocked = false;
    private AlertDialog mDialog;
    private long mFileDialogCallback = 0L;
    private FilePickerManager mFilePickerManager;
    private HardwareInformation mHardwareInformation;
    private boolean mIsSoftKeyboardVisible = false;
    public int mLastPermissionRequestReason;
    final Messenger mMessenger = new Messenger(new MainActivity.IncomingHandler());
    public ParcelFileDescriptor mPickedFileDescriptor;
    private Messenger mService = null;
    private ArrayList<SessionInfo> mSessionHistory;
    private ThermalMonitor mThermalMonitor;
    private float mVolume = 1.0F;
    private WorldRecovery mWorldRecovery;
    private Platform platform;
    public TextInputProxyEditTextbox textInputWidget;
    private TextToSpeech textToSpeechManager;
    public int virtualKeyboardHeight = 0;

    public void startPlayIntegrityCheck(){}

    private void copyFile(InputStream var1, OutputStream var2) throws IOException {
        byte[] var3 = new byte[1024];

        for (int var4 = var1.read(var3); var4 != -1; var4 = var1.read(var3)) {
            var2.write(var3, 0, var4);
        }

        var1.close();
        var2.close();
    }

    @Keep
    public void enableBrazeSDK(){
    }

    @Keep
    public void setBrazeID(String id){
    }

    @Keep
    public void disableBrazeSDK(){
    }

    @Keep
    public long initializeLibHttpClient(final long hcInitArgs) {
        FutureTask<Long> futureTask = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return nativeInitializeLibHttpClient(hcInitArgs);
            }
        });
        runOnUiThread(futureTask);
        try {
            return futureTask.get();
        } catch (Exception e) {
            Log.e("MCPE", "Failed to initialize libHttpClient: '" + e.getMessage() + "'");
            return -2147467259L;
        }
    }

    @Keep
    public long initializeXboxLive(final long xalInitArgs, final long xblInitArgs) {
        if (isEduMode()) {
            return 0L;
        }
        FutureTask<Long> futureTask = new FutureTask<>(new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return nativeInitializeXboxLive(xalInitArgs, xblInitArgs);
            }
        });
        runOnUiThread(futureTask);
        try {
            return futureTask.get();
        } catch (Exception e) {
            Log.e("MCPE", "Failed to initialize xbox live: '" + e.getMessage() + "'");
            return -2147467259L;
        }
    }


    @Keep
    public CrashManager initializeCrashManager(String crashDumpFolder, String sessionId) {
        return CrashManager.getInstance();
    }

    @Keep
    public int isExternalStorageLegacy_debug() {
        return -1;
    }

    private void createAlertDialog(boolean var1, boolean var2, boolean var3) {
        android.app.AlertDialog.Builder var4 = new android.app.AlertDialog.Builder(this);
        var4.setTitle("");
        if (var3) {
            var4.setCancelable(false);
        }

        var4.setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface var1) {
                MainActivity.this.onDialogCanceled();
            }
        });
        if (var1) {
            var4.setPositiveButton("Ok", new OnClickListener() {
                public void onClick(DialogInterface var1, int var2) {
                    MainActivity.this.onDialogCompleted();
                }
            });
        }

        if (var2) {
            var4.setNegativeButton("Cancel", new OnClickListener() {
                public void onClick(DialogInterface var1, int var2) {
                    MainActivity.this.onDialogCanceled();
                }
            });
        }

        AlertDialog var5 = var4.create();
        this.mDialog = var5;
        var5.setOwnerActivity(this);
    }

    private void dismissTextWidget() {
        if (this.isTextWidgetActive()) {
            this.getInputMethodManager().hideSoftInputFromWindow(this.textInputWidget.getWindowToken(), 0);
            this.textInputWidget.setInputType(524288);
            this.textInputWidget.setVisibility(View.GONE);
        }

    }

    private InputMethodManager getInputMethodManager() {
        return this.getSystemService(InputMethodManager.class);
    }

    public static boolean isPowerVR() {
        return _isPowerVr;
    }

    private boolean isTextWidgetActive() {
        return this.textInputWidget != null && this.textInputWidget.getVisibility() == View.VISIBLE;
    }

    private static native void nativeWaitCrashManagementSetupComplete();

    private void onDialogCanceled() {
        this._userInputStatus = 0;
    }

    private void onDialogCompleted() {
        int var1 = this._userInputValues.size();
        this._userInputText = new String[var1];
        byte var2 = 0;

        int var3;
        for (var3 = 0; var3 < var1; ++var3) {
            this._userInputText[var3] = ((StringValue) this._userInputValues.get(var3)).getStringValue();
        }

        String[] var4 = this._userInputText;
        var1 = var4.length;

        for (var3 = var2; var3 < var1; ++var3) {
            String var5 = var4[var3];
            PrintStream var6 = System.out;
            StringBuilder var7 = new StringBuilder();
            var7.append("js: ");
            var7.append(var5);
            var6.println(var7.toString());
        }

        this._userInputStatus = 1;
        ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(this.getCurrentFocus(), 1);
    }

    private void processIntent(Intent intent) {
        IOException e;
        StringBuilder sb;
        if (intent != null) {
            String stringExtra = intent.getStringExtra("intent_cmd");
            if (stringExtra == null || stringExtra.length() <= 0) {
                String action = intent.getAction();
                intent.getType();
                if ("xbox_live_game_invite".equals(action)) {
                    String stringExtra2 = intent.getStringExtra("xbl");
                    Log.d("MCPE", "[XboxLive] Received Invite " + stringExtra2);
                    nativeProcessIntentUriQuery(action, stringExtra2);
                } else if ("android.intent.action.VIEW".equals(action) || "org.chromium.arc.intent.action.VIEW".equals(action)) {
                    String scheme = intent.getScheme();
                    Uri data = intent.getData();
                    if (data != null) {
                        if ("minecraft".equalsIgnoreCase(scheme) || "minecraftedu".equalsIgnoreCase(scheme)) {
                            String host = data.getHost();
                            String query = data.getQuery();
                            if (host != null || query != null) {
                                nativeProcessIntentUriQuery(host, query);
                            }
                        } else if ("file".equalsIgnoreCase(scheme)) {
                            nativeProcessIntentUriQuery("fileIntent", data.getPath() + "&" + data.getPath());
                        } else if ("content".equalsIgnoreCase(scheme)) {
                            String name = new File(data.getPath()).getName();
                            File file = new File(getApplicationContext().getCacheDir() + "/" + name);
                            try {
                                InputStream openInputStream = getContentResolver().openInputStream(data);
                                try {
                                    try {
                                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                                        byte[] bArr = new byte[1048576];
                                        while (true) {
                                            int read = openInputStream.read(bArr);
                                            if (read != -1) {
                                                fileOutputStream.write(bArr, 0, read);
                                            } else {
                                                fileOutputStream.close();
                                                nativeProcessIntentUriQuery("contentIntent", data.getPath() + "&" + file.getAbsolutePath());
                                                try {
                                                    openInputStream.close();
                                                    return;
                                                } catch (IOException e2) {
                                                    e = e2;
                                                    sb = new StringBuilder();
                                                    sb.append("IOException while closing input stream\n");
                                                    sb.append(e.toString());
                                                    Log.e("MCPE", sb.toString());
                                                }
                                            }
                                        }
                                    } catch (IOException e3) {
                                        Log.e("MCPE", "IOException while copying file from content intent\n" + e3.toString());
                                        try {
                                            file.delete();
                                        } catch (Exception unused) {
                                        }
                                        try {
                                            openInputStream.close();
                                        } catch (IOException e4) {
                                            e = e4;
                                            sb = new StringBuilder();
                                            sb.append("IOException while closing input stream\n");
                                            sb.append(e.toString());
                                            Log.e("MCPE", sb.toString());
                                        }
                                    }
                                } catch (Throwable th) {
                                    try {
                                        openInputStream.close();
                                    } catch (IOException e5) {
                                        Log.e("MCPE", "IOException while closing input stream\n" + e5.toString());
                                    }
                                    throw th;
                                }
                            } catch (IOException e6) {
                                Log.e("MCPE", "IOException while opening file from content intent\n" + e6.toString());
                            }
                        }
                    }
                }
            } else {
                try {
                    JSONObject jSONObject = new JSONObject(stringExtra);
                    String string = jSONObject.getString("Command");
                    if (string.equals("keyboardResult")) {
                        nativeSetTextboxText(jSONObject.getString("Text"));
                    } else if (string.equals("fileDialogResult") && this.mFileDialogCallback != 0) {
                        if (jSONObject.getString("Result").equals("Ok")) {
                            nativeOnPickImageSuccess(this.mFileDialogCallback, jSONObject.getString("Path"));
                        } else {
                            nativeOnPickImageCanceled(this.mFileDialogCallback);
                        }
                        this.mFileDialogCallback = 0L;
                    }
                } catch (JSONException e7) {
                    Log.d("MCPE", "JSONObject exception:" + e7.toString());
                }
            }
        }
    }

    public static void saveScreenshot(String param0, int param1, int param2, int[] param3) {
    }

    public String MC_CheckIfTestsAreFinished() {
        return this.isTestInfrastructureDisabled() ? "" : this.nativeCheckIfTestsAreFinished();
    }

    public String MC_GetActiveScreen() {
        return this.isTestInfrastructureDisabled() ? "" : this.nativeGetActiveScreen();
    }

    public String MC_GetDevConsoleLogName() {
        return this.isTestInfrastructureDisabled() ? "" : this.nativeGetDevConsoleLogName();
    }

    public String MC_GetLogText(String var1) {
        return this.isTestInfrastructureDisabled() ? "" : this.nativeGetLogText(var1);
    }

    public String MC_SetOptions(String var1) {
        return this.isTestInfrastructureDisabled() ? "" : this.nativeSetOptions(var1);
    }

    public void addListener(ActivityListener var1) {
        this.mActivityListeners.add(var1);
    }

    public void buyGame() {
    }

    public long calculateAvailableDiskFreeSpace(String var1) {
        long var3;
        int var5;
        int var6;
        try {
            StatFs var2 = new StatFs(var1);
            if (VERSION.SDK_INT >= 18) {
                var3 = var2.getAvailableBytes();
                return var3;
            }

            var5 = var2.getAvailableBlocks();
            var6 = var2.getBlockSize();
        } catch (Exception var7) {
            var3 = 0L;
            return var3;
        }

        var3 = (long) (var5 * var6);
        return var3;
    }

    @Keep
    public int checkLicense() {
        return 0;
    }

    public void clearLoginInformation() {
        Editor var1 = PreferenceManager.getDefaultSharedPreferences(this).edit();
        var1.remove("accessToken");
        var1.remove("clientId");
        var1.remove("profileId");
        var1.remove("profileName");
        var1.apply();
    }

    @Keep
    public void copyFromPickedFile(String var1) {

    }

    @Keep
    public void copyToPickedFile(String var1) {
    }

    public Intent createAndroidLaunchIntent() {
        Context var1 = this.getApplicationContext();
        return var1.getPackageManager().getLaunchIntentForPackage(var1.getPackageName());
    }

    public void createTextWidget() {
        textInputWidget = new TextInputProxyEditTextbox(this);
        textInputWidget.setVisibility(View.GONE);
        textInputWidget.setFocusable(true);
        textInputWidget.setFocusableInTouchMode(true);
        textInputWidget.setImeOptions(268435461);
        textInputWidget.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView var1x, int var2, KeyEvent var3) {
                boolean var4 = false;
                boolean var5;
                if (var2 == 5) {
                    var5 = true;
                } else {
                    var5 = false;
                }

                boolean var6;
                if (var2 == 0 && var3 != null && var3.getAction() == 0) {
                    var6 = true;
                } else {
                    var6 = false;
                }

                if (!var5 && !var6) {
                    if (var2 != 7) {
                        return var4;
                    }

                    MainActivity.this.nativeBackPressed();
                } else {
                    if (var5) {
                        MainActivity.this.nativeReturnKeyPressed();
                    }

                    String var8;
                    label40:
                    {
                        var8 = textInputWidget.getText().toString();
                        int var10 = textInputWidget.getSelectionEnd();
                        if (var10 >= 0) {
                            var2 = var10;
                            if (var10 <= var8.length()) {
                                break label40;
                            }
                        }

                        var2 = var8.length();
                    }

                    if ((131072 & textInputWidget.getInputType()) != 0) {
                        var5 = true;
                    } else {
                        var5 = false;
                    }

                    if (var5) {
                        StringBuilder var9 = new StringBuilder();
                        var9.append(var8.substring(0, var2));
                        var9.append("\n");
                        var9.append(var8.substring(var2, var8.length()));
                        var8 = var9.toString();
                        textInputWidget.setText(var8);
                        var2 = Math.min(var2 + 1, textInputWidget.getText().length());
                        textInputWidget.setSelection(var2);
                    }
                }

                var4 = true;
                return var4;
            }
        });
        textInputWidget.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable editable) {
                if (textInputWidget.shouldSendText()) {
                    MainActivity.this.nativeSetTextboxText(editable.toString());
                    textInputWidget.updateLastSentText();
                }

            }

            public void beforeTextChanged(CharSequence var1x, int var2, int var3, int var4) {
            }

            public void onTextChanged(CharSequence var1x, int var2, int var3, int var4) {
            }
        });
        textInputWidget.setOnMCPEKeyWatcher(new MCPEKeyWatcher() {
            public boolean onBackKeyPressed() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        MainActivity.this.nativeBackPressed();
                    }
                });
                return true;
            }

            public void onDeleteKeyPressed() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        MainActivity.this.nativeBackSpacePressed();
                    }
                });
            }
        });
        ((ViewGroup) this.findViewById(android.R.id.content)).addView(textInputWidget, new LayoutParams(1, 1));
        View var2 = this.findViewById(android.R.id.content).getRootView();
        var2.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                Rect var1 = new Rect();
                var2.getWindowVisibleDisplayFrame(var1);
                MainActivity.this.virtualKeyboardHeight = var2.getRootView().getHeight() - var1.height();
                if (var2.getRootView().getHeight() - var1.bottom > 0) {
                    if (!MainActivity.this.mIsSoftKeyboardVisible) {
                        MainActivity.this.mIsSoftKeyboardVisible = true;
                    }
                } else if (MainActivity.this.mIsSoftKeyboardVisible) {
                    MainActivity.this.mIsSoftKeyboardVisible = false;
                    MainActivity.this.onSoftKeyboardClosed();
                }

            }
        });
    }

    @Keep
    public String createUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public void deviceIdCorrelationStart() {
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent var1) {
        if (this.mCursorLocked && (var1.getSource() & 8194) == 8194) {
            this.lockCursor();
        }

        return super.dispatchGenericMotionEvent(var1);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent var1) {
        return this.nativeKeyHandler(var1.getKeyCode(), var1.getAction()) ? true : super.dispatchKeyEvent(var1);
    }

    @Keep
    public void displayDialog(int var1) {
    }

    @Keep
    public int getAPIVersion(String str) {
        try {
            return Build.VERSION_CODES.class.getField(str).getInt(null);
        } catch (Throwable unused) {
            return -1;
        }
    }

    @Keep
    public String getAccessToken() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("accessToken", "");
    }

    @Keep
    public int getAndroidVersion() {
        return VERSION.SDK_INT;
    }

    @Keep
    public BatteryMonitor getBatteryMonitor() {
        if (this.mBatteryMonitor == null) {
            this.mBatteryMonitor = new BatteryMonitor();
        }
        return this.mBatteryMonitor;
    }

    @Keep
    public String[] getBroadcastAddresses() {
        ArrayList arrayList = new ArrayList();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface nextElement = networkInterfaces.nextElement();
                if (!nextElement.isLoopback()) {
                    for (InterfaceAddress interfaceAddress : nextElement.getInterfaceAddresses()) {
                        if (interfaceAddress.getBroadcast() != null) {
                            arrayList.add(interfaceAddress.getBroadcast().toString().substring(1));
                        }
                    }
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return (String[]) arrayList.toArray(new String[arrayList.size()]);
    }

    @Keep
    public String getClientId() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("clientId", "");
    }

    @Keep
    public int getCursorPosition() {
        return !this.isTextWidgetActive() ? -1 : this.textInputWidget.getSelectionStart();
    }

    @Keep
    public String getDeviceModel() {
        return HardwareInformation.getDeviceModelName();
    }

    @Keep
    public int getDisplayHeight() {
        if (this.getAndroidVersion() >= 17) {
            Point var1 = new Point();
            this.getWindowManager().getDefaultDisplay().getRealSize(var1);
            return var1.y;
        } else {
            return 0;
        }
    }

    @Keep
    public int getDisplayWidth() {
        if (this.getAndroidVersion() >= 17) {
            Point var1 = new Point();
            this.getWindowManager().getDefaultDisplay().getRealSize(var1);
            return var1.x;
        } else {
            return 0;
        }
    }

    @Keep
    public String getExternalStoragePath() {
        return this.getExternalFilesDir((String) null).getAbsolutePath();
    }

    @Keep
    public byte[] getFileDataBytes(String str) {
        byte[] bArr = new byte[16384];
        try {
            InputStream openAssetFile = getAssets().open(str);
            if (openAssetFile == null) {
                openAssetFile = new FileInputStream(str);
            }
            BufferedInputStream bufferedInputStream = new BufferedInputStream(openAssetFile, 16384);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(32768);
            while (true) {
                int read = bufferedInputStream.read(bArr);
                if (read <= 0) {
                    return byteArrayOutputStream.toByteArray();
                }
                byteArrayOutputStream.write(bArr, 0, read);
            }
        } catch (IOException unused) {
            return null;
        }
    }

    @Keep
    public long getFreeMemory() {
        MemoryInfo var1 = this.getMemoryInfo();
        return var1.availMem - var1.threshold;
    }

    @Keep
    public HardwareInformation getHardwareInfo() {
        if (this.mHardwareInformation == null) {
            this.mHardwareInformation = new HardwareInformation(this);
        }

        return this.mHardwareInformation;
    }

    @Keep
    public String[] getIPAddresses() {
        return new String[0];
    }

    @Keep
    public int[] getImageData(String str) {
        Bitmap bitmap;
        try {
            bitmap = BitmapFactory.decodeFile(str);
        } catch (Throwable unused) {
            bitmap = null;
        }
        if (bitmap == null) {
            try {
                InputStream openAssetFile = getAssets().open(str);
                if (openAssetFile != null) {
                    bitmap = BitmapFactory.decodeStream(new BufferedInputStream(openAssetFile));
                }
            } catch (Throwable unused2) {
            }
        }
        if (bitmap != null) {
            try {
                int[] iArr = new int[(bitmap.getWidth() * bitmap.getHeight()) + 2];
                iArr[0] = bitmap.getWidth();
                iArr[1] = bitmap.getHeight();
                bitmap.getPixels(iArr, 2, iArr[0], 0, 0, iArr[0], iArr[1]);
                return iArr;
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return null;
    }

    @Keep
    public String getInternalStoragePath() {
        return VERSION.SDK_INT >= 24 ? this.getDataDir().getAbsolutePath() : this.getFilesDir().getParent();
    }

    @Keep
    public int getKeyFromKeyCode(int var1, int var2, int var3) {
        int var4 = var3;
        if (var3 < 0) {
            int[] var5 = InputDevice.getDeviceIds();
            if (var5.length == 0) {
                return 0;
            }

            var4 = var5[0];
        }

        InputDevice var6 = InputDevice.getDevice(var4);
        return var6 == null ? 0 : var6.getKeyCharacterMap().get(var1, var2);
    }

    @Keep
    public float getKeyboardHeight() {
        return (float) this.virtualKeyboardHeight;
    }

    @Keep
    public String getLegacyDeviceID() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("snooperId", "");
    }

    @Keep
    public boolean getLegacyExternalStorageDirWritable_debug() {
        return Environment.getExternalStorageDirectory().canWrite();
    }

    @Keep
    public boolean getLegacyExternalStorageGameFolderReadable_debug(String var1) {
        File var2 = new File(new File(Environment.getExternalStorageDirectory(), var1), "minecraftpe/clientId.txt");

        int var4;
        try {
            byte[] var3 = new byte[1024];
            FileInputStream var6 = new FileInputStream(var2);
            var4 = var6.read(var3);
            var6.close();
        } catch (Exception var5) {
            return false;
        }

        return var4 > 0;
    }

    @Keep
    public boolean getLegacyExternalStorageGameFolderWritable_debug(String var1) {
        return (new File(Environment.getExternalStorageDirectory(), var1)).canWrite();
    }

    @Keep
    public String getLegacyExternalStoragePath(String var1) {
        File var2 = Environment.getExternalStorageDirectory();
        File var3 = new File(new File(var2, var1), "test");

        boolean var4;
        label18:
        {
            try {
                FileOutputStream var6 = new FileOutputStream(var3);
                var6.close();
            } catch (Exception var5) {
                var4 = false;
                break label18;
            }

            var4 = true;
        }

        if (var4) {
            var1 = var2.getAbsolutePath();
        } else {
            var1 = "";
        }

        return var1;
    }

    @Keep
    public boolean getLegacyExternalStorageReadable_debug() {
        return Environment.getExternalStorageDirectory().canRead();
    }

    @Keep
    public String getLegacyExternalStorage_debug() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    @Keep
    public String getLocale() {
        return HardwareInformation.getLocale();
    }

    MemoryInfo getMemoryInfo() {
        long var1 = SystemClock.uptimeMillis();
        if (var1 >= this.mCachedMemoryInfoUpdateTime) {
            this.getSystemService(ActivityManager.class).getMemoryInfo(this.mCachedMemoryInfo);
            this.mCachedMemoryInfoUpdateTime = var1 + 2000L;
        }

        return this.mCachedMemoryInfo;
    }

    @Keep
    public long getMemoryLimit() {
        return this.getTotalMemory() - this.getMemoryInfo().threshold;
    }

    @Keep
    public String getObbDirPath() {
        return this.getApplicationContext().getObbDir().getAbsolutePath();
    }

    @Keep
    public float getPixelsPerMillimeter() {
        DisplayMetrics var1 = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(var1);
        return (var1.xdpi + var1.ydpi) * 0.5F / 25.4F;
    }

    @Keep
    public String getPlatformStringVar(int var1) {
        return var1 == 0 ? Build.MODEL : null;
    }

    @Keep
    public String getProfileId() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("profileId", "");
    }

    @Keep
    public String getProfileName() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("profileName", "");
    }

    @Keep
    public int getScreenHeight() {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        return Math.min(point.x, point.y);
    }

    @Keep
    public int getScreenWidth() {
        Display display = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        display.getRealSize(point);
        return Math.min(point.x, point.y);
    }

    @Keep
    public String getSecureStorageKey(String var1) {
        return PreferenceManager.getDefaultSharedPreferences(this).getString(var1, "");
    }

    public ThermalMonitor getThermalMonitor() {
        if (this.mThermalMonitor == null) {
            this.mThermalMonitor = new ThermalMonitor();
        }
        return this.mThermalMonitor;
    }

    @Keep
    public long getTotalMemory() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        getSystemService(ActivityManager.class).getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    @Keep
    public long getUsedMemory() {
        long var1 = SystemClock.uptimeMillis();
        if (var1 >= this.mCachedUsedMemoryUpdateTime) {
            this.mCachedUsedMemory = Debug.getNativeHeapAllocatedSize();
            this.mCachedUsedMemoryUpdateTime = var1 + 10000L;
        }

        return this.mCachedUsedMemory;
    }

    @Keep
    public int getUserInputStatus() {
        return this._userInputStatus;
    }

    @Keep
    public String[] getUserInputString() {
        return this._userInputText;
    }

    @Keep
    public boolean hasBuyButtonWhenInvalidLicense() {
        return true;
    }

    boolean hasHardwareChanged() {
        SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(this);
        String var2 = var1.getString("lastAndroidVersion", "");
        boolean var3;
        if (!var2.isEmpty() && var2.equals(VERSION.RELEASE)) {
            var3 = false;
        } else {
            var3 = true;
        }

        if (var3) {
            Editor var4 = var1.edit();
            var4.putString("lastAndroidVersion", VERSION.RELEASE);
            var4.apply();
        }

        return var3;
    }

    public boolean hasHardwareKeyboard() {
        return this.getResources().getConfiguration().keyboard == 2;
    }

    public boolean hasWriteExternalStoragePermission() {
        boolean var1;
        var1 = this.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED;

        mHasStoragePermission = var1;
        return var1;
    }

    public void hideKeyboard() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                MainActivity.this.dismissTextWidget();
            }
        });
    }

    public void initiateUserInput(int var1) {
        this._userInputText = null;
        this._userInputStatus = -1;
    }

    native boolean isAndroidTrial();

    native boolean isBrazeEnabled();

    boolean isChromebook() {
        return this.getWindow().getContext().getPackageManager().hasSystemFeature("android.hardware.type.pc");
    }

    protected boolean isDemo() {
        return false;
    }

    public native boolean isEduMode();

    public boolean isFirstSnooperStart() {
        return PreferenceManager.getDefaultSharedPreferences(this).getString("snooperId", "").isEmpty();
    }

    public boolean isNetworkEnabled(boolean var1) {
        ConnectivityManager var2 = this.getSystemService(ConnectivityManager.class);
        NetworkInfo var3 = var2.getNetworkInfo(9);
        boolean var4 = true;
        if (var3 != null && var3.isConnected()) {
            return true;
        } else {
            var3 = var2.getNetworkInfo(1);
            if (var3 != null && var3.isConnected()) {
                return true;
            } else {
                NetworkInfo var5 = var2.getActiveNetworkInfo();
                if (var5 != null && var5.isConnected() && !var1) {
                    var1 = var4;
                } else {
                    var1 = false;
                }

                return var1;
            }
        }
    }

    public boolean isOnWifi() {
        return this.getSystemService(ConnectivityManager.class).getNetworkInfo(1).isConnectedOrConnecting();
    }

    public native boolean isPublishBuild();

    public boolean isSoftKeyboardVisible() {
        return this.mIsSoftKeyboardVisible;
    }

    public boolean isTTSEnabled() {
        Context var1 = this.getApplicationContext();
        if (!this.isTestInfrastructureDisabled() && this.isRunningInAppCenter) {
            Log.w("MCPE", "Automation: We are running in AppCenter, forcing isTTSEnabled to false to avoid screen reader popup");
            return false;
        } else {
            if (var1 != null) {
                AccessibilityManager var2 = this.getSystemService(AccessibilityManager.class);
                if (var2 != null && var2.isEnabled() && !var2.getEnabledAccessibilityServiceList(1).isEmpty()) {
                    return true;
                }
            }

            return false;
        }
    }

    public boolean isTTSInstalled() {
        Iterator var1 = ( this.getSystemService(AccessibilityManager.class)).getInstalledAccessibilityServiceList().iterator();

        do {
            if (!var1.hasNext()) {
                return false;
            }
        } while ((((AccessibilityServiceInfo) var1.next()).feedbackType & 1) == 0);

        return true;
    }

    boolean isTablet() {
        return (this.getResources().getConfiguration().screenLayout & 15) == 4;
    }

    native boolean isTestInfrastructureDisabled();

    public boolean isTextToSpeechInProgress() {
        TextToSpeech var1 = this.textToSpeechManager;
        return var1 != null ? var1.isSpeaking() : false;
    }

    public void launchUri(String var1) {
        this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(var1)));
    }

    public void lockCursor() {
        if (this.getAndroidVersion() >= 26) {
            View var1 = this.findViewById(android.R.id.content).getRootView();
            this.mCursorLocked = true;
            if (VERSION.SDK_INT >= VERSION_CODES.O) {
                var1.requestPointerCapture();
            }
        }

    }

    native void nativeBackPressed();

    native void nativeBackSpacePressed();

    native String nativeCheckIfTestsAreFinished();

    native void nativeClearAButtonState();

    native void nativeDeviceCorrelation(long var1, String var3, long var4, String var6);

    native String nativeGetActiveScreen();

    native String nativeGetDevConsoleLogName();

    native String nativeGetDeviceId();

    native String nativeGetLogText(String var1);

    native long nativeInitializeLibHttpClient(long hcInitArgs);

    native long nativeInitializeXboxLive(long xalInitArgs, long xblInitArgs);

    native boolean nativeKeyHandler(int var1, int var2);

    native void nativeOnDestroy();

    native void nativeOnPickFileCanceled();

    native void nativeOnPickFileSuccess(String var1);

    native void nativeOnPickImageCanceled(long var1);

    native void nativeOnPickImageSuccess(long var1, String var3);

    native void nativeProcessIntentUriQuery(String var1, String var2);

    native void nativeResize(int var1, int var2);

    native void nativeReturnKeyPressed();

    native void nativeSetHeadphonesConnected(boolean var1);

    native String nativeSetOptions(String var1);

    native void nativeSetTextboxText(String var1);

    native void nativeShutdown();

    native void nativeStopThis();

    native void nativeStoragePermissionRequestResult(boolean var1, int var2);

    native void nativeSuspend();

    @SuppressLint("Range")
    @Override
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == RESULT_PICK_IMAGE) {
            if (intent == null || intent.getData() == null) {
                nativeOnPickImageCanceled(this.mCallback);
            } else {
                Cursor query = getContentResolver().query(intent.getData(), new String[]{"_data"}, null, null, null);
                query.moveToFirst();
                nativeOnPickImageSuccess(this.mCallback, query.getString(query.getColumnIndex("_data")));
            }
            this.mCallback = 0L;
            return;
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void onCreate(Bundle var1) {
        super.onCreate(var1);
        nativeWaitCrashManagementSetupComplete();
        boolean var3 = false;


        this.platform = Platform.createPlatform(true);
        this.setVolumeControlStream(3);
        FMOD.init(this);
        this.deviceManager = InputDeviceManager.create(this);
        this.platform.onAppStart(this.getWindow().getDecorView());
        if (this.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", Process.myPid(), Process.myUid()) == PackageManager.PERMISSION_DENIED) {
            var3 = true;
        }

        mHasStoragePermission = var3;
        this.nativeSetHeadphonesConnected(this.getSystemService(AudioManager.class).isWiredHeadsetOn());
        this.clipboardManager = this.getSystemService(ClipboardManager.class);
        this.initialUserLocale = Locale.getDefault();
        FilePickerManager var5 = new FilePickerManager(this);
        this.mFilePickerManager = var5;
        this.addListener(var5);

        mInstance = this;
        this._fromOnCreate = true;
        this.createTextWidget();
        this.findViewById(android.R.id.content).getRootView().addOnLayoutChangeListener(new OnLayoutChangeListener() {
            public void onLayoutChange(View var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
                MainActivity.this.nativeResize(var4 - var2, var5 - var3);
            }
        });
        if (this.isEduMode()) {
            (new PythonPackageLoader(this.getApplicationContext().getAssets(), this.getFilesDir())).unpack();
        }

        if (!this.isTestInfrastructureDisabled() && InstrumentationRegistryHelper.getIsRunningInAppCenter()) {
            this.isRunningInAppCenter = true;
            Log.w("MCPE", "Automation: in MainActivity::onCreate, we are running in AppCenter");
        }

        this.mWorldRecovery = new WorldRecovery(this.getApplicationContext(), this.getApplicationContext().getContentResolver());
    }

    @Override
    protected void onDestroy() {
        mInstance = null;
        FMOD.close();
        Iterator var1 = (new ArrayList(this.mActivityListeners)).iterator();

        while (var1.hasNext()) {
            ((ActivityListener) var1.next()).onDestroy();
        }

        this.nativeOnDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onKey(View var1, int var2, KeyEvent var3) {
        return false;
    }

    @Override
    public boolean onKeyDown(int var1, KeyEvent var2) {
        return super.onKeyDown(var1, var2);
    }

    @Override
    public boolean onKeyMultiple(int var1, int var2, KeyEvent var3) {
        return super.onKeyMultiple(var1, var2, var3);
    }

    @Override
    public boolean onKeyUp(int var1, KeyEvent var2) {
        if (var1 == 25 || var1 == 24) {
            this.platform.onVolumePressed();
        }

        return super.onKeyUp(var1, var2);
    }

    @Override
    public void onNewIntent(Intent var1) {
        this.setIntent(var1);
        this.processIntent(var1);
    }

    @Override
    protected void onPause() {
        this.nativeSuspend();
        super.onPause();
        if (this.isFinishing()) {
            this.nativeShutdown();
        }

    }

    void onPickFileSuccess(boolean var1) {
        StringBuilder var2 = new StringBuilder();
        var2.append(this.getApplicationContext().getCacheDir());
        var2.append("/tempPickedFile");
        String var3 = var2.toString();
        if (var1) {
            this.copyFromPickedFile(var3);
        }

        this.nativeOnPickFileSuccess(var3);
    }

    @Override
    public void onRequestPermissionsResult(int var1, String[] var2, int[] var3) {
        if (var1 == 1) {
            if (var3.length > 0 && var3[0] == 0) {
                mHasStoragePermission = true;
            } else {
                mHasStoragePermission = false;
            }

            this.nativeStoragePermissionRequestResult(mHasStoragePermission, this.mLastPermissionRequestReason);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter var1 = new IntentFilter("android.intent.action.HEADSET_PLUG");
        if (this.isTextWidgetActive()) {
            String var5 = this.textInputWidget.getText().toString();
            int var2 = this.textInputWidget.allowedLength;
            boolean var3;
            if ((this.textInputWidget.getInputType() & 2) == 2) {
                var3 = true;
            } else {
                var3 = false;
            }

            boolean var4;
            if ((this.textInputWidget.getInputType() & 131072) == 131072) {
                var4 = true;
            } else {
                var4 = false;
            }

            this.dismissTextWidget();
            this.showKeyboard(var5, var2, false, var3, var4);
        }

        Iterator var6 = this.mActivityListeners.iterator();

        while (var6.hasNext()) {
            ((ActivityListener) var6.next()).onResume();
        }

    }

    native void onSoftKeyboardClosed();

    @Override
    protected void onStart() {
        super.onStart();
        this.deviceManager.register();
        if (this._fromOnCreate) {
            this._fromOnCreate = false;
            this.processIntent(this.getIntent());
        }

    }

    @Override
    protected void onStop() {
        this.nativeStopThis();
        super.onStop();
        this.deviceManager.unregister();
        Iterator var1 = this.mActivityListeners.iterator();

        while (var1.hasNext()) {
            ((ActivityListener) var1.next()).onStop();
        }

    }

    @Override
    public void onWindowFocusChanged(boolean var1) {
        if (var1 && this.mCursorLocked) {
            this.lockCursor();
        }

        super.onWindowFocusChanged(var1);
        this.platform.onViewFocusChanged(var1);
    }

    public void openAndroidAppSettings() {
        try {
            startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS", Uri.parse("package:" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            Log.e("MCPE", "openAndroidAppSettings: Failed to open android app settings: " + e.toString());
        }
    }

    void openFile() {
        Intent var1 = new Intent("android.intent.action.OPEN_DOCUMENT");
        var1.addCategory("android.intent.category.OPENABLE");
        var1.setType("*/*");
        this.startActivityForResult(var1, 5);
    }

    void pickImage(long var1) {
        this.mCallback = var1;

        try {
            Intent var3 = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
            this.startActivityForResult(var3, RESULT_PICK_IMAGE);
        } catch (ActivityNotFoundException var4) {
            var4.printStackTrace();
        }

    }

    public void postScreenshotToFacebook(String var1, int var2, int var3, int[] var4) {
    }

    public void quit() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                MainActivity.this.finish();
            }
        });
    }

    public void removeListener(ActivityListener var1) {
        this.mActivityListeners.remove(var1);
    }

    public void requestStoragePermission(int var1) {
        this.mLastPermissionRequestReason = var1;
        this.requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
    }

    void saveFile(String var1) {
        Intent var2 = new Intent("android.intent.action.CREATE_DOCUMENT");
        var2.addCategory("android.intent.category.OPENABLE");
        var2.setType("*/*");
        var2.putExtra("android.intent.extra.TITLE", var1);
        this.startActivityForResult(var2, 4);
    }

    public void sendBrazeDialogButtonClick(int var1) {
        if (this.isBrazeEnabled()) {
            this._brazeMessageManagerListener.logClickOnMostRecentDialog(var1);
        }
    }

    public void sendBrazeEvent(String var1) {
        if (this.isBrazeEnabled()) {
            Appboy.getInstance(this.getApplicationContext()).logCustomEvent(var1);
            Appboy.getInstance(this.getApplicationContext()).requestImmediateDataFlush();
        }
    }

    public void sendBrazeEventWithProperty(String var1, String var2, int var3) {
    }

    public void sendBrazeEventWithStringProperty(String var1, String var2, String var3) {
    }

    public void sendBrazeToastClick() {
        if (this.isBrazeEnabled()) {
            this._brazeMessageManagerListener.logClickOnMostRecentToast();
        }
    }

    public void setCachedDeviceId(String var1) {
        Editor var2 = PreferenceManager.getDefaultSharedPreferences(this).edit();
        var2.putString("deviceId", var1);
        var2.apply();
    }

    public void setClipboard(String value) {
        this.clipboardManager.setPrimaryClip(ClipData.newPlainText("MCPE-Clipdata", value));
    }

    void setFileDialogCallback(long var1) {
        this.mFileDialogCallback = var1;
    }

    public void setIsPowerVR(boolean var1) {
        _isPowerVr = var1;
    }

    public void setLoginInformation(String var1, String var2, String var3, String var4) {
        Editor var5 = PreferenceManager.getDefaultSharedPreferences(this).edit();
        var5.putString("accessToken", var1);
        var5.putString("clientId", var2);
        var5.putString("profileId", var3);
        var5.putString("profileName", var4);
        var5.apply();
    }

    public void setRefreshToken(String var1) {
        Editor var2 = PreferenceManager.getDefaultSharedPreferences(this).edit();
        var2.putString("refreshToken", var1);
        var2.apply();
    }

    public void setSecureStorageKey(String var1, String var2) {
        Editor var3 = PreferenceManager.getDefaultSharedPreferences(this).edit();
        var3.putString(var1, var2);
        var3.apply();
    }

    public void setSession(String var1) {
        Editor var2 = PreferenceManager.getDefaultSharedPreferences(this).edit();
        var2.putString("sessionID", var1);
        var2.apply();
    }

    public void setTextToSpeechEnabled(boolean var1) {
        if (var1) {
            if (this.textToSpeechManager == null) {
                try {
                    Context var3 = this.getApplicationContext();
                    OnInitListener var4 = new OnInitListener() {
                        public void onInit(int var1) {
                        }
                    };
                    TextToSpeech var2 = new TextToSpeech(var3, var4);
                    this.textToSpeechManager = var2;
                } catch (Exception var5) {
                }
            }
        } else {
            this.textToSpeechManager = null;
        }

    }

    public void setVolume(float var1) {
        this.mVolume = var1;
    }

    public void setupKeyboardViews(String var1, int var2, boolean var3, boolean var4, boolean var5) {
        this.textInputWidget.updateFilters(var2, !var5);
        this.textInputWidget.setTextFromGame(var1);
        this.textInputWidget.setVisibility(View.VISIBLE);
        if (var5) {
            var2 = 131072;
        } else {
            var2 = 524288;
        }

        this.textInputWidget.setInputType(var2);
        if (var4) {
            this.textInputWidget.setInputType(this.textInputWidget.getInputType() | 2);
        } else {
            this.textInputWidget.setInputType(this.textInputWidget.getInputType() | 1);
        }

        this.textInputWidget.requestFocus();
        this.getInputMethodManager().showSoftInput(this.textInputWidget, 0);
        this.textInputWidget.setSelection(this.textInputWidget.length());
    }

    public void share(String var1, String var2, String var3) {
        Intent var4 = new Intent();
        var4.setAction("android.intent.action.SEND");
        var4.putExtra("android.intent.extra.SUBJECT", var1);
        var4.putExtra("android.intent.extra.TITLE", var2);
        var4.putExtra("android.intent.extra.TEXT", var3);
        var4.setType("text/plain");
        this.startActivity(Intent.createChooser(var4, var1));
    }

    public void showKeyboard(final String var1, final int var2, final boolean var3, final boolean var4, final boolean var5) {
        this.nativeClearAButtonState();
        this.runOnUiThread(new Runnable() {
            public void run() {
                MainActivity.this.setupKeyboardViews(var1, var2, var3, var4, var5);
            }
        });
    }

    @Override
    public void startPickerActivity(Intent var1, int var2) {
        this.startActivityForResult(var1, var2);
    }

    public void startTextToSpeech(String var1) {
        if (this.textToSpeechManager != null) {
            if (VERSION.SDK_INT < 21) {
                HashMap var2 = new HashMap();
                var2.put("volume", String.valueOf(this.mVolume));
                this.textToSpeechManager.speak(var1, 0, var2);
            } else {
                Bundle var3 = new Bundle();
                var3.putFloat("volume", this.mVolume);
                this.textToSpeechManager.speak(var1, 0, var3, (String) null);
            }
        }

    }

    public void statsTrackEvent(String var1, String var2) {
    }

    public void statsUpdateUserData(String var1, String var2) {
    }

    public void stopTextToSpeech() {
        TextToSpeech var1 = this.textToSpeechManager;
        if (var1 != null) {
            var1.stop();
        }

    }

    public void throwRuntimeExceptionFromNative(final String var1) {
        Log.e(this.getClass().getCanonicalName(), "throwRuntimeExceptionFromNative: "+ var1);
    }

    public void tick() {
    }

    public void trackPurchaseEvent(String var1, String var2, String var3, String var4, String var5, String var6, String var7, String var8) {
    }

    public void unlockCursor() {
        if (this.getAndroidVersion() >= 26) {
            View var1 = this.findViewById(android.R.id.content).getRootView();
            this.mCursorLocked = false;
            if (VERSION.SDK_INT >= VERSION_CODES.O) {
                var1.releasePointerCapture();
            }
        }

    }

    public void updateLocalization(final String var1, final String var2) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Locale var1x = new Locale(var1, var2);
                Locale.setDefault(var1x);
                Configuration var2x = new Configuration();
                var2x.locale = var1x;
                MainActivity.this.getResources().updateConfiguration(var2x, MainActivity.this.getResources().getDisplayMetrics());
            }
        });
    }

    public void updateTextboxText(final String var1) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                if (MainActivity.this.isTextWidgetActive()) {
                    MainActivity.this.textInputWidget.setTextFromGame(var1);
                    MainActivity.this.textInputWidget.setSelection(MainActivity.this.textInputWidget.length());
                }

            }
        });
    }

    public void vibrate(int var1) {
        this.getSystemService(Vibrator.class).vibrate((long) var1);
    }

    private class HeadsetConnectionReceiver extends BroadcastReceiver {
        private HeadsetConnectionReceiver() {
        }

        public void onReceive(Context var1, Intent var2) {
            if (var2.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                int var3 = var2.getIntExtra("state", -1);
                if (var3 != 0) {
                    if (var3 == 1) {
                        Log.d("MCPE", "Headset plugged in");
                        MainActivity.this.nativeSetHeadphonesConnected(true);
                    }
                } else {
                    Log.d("MCPE", "Headset unplugged");
                    MainActivity.this.nativeSetHeadphonesConnected(false);
                }
            }

        }
    }

    class IncomingHandler extends Handler {
        public void handleMessage(Message var1) {
            if (var1.what != 837) {
                super.handleMessage(var1);
            } else {
                String var2 = MainActivity.this.getApplicationContext().getPackageName();
                SharedPreferences var3 = PreferenceManager.getDefaultSharedPreferences(MainActivity.this.getApplicationContext());
                var3.getString("deviceId", "");

                long var4;
                try {
                    var4 = MainActivity.this.getPackageManager().getPackageInfo(var2, 0).firstInstallTime;
                } catch (NameNotFoundException var9) {
                    return;
                }

                var2 = var1.getData().getString("deviceId");
                String var6 = var1.getData().getString("sessionId");
                long var7 = var1.getData().getLong("time");
                if (var4 > var7) {
                    MainActivity.this.nativeDeviceCorrelation(var4, var2, var7, var6);
                }

                Editor var10 = var3.edit();
                var10.putInt("correlationAttempts", 0);
                var10.apply();
                if (MainActivity.this.mBound == MainActivity.MessageConnectionStatus.CONNECTED) {
                    MainActivity var11 = MainActivity.this;
                    var11.unbindService(var11.mConnection);
                }
            }

        }
    }

    enum MessageConnectionStatus {
        CONNECTED,
        DISCONNECTED,
        NOTSET;
    }
}
