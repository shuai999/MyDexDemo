package wangyang.zun.com.mydexdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import dalvik.system.DexClassLoader;
import wangyang.zun.com.mydexdemo.dynamic.Dynamic;


/**
 * Email: 2185134304@qq.com
 * Created by Novate 2018/5/11 7:53
 * Version 1.0
 * Params:
 * Description:    点击Button按钮 ，加载dex文件中的 class，并调用其中的 sayHello()方法
 */

public class MainActivity extends AppCompatActivity {

    private Dynamic dynamic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //添加一个点击事件
        findViewById(R.id.tx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDexClass();
            }
        });
    }

    /**
     * 加载dex文件中的class，并调用其中的sayHello方法
     */
    private void loadDexClass() {
        File cacheFile = FileUtils.getCacheDir(getApplicationContext());
        String internalPath = cacheFile.getAbsolutePath() + File.separator + "dynamic_dex.jar";
        File desFile = new File(internalPath);
        try {
            if (!desFile.exists()) {
                desFile.createNewFile();
                // 从assets目录下 copy 文件到 app/data/cache目录
                FileUtils.copyFiles(this, "dynamic_dex.jar", desFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 这里由于是加载 jar文件，所以采用DexClassLoader
        //下面开始加载dex class
        DexClassLoader dexClassLoader = new DexClassLoader(internalPath, cacheFile.getAbsolutePath(), null, getClassLoader());
        try {
            // 类加载器负责读取 .class文件，并把它转为 Class实例，这个实例就表示一个java类
            // 加载 dex文件中的Class，格式是：包名+类名（全类名）
            Class libClazz = dexClassLoader.loadClass("wangyang.zun.com.mydexdemo.dynamic.impl.IDynamic");
            // 调用Class的 newInstance()方法，创建Class的对象 dynamic
            // Dynamic 是 dex文件中之前的一个接口类
            dynamic = (Dynamic) libClazz.newInstance();
            if (dynamic != null)
                Toast.makeText(this, dynamic.sayHelloy(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
