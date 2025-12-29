import SwiftUI

struct DivTextView: View {
    let content: String
    let fontSize: CGFloat
    let color: Color
    let fontWeight: Font.Weight
    let style: DivStyle

    var body: some View {
        Text(content)
            .font(.system(size: fontSize, weight: fontWeight))
            .foregroundColor(color)
            .applyDivStyle(style)
    }
}
