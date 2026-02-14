import SwiftUI

struct DivModifier: ViewModifier {
    let style: DivStyle

    func body(content: Content) -> some View {
        content
            // 1. Size
            .frame(width: style.width, height: style.height)

            // 2. Padding (Inner padding) - Applied FIRST so it expands the view before background
            .padding(style.padding)

            // 3. Background & Border & Clip - Painted on the expanded view
            .background(
                style.background ?? Color.clear
            )
            .clipShape(
                RoundedRectangle(cornerRadius: style.border?.cornerRadius ?? 0)
            )
            .overlay(
                RoundedRectangle(cornerRadius: style.border?.cornerRadius ?? 0)
                    .stroke(style.border?.color ?? .clear, lineWidth: style.border?.width ?? 0)
            )

            // 4. Action
            .onTapGesture {
                if let action = style.action {
                    print("DivAction Triggered: \(action)")
                }
            }

            // 5. Margin (Outer padding) - Applied LAST to create space outside the background
            .padding(style.margin)
    }
}

extension View {
    func applyDivStyle(_ style: DivStyle) -> some View {
        self.modifier(DivModifier(style: style))
    }
}
