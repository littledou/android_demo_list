//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.affectiva.android.affdex.newsdk;

import java.io.File;

class AffdexFaceParams {
    private static final String TRACKER_CONFIG_LARGE_FACES = "tracker_noctali0.81";
    private static final String TRACKER_CONFIG_SMALL_FACES = "tracker_noctali0.81_emoto";
    private static final String TRACKER_CONFIG_DEFAULT = "tracker_noctali0.81";
    private String mDataDirStr;
    private String name_tracker_config;

    private AffdexFaceParams(File dataDir) {
        this(dataDir, "tracker_noctali0.81");
    }

    private AffdexFaceParams(File dataDir, String trackerConfig) {
        this.mDataDirStr = dataDir.getPath();
        if(!dataDir.exists()) {
            String msg = "Config data directory does not exist. (" + this.mDataDirStr + ")";
            throw new IllegalArgumentException(msg);
        } else {
            this.name_tracker_config = trackerConfig;
        }
    }

    boolean isUsingMarketResearchTracker() {
        return this.name_tracker_config.equals("tracker_noctali0.81");
    }

    boolean isUsingConsumerTracker() {
        return this.name_tracker_config.equals("tracker_noctali0.81_emoto");
    }

    static enum Mode {
        LARGE_FACES,
        SMALL_FACES,
        UNKNOWN;

        private Mode() {
        }

        AffdexFaceParams toParams(File dataDir) {
            AffdexFaceParams result;
            switch(this.ordinal()) {
            case 1:
                result = new AffdexFaceParams(dataDir, "tracker_noctali0.81");
                break;
            case 2:
                result = new AffdexFaceParams(dataDir, "tracker_noctali0.81_emoto");
                break;
            default:
                result = new AffdexFaceParams(dataDir);
            }

            return result;
        }
    }
}
