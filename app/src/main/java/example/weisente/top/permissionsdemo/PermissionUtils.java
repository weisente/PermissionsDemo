package example.weisente.top.permissionsdemo;

import android.app.Activity;
import android.app.Fragment;
import android.os.Build;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by san on 2017/11/30.
 */

public class PermissionUtils {




    //判断是否为6.0以上
    public static Boolean isOverMarshmallow(){
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.M;
    }

    /**
     * 执行成功的方法
     */
    public static void executeSucceedMethod(Object reflectObject, int requestCode) {
        // 获取class中多有的方法
        Method[] methods = reflectObject.getClass().getDeclaredMethods();

        // 遍历找我们打了标记的方法
        for (Method method:methods){
            Log.e("TAG",method+"");
            // 获取该方法上面有没有打这个成功的标记
            PermissionSucceed succeedMethod =  method.getAnnotation(PermissionSucceed.class);
            if(succeedMethod != null){
                // 代表该方法打了标记
                // 并且我们的请求码必须 requestCode 一样
                int methodCode = succeedMethod.requestCode();
                if(methodCode == requestCode){
                    // 这个就是我们要找的成功方法
                    // 反射执行该方法
                    Log.e("TAG","找到了该方法 ："+method);
                    executeMethod(reflectObject,method);
                }
            }
        }
    }

    /**
     * 反射执行该方法
     */
    private static void executeMethod(Object reflectObject,Method method) {
        // 反射执行方法  第一个是传该方法是属于哪个类   第二个参数是反射方法的参数
        try {
            method.setAccessible(true); // 允许执行私有方法
            method.invoke(reflectObject,new Object[]{});
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public static Activity getActivity(Object object) {
        if(object instanceof Activity ){
            return  (Activity) object;
        }else if( object instanceof Fragment){
            return ((Fragment) object).getActivity();
        }
        return null;

    }

    /**
     * 执行成功的方法
     */
    public static void executeFailMethod(Object reflectObject, int requestCode) {
        // 获取class中多有的方法
        Method[] methods = reflectObject.getClass().getDeclaredMethods();

        // 遍历找我们打了标记的方法
        for (Method method:methods){
            Log.e("TAG",method+"");
            // 获取该方法上面有没有打这个成功的标记
            PermissionFail succeedMethod =  method.getAnnotation(PermissionFail.class);
            if(succeedMethod != null){
                // 代表该方法打了标记
                // 并且我们的请求码必须 requestCode 一样
                int methodCode = succeedMethod.requestCode();
                if(methodCode == requestCode){
                    // 这个就是我们要找的成功方法
                    // 反射执行该方法
                    Log.e("TAG","找到了该方法 ："+method);
                    executeMethod(reflectObject,method);
                }
            }
        }
    }

    public static List<String> getDeniedPermissions(Object object, String[] permissions) {


        ArrayList<String> denied = new ArrayList<String>();

        for(int i = 0;i < permissions.length;i++){
            //检查那一个权限没有被授权
            boolean permissionGranted = PermissionsChecker.isPermissionGranted(getActivity(object), permissions[i]);
            // 标记
            if(permissionGranted){
                denied.add(permissions[i]);
            }
        }
        return denied;
    }
}
