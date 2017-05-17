# TinkerDemo
快速在项目中集成Tinker

## 前言
Tinker 的介绍我就不多说了，网上很多关于 Tinker 的文章，本篇文章的目的旨在快速集成 Tinker，方便在项目中使用。
需要了解 Tinker 的可以点击一下相关链接：
> Tinker Github ： [https://github.com/Tencent/tinker](https://github.com/Tencent/tinker)
> Tinker Wiki ：[https://github.com/Tencent/tinker/wiki](https://github.com/Tencent/tinker/wiki)
> Tinker 接入指南: [https://github.com/Tencent/tinker/wiki/Tinker-%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97](https://github.com/Tencent/tinker/wiki/Tinker-%E6%8E%A5%E5%85%A5%E6%8C%87%E5%8D%97)

### Demo 地址
本篇博客的 Demo 已经托管在 GitHub 上面了，如有细节问题欢迎指出。
> GitHub : [SimpleTinkerDemo](https://github.com/SenhLinsh/SimpleTinkerDemo)
> [https://github.com/SenhLinsh/SimpleTinkerDemo](https://github.com/SenhLinsh/SimpleTinkerDemo)

### 使用前需要注意
Tinker 不是万能的热更新，存在的一些已知问题如下，开发时请避免：

1. Tinker不支持修改 AndroidManifest.xml，Tinker 不支持新增四大组件；
2. 由于 Google Play 的开发者条款限制，不建议在 GP渠道动态更新代码；
3. 在 Android N 上，补丁对应用启动时间有轻微的影响；
4. 不支持部分三星 android-21 机型，加载补丁时会主动抛出 "TinkerRuntimeException:checkDexInstall failed"；
5. 对于资源替换，不支持修改 remoteView。例如 transition 动画，notification icon 以及桌面图标。

## 快速集成Tinker

### 一、添加依赖
project 的 build.gradle

```
buildscript {
    dependencies {
        classpath ('com.tencent.tinker:tinker-patch-gradle-plugin:1.7.9') // Tinker插件
    }
}
```

module 的 build.gradle

```
    // Tinker
    compile 'com.tencent.tinker:tinker-android-lib:1.7.9'
    provided 'com.tencent.tinker:tinker-android-anno:1.7.9' // 通过注解生成Application的库，如果使用ApplicationLike就添加，建议使用
    // Multidex  // 如果没有Multidex就添加吧
    compile 'com.android.support:multidex:1.0.1'
```

### 二、配置Gradle

#### 1. 创建 tinkerpatch.gradle
app 目录下创建 tinkerpatch.gradle，将 Tinker 所有的配置逻辑放在该 gradle（目的是防止 build.gradle 过于臃肿）

```
//apply tinker插件
apply plugin: 'com.tencent.tinker.patch'

// 每次打包补丁时需要配置的变量
/**
 * 用于存放生成和读取APK/Mapping/R文件的文件夹名称.
 * isDebug为true时, 编译时都会将文件复制到 app/build/bakApk 文件夹下, 方便每次Rebuild时都可以将这些文件夹清理掉
 * isDebug为false时, 文件夹在 app/bakApk 下, 防止Rebuild操作将其清理
 * 每次打补丁包时, 都必须指定好 isDebug 以及基准APK文件所对应的文件夹
 */
def packageName = "app-1.0.0-0516-20-40-02" // 基准APK所对应的文件夹, 每次打补丁时都需要仔细检查此项是否为目标基准APK

def isDebug = false; // 是否处于Debug模式, 区别在是否将存放基准APK等的文件夹指定在build文件夹之内或者之外

def tinkerEnabled = true // 开发调试时可以将此选项关闭, 可以避免生成多余的基准包文件

// 下面不需要每次重新配置
def variantName = isDebug ? "debug" : "release";

def bakPath = isDebug ? file("${buildDir}/bakApk/") : file("bakApk/"); // 基准包路径

def tinkerPathPrefix = "${bakPath}/${packageName}/${variantName}";

def tinkerOldApkPath = "${tinkerPathPrefix}/${project.name}-${variantName}.apk" // 旧APK地址

def tinkerApplyMappingPath = "${tinkerPathPrefix}/${project.name}-${variantName}-mapping.txt" // 旧Mapping文件地址

def tinkerApplyResourcePath = "${tinkerPathPrefix}/${project.name}-${variantName}-R.txt" // 旧R文件地址

tinkerPatch {

    oldApk = tinkerOldApkPath

    ignoreWarning = false

    useSign = true

    tinkerEnable = tinkerEnabled

    buildConfig {

        applyMapping = tinkerApplyMappingPath

        applyResourceMapping = tinkerApplyResourcePath

        tinkerId = android.defaultConfig.versionName;

        keepDexApply = false

        /**
         * 是否使用加固模式，仅仅将变更的类合成补丁。注意，这种模式仅仅可以用于加固应用中。
         * 如果设置为 true, 生成的补丁包将包含所有的变更类而不是变更文件
         */
        isProtectedApp = false
    }

    dex {

        dexMode = "jar"

        pattern = ["classes*.dex",
                   "assets/secondary-dex-?.jar"]

        /**
         * 它定义了哪些类在加载补丁包的时候会用到, 这些类是通过Tinker无法修改的类，也是一定要放在main dex的类。
         * Tinker 已经自动把需要的类配置好了, 这个一般不需要配置.
         */
        loader = [
                // 如果有不希望被 Tinker 更改的类, 可放到其中
                // 如 "tinker.sample.android.app.BaseBuildInfo"
        ]
    }

    lib {
        pattern = ["lib/*/*.so"]
    }

    res {
        pattern = ["res/*", "assets/*", "resources.arsc", "AndroidManifest.xml"]

        ignoreChange = ["assets/sample_meta.txt"]

        largeModSize = 100
    }

    /**
     * 用于生成补丁包中的 'package_meta.txt' 文件
     * 可选, 默认生成 "TINKER_ID" 和 "NEW_TINKER_ID"
     */
    packageConfig {
        /**
         * 运行时可以通过 TinkerLoadResult.getPackageConfigByName() 得到相应的数值, 但是建议直接通过修改代码来实现，例如BuildConfig
         * platform 只是一个示范, 你可以使用 sdkVersion / brand 等在 TinkerPatchListener 内解析使用, 这样就可以有条件地加载补丁了
         */
        configField("platform", "all")
    }

    sevenZip {
        zipArtifact = "com.tencent.mm:SevenZip:1.1.10"
    }
}

/**
 * 将 APK、Mapping 文件、R 文件复制到指定目录
 */
android.applicationVariants.all { variant ->

    def taskName = variant.name

    tasks.all {
        if ("assemble${taskName.capitalize()}".equalsIgnoreCase(it.name)) {

            it.doLast {
                copy {
                    def release = isDebug ? "" : "-${versionName}";
                    def date = new Date().format("MMdd-HH-mm-ss")
                    def pathPrefix = "${bakPath}/${project.name}${release}-${date}/${variantName}/"
                    def name = "${project.name}-${variantName}"

                    // 复制APK文件
                    def destPath = file(pathPrefix)
                    from variant.outputs.outputFile
                    into destPath
                    rename { String fileName ->
                    }

                    // 复制mapping文件
                    from "${buildDir}/outputs/mapping/${variant.dirName}/mapping.txt"
                    into destPath
                    rename { String fileName ->
                        fileName.replace("mapping.txt", "${name}-mapping.txt")
                    }

                    // 复制R文件
                    from "${buildDir}/intermediates/symbols/${variant.dirName}/R.txt"
                    into destPath
                    rename { String fileName ->
                        fileName.replace("R.txt", "${name}-R.txt")
                    }
                }
            }
        }
    }
}
```
注意：需要修改的地方一般只有最开始的三个变量，其他地方一般只在第一次需要根据自己的需求配置的时候注意一下有没有不合适本项目的地方需要重新编辑一下的。

#### 2. 应用 tinkerpatch.gradle
在 build.gradle 末尾应用该 tinkerpatch.gradle

``` apply from: 'tinkerpatch.gradle' ```

注意：尽量放在末尾出，因为 tinkerpatch.gradle 引用了项目版本号，如果放在开头或者其他地方可能会导致报没有配置 tinkerId 的错。

#### 3. 添加签名配置
如果没有release签名的配置，会导致补丁因为签名冲突而无法加载。在 build.gradle 中的 android { } 中添加如下配置，然后在 local.properties 中添加相对应的字段配置（ keyStore 和 Password 比较隐私所以放在 local.properties 中，不上传到 Git 。ps.如果是私人 Git 项目可以直接在 gradle 中配置）

```
signingConfigs {
    release {
        //加载资源
        Properties properties = new Properties()
        InputStream inputStream = project.rootProject.file('local.properties').newDataInputStream();
        properties.load(inputStream)
        //读取文件
        def sdkDir = properties.getProperty('key.file')
        storeFile file(sdkDir)
        //读取字段
        def key_keyAlias = properties.getProperty('keyAlias')
        def key_keyPassword = properties.getProperty('keyPassword');
        def key_storePassword = properties.getProperty('storePassword');

        storePassword key_storePassword
        keyAlias key_keyAlias
        keyPassword key_keyPassword
    }
}
```

### 三、代码配置

#### 1. 自定义 ApplicationLike
将自定义的 Application 转型为自定义的 ApplicationLike（没有的话直接写就好了）

```
@SuppressWarnings("unused")
@DefaultLifeCycle(application = "com.linsh.lshapp.LshApplication",
        flags = ShareConstants.TINKER_ENABLE_ALL,
        loadVerifyFlag = false)
public class LshApplicationLike extends DefaultApplicationLike {

    public LshApplicationLike(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }
    
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // 初始化MultiDex必须先于初始化Tinker
        MultiDex.install(base);
        // 初始化Tinker
        TinkerInstaller.install(this);
    }
    
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void registerActivityLifecycleCallbacks(Application.ActivityLifecycleCallbacks callback) {
        getApplication().registerActivityLifecycleCallbacks(callback);
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        // 这个就相当于Application的onCreate()方法, 一些之前自定义Application的初始化可以放在这里执行
        LshApplicationUtils.init(getApplication()); // 可以通过 getApplication() 获取Application引用
    }
}
```

如果你像我一样，自定义了 Tinker 相关的 Reporter、Service等东西（详见 [Tinker 自定义扩展](https://github.com/Tencent/tinker/wiki/Tinker-%E8%87%AA%E5%AE%9A%E4%B9%89%E6%89%A9%E5%B1%95)），可以把初始化Tinker换成如下：

```
TinkerManager.setTinkerApplicationLike(this);
TinkerManager.setUpgradeRetryEnable(true);
TinkerInstaller.setLogIml(new MyLogImp());
TinkerManager.installTinker(this);
Tinker tinker = Tinker.with(getApplication());
TinkerManager.setReporter(new LshTinkerReporter());
```
注意：本人经过适当修改，具体使用方法可以参看[官方 Demo](https://github.com/Tencent/tinker/tree/master/tinker-sample-android)

#### 2.配置 Manifest
rebuild project 之后，会自动生成 Application（在 build 文件夹中），所以我们需要在 Manifest 中配置 Application，以及处理补丁结果的 service (如果自定义了的话)

```
<application
    android:name=".LshApplication"
    ...>
    <service android:name=".lib.tinker.service.MyResultService"/> // 如果自定义了继承DefaultResultService的Service
</application>
```

#### 3、执行加载补丁

接下来剩下加载补丁的代码了。

根据需求选一个需要加载补丁的地方（比如说启动页判断是否有更新，然后下载下来就可以加载补丁了。测试的时候可以使用一个 Button 来执行加载补丁更方便）。

ps.补丁从哪来？当然是从自己的服务器下载更新补丁，就像应用内下载更新包一样。当然，如果是测试的话，可以直接复制粘贴到 SD卡中去。

```
// 为了方便测试我才放在SD卡根目录的，说实话我最讨厌将『垃圾』下载到SD卡根目录的软件了
String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk";
File file = new File(path);
if (file.exists() && file.isFile()) {
    LshLogUtils.i("正在升级...");
    TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), path);
} else {
    LshLogUtils.i("没有可以升级的补丁, 当前版本号: v" + BuildConfig.VERSION_NAME);
}
```

### 四、尝试补丁升级

1. 在项目代码中修改代码
2. 修改 tinkerpatch.gradle，指定 baseInfo 文件夹（即基准APK等相关文件），根据是否 Debug 模式选择 isDebug（用于将编译生成的文件放在哪个地方） ，如有必要还可更新版本号
3. 点击 Studio 右侧的 gradle，找到 :app -> Tasks -> tinker -> tinkerPatchDebug / tinkerPatchRelease，点击执行
4. 在 build -> outputs -> tinkerPatch -> debug / release 文件夹下可以找到 patch_signed_7zip.apk，将补丁放在SD卡目录中就可以根据之前的加载不定逻辑进行测试了
5. 祝一次搞定！

## TinkerPatch补丁管理后台

Tinker 的集成对于初次使用来说还是挺麻烦的，再加上补丁的分配、升级和管理，使用的时候需要特别的注意才能防止出现问题。<br/>

对此呢，Tinker 提供了一个补丁管理后台 www.tinkerpatch.com ，可以一键集成 Tinker，还包括补丁的发布和管理，感觉上来说应该非常方便的。但是我刚开始尝试了一次没有成功就懒得去试了。<br/>

虽然正常情况下是免费的，但是如果请求量上去了的话，就得收费了。估计目前还是很少人用的吧，毕竟 TinkerPatch 在 Github上 面的 Stars 和 Issues 还是比较少的。<br/>

![TinkerPatch补丁管理后台价格展示](http://img.blog.csdn.net/20170515172536564?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveXVhbnhpYW5nMDgxMg==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
