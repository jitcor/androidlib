# 接入RSharedPreferences
- 解决`MODE_WORLD_WRITEABLE no longer supported`问题
```groovy
allprojects {
		repositories {
			//...
			maven { url 'https://jitpack.io' }
		}
	}
```
```groovy
dependencies {
	            implementation 'com.github.Humenger.androidlib:RSharedPreferences:1.0.2'
	}
```
# 使用RSharedPreferences
```java
RSharedPreferences.getSharedPreferences(context,name,mode);
```

# 接入XReflectHelpers
- 移植Xposed的反射库，可以在任何APP中使用，就相当于一个普通的反射库
```groovy
allprojects {
		repositories {
			//...
			maven { url 'https://jitpack.io' }
		}
	}
```
```groovy
dependencies {
	            implementation 'com.github.Humenger.androidlib:XReflectHelpers:1.0.2'
	}
```
# 使用XReflectHelpers
```java
XReflectHelpers.XXXX();
```
### api列表
 ![XReflectHelpers](./images/XReflectHelpers.png)

