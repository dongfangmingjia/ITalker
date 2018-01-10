package com.warner.italker.fragment.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.warner.italker.App;
import com.warner.italker.R;
import com.warner.italker.fragment.media.GalleryFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks{

    private static final int RC = 0x0010;

    public PermissionsFragment() {
        // Required empty public constructor
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPerm();
            }
        });
        return root;
    }


    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());
    }

    /**
     * 刷新我们的布局中的图片的状态
     * @param root
     */
    private void refreshState(View root) {
        if (root == null) {
            return;
        }
        Context context = getContext();

        root.findViewById(R.id.im_state_permission_network).setVisibility(hasNetWorkPerm(context) ? View.VISIBLE : View.INVISIBLE);
        root.findViewById(R.id.im_state_permission_write).setVisibility(hasWritePerm(context) ? View.VISIBLE : View.INVISIBLE);
        root.findViewById(R.id.im_state_permission_read).setVisibility(hasReadPerm(context) ? View.VISIBLE : View.INVISIBLE);
        root.findViewById(R.id.im_state_permission_record_audio).setVisibility(hasRecordAudioPerm(context) ? View.VISIBLE : View.INVISIBLE);
    }


    private static boolean hasNetWorkPerm(Context context) {
        String[] permission = new String[] {
            Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_WIFI_STATE};

        return EasyPermissions.hasPermissions(context, permission);
    }

    private static boolean hasReadPerm(Context context) {
        String[] permission = new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE};

        return EasyPermissions.hasPermissions(context, permission);
    }

    private static boolean hasWritePerm(Context context) {
        String[] permission = new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        return EasyPermissions.hasPermissions(context, permission);
    }


    private static boolean hasRecordAudioPerm(Context context) {
        String[] permission = new String[] {
                Manifest.permission.RECORD_AUDIO};

        return EasyPermissions.hasPermissions(context, permission);
    }



    private static void show(FragmentManager manager) {
        new PermissionsFragment().show(manager, PermissionsFragment.class.getName());
    }

    /**
     *
     * @param context
     * @param manager
     * @return
     */
    public static boolean hasAllPerm(Context context, FragmentManager manager) {
        boolean hasAllPerm = hasNetWorkPerm(context)
                && hasReadPerm(context)
                && hasWritePerm(context)
                && hasRecordAudioPerm(context);
        if (!hasAllPerm) {
            // 没有权限显示改界面
            show(manager);
        }

        return hasAllPerm;
    }

    @AfterPermissionGranted(RC)
    private void requestPerm() {
        String[] permission = new String[] {
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO};

        if (EasyPermissions.hasPermissions(getContext(), permission)) {
            App.showToast(R.string.label_permission_ok);
            // Fragment中可以通过getView获取根布局，前提是在onCreateView方法之后
            refreshState(getView());
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions), RC, permission);
        }
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 如果权限没有申请成功
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
