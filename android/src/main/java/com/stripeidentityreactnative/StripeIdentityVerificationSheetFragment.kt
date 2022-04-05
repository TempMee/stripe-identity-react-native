package com.stripeidentityreactnative

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.WritableNativeMap
import com.stripe.android.identity.IdentityVerificationSheet
import com.stripe.android.identity.IdentityVerificationSheet.VerificationFlowResult

class StripeIdentityVerificationSheetFragment : Fragment() {

  private lateinit var identityVerificationSheet: IdentityVerificationSheet
  private lateinit var verificationSessionId: String
  private lateinit var ephemeralKeySecret: String

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return FrameLayout(requireActivity())
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    verificationSessionId = arguments?.getString("sessionId").orEmpty()
    ephemeralKeySecret = arguments?.getString("ephemeralKeySecret").orEmpty()
    val imageUri = arguments?.getBundle("brandLogo")?.getString("uri").orEmpty()
    identityVerificationSheet = IdentityVerificationSheet.create(this, IdentityVerificationSheet.Configuration(brandLogo = Uri.parse(imageUri))) {
      val result = WritableNativeMap()

      when (it) {
        VerificationFlowResult.Completed -> result.putString("status", "FlowCompleted")
        VerificationFlowResult.Canceled -> result.putString("status", "FlowCanceled")
        else -> result.putString("status", "FlowFailed")
      }
      // promise.resolve(result)
    }
  }

  fun present(promise: Promise) {
    identityVerificationSheet.present(
      verificationSessionId = verificationSessionId,
      ephemeralKeySecret = ephemeralKeySecret
    )
  }
}
