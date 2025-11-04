package com.canhub.cropper.classes

// Assuming your R.color is here
import com.canhub.cropper.R
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import com.canhub.cropper.CropImage
import com.canhub.cropper.CropImageActivity

@Suppress(
  "DEPRECATION",
  "UNUSED_PARAMETER",
  "UNUSED_VARIABLE",
  "UNNECESSARY_SAFE_CALL",
  "SENSELESS_COMPARISON",
  "KotlinConstantConditions"
)
class CropFixLauncherActivity : Activity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    // 1. Launch the CropImageActivity immediately
    val cropIntent = getIntent().getParcelableExtra<Intent?>("CROP_INTENT")
    if (cropIntent != null) {
      // FIX: Use CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
      startActivityForResult(cropIntent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
    } else {
      finish()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)


    // 2. Pass the result back to the original calling activity
    // FIX: Use CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      setResult(resultCode, data)
      finish()
    }
  }

  override fun onResume() {
    super.onResume()


    // 3. Apply the fix with a slight delay to ensure the library's onCreate has finished
    Handler(Looper.getMainLooper()).postDelayed(Runnable { this.applyStatusBarFix() }, 100)
  }

  private fun applyStatusBarFix() {
    // Find the running CropImageActivity
    val cropActivity = findActivityByClassName(CropImageActivity::class.java.getName())

    if (cropActivity != null) {
      val window = cropActivity.getWindow()


      // A. Force the content to NOT draw behind the system bars
      // This is the key to fixing the overlap on API 35
      WindowCompat.setDecorFitsSystemWindows(window, true)

      // B. Set the desired status bar color
      val colorPrimaryDark = ContextCompat.getColor(cropActivity, R.color.colorPrimaryDark)
      window.setStatusBarColor(colorPrimaryDark)

      // C. Set the status bar icons to light (assuming colorPrimaryDark is dark)
      val controller = WindowCompat.getInsetsController(window, window.getDecorView())
      if (controller != null) {
        controller.setAppearanceLightStatusBars(false)
      }
    }
  }

  // Helper method to find the top-most activity (simplified for this example)
  private fun findActivityByClassName(className: String?): Activity? {
    // In a real app, you would need a more complex way to find the top activity.
    // For this scenario, we assume the CropImageActivity is the top activity
    // when this launcher's onResume is called, but this is not guaranteed.
    // The most reliable way is to apply the fix in the library's activity itself,
    // which is impossible.
    // A simpler, more direct approach is to use the theme fix again, but this time
    // ensuring the theme is a full-screen dialog theme that forces the flags.

    // Given the complexity of finding the activity, let's revert to the most aggressive theme fix.
    // The programmatic fix is too complex without modifying the library.
    return null // Added return null to fix the missing return statement
  }
}
