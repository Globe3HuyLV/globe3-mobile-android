package com.globe3.tno.g3_mobile.util;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import com.neurotec.biometrics.NBiometric;
import com.neurotec.biometrics.NBiometricOperation;
import com.neurotec.biometrics.NBiometricStatus;
import com.neurotec.biometrics.NBiometricTask;
import com.neurotec.biometrics.NFinger;
import com.neurotec.biometrics.NSubject;
import com.neurotec.biometrics.client.NBiometricClient;
import com.neurotec.images.NImage;
import com.neurotec.util.NImageUtils;
import com.neurotec.util.concurrent.CompletionHandler;

import java.io.IOException;
import java.util.EnumSet;

import static com.globe3.tno.g3_mobile.constants.App.APP_NAME;
import static com.globe3.tno.g3_mobile.globals.Globals.BIOMETRIC_DATA;
import static com.globe3.tno.g3_mobile.globals.Globals.DEVICES_LICENSE_OBTAINED;
import static com.globe3.tno.g3_mobile.globals.Globals.EXTRACT_LICENSE_OBTAINED;
import static com.globe3.tno.g3_mobile.globals.Globals.MATCHER_LICENSE_OBTAINED;

public class BiometricUtility {
    public static NSubject createSubject(Activity activity, Uri uri) throws IOException {
        NSubject subject = new NSubject();
        NFinger finger = new NFinger();
        finger.setImage(NImageUtils.fromUri(activity, uri));
        subject.getFingers().add(finger);
        subject.setId(uri.getPath());
        return subject;
    }

    public static NSubject createSubject(byte[] fingerData, String subjectId) throws IOException {
        NSubject subject = new NSubject();
        NFinger finger = new NFinger();

        NImage image = NImageUtils.fromJPEG(fingerData);

        finger.setImage(image);
        subject.getFingers().add(finger);
        subject.setId(subjectId);
        return subject;
    }

    public static boolean enrollFinger(byte[] finger_data, String finger_data_id) {
        if(finger_data != null){
            NSubject candidateSubject = null;
            try {
                candidateSubject = createSubject(finger_data, finger_data_id);
                BIOMETRIC_DATA.enroll(candidateSubject);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            /*NBiometricTask enrollTask = BIOMETRIC_DATA.createTask(EnumSet.of(NBiometricOperation.ENROLL), candidateSubject);
            BIOMETRIC_DATA.performTask(enrollTask, NBiometricOperation.ENROLL, new CompletionHandler<NBiometricTask, NBiometricOperation>() {
                @Override
                public void completed(NBiometricTask result, NBiometricOperation attachment) {
                    Log.i(APP_NAME, "enroll_result:"+result.getStatus().toString());
                }

                @Override
                public void failed(Throwable th, NBiometricOperation attachment) {
                    Log.i(APP_NAME, "enrol_error");
                    th.printStackTrace();
                }
            });*/
        }

        return true;
    }

    public static boolean updateFinger(byte[] finger_data, String finger_data_id) {
        if(finger_data != null){
            NSubject candidateSubject = null;
            try {
                candidateSubject = createSubject(finger_data, finger_data_id);
                BIOMETRIC_DATA.update(candidateSubject);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            /*NBiometricTask enrollTask = BIOMETRIC_DATA.createTask(EnumSet.of(NBiometricOperation.ENROLL), candidateSubject);
            BIOMETRIC_DATA.performTask(enrollTask, NBiometricOperation.ENROLL, new CompletionHandler<NBiometricTask, NBiometricOperation>() {
                @Override
                public void completed(NBiometricTask result, NBiometricOperation attachment) {
                    Log.i(APP_NAME, "enroll_result:"+result.getStatus().toString());
                }

                @Override
                public void failed(Throwable th, NBiometricOperation attachment) {
                    Log.i(APP_NAME, "enrol_error");
                    th.printStackTrace();
                }
            });*/
        }

        return true;
    }

    public static boolean enrollFinger(NBiometricClient nBiometricClient, byte[] finger_data, String finger_data_id) {
        if(finger_data != null){
            NSubject candidateSubject = null;
            try {
                candidateSubject = createSubject(finger_data, finger_data_id);
                nBiometricClient.enroll(candidateSubject);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            /*NBiometricTask enrollTask = nBiometricClient.createTask(EnumSet.of(NBiometricOperation.ENROLL), candidateSubject);
            nBiometricClient.performTask(enrollTask, NBiometricOperation.ENROLL, new CompletionHandler<NBiometricTask, NBiometricOperation>() {
                @Override
                public void completed(NBiometricTask result, NBiometricOperation attachment) {
                    Log.i(APP_NAME, "enroll_result:"+result.getStatus().toString());
                }

                @Override
                public void failed(Throwable th, NBiometricOperation attachment) {
                    Log.i(APP_NAME, "enrol_error");
                    th.printStackTrace();
                }
            });*/
        }

        return true;
    }

    public static boolean deleteFinger(String fingerId){
        try {
            BIOMETRIC_DATA.delete(fingerId);

            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean licenseObtained(){
        return (EXTRACT_LICENSE_OBTAINED && MATCHER_LICENSE_OBTAINED && DEVICES_LICENSE_OBTAINED);
    }
}
