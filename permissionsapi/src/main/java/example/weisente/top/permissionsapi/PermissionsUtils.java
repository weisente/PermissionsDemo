package example.weisente.top.permissionsapi;

import android.app.Activity;

import java.sql.Wrapper;

/**
 * Created by san on 2017/11/28.
 */

public class PermissionsUtils {

    public static Wrapper get(Activity activity) {
        return new ActivityWrapper(activity);
    }

    public static Wrapper get(android.app.Fragment fragment) {
        return new FragmentWrapper(fragment);
    }

    public static Wrapper get(android.support.v4.app.Fragment fragment) {
        return new SupportFragmentWrapper(fragment);
    }
}

