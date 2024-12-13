package top.deeke.jni;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import top.deeke.jni.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'jni' library on application startup.
    static {
        System.loadLibrary("jni");
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Example of a call to a native method
        TextView tv = binding.sampleText;
        //tv.setText(stringFromJNI());
        // Log.d("debug", "返回值：" + exec());
        // Log.d("debug", "execScript：" + execScript("let a = 12;let b = 13;a+b;", "jiang"));
        Log.d("debug", execScriptConsole("path", "console.log(222222);"));
    }

    /**
     * A native method that is implemented by the 'jni' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native String exec();

    public native String execScript(String code, String path);

    public native String execScriptConsole(String path, String code);
}