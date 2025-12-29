import SwiftUI

struct DivRenderer: View {
    let component: DivComponent

    var body: some View {
        switch component {
        case .text(let content, let fontSize, let color, let fontWeight, let style):
            DivTextView(content: content, fontSize: fontSize, color: color, fontWeight: fontWeight, style: style)

        case .image(let url, let contentScale, let style):
            DivImageView(url: url, contentScale: contentScale, style: style)

        case .button(let text, let action, let backgroundColor, let textColor, let style):
            DivButtonView(text: text, action: action, backgroundColor: backgroundColor, textColor: textColor, style: style)

        case .container(let items, let orientation, let alignment, let style):
            DivContainerView(items: items, orientation: orientation, alignment: alignment, style: style)
        }
    }
}
