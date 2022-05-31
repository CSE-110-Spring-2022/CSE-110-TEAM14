package com.example.cse_110_team14;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import java.util.Arrays;

public class PermissionChecker {

    private ComponentActivity activity;

    final ActivityResultLauncher<String[]> requestPermissionLauncher;

    private final String[] requiredPermissions = new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    public PermissionChecker(ComponentActivity activity) {
        this.activity = activity;
        requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), perms -> {
            perms.forEach((perm, isGranted) -> {
                Log.i("PermissionChecker", String.format("Permission %s granted: %s", perm, isGranted));
            });
        });
    }

    public void ensurePermissions() {
        var hasNoLocationPerms = Arrays.stream(requiredPermissions)
                .map(perm -> ContextCompat.checkSelfPermission(activity, perm))
                .allMatch(status -> status == PackageManager.PERMISSION_DENIED);

        if (hasNoLocationPerms) {
            requestPermissionLauncher.launch(requiredPermissions);
        }
    }

    public boolean hasPermissions() {
        return (activity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                && (activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
}
