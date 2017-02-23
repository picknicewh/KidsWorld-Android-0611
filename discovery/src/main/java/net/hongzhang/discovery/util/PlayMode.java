package net.hongzhang.discovery.util;

/**
 * Created with Android Studio.
 * User: ryan.hoo.j@gmail.com
 * Date: 9/5/16
 * Time: 5:48 PM
 * Desc: PlayMode
 */
public enum PlayMode {
    SINGLE,
    LOOP,
    SHUFFLE;
    public static PlayMode getDefault() {
        return LOOP;
    }
    public static PlayMode switchNextMode(PlayMode current) {
        if (current == null) return getDefault();
        switch (current) {
            case LOOP:
                return SINGLE;
            case SINGLE:
                return SHUFFLE;
            case SHUFFLE:
                return  LOOP;
        }
        return getDefault();
    }
}
