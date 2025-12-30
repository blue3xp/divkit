import SwiftUI

struct DivButtonView: View {
    let text: String
    let action: DivAction
    let backgroundColor: Color
    let textColor: Color
    let style: DivStyle

    @EnvironmentObject var context: DivContext

    var body: some View {
        Button(action: {
            // Delegate to the ActionHandler chain
            let handled = context.actionHandler.handleAction(action, context: context)

            if !handled {
                print("Unhandled Button Action: \(action)")
            }
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
