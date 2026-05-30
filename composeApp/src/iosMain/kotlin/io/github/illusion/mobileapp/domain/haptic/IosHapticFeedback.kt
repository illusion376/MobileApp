package io.github.illusion.mobileapp.domain.haptic

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyle

class IosHapticFeedback : HapticFeedback {
    override fun performPaymentImpact() {
        val generator = UIImpactFeedbackGenerator(style = UIImpactFeedbackStyle.UIImpactFeedbackStyleHeavy)
        generator.prepare()
        generator.impactOccurred()
    }
}
