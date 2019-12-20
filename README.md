# ReleasePlugin

ReleasePlugin is a Plugin for Android Developer. He helps developer to copy release apk from their project folder to one place.

## How to install

## How to Use

1. `app/build.gradle`

   ```groovy
   apply plugin: 'me.monster.lab.move.plugin'

   android {
     ......
   }
   moveapk {
       minifyEnabled true
       distPath "/Users/jiang/Zuo"
   }
   ```

   - minifyEnabled
      is your buildType release minifyEnable value.
   - distPath
     is a place in your computer. eg: Mac: `/Users/YourName/releaseApk`, Windows: `D:\\releaseApk`

Now, you can generate signed apk.