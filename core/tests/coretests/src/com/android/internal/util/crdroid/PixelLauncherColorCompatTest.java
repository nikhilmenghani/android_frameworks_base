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

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;

import com.android.internal.R;

import org.junit.Test;
import org.junit.runner.RunWith;

@SmallTest
@RunWith(AndroidJUnit4.class)
public class PixelLauncherColorCompatTest {
    private static final String LAUNCHER = "com.google.android.apps.nexuslauncher";
    private static final int[] PIXEL_IDS = {0x010602c7, 0x010602cb, 0x010602df, 0x010602e6};

    @Test
    public void affectedLauncher_resolvesIntendedSemanticColors() {
        assertEquals(R.color.materialColorOnSecondaryContainer, remap(0x010602c7));
        assertEquals(R.color.materialColorOnSurfaceVariant, remap(0x010602cb));
        assertEquals(R.color.materialColorSecondaryContainer, remap(0x010602df));
        assertEquals(R.color.materialColorSurfaceContainer, remap(0x010602e6));
    }

    @Test
    public void otherPackages_keepOriginalIds() {
        for (String packageName : new String[] {null, "android", "com.android.systemui",
                "com.android.launcher3", "com.google.android.googlequicksearchbox"}) {
            for (int id : PIXEL_IDS) {
                assertEquals(id,
                        PixelLauncherColorCompat.remapColorResourceId(packageName, 907L, id));
            }
        }
    }

    @Test
    public void otherLauncherVersions_keepOriginalIds() {
        for (long versionCode : new long[] {0L, 906L, 908L}) {
            for (int id : PIXEL_IDS) {
                assertEquals(id,
                        PixelLauncherColorCompat.remapColorResourceId(LAUNCHER, versionCode, id));
            }
        }
    }

    @Test
    public void unrelatedAndApplicationResources_keepOriginalIds() {
        for (int id : new int[] {0, android.R.color.white, 0x010602ca, 0x7f06035e}) {
            assertEquals(id, remap(id));
        }
    }

    private static int remap(int id) {
        return PixelLauncherColorCompat.remapColorResourceId(LAUNCHER, 907L, id);
    }
}
