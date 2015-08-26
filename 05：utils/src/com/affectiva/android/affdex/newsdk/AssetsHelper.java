//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

class AssetsHelper {
    private static final String TAG = "AssetsHelper";
    private static final String AFFDEX_ASSETS_DIR = "Affdex";
    private static final String VERSION_PREFIX = "v_";
    private AssetManager mAssetsMgr;

    AssetsHelper(Context context) {
        if(context == null) {
            throw new NullPointerException("context must not be null");
        } else {
            this.mAssetsMgr = context.getResources().getAssets();
        }
    }

    InputStream openAffdexFile(String relativePath) throws AffdexException {
        if(relativePath == null) {
            throw new NullPointerException("relativePath must not be null");
        } else {
            String path = (new File("Affdex", relativePath)).getPath();

            try {
                InputStream result = this.mAssetsMgr.open(path);
                return result;
            } catch (IOException var5) {
                throw new AffdexException("unable to open asset file: " + path, var5);
            }
        }
    }

    File copyAssetsContent(String srcPath, boolean deleteAndReplace) {
        File srcDir = new File("Affdex", srcPath);
        try {
            String[] dstAffdexDir = this.mAssetsMgr.list(srcDir.getPath());
            if(dstAffdexDir.length == 0) {
                throw new IllegalArgumentException(srcPath + " is not a directory, is an empty directory, or does not exist");
            }
        } catch (IOException var6) {
            throw new IllegalArgumentException("failed to open directory: " + srcPath);
        }

        File dstAffdexDir1 = new File(Environment.getExternalStorageDirectory(), "Affdex");
        dstAffdexDir1.mkdirs();
        File dstPath = new File(dstAffdexDir1, srcPath);
        if(deleteAndReplace) {
            deleteDirectory(dstPath);
        }

        this.copyAssets(srcDir.getPath(), dstAffdexDir1);
        return dstPath;
    }

    private void copyAssets(String assetsPath, File dstDir) {
        File dstPath = null;

        String[] filenames;
        boolean isDir;
        try {
            filenames = this.mAssetsMgr.list(assetsPath);
            isDir = filenames != null && filenames.length > 0;
        } catch (IOException var11) {
            Log.e("AssetsHelper", "Failed to get asset file list(" + assetsPath + ").", var11);
            return;
        }

        dstPath = new File(dstDir, (new File(assetsPath)).getName());
        if(isDir) {
            dstPath.mkdir();
            String[] e = filenames;
            int len$ = filenames.length;

            for(int i$ = 0; i$ < len$; ++i$) {
                String fname = e[i$];
                if(!fname.startsWith("v_") || !(new File(dstPath, fname)).exists()) {
                    this.copyAssets((new File(assetsPath, fname)).getPath(), dstPath);
                }
            }
        } else {
            try {
                this.copyAssetFile(assetsPath, dstPath);
            } catch (IOException var10) {
                Log.e("AssetsHelper", "Failed to copy " + assetsPath + " from assets.", var10);
            }
        }

    }

    File getVersionedDestDir(String srcParentDirName, String dstParentDirPath) throws AffdexException {
        File srcParentDir = new File("Affdex", srcParentDirName);

        String[] filenames;
        try {
            filenames = this.mAssetsMgr.list(srcParentDir.getPath());
        } catch (IOException var10) {
            throw new AffdexException("failed to open parent dir: " + srcParentDirName, var10);
        }

        String srcVersionedDirName = null;
        String[] dstVersionedDir = filenames;
        int len$ = filenames.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String filename = dstVersionedDir[i$];
            if(filename.startsWith("v_")) {
                srcVersionedDirName = filename;
                break;
            }
        }

        if(srcVersionedDirName == null) {
            throw new AffdexException("no versioned dir in source parent dir: " + srcParentDirName);
        } else {
            File var11 = new File(dstParentDirPath, srcVersionedDirName);
            if(!var11.exists()) {
                throw new AffdexException("no versioned dir in destination matches " + srcVersionedDirName + " in source");
            } else {
                return var11;
            }
        }
    }

    private void copyAssetFile(String assetPath, File destFile) throws IOException {
        InputStream in = this.mAssetsMgr.open(assetPath);
        if(!destFile.exists()) {
            destFile.createNewFile();
        }

        FileOutputStream out = new FileOutputStream(destFile);

        try {
            byte[] e = new byte[1024];

            int read;
            while((read = in.read(e)) != -1) {
                out.write(e, 0, read);
            }

            out.flush();
            out.close();
            in.close();
        } catch (IOException var7) {
            Log.e("AssetsHelper", "Failed to copy file.", var7);
            throw var7;
        }
    }

    static boolean deleteDirectory(File path) {
        if(path.exists()) {
            File[] files = path.listFiles();
            if(files == null) {
                return true;
            }

            for(int i = 0; i < files.length; ++i) {
                if(files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    safeFileDelete(files[i]);
                }
            }
        }

        return safeFileDelete(path);
    }

    private static boolean safeFileDelete(File file) {
        File tempFile = new File(file.getPath() + System.currentTimeMillis());
        return !file.renameTo(tempFile)?false:tempFile.delete();
    }
}
