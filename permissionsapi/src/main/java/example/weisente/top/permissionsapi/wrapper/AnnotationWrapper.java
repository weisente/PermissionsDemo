package example.weisente.top.permissionsapi.wrapper;

import android.content.Intent;

/**
 * Created by san on 2017/11/28.
 */

public interface AnnotationWrapper {
    /**
     * get the proxy class
     * @param className target class name
     * @return
     */
    PermissionsProxy getProxy(String className);

    interface PermissionsProxy<T> {
        void rationale(T object, int code);

        void denied(T object, int code);

        void granted(T object, int code);

        void intent(T object, int code, Intent intent);

        boolean customRationale(T object, int code);

        void startSyncRequestPermissionsMethod(T object);
    }
}
