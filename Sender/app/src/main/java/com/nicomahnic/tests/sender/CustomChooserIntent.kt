package com.nicomahnic.tests.sender

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Parcelable
import java.util.*

//Taken from https://gist.github.com/mediavrog/5625602

object CustomChooserIntent {
    /**
     * Creates a chooser that only shows installed apps that are allowed by the whitelist.
     *
     * @param pm PackageManager instance.
     * @param target The intent to share.
     * @param title The title of the chooser dialog.
     * @param whitelist A list of package names that are allowed to show.
     * @return Updated intent, to be passed to [android.content.Context.startActivity].
     */
    fun create(pm: PackageManager, target: Intent, title: String?,
               whitelist: List<String?>): Intent {
        val dummy = Intent(target.action)
        dummy.type = target.type
        val resInfo = pm.queryIntentActivities(dummy, 0)
        val metaInfo: MutableList<HashMap<String, String>> = ArrayList()
        for (ri in resInfo) {
            if (ri.activityInfo == null || !whitelist.contains(ri.activityInfo.packageName)) continue
            val info = HashMap<String, String>()
            info["packageName"] = ri.activityInfo.packageName
            info["className"] = ri.activityInfo.name
            info["simpleName"] = ri.activityInfo.loadLabel(pm).toString()
            metaInfo.add(info)
        }
        if (metaInfo.isEmpty()) {
            // Force empty chooser by setting a nonexistent target class.
            val emptyIntent = target.clone() as Intent
            emptyIntent.setPackage("your.package.name")
            emptyIntent.setClassName("your.package.name", "NonExistingActivity")
            return Intent.createChooser(emptyIntent, title)
        }

        // create the custom intent list
        val targetedIntents: MutableList<Intent> = ArrayList()
        for (mi in metaInfo) {
            val targetedShareIntent = target.clone() as Intent
            targetedShareIntent.setPackage(mi["packageName"])
            targetedShareIntent.setClassName(mi["packageName"]!!, mi["className"]!!)
            targetedIntents.add(targetedShareIntent)
        }
        val chooserIntent = Intent.createChooser(targetedIntents[0], title)
        targetedIntents.removeAt(0)
        val targetedIntentsParcelable: Array<Parcelable> = targetedIntents.toTypedArray()
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedIntentsParcelable)
        return chooserIntent
    }
}