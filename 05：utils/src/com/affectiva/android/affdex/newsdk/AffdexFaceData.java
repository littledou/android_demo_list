//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

class AffdexFaceData {
    public float value;
    public boolean is_valid;

    public void set(float value, boolean is_valid) {
        this.value = value;
        this.is_valid = is_valid;
    }

    public AffdexFaceData() {
        this.is_valid = false;
    }

    public AffdexFaceData(float value, boolean is_valid) {
        this.set(value, is_valid);
    }
}
