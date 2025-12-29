import SwiftUI

struct DivButtonView: View {
    let text: String
    let action: DivAction
    let backgroundColor: Color
    let textColor: Color
    let style: DivStyle

    var body: some View {
        Button(action: {
            print("Button Action: \(action)")
        }) {
            Text(text)
                .foregroundColor(textColor)
                .padding()
                .background(backgroundColor)
                .cornerRadius(8) // Default styling for button
        }
        .applyDivStyle(style)
    }
}
