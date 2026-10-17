/*
 * Copyright 2026 Morphe.
 * https://github.com/MorpheApp/morphe-patches
 *
 * See the included NOTICE file for GPLv3 Section 7 terms that apply to this code.
 */

package app.indierodo.patches.reddit.layout.maturecontent

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.extensions.InstructionExtensions.addInstruction
import app.morphe.patcher.patch.bytecodePatch
import app.morphe.patcher.resource.ResourceType
import app.morphe.patcher.resourceLiteral
import app.morphe.patches.reddit.shared.Constants.COMPATIBILITY_REDDIT
import com.android.tools.smali.dexlib2.AccessFlags

@Suppress("unused")
val hideMatureContentSettingsPatch = bytecodePatch(
    name = "Hide mature content settings",
    description = "Hides the mature-content and mature-media blur settings without adding a way to restore them."
) {
    compatibleWith(COMPATIBILITY_REDDIT)

    execute {
        // Both settings are Compose-backed switch functions. Returning before either function is
        // composed removes its row entirely, rather than merely disabling its switch.
        fun Fingerprint.hideSettingsItem() = method.addInstruction(0, "return-void")

        Over18SwitchFingerprint.hideSettingsItem()
        BlurNsfwSwitchFingerprint.hideSettingsItem()
    }
}

private object Over18SwitchFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Z", "Z", "L", "L", "I"),
    filters = listOf(
        resourceLiteral(ResourceType.STRING, "label_over18")
    )
)

private object BlurNsfwSwitchFingerprint : Fingerprint(
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    returnType = "V",
    parameters = listOf("Z", "Z", "Z", "L", "L", "I"),
    filters = listOf(
        resourceLiteral(ResourceType.STRING, "label_blur_nsfw_images")
    )
)
