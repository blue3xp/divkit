import SwiftUI

struct DivRenderer: View {
    let component: DivComponent

    var body: some View {
        switch component {
        case .text(_, let content, let fontSize, let color, let fontWeight, let style):
            DivTextView(content: content, fontSize: fontSize, color: color, fontWeight: fontWeight, style: style)

        case .input(_, let hint, let variable, let validators, let style):
            DivInputView(hint: hint, variable: variable, validators: validators, style: style)

        case .image(_, let url, let contentScale, let style):
            DivImageView(url: url, contentScale: contentScale, style: style)

        case .button(_, let text, let action, let backgroundColor, let textColor, let style):
            DivButtonView(text: text, action: action, backgroundColor: backgroundColor, textColor: textColor, style: style)

        case .container(_, let items, let orientation, let alignment, let style):
            DivContainerView(items: items, orientation: orientation, alignment: alignment, style: style)

        case .grid(_, let items, let columnCount, let style):
            DivGridView(items: items, columnCount: columnCount, style: style)
        }
    }
}
