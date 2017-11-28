package example.weisente.top.permissionsapi.wrapper;

import android.content.Intent;

import java.sql.Wrapper;

/**
 * Created by san on 2017/11/28.
 */

public interface ListenerWrapper {

    /**
     * call back listener
     *
     * @param listener {@link PermissionPageListener}
     * @return
     */
    Wrapper requestListener(PermissionRequestListener listener);

    /**
     * according to {@link Wrapper#requestPageType(int)}, will return different Intent type
     *
     * @param listener {@link PermissionPageListener}
     * @return
     */
    Wrapper requestPage(PermissionPageListener listener);

    Wrapper requestCustomRationaleListener(PermissionCustomRationaleListener listener);

    PermissionRequestListener getPermissionRequestListener();

    PermissionPageListener getPermissionPageListener();

    PermissionCustomRationaleListener getPermissionCustomRationaleListener();

    interface PermissionRequestListener {
        void permissionGranted(int code);

        void permissionDenied(int code);

        void permissionRationale(int code);
    }

    interface PermissionPageListener {
        void pageIntent(int code, Intent intent);
    }

    interface PermissionCustomRationaleListener {
        void permissionCustomRationale(int code);
    }



}
