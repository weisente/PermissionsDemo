package example.weisente.top.permissionsdemo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;



import java.io.File;
import java.io.IOException;
import java.util.List;

import static android.content.Context.SENSOR_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Created by joker on 2017/8/9.
 *
 * 检测是否获取了权限
 */

public class PermissionsChecker {
    private static final String TAG = "permissions4m";
    private static final String TAG_NUMBER = "1";
    private static boolean granted = false;

    /**
     * ensure whether permission granted
     *
     * @param activity
     * @param permission
     * @return true if granted else denied
     */
    public static boolean isPermissionGranted(Activity activity, String permission) {
        try {
            switch (permission) {
                case Manifest.permission.READ_CONTACTS:
//                    return checkReadContacts(activity);
                case Manifest.permission.WRITE_CONTACTS:
//                    return checkWriteContacts(activity);
                case Manifest.permission.GET_ACCOUNTS:
                    return true;

                case Manifest.permission.READ_CALL_LOG:
//                    return checkReadCallLog(activity);
                case Manifest.permission.READ_PHONE_STATE:
//                    return checkReadPhoneState(activity);
                case Manifest.permission.CALL_PHONE:
                    return true;
                case Manifest.permission.WRITE_CALL_LOG:
                    return checkWriteCallLog(activity);
                case Manifest.permission.USE_SIP:
                    return true;
                case Manifest.permission.PROCESS_OUTGOING_CALLS:
                    return true;
                case Manifest.permission.ADD_VOICEMAIL:
                    return true;

                case Manifest.permission.READ_CALENDAR:
                    return checkReadCalendar(activity);
                case Manifest.permission.WRITE_CALENDAR:
                    return true;

                case Manifest.permission.BODY_SENSORS:
                    return checkBodySensors(activity);

                case Manifest.permission.CAMERA:
                    return true;

                case Manifest.permission.ACCESS_COARSE_LOCATION:
                case Manifest.permission.ACCESS_FINE_LOCATION:
                    return checkLocation(activity);

                case Manifest.permission.READ_EXTERNAL_STORAGE:
                    return checkReadStorage(activity);
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    return checkWriteStorage(activity);

                case Manifest.permission.RECORD_AUDIO:
//                    return checkRecordAudio(activity);

                case Manifest.permission.READ_SMS:
//                    return checkReadSms(activity);
                case Manifest.permission.SEND_SMS:
                case Manifest.permission.RECEIVE_WAP_PUSH:
                case Manifest.permission.RECEIVE_MMS:
                case Manifest.permission.RECEIVE_SMS:
                    return true;
                default:
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "throwing exception in PermissionChecker:  ", e);
            return false;
        }
    }


    /**
     * read calendar, {@link android.Manifest.permission#READ_CALENDAR}
     *
     * @param activity
     * @return true if success
     */
    private static boolean checkReadCalendar(Activity activity) throws Exception {
        Cursor cursor = activity.getContentResolver().query(Uri.parse("content://com" +
                ".android.calendar/calendars"), null, null, null, null);
        if (cursor != null) {
            cursor.close();
            return true;
        } else {
            return false;
        }
    }
    /**
     * write or delete call log, {@link android.Manifest.permission#WRITE_CALL_LOG}
     *
     * @param activity
     * @return true if success
     */
    private static boolean checkWriteCallLog(Activity activity) throws Exception {
        ContentResolver contentResolver = activity.getContentResolver();
        ContentValues content = new ContentValues();
        content.put(CallLog.Calls.TYPE, CallLog.Calls.INCOMING_TYPE);
        content.put(CallLog.Calls.NUMBER, TAG_NUMBER);
        content.put(CallLog.Calls.DATE, 20140808);
        content.put(CallLog.Calls.NEW, "0");
        contentResolver.insert(Uri.parse("content://call_log/calls"), content);

        contentResolver.delete(Uri.parse("content://call_log/calls"), "number = ?", new
                String[]{TAG_NUMBER});

        return true;
    }


    /**
     * write storage, {@link android.Manifest.permission#WRITE_EXTERNAL_STORAGE}
     *
     * @param activity
     * @return true if success
     * @throws Exception
     */
    private static boolean checkWriteStorage(Activity activity) throws Exception {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath(), TAG);
        if (!file.exists()) {
            boolean newFile;
            try {
                newFile = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return newFile;
        } else {
            return file.delete();
        }
    }

    /**
     * read storage, {@link android.Manifest.permission#READ_EXTERNAL_STORAGE}
     *
     * @param activity
     * @return true if success
     * @throws Exception
     */
    private static boolean checkReadStorage(Activity activity) throws Exception {
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).getPath());
        File[] files = file.listFiles();
        return files != null;
    }

    /**
     * use location, {@link android.Manifest.permission#ACCESS_FINE_LOCATION},
     * {@link android.Manifest.permission#ACCESS_COARSE_LOCATION}
     *
     * @param activity
     * @return true if success
     * @throws Exception
     */
    private static boolean checkLocation(Activity activity) throws Exception {
        granted = false;
        final LocationManager locationManager = (LocationManager) activity.getSystemService(Context
                .LOCATION_SERVICE);
        List<String> list = locationManager.getProviders(true);

        if (list.contains(LocationManager.GPS_PROVIDER)) {
            return true;
        } else if (list.contains(LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            if (!locationManager.isProviderEnabled("gps")) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0F, new
                        LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                locationManager.removeUpdates(this);
                            }

                            @Override
                            public void onStatusChanged(String provider, int status, Bundle extras) {
                                locationManager.removeUpdates(this);
                                granted = true;
                            }

                            @Override
                            public void onProviderEnabled(String provider) {
                                locationManager.removeUpdates(this);
                            }

                            @Override
                            public void onProviderDisabled(String provider) {
                                locationManager.removeUpdates(this);
                            }
                        });
            }
            return granted;
        }
    }

    /**
     * use sensors, {@link android.Manifest.permission#BODY_SENSORS}
     *
     * @param activity
     * @return true if success
     * @throws Exception
     */
    private static boolean checkBodySensors(Activity activity) throws Exception {
        SensorManager sensorManager = (SensorManager) activity.getSystemService(SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor((Sensor.TYPE_ACCELEROMETER));
        SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.unregisterListener(listener, sensor);

        return true;
    }



    /**
     * 1.denied {@link android.Manifest.permission#READ_CONTACTS} permission
     * ---->cursor.getCount == 0
     * 2.granted {@link android.Manifest.permission#READ_CONTACTS} permission
     * ---->cursor.getCount return real count in contacts
     * <p>
     * so when there are no user or permission denied, it will return 0
     *
     * @param cursor
     * @param numberIndex
     * @return true if can not get info
     */
    private static boolean isNumberIndexInfoIsNull(Cursor cursor, int numberIndex) {
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                return TextUtils.isEmpty(cursor.getString(numberIndex));
            }
            return false;
        } else {
            return true;
        }
    }
}
