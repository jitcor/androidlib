# 统一接入源
```groovy
maven { url 'https://jitpack.io' }
```
# 最新版本
[![](https://jitpack.io/v/Humenger/androidlib.svg)](https://jitpack.io/#Humenger/androidlib)
# 接入RSharedPreferences
- 解决`MODE_WORLD_WRITEABLE no longer supported`问题

```groovy
implementation 'com.github.Humenger.androidlib:RSharedPreferences:{latest_version}'
```
# 使用RSharedPreferences
```java
RSharedPreferences.getSharedPreferences(context,name,mode);
RSharedPreferences.getSharedPreferences(getPreferenceManager());
```
#### 包裹方式使用
```java
RSharedPreferences.sharedPreferencesBypass(getContext(), (RBypassCallback<Void>) () -> {
                setPreferencesFromResource(R.xml.root_preferences,rootKey);
                return null;
            });
```
```java
//带回调
SharedPreferences preferences= RSharedPreferences.sharedPreferencesBypass(getContext(), (RBypassCallback<SharedPreferences>) () -> 
                    getPreferenceManager().getSharedPreferences());
```
# 接入XReflectHelpers
- 移植Xposed的反射库，可以在任何APP中使用，就相当于一个普通的反射库
```groovy
implementation 'com.github.Humenger.androidlib:XReflectHelpers:{latest_version}'
```
# 使用XReflectHelpers
```java
XReflectHelpers.XXXX();
```
### api列表
 ![XReflectHelpers](./images/XReflectHelpers.png)

# 接入HSystemHelpers
- 一些系统辅助类
```groovy
 implementation 'com.github.Humenger.androidlib:HSystemHelpers:{latest_version}'
```
# 使用HSystemHelpers
```java
//根据class名字查找其对应的系统jar所在路径
HSystemHelpers.findSystemJarPathWithClassName("");
```
