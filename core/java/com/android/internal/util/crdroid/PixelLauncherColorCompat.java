/*
 * Copyright (C) 2026 The crDroid Android Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.internal.util.crdroid;

import android.app.ActivityThread;
import android.app.Application;

import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;

/**
 * Compatibility for private framework color IDs used by the Kodiak Pixel Launcher.
 *
 * <p>The audited Android 17 launcher (version code 907) embeds Pixel framework IDs
 * in its icon-rendering code. Those IDs resolve to different Material colors on
 * AOSP. Resolve the intended semantic colors without replacing the signed APK or
 * changing the framework palette for other applications.
 *
 * @hide
 */
public final class PixelLauncherColorCompat {
    private static final String PACKAGE_NAME = "com.google.android.apps.nexuslauncher";
    private static final long AFFECTED_VERSION_CODE = 907L;

    private PixelLauncherColorCompat() {}

    public static int remapColorResourceId(int id) {
        // Avoid looking up application state on the normal Resources.getColor path.
        switch (id) {
            case 0x010602c7:
            case 0x010602cb:
            case 0x010602df:
            case 0x010602e6:
                break;
            default:
                return id;
        }

        final Application application = ActivityThread.currentApplication();
        if (application == null) {
            return id;
        }
        return remapColorResourceId(application.getPackageName(),
                application.getApplicationInfo().longVersionCode, id);
    }

    @VisibleForTesting
    static int remapColorResourceId(String packageName, long versionCode, int id) {
        if (!PACKAGE_NAME.equals(packageName) || versionCode != AFFECTED_VERSION_CODE) {
            return id;
        }

        // These are IDs from the Pixel APK, not IDs to assign in AOSP's resource table.
        // Resolve destinations symbolically so night mode, Monet and contrast apply.
        switch (id) {
            case 0x010602c7:
                return R.color.materialColorOnSecondaryContainer;
            case 0x010602cb:
                return R.color.materialColorOnSurfaceVariant;
            case 0x010602df:
                return R.color.materialColorSecondaryContainer;
            case 0x010602e6:
                return R.color.materialColorSurfaceContainer;
            default:
                return id;
        }
    }
}
