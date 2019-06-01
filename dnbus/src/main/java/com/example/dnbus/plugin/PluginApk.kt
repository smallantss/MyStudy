package com.example.dnbus.plugin

import android.content.pm.PackageInfo
import android.content.res.Resources
import dalvik.system.DexClassLoader

class PluginApk(val mPackageInfo: PackageInfo,val mClassLoader: DexClassLoader,val mResource: Resources){

   val mManager = mResource.assets
}